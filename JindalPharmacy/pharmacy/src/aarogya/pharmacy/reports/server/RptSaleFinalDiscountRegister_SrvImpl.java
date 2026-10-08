package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptSaleFinalDiscountRegister_Srv;
import aarogya.pharmacy.reports.shared.RptSaleFinalDiscountRegister_Vld;
import aarogya.pharmacy.reports.shared.TxtHLMCode_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptSaleFinalDiscountRegister_SrvImpl extends DmRSSPrivate implements RptSaleFinalDiscountRegister_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", conditionDetail = " ", conditionSummary = "", conditionItem = "";

        try {
            RptSaleFinalDiscountRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and  sb.v_Date >= :fromDate ";
                conditionSummary += " and  sb.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and  sb.v_Date <= :toDate ";
                conditionSummary += " and  sb.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("voucherNo").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.docId IN (" + DmServer.getParamList(":docId", filter.get("voucherNo").getTag()) + " ) ";
                conditionSummary += " AND  sb.docId IN (" + DmServer.getParamList(":docId", filter.get("voucherNo").getTag()) + " ) ";
            }

            if (!DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.OPD)) {
                    conditionDetail += " and  vtpr.category = ('" + pvc.OPD + "') and isNull(lm.regDocId,'')<>''";
                    conditionSummary += " and  vtpr.category = ('" + pvc.OPD + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.IPD)) {
                    conditionDetail += " and  vtpr.category = ('" + pvc.IPD + "') and isNull(lm.regDocId,'')<>''";
                    conditionSummary += " and  vtpr.category = ('" + pvc.IPD + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
                    conditionDetail += " and  vtpr.category = ('" + pvc.EMERGENCY + "') and isNull(lm.regDocId,'')<>''";
                    conditionSummary += " and  vtpr.category = ('" + pvc.EMERGENCY + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.DAY_CARE)) {
                    conditionDetail += " and  vtpr.category = ('" + pvc.DAY_CARE + "') and isNull(lm.regDocId,'')<>'' ";
                    conditionSummary += " and  vtpr.category = ('" + pvc.DAY_CARE + "') and isNull(lm.regDocId,'')<>'' ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("L")) {
                    conditionDetail += " and isNull(lm.subCode,'')<>''  ";
                    conditionSummary += " and isNull(lm.subCode,'')<>''  ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("W")) {
                    conditionDetail += " and isNull(lm.subCode,'')= '' and isNull(lm.regDocId,'')=''  ";
                    conditionSummary += " and isNull(lm.subCode,'')= '' and isNull(lm.regDocId,'')=''  ";
                }
            }
            if (!DmShared.xNull(filter.get("phAccount").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  lm.code IN (" + DmServer.getParamList(":phAccount", filter.get("phAccount").getTag()) + " ) ";
                conditionSummary += " AND  lm.code IN (" + DmServer.getParamList(":phAccount", filter.get("phAccount").getTag()) + " ) ";
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
                conditionSummary += " AND  sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            } else {
                conditionDetail += " AND  sb.site_Code in (" + DmServer.getSsnSiteCode(this) + ")";
                conditionSummary += " AND  sb.site_Code in (" + DmServer.getSsnSiteCode(this) + ")";
            }
            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("S")) {
                sqlQuery = " SELECT "
                        + "  sb.recid as voucherNo"
                        + " , sb.docId "
                        + " , sb.v_Date as voucherDate"
                        + " , vt.short_Name as type"
                        + " , vt.description"
                        + " , vtpr.short_Name as regType "
                        + " , CASE WHEN isNull(lm.Initial,'')<>'' then concat(lm.initial,'',lm.displayName) else lm.displayName END as patientName"
                        + " , pr.recid as regNo"
                        + " , sb.oh_Amt_Gross "
                        + " , sb.oh_Amt_Discount "
                        + " , sb.oh_Amt_Taxable "
                        + " , sb.oh_Amt_BAMT "
                        + " , sb.oh_Amt_UGST"
                        + " , sb.oh_Amt_SGST"
                        + " , sb.oh_Amt_CGST"
                        + " , sb.oh_Amt_Net "
                        + " , sb.oh_Amt_Roundoff"
                        + " FROM saleBIll as sb"
                        + " LEFT JOIN ledgerMast as lm on sb.lmCode=lm.Code"
                        + " LEFT JOIN voucher_Type as vt on sb.v_Type=vt.v_Type"
                        + " LEFT JOIN patientregistration pr ON sb.regDocid=pr.regDocid  "
                        + " LEFT JOIN voucher_Type as vtpr ON pr.v_type=vtpr.v_type"
                        + " LEFT JOIN "
                        + "         ("
                        + "		 SELECT sbd.docId"
                        + "		 from saleBillDetail as sbd"
                        + "		 LEFT JOIN saleBill sb ON sbd.docId = sb.docid"
                        + "		 LEFT JOIN voucher_Type vtp ON vtp.v_Type = sb.v_Type"
                        + "		 LEFT JOIN itemMast im ON im.code = sbd.itemCode"
                        + "		 AND vtp.category=('" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') " + conditionItem
                        + "		 group by sbd.docId"
                        + "         )as sbd on sb.docid=sbd.docid"
                        + " WHERE vt.category=('" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') and sb.isCancelled=0 "
                        + " " + conditionSummary
                        + " order by patientName,sb.v_date";

            } else {
                sqlQuery = " SELECT "
                        + "  sb.docId"
                        + " , sb.recid as voucherNo"
                        + " , sb.v_Date as voucherDate"
                        + " , vt.short_Name as type"
                        + " , sb.v_Type"
                        + " , case when isNull(lm.Initial,'')<>'' then concat(lm.initial,'',lm.displayName) else lm.displayName end as patientName"
                        + " , pr.recid as regNo"
                        + " , vtpr.short_Name as regType "
                        + " , umsale.unit AS saleUnitName"
                        + " , sbd.oh_Amt_Gross"
                        + " , sbd.oh_Amt_Discount"
                        + " , sbd.oh_Amt_Taxable"
                        + " , sbd.oh_Amt_UGST"
                        + " , sbd.hsnCode"
                        + " , hm.hsn as hsnName"
                        + " , ISNULL(sbd.oh_Amt_SGST,0) as oh_Amt_SGST"
                        + " , ISNULL(sbd.oh_Amt_CGST,0) as oh_Amt_CGST"
                        + " , ISNULL(sbd.oh_Amt_IGST,0) as oh_Amt_IGST"
                        + " , ISNULL(sbd.oh_Amt_Net,0) as oh_Amt_Net"
                        + " , ISNULL(sb.oh_Amt_RoundOff,0) as oh_Amt_RoundOff"
                        + " FROM saleBIll as sb"
                        + " LEFT JOIN ledgerMast as lm on sb.lmCode=lm.Code"
                        + " LEFT JOIN voucher_Type as vt on sb.v_Type=vt.v_Type"
                        + " LEFT JOIN saleBillDetail as sbd on sb.docId=sbd.docId"
                        + " LEFT JOIN patientregistration pr ON sb.regDocid = pr.regDocid  "
                        + " LEFT JOIN voucher_Type as vtpr ON pr.v_type=vtpr.v_type"
                        + " LEFT JOIN itemMast im ON im.code = sbd.itemCode "
                        + " LEFT JOIN hsnMast AS hm ON sbd.hsnCode = hm.code "
                        + " LEFT JOIN unitMast AS umsale ON umsale.code = sbd.saleUnitCode "
                        + " WHERE sbd.isFooter=0 "
                        + " AND sb.isCancelled=0   "
                        + " AND vt.category=('" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "')" + conditionDetail
                        + " ORDER BY  patientName,sb.v_Date, vt.short_Name, sb.v_No, sb.recId, sbd.v_SNo ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            LinkedHashMap hm = new LinkedHashMap();

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("S")) {
                jasperReport = DmServer.compileJasper("Pharmacy_SaleFinalDiscountRegister_Summary");
                jasperPrint = DmServer.fillJasper(this, "Sale Final Discount Report (Summary) ", jasperReport, hm, filter, rsTemp);
            } else {
                jasperReport = DmServer.compileJasper("Pharmacy_SaleFinalDiscountRegister_Detail");
                jasperPrint = DmServer.fillJasper(this, "Sale Final Discount Report (Detail) ", jasperReport, hm, filter, rsTemp);
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
            if (!DmShared.xNull(filter.get("voucherNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(filter.get("voucherNo").getTag()));
            }
            if (!DmShared.xNull(filter.get("phAccount").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("phAccount", DmShared.xNull(filter.get("phAccount").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }

        } catch (Exception e) {
        }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String vCat = "'ESTMT'";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   sb.docId "
                    + " , sb.recId AS name"
                    + " FROM saleBill sb"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type"
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') and sb.isCancelled=0");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
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
    public DmResultPageing<List<TxtHLMCode_Mdl>> getTxtHLMCode(String type, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHLMCode_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHLMCode_Mdl> lstTemp = new ArrayList<>();
        TxtHLMCode_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(type).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.OPD)) {
                    condition += " and  vt.category = ('" + pvc.OPD + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.IPD)) {
                    condition += " and  vt.category = ('" + pvc.IPD + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
                    condition += " and  vt.category = ('" + pvc.EMERGENCY + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.DAY_CARE)) {
                    condition += " and  vt.category = ('" + pvc.DAY_CARE + "') and isNull(lm.regDocId,'')<>'' ";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase("L")) {
                    condition += " and isNull(lm.subCode,'')<>''  ";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase("W")) {
                    condition += " and isNull(lm.subCode,'')= '' and isNull(lm.regDocId,'')=''  ";
                }
            }
            sqlWithParam.setSQL("select lm.code,lm.name,lm.manualCode,c.cityName,pr.recid as regNo,sg.name as acName"
                    + " from ledgerMast as lm"
                    + " left join city as c on lm.cityCode=c.cityCode"
                    + " left join patientRegistration as pr on lm.regDocId=pr.regDocId"
                    + " left join voucher_Type as vt on vt.v_Type=pr.v_type"
                    + " left join subGroup as sg on lm.subCode=sg.subCode"
                    + " where 1=1 " + condition);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHLMCode_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setAcName(DmShared.xNull(rsTemp.getString("acName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.IPD);
            listNode.setSharedKeyFieldName(pvc.IPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.OPD);
            listNode.setSharedKeyFieldName(pvc.OPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.DAY_CARE);
            listNode.setSharedKeyFieldName("Day Care");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.EMERGENCY);
            listNode.setSharedKeyFieldName("Emergency");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("L");
            listNode.setSharedKeyFieldName("Ledger");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("W");
            listNode.setSharedKeyFieldName("Walk-In");
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

}
