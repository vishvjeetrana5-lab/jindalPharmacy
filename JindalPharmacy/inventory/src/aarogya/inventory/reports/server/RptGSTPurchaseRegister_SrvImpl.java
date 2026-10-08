package aarogya.inventory.reports.server;

import aarogya.inventory.reports.shared.RptGSTPurchaseRegister_Srv;
import aarogya.inventory.reports.shared.RptGSTPurchaseRegister_Vld;
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

public class RptGSTPurchaseRegister_SrvImpl extends DmRSSPrivate implements RptGSTPurchaseRegister_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTempSubReport = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        DmIJasperCompiled jasperCompiled = null;
        String sqlQuery = "", condition = "", subQueryTotalTax = "";

        try {
            RptGSTPurchaseRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND pb.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND pb.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("partyType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pb.partyType IN (" + DmServer.getParamList(":partyType", filter.get("partyType").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  pb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("vType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  pb.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("vType").getTag()) + " ) ";
            }
            if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("S")) {
                sqlQuery = "SELECT "
                        + " case when  MAX(pb.partyType)='" + DmEnum.DmPartyType.Unregistered.getId() + "' then '" + DmEnum.DmPartyType.Unregistered.getValue() + "'"
                        + " when MAX(pb.partyType)='" + DmEnum.DmPartyType.Composite.getId() + "' then '" + DmEnum.DmPartyType.Composite.getValue() + "'"
                        + " when MAX(pb.partyType)='" + DmEnum.DmPartyType.Registered.getId() + "' then '" + DmEnum.DmPartyType.Registered.getValue() + "'"
                        + " end as supType,"
                        + " case when MAX(pb.partyType)='" + DmEnum.DmPartyType.Unregistered.getId() + "' then '' else MAX(sg.gsTIN) end as gstIN,"
                        + " MAX(pb.subCode) as supplierCode,MAX(sg.name) as supplierName ,"
                        + " CASE WHEN isNull(MAX(c.cityName),'') <>''  THEN ' ,'+MAX(c.cityName)  END  as supCity  , "
                        + " pb.docId, MAX(pb.recId) as recId , MAX(pb.v_Date) as v_Date,MAX(pb.partyBillNo) as partyBillNo, MAX(pb.partyBillDate) as partyBillDate,"
                        + " case when MAX(vt.category) IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "')  then  0-isNull(SUM(pbd.oh_Amt_Gross),0) else  SUM(pbd.oh_Amt_Gross) end as amount, "
                        + " case when MAX(vt.category) IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "')  then  0-isNull(SUM(pbd.oh_Amt_Taxable),0) else  SUM(pbd.oh_Amt_Taxable) end as oh_Amt_Taxable, "
                        + " case when MAX(vt.category) IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "')  then  0-isNull(SUM(pbd.oh_Amt_SGST),0) else  SUM(pbd.oh_Amt_SGST) end as SGST, "
                        + " case when MAX(vt.category) IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "')  then  0-isNull(SUM(pbd.oh_Amt_UGST),0) else  SUM(pbd.oh_Amt_UGST) end as UGST, "
                        + " case when MAX(vt.category) IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "')  then  0-isNull(SUM(pbd.oh_Amt_CGST),0) else  SUM(pbd.oh_Amt_CGST) end as CGST, "
                        + " case when MAX(vt.category) IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "')  then  0-isNull(SUM(pbd.oh_Amt_IGST),0) else  SUM(pbd.oh_Amt_IGST) end as IGST, "
                        + " case when MAX(vt.category) IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "')  then 0-isNull(SUM(pbd.oh_Amt_Net),0) else  SUM(pbd.oh_Amt_Net) end as netAmount"
                        + " FROM pBill pb "
                        + " LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId "
                        + " LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type "
                        + " LEFT JOIN subgroup sg ON sg.subCode = pb.subCode "
                        + " left join city as c on c.cityCode = sg.cityCode  "
                        + " WHERE vt.category IN ('" + pvc.PURCHASE_BILL + "','" + pvc.PURCHASE_BILL_DIRECT + "','" + pvc.PURCHASE_BILL_UNREGISTERED + "',"
                        + " '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "', '" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') "
                        + " AND ISNULL(pb.approvedBy, '') <> '' " + condition
                        + " GROUP BY pb.docId "
                        + " ORDER BY MAX(sg.name),MAX(pb.partyType),MAX(pb.v_Date),MAX(vt.short_Name), MAX(pb.v_No), MAX(pb.recId)";
            } else {
                sqlQuery = "SELECT "
                        + " case when  pb.partyType='" + DmEnum.DmPartyType.Unregistered.getId() + "' then '" + DmEnum.DmPartyType.Unregistered.getValue() + "'"
                        + " when pb.partyType='" + DmEnum.DmPartyType.Composite.getId() + "' then '" + DmEnum.DmPartyType.Composite.getValue() + "'"
                        + " when pb.partyType='" + DmEnum.DmPartyType.Registered.getId() + "' then '" + DmEnum.DmPartyType.Registered.getValue() + "'"
                        + " end as supType,"
                        + " case when pb.partyType='" + DmEnum.DmPartyType.Unregistered.getId() + "' then '' else sg.gsTIN end as gstIN,"
                        + " pb.subCode as supplierCode,sg.name as supplierName ,"
                        + " CASE WHEN isNull(c.cityName,'') <>''  THEN ', '+c.cityName ELSE '' END  as supCity  , "
                        + " pb.docId,pb.recId ,pb.v_Date,pb.partyBillNo, pb.partyBillDate,"
                        + " ic.name as itemCategory, ig.name as itemGroup,im.name AS itemName , hsn.hsn,"
                        + " case when vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') then  0 - isNull(CASE WHEN ISNULL(pb.calcOn,0) = 1  THEN pbd.acceptQty ELSE pbd.docQty END,0) "
                        + " else (CASE WHEN ISNULL(pb.calcOn,0) = 1  THEN pbd.acceptQty ELSE pbd.docQty END) END as quantity "
                        + " ,pbd.rate ,"
                        + " case when vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') then  0-isNull(pbd.oh_Amt_Gross,0) else  pbd.oh_Amt_Gross end as amount, "
                        + " case when vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') then  0-isNull(pbd.oh_Amt_Taxable,0) else  pbd.oh_Amt_Taxable end as oh_Amt_Taxable, "
                        + " (ISNULL(pbd.oh_At_SGST,0) + ISNULL(pbd.oh_At_UGST,0) + ISNULL(pbd.oh_At_CGST,0) + ISNULL(pbd.oh_At_IGST,0)) as taxPer,"
                        + " case when vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') then  0-isNull(pbd.oh_Amt_SGST,0) else  pbd.oh_Amt_SGST end as SGST, "
                        + " case when vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') then  0-isNull(pbd.oh_Amt_CGST,0) else  pbd.oh_Amt_CGST end as CGST, "
                        + " case when vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') then  0-isNull(pbd.oh_Amt_IGST,0) else  pbd.oh_Amt_IGST end as IGST, "
                        + " case when vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') then  0-isNull(pbd.oh_Amt_UGST,0) else  pbd.oh_Amt_UGST end as UGST, "
                        + " case when vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') then 0-isNull(pbd.oh_Amt_Net,0) else  pbd.oh_Amt_Net end as netAmount"
                        + " FROM pBill pb "
                        + " LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId "
                        + " LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type "
                        + " LEFT JOIN subgroup sg ON sg.subCode = pb.subCode "
                        + " LEFT JOIN itemMast im ON im.code = pbd.itemCode "
                        + " left join itemCat as ic on im.itemCategoryCode=ic.Code"
                        + " left join itemGroup as ig on im.itemGroup=ig.Code"
                        + " left join hsnMast as hsn on im.hsnCode=hsn.Code"
                        + " left join city as c on c.cityCode = sg.cityCode  "
                        + " WHERE vt.category IN ('" + pvc.PURCHASE_BILL + "','" + pvc.PURCHASE_BILL_DIRECT + "','" + pvc.PURCHASE_BILL_UNREGISTERED + "',"
                        + " '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "', '" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') "
                        + " AND ISNULL(pb.approvedBy, '') <> '' " + condition
                        + " ORDER BY sg.name,pb.partyType, pb.v_Date, vt.short_Name, pb.v_No, pb.recId, pbd.v_SNo";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            subQueryTotalTax = "SELECT (ISNULL(pbd.oh_At_SGST,0) + ISNULL(pbd.oh_At_UGST,0) + ISNULL(pbd.oh_At_CGST,0) + ISNULL(pbd.oh_At_IGST,0))  AS taxRate"
                    + " , SUM(CASE WHEN vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') THEN 0 - ISNULL(pbd.oh_Amt_Taxable,0) ELSE ISNULL(pbd.oh_Amt_Taxable,0) END) AS taxableAmount"
                    + " , SUM(CASE WHEN vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') THEN 0 - ISNULL(pbd.oh_Amt_SGST,0) ELSE ISNULL(pbd.oh_Amt_SGST,0) END) AS SGST"
                    + " , SUM(CASE WHEN vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') THEN 0 - ISNULL(pbd.oh_Amt_UGST,0) ELSE ISNULL(pbd.oh_Amt_UGST,0) END) AS UGST"
                    + " , SUM(CASE WHEN vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') THEN 0 - ISNULL(pbd.oh_Amt_CGST,0) ELSE ISNULL(pbd.oh_Amt_CGST,0) END) AS CGST"
                    + " , SUM(CASE WHEN vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') THEN 0 - ISNULL(pbd.oh_Amt_IGST,0) ELSE ISNULL(pbd.oh_Amt_IGST,0) END) AS IGST"
                    + " , SUM((CASE WHEN vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') THEN 0-isNull(pbd.oh_Amt_SGST,0) ELSE isNull(pbd.oh_Amt_SGST,0) END)+"
                    + "       (CASE WHEN vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') THEN 0-isNull(pbd.oh_Amt_CGST,0) ELSE isNull(pbd.oh_Amt_CGST,0) END)+"
                    + "       (CASE WHEN vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') THEN 0-isNull(pbd.oh_Amt_UGST,0) ELSE isNull(pbd.oh_Amt_UGST,0) END)+"
                    + "       (CASE WHEN vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') THEN 0-isNull(pbd.oh_Amt_IGST,0) ELSE isNull(pbd.oh_Amt_IGST,0) END)"
                    + "      ) as totalTaxAmount"
                    + "   FROM   pBillDetail AS pbd"
                    + "   LEFT JOIN pBill AS pb ON pbd.docid=pb.docId"
                    + "   LEFT JOIN voucher_type AS vt ON pb.v_type=vt.v_type"
                    + "   WHERE vt.category IN ('" + pvc.PURCHASE_BILL + "','" + pvc.PURCHASE_BILL_DIRECT + "','" + pvc.PURCHASE_BILL_UNREGISTERED + "',"
                    + "   '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "', '" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') "
                    + "   AND ISNULL(pb.approvedBy, '') <> '' " + condition
                    + "   GROUP BY (ISNULL(pbd.oh_At_SGST,0) + ISNULL(pbd.oh_At_UGST,0) + ISNULL(pbd.oh_At_CGST,0) + ISNULL(pbd.oh_At_IGST,0))";

            psTempSubReport = DmPreparedStatement.prepareStatement(cnTemp, subQueryTotalTax);
            subQueryTotalTax = getParameter(filter, psTempSubReport);
            HashMap hm = new HashMap();

            jasperCompiled = DmServer.compileJasper("Inventory_GSTPurchaseRegisterSummary", "", true);
            hm.put("REPORT_CONNECTION", cnTemp);
            hm.putAll(jasperCompiled.getSubReports());
            hm.put("dmSubReportTotalTax", subQueryTotalTax);

            if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("S")) {
                jasperReport = DmServer.compileJasper("Inventory_GSTPurchaseRegisterSummary");
                jasperPrint = DmServer.fillJasper(this, "GST Purchase Register(Summary) ", jasperReport, hm, filter, rsTemp);
            } else {
                jasperReport = DmServer.compileJasper("Inventory_GSTPurchaseRegisterDetail");
                jasperPrint = DmServer.fillJasper(this, "GST Purchase Register (Detail) ", jasperReport, hm, filter, rsTemp);
            }
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
                if (psTempSubReport != null) {
                    psTempSubReport.close();
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
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
            if (!DmShared.xNull(filter.get("partyType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("partyType", DmShared.xNull(filter.get("partyType").getTag()));
            }
            if (!DmShared.xNull(filter.get("vType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmShared.xNull(filter.get("vType").getTag()));
            }

        } catch (Exception e) {
        }
        return psTemp.getQuery();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Detail");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Summary");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPartyType(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (int i = 0; i < DmEnum.DmPartyType.values().length; i++) {
                DmEnum.DmPartyType cdn = DmEnum.DmPartyType.values()[i];
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(cdn.getId());
                listNode.setSharedKeyFieldName(cdn.getValue());
                lstTemp.add(listNode);
            }

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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVType(DmHelper pageing, boolean blnToEndOfFile) {
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
                    + "    vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_BILL + "','" + pvc.PURCHASE_BILL_DIRECT + "','" + pvc.PURCHASE_BILL_UNREGISTERED + "',"
                    + "   '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "', '" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "') "
                    + " AND isActive=1");
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
}
