package aarogya.inventory.reports.server;

import aarogya.inventory.reports.shared.RptQuotation_Srv;
import aarogya.inventory.reports.shared.RptQuotation_Vld;
import aarogya.inventory.reports.shared.TxtHItem_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmInventory;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptQuotation_SrvImpl extends DmRSSPrivate implements RptQuotation_Srv {

    private String vCat = "'" + new ProjectVoucherCategory().QUOTATION + "'";
    private String vCatEnquiry = "'" + new ProjectVoucherCategory().INVENTORY_ENQUIRY + "'";
    private String vCatIndent = "'" + new ProjectVoucherCategory().INVENTORY_INDENT + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "";
        String condition = " ", joinIndent = "";

        try {
            RptQuotation_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  q.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  q.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and q.subCode in (" + DmServer.getParamList(":subCode", filter.get("supplier").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("enquiryno").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and qd.enquiryDocId in (" + DmServer.getParamList(":enquiryDocId", filter.get("enquiryno").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("quotationNo").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and q.docId in (" + DmServer.getParamList(":docId", filter.get("quotationNo").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("indent").getTag()).trim().equalsIgnoreCase("")) {
                joinIndent = "INNER JOIN ("
                        + "         SELECT eid.docId, eid.v_SNo "
                        + "         FROM quotationIndentDetail eid "
                        + "         WHERE eid.indentDocid in (" + DmServer.getParamList(":indent", filter.get("indent").getTag()) + " ) "
                        + "         GROUP BY eid.docId, eid.v_SNo "
                        + "         ) vEid ON vEid.docid = qd.docid AND vEid.v_SNo = qd.v_SNo ";

            }
            if (!DmShared.xNull(filter.get("quotationtype").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  q.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("quotationtype").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("itemGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemGroup  in (" + DmServer.getParamList(":itemGroup ", filter.get("itemGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemCategory").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemCategoryCode  in (" + DmServer.getParamList(":itemCategoryCode ", filter.get("itemCategory").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and qd.itemCode  in (" + DmServer.getParamList(":itemCode ", filter.get("itemName").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND   q.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }

            sqlQuery = " select	q.docId, qd.v_SNo, q.recId, vt.short_name AS qType, q.recId, q.v_Date, sgv.name as supplier,  "
                    + "         CASE WHEN isNull(c.cityName,'') <>''  THEN ', '+c.cityName ELSE '' END  as supCity, "
                    + "		im.name as itemname, qd.deliveryDate, qd.oh_Amt_Gross as amount, qd.rate,qd.quantity AS reqqty,qd.oh_Amt_Discount, "
                    + "         qd.oh_Amt_Net AS netAmount, um.unit,q.site_Code,sm.name as siteName,qd.colourCode,icm.name as colourName,"
                    + "         qd.sizeCode,ism.name as sizeName,enq.recId as enquiryNo  "
                    + "		from quotation as q "
                    + "		left join quotationdetail as qd on qd.docId = q.docId "
                    + "" + joinIndent
                    + "		left join enquiry as enq on enq.docId = qd.enquiryDocId "
                    + "		left join voucher_Type as vt on q.v_Type = vt.v_Type "
                    + "		left join subGroup as sgv on q.subCode = sgv.subCode "
                    + "		left join itemMast as im on im.code=qd.itemCode "
                    + "		left join itemGroup as ig on ig.code = im.itemGroup "
                    + "		left join itemCat as ic on ic.code = im.itemCategoryCode "
                    + "		left join unitMast as um on um.code = im.purchaseUnit "
                    + "         LEFT JOIN itemColourMast icm ON icm.code=qd.colourCode "
                    + "     	LEFT JOIN itemSizeMast ism ON ism.code=qd.sizeCode "
                    + "         LEFT JOIN siteMast sm ON sm.code=q.site_Code "
                    + "        left join city as c on c.cityCode = sgv.cityCode "
                    + "		where vt.category = " + vCat + " " + condition
                    + "         ORDER BY q.v_Date, vt.short_Name, q.v_No, q.recId, qd.v_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("Inventory_QuotationRegister");
            jasperPrint = DmServer.fillJasper(this, new pmInventory().QUOTATION_REGISTER.caption, jasperReport, null, filter, rsTemp);

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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(filter.get("supplier").getTag()));
            }
            if (!DmShared.xNull(filter.get("enquiryno").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("enquiryDocId", DmShared.xNull(filter.get("enquiryno").getTag()));
            }
            if (!DmShared.xNull(filter.get("indent").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("indent", DmShared.xNull(filter.get("indent").getTag()));
            }
            if (!DmShared.xNull(filter.get("quotationNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(filter.get("quotationNo").getTag()));
            }
            if (!DmShared.xNull(filter.get("quotationtype").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmServer.vNull(filter.get("quotationtype").getTag(), "0"), true);
            }
            if (!DmShared.xNull(filter.get("itemName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemCode", DmShared.xNull(filter.get("itemName").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemCategory").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemCategoryCode", DmShared.xNull(filter.get("itemCategory").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemGroup", DmShared.xNull(filter.get("itemGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVendorname(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   sg.subCode AS code"
                    + " , sg.name,sg.manualCode,c.cityName as city"
                    + " FROM subGroup sg"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "  left join city as c on c.cityCode = sg.cityCode  "
                    + " WHERE sgn.code IN ("
                    + "         SELECT acf.Nature"
                    + "         FROM AcFilteration acf"
                    + "         LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type"
                    + "         WHERE vt.category IN (" + vCat + ")"
                    + " )";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("city")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEnquiryNo(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT docid,recid FROM enquiry e "
                    + " LEFT JOIN voucher_Type vt ON e.v_Type=vt.v_Type "
                    + "	WHERE vt.category = " + vCatEnquiry
                    + " and  isNull(e.approvedBy,'') <> '' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHQuotationType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" select v_Type as code, description as name from voucher_Type where category = " + vCat + " and isActive = 1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT CODE, NAME, filteritemType FROM itemgroup  where (filterItemType = :filterItemType OR isCommon = 1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select code, name from itemcat  where (filterItemType = :filterItemType OR isCommon = 1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT im.code, im.name,im.manualCode "
                    + " FROM itemMast im "
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1)  ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItem_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHQuotationNo(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT docid,recid FROM quotation q "
                    + " LEFT JOIN voucher_Type vt ON q.v_Type=vt.v_Type "
                    + "	WHERE vt.category = " + vCat);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndent(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" Select docId,recId AS name from indent i "
                    + " LEFT JOIN voucher_Type vt ON i.v_Type=vt.v_Type "
                    + "	WHERE vt.category = " + vCatIndent
                    + " and  isNull(i.approvedBy,'') <> '' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
}
