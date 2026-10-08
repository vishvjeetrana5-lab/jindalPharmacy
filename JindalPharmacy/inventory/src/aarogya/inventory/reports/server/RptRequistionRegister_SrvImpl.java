package aarogya.inventory.reports.server;

import aarogya.inventory.reports.shared.RptRequistionRegister_Srv;
import aarogya.inventory.reports.shared.TxtHItem_Mdl;
import aarogya.inventory.reports.shared.TxtHRequisitionNo_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmIJasperCompiled;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptRequistionRegister_SrvImpl extends DmRSSPrivate implements RptRequistionRegister_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = new ProjectVoucherCategory().INVENTORY_REQUISITION;

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTempSubReport = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        DmIJasperCompiled jasperCompiled = null;
        String sqlQuery = " ", condition = "", subQueryUsage = "", isUsage = "", condition2 = " ";

        try {
            validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  rq.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  rq.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("reqType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  vt.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("reqType").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("requisitionNo").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND rq.docId IN (" + DmServer.getParamList(":requisitionNo", filter.get("requisitionNo").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("departmentBy").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND rq.departmentCode IN (" + DmServer.getParamList(":departmentBy", filter.get("departmentBy").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("departmentTo").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND rq.departmentToCode IN (" + DmServer.getParamList(":departmentTo", filter.get("departmentTo").getTag()) + " )";
                if (DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("0")) {
                    condition += " and tdStk.balanceQty>0 ";
                } else if (DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("1")) {
                    condition += " and tdStk.balanceQty=0 ";
                }
            }

            if (!DmShared.xNull(filter.get("requisition").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND sg.subCode IN (" + DmServer.getParamList(":requisition", filter.get("requisition").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND rqd.itemCode IN (" + DmServer.getParamList(":itemName", filter.get("itemName").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemStatus").getTag()).trim().equalsIgnoreCase("")) {
                condition += "    AND rqd.isStatus = :itemStatus";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND   rq.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
                condition2 = " where ibs.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }
            if (DmShared.xNull(filter.get("usage").getTag()).trim().equalsIgnoreCase("1")) {
                isUsage = "1";
            }
            sqlQuery = " SELECT rq.recId,rq.docId,rqd.v_Sno as VSNo, rq.v_Date,sg.name AS subgroup,sdpto.name as departmentTo, vt.short_Name,\n"
                    + " sdp.name as departmentBy,rqd.reqQty,rqd.reqDate,rqd.reqTime,\n"
                    + " rqd.remark,CASE rqd.isStatus WHEN 0 THEN 'Open' WHEN 1 THEN 'Cancel' WHEN 2 THEN 'Complete' ELSE 'Open' END as isStatus,\n"
                    + " im.name as itemName,um.unit AS sku,um.code skuCode,sm.name as siteCodeToName,rq.site_Code_To as siteCodeTo,rqd.issueQty,\n"
                    + " Cast(IsNull(rqd.reqQty,0)-(IsNull(rqd.issueQty,0)) as decimal(18,7)) as pendingQuantity,\n"
                    + " icm.name as colourName,ism.name as sizeName,tdStk.balanceQty as stkToDept,rqd.statusRemark\n"
                    + " from requisition rq \n"
                    + " left join requisitiondetail as rqd on rqd.docId = rq.docId\n"
                    + " LEFT JOIN subGroup sg ON sg.subCode = rq.requestedByCode \n"
                    + " LEFT JOIN subgroup sdp ON sdp.subcode= rq.departmentCode \n"
                    + " LEFT JOIN subgroup sdpto ON sdpto.subcode = rq.departmentToCode \n"
                    + " LEFT JOIN (\n"
                    + "           select ibs.itemCode,ibs.departmentCode,sum(ibs.balanceQty) as balanceQty\n"
                    + "           	from itemBatchStock ibs\n"
                    + "           	" + condition2
                    + "           	group by ibs.itemCode,ibs.departmentCode\n"
                    + "           ) as tdStk on rqd.itemCode=tdStk.itemCode and rq.departmentToCode=tdStk.departmentCode\n"
                    + " left join voucher_Type vt on vt.v_Type = rq.v_Type \n"
                    + " LEFT JOIN itemMast im ON im.code = rqd.itemCode \n"
                    + " LEFT JOIN unitMast um ON um.code = im.purchaseUnit \n"
                    + " Left join siteMast as sm on sm.code =rq.site_Code_To \n"
                    + " Left join itemColourMast as icm on icm.code =rqd.colourCode \n"
                    + " Left join itemSizeMast as ism on ism.code =rqd.sizeCode \n"
                    + " where vt.category ='" + vCat + "'\n" + condition
                    + " AND ISNULL(rq.approvedBy, '') <> ''\n"
                    + " ORDER BY rq.v_Date, vt.short_Name, rq.v_No, rq.recId, rqd.v_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            subQueryUsage = "SELECT"
                    + "  CASE WHEN ISNULL(rqd.itemCode, 0) <> ISNULL(shd.itemCode, 0) THEN 1 ELSE 0 END AS isItemChanged"
                    + ", CASE "
                    + "         WHEN ISNULL(rqd.colourCode, 0) <> ISNULL(shd.colourCode, 0) THEN 1 "
                    + "         WHEN ISNULL(rqd.sizeCode, 0) <> ISNULL(shd.sizeCode, 0) THEN 1 "
                    + "         ELSE 0 "
                    + "  END AS isPropertiesChanged"
                    + ", sh.recId AS issueNo"
                    + ", sh.v_Date AS issueDate"
                    + ", isNull(im.name,'') AS issueItem"
                    + ", isNull(icm.name,'') AS issueColour"
                    + ", isNull(ism.name,'') AS issueSize,"
                    + "  shd.quantity"
                    + " FROM requisition rq"
                    + " LEFT JOIN requisitionDetail rqd ON rqd.docId = rq.docId"
                    + " LEFT JOIN ("
                    + "     SELECT shd.*"
                    + "     FROM stockHeadDetail shd "
                    + "     left join stockHead as sh on shd.docId=sh.docId"
                    + "     left join voucher_type as vt on vt.v_Type=sh.v_type"
                    + "     WHERE vt.category IN ('" + pvc.ITEM_ISSUE + "') and shd.requisitionDocId = $P{docId}"
                    + "     AND shd.requisitionV_SNo = $P{VSNo}"
                    + "     ) AS shd ON shd.requisitionDocId = rqd.docId AND shd.requisitionV_SNo = rqd.v_SNo"
                    + " LEFT JOIN stockHead sh ON sh.docId = shd.docId"
                    + " LEFT JOIN itemMast im ON im.code = shd.itemCode"
                    + " LEFT JOIN itemColourMast icm ON icm.code = shd.colourCode"
                    + " LEFT JOIN itemSizeMast ism ON ism.code = shd.sizeCode"
                    + " WHERE rqd.docId = $P{docId}"
                    + " AND rqd.v_SNo = $P{VSNo}";
            psTempSubReport = DmPreparedStatement.prepareStatement(cnTemp, subQueryUsage);
            subQueryUsage = getParameter(filter, psTempSubReport);
            HashMap hm = new HashMap();

            jasperCompiled = DmServer.compileJasper("Inventory_RequistionRegister", "", true);
            hm.put("REPORT_CONNECTION", cnTemp);
            hm.putAll(jasperCompiled.getSubReports());
            hm.put("dmSubReportUsage", subQueryUsage);
            hm.put("isUsage", isUsage);

            jasperReport = DmServer.compileJasper("Inventory_RequistionRegister");
            jasperPrint = DmServer.fillJasper(this, "Requisition Register", jasperReport, hm, filter, rsTemp);

            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    public void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(20, "From Date is mandatory.");
        }
        if (DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(40, "To Date is mandatory.");
        }

        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(100, "From Date should be less than or equal to To Date.");
            }
        }
        if ((!DmShared.xNull(hashMapFilter.get("departmentBy").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("departmentTo").getText()).equalsIgnoreCase(""))) {
            if (DmShared.xNull(hashMapFilter.get("departmentBy").getText()).equalsIgnoreCase(DmShared.xNull(hashMapFilter.get("departmentTo").getText()))) {
                throw new DmException(100, "Department By and Department To can't be same.");
            }
        }
        DmShared.isValidText(hashMapFilter.get("site").getTag(), "Site", 200);
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Open");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Cancel");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Complete");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select	dm.subCode as code, sg.name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sg.nature"
                    + "         where sgn.manualCode IN ('CM','PR') and sg.isActive = 1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private String getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("itemStatus").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setString("itemStatus", DmShared.xNull(filter.get("itemStatus").getTag()));
            }

            if (!DmShared.xNull(filter.get("departmentBy").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("departmentBy", DmShared.xNull(filter.get("departmentBy").getTag()));
            }
            if (!DmShared.xNull(filter.get("departmentTo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("departmentTo", DmShared.xNull(filter.get("departmentTo").getTag()));
            }
            if (!DmShared.xNull(filter.get("reqType").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmServer.vNull(filter.get("reqType").getTag(), "0"), true);
            }
            if (!DmShared.xNull(filter.get("requisitionNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("requisitionNo", DmShared.xNull(filter.get("requisitionNo").getTag()));
            }
            if (!DmShared.xNull(filter.get("requisition").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("requisition", DmShared.xNull(filter.get("requisition").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemName", DmShared.xNull(filter.get("itemName").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
            psTemp.executeQuery();
        } catch (Exception e) {
        }
        return psTemp.getQuery();

    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRequisition(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" SELECT sg.subCode as code,sg.name,sg.manualCode "
                    + " FROM subGroup sg where sg.filterNature=:filterNature");
            sqlWithParam.setString("filterNature", DmServer.getCodeViaManualCode("EM", "manualCode", "subgroupNature", "code", cnTemp));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("manualCode")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItemName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItem_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItem_Mdl> lstTemp = new ArrayList<>();
        TxtHItem_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition, sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT im.code,im.name,um.unit AS sku,ig.name itemGroup,ic.name itemcategory,im.manualCode "
                    + " FROM itemMast im "
                    + " LEFT JOIN itemGroup ig ON ig.code = im.itemGroup "
                    + " LEFT JOIN itemCat ic ON ic.code = im.itemCategoryCode "
                    + " LEFT JOIN unitMast um ON um.code = im.purchaseUnit "
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1)  ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItem_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setItemGroup(DmShared.xNull(rsTemp.getString("itemGroup")));
                listNode.setItemCategory(DmShared.xNull(rsTemp.getString("itemcategory")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where sm.code in(" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReqType(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = '" + vCat + "'"
                    + " AND isActive = 1");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHShowUsage(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getTxtHRequisitionNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHRequisitionNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHRequisitionNo_Mdl> lstTemp = new ArrayList<TxtHRequisitionNo_Mdl>();
        TxtHRequisitionNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select	rq.docId, rq.recId as requisitionNo, rq.v_Date as requisitionDate, "
                    + "   rq.v_Type "
                    + "   from requisition as rq "
                    + "   LEFT JOIN voucher_Type vt ON rq.v_Type=vt.v_Type "
                    + "   where  rq.site_Code =:site_Code "
                    + "   and vt.category = '" + vCat + "'"
                    + "   and  isNull(rq.approvedBy,'') <> '' ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHRequisitionNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("requisitionNo")));
                listNode.setRequisitionDate(DmShared.dNull(rsTemp.getDate("requisitionDate")));
                listNode.setRequisitionVType(DmShared.xNull(rsTemp.getString("v_Type")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResultPageing<KeyFieldExtended> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();
            
            mrTemp.setSharedModifiedBy("2");
            mrTemp.setSharedPreparedBy("Both");

            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }
    @Override
    public DmResultPageing<List<KeyFieldExtended>> TxtHHavingStock(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Both");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }
}
