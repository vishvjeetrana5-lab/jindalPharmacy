package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptGSTSaleRegister_Srv;
import aarogya.pharmacy.reports.shared.RptGSTSaleRegister_Vld;
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

public class RptGSTSaleRegister_SrvImpl extends DmRSSPrivate implements RptGSTSaleRegister_Srv {

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
            RptGSTSaleRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND sb.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND sb.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("partyType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND sb.partyType IN (" + DmServer.getParamList(":partyType", filter.get("partyType").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("vType").getTag()).trim().equalsIgnoreCase("")) {
//                condition += " AND  sb.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("vType").getTag()) + " ) ";
                condition += " AND  vt.category IN (" + DmServer.getParamList(":v_Type", filter.get("vType").getTag()) + " ) ";
            }
            if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("SPW")
                    || DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("SBW")) {
                sqlQuery = "SELECT  "
                        + " case when  MAX(sb.partyType)='" + DmEnum.DmPartyType.Unregistered.getId() + "' then '" + DmEnum.DmPartyType.Unregistered.getValue() + "'"
                        + " when MAX(sb.partyType)='" + DmEnum.DmPartyType.Composite.getId() + "'  then '" + DmEnum.DmPartyType.Composite.getValue() + "' "
                        + " when MAX(sb.partyType)='" + DmEnum.DmPartyType.Registered.getId() + "'  then '" + DmEnum.DmPartyType.Registered.getValue() + "'  else '' end as supType, "
                        + " case when MAX(sb.partyType)='" + DmEnum.DmPartyType.Unregistered.getId() + "' then '' else MAX(sg.gsTIN) end as gstIN,"
                        + " case when isNull(MAX(sb.subCode),0)<>0 then MAX(sg.subCode) else 0 end as supplierCode,"
                        + " case when isNull(MAX(sb.subCode),0)<>0 then MAX(sg.name) else 'Pharmacy Sale' end as supplierName, "
                        + " case when isNull(MAX(sb.subCode),0)<>0 and isNull(MAX(c.cityName),'') <>'' then ' ,'+MAX(c.cityName)  else '' end  as supCity,"
                        + " sb.docId, MAX(sb.recId) as saleBillNo, MAX(sb.v_Date) as saleBillDate,"
                        + " case when MAX(vt.category) in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "')  then  0-isNull(SUM(sbd.oh_Amt_Gross),0) else  SUM(sbd.oh_Amt_Gross) end as amount,  "
                        + " case when MAX(vt.category) in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') then  0-isNull(SUM(sbd.oh_Amt_Taxable),0) else  SUM(sbd.oh_Amt_Taxable) end as oh_Amt_Taxable,  "
                        + " case when MAX(vt.category) in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "')  then  0-isNull(SUM(sbd.oh_Amt_SGST),0) else  isNull(SUM(sbd.oh_Amt_SGST),0) end as SGST,  "
                        + " case when MAX(vt.category) in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "')  then  0-isNull(SUM(sbd.oh_Amt_UGST),0) else  isNull(SUM(sbd.oh_Amt_UGST),0) end as UGST,  "
                        + " case when MAX(vt.category) in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "')  then  0-isNull(SUM(sbd.oh_Amt_CGST),0) else  isNull(SUM(sbd.oh_Amt_CGST),0) end as CGST,  "
                        + " case when MAX(vt.category) in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "')  then  0-isNull(SUM(sbd.oh_Amt_IGST),0) else  isNull(SUM(sbd.oh_Amt_IGST),0) end as IGST,  "
                        + " case when MAX(vt.category) in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "')  then  0-isNull(SUM(sbd.oh_Amt_Net),0)  else  isNull(SUM(sbd.oh_Amt_Net),0)  end as netAmount "
                        + " FROM saleBill sb  "
                        + " LEFT JOIN saleBillDetail sbd ON sbd.docId = sb.docId  "
                        + " LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type  "
                        + " LEFT JOIN subgroup sg ON sg.subCode = sb.subCode  "
                        + " left join city as c on c.cityCode = sg.cityCode   "
                        + " WHERE 1=1 "
                        //  "vt.category IN ('" + pvc.PHARMACY_BILL_CASH + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "') "
                        + " AND ISNULL(sb.isCancelled,0) = 0 " + condition
                        + " GROUP BY sb.docId "
                        + " ORDER BY MAX(sb.v_Date), MAX(vt.short_Name), MAX(sb.v_No), MAX(sb.recId)";
            } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("SSW")) {
                sqlQuery = " SELECT"
                        + " case when sb.subCode is not null  then c.stateCode else  sm1.code end as stateCode,"
                        + " sb.partyType, "
                        + " max(case when sb.subCode is not null then  sm.name else sitem.stateName end) as stateName ,"
                        + " sum(case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') then  0-isNull(sb.oh_Amt_Gross,0) else  isNull(sb.oh_Amt_Gross,0) end) as amount,  "
                        + " sum(case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "')  then  0-isNull(sb.oh_Amt_Taxable,0) else  isNull(sb.oh_Amt_Taxable,0) end) as oh_Amt_Taxable,  "
                        + " sum(case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "')  then  0-isNull(sb.oh_Amt_SGST,0) else  isNull(sb.oh_Amt_SGST,0) end) as SGST,  "
                        + " sum(case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "')  then  0-isNull(sb.oh_Amt_UGST,0) else  isNull(sb.oh_Amt_UGST,0) end) as UGST,  "
                        + " sum(case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "')  then  0-isNull(sb.oh_Amt_CGST,0) else  isNull(sb.oh_Amt_CGST,0) end) as CGST,  "
                        + " sum(case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "')  then  0-isNull(sb.oh_Amt_IGST,0) else  isNull(sb.oh_Amt_IGST,0) end) as IGST,  "
                        + " sum(case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "')  then  0-isNull(sb.oh_Amt_Net,0)  else  isNull(sb.oh_Amt_Net,0)  end) as netAmount "
                        + " FROM saleBill sb  "
                        + " LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type  "
                        + " LEFT JOIN subgroup sg ON sg.subCode = sb.subCode  "
                        + " left join city as c on c.cityCode = sg.cityCode   "
                        + " left join stateMast as sm on c.stateCode = sm.Code   "
                        + " left join siteMast as sitem on sb.site_Code=sitem.code"
                        + " left join stateMast as sm1 on sitem.stateNumericCode=sm1.numericCode"
                        + " WHERE 1=1"
                        //                        + " vt.category IN ('" + pvc.PHARMACY_BILL_CASH + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "')  "
                        + " AND ISNULL(sb.isCancelled,0) = 0 " + condition
                        + " group by  case when sb.subCode is not null  then c.stateCode else  sm1.code end, sb.partyType"
                        + " ORDER BY max(case when sb.subCode is not null then  sm.name else sitem.stateName end), sb.partyType";

            } else {
                sqlQuery = "SELECT  "
                        + " case when  sb.partyType='" + DmEnum.DmPartyType.Unregistered.getId() + "' then '" + DmEnum.DmPartyType.Unregistered.getValue() + "'"
                        + " when sb.partyType='" + DmEnum.DmPartyType.Composite.getId() + "'  then '" + DmEnum.DmPartyType.Composite.getValue() + "'  "
                        + " when sb.partyType='" + DmEnum.DmPartyType.Registered.getId() + "' then '" + DmEnum.DmPartyType.Registered.getValue() + "' else '' end as supType, "
                        + " case when sb.partyType='" + DmEnum.DmPartyType.Unregistered.getId() + "' then '' else sg.gsTIN end as gstIN, "
                        + " case when isNull(sb.subCode,0)<>0 then sg.subCode else 0 end as supplierCode,"
                        + " case when isNull(sb.subCode,0)<>0 then sg.name else 'Pharmacy Sale' end as supplierName, "
                        + " case when isNull(sb.subCode,0)<>0 and isNull(c.cityName,'') <>'' then ' ,'+c.cityName  else '' end  as supCity,"
                        + " sb.docId,sb.recId ,sb.v_Date,im.name AS itemName , hsn.hsn,"
                        + "  case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') then 0-isNull(sbd.quantity,0) else sbd.quantity end as quantity,sbd.rate , "
                        + " case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') then  0-isNull(sbd.oh_Amt_Gross,0) else  sbd.oh_Amt_Gross end as amount,  "
                        + " case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') then  0-isNull(sbd.oh_Amt_Taxable,0) else  sbd.oh_Amt_Taxable end as oh_Amt_Taxable,  "
                        + " (ISNULL(sbd.oh_At_SGST,0) + ISNULL(sbd.oh_At_UGST,0) + ISNULL(sbd.oh_At_CGST,0) + ISNULL(sbd.oh_At_IGST,0))  as taxPer, "
                        + " case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') then  0-isNull(sbd.oh_Amt_SGST,0) else  isNull(sbd.oh_Amt_SGST,0) end as SGST,  "
                        + " case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') then  0-isNull(sbd.oh_Amt_CGST,0) else  isNull(sbd.oh_Amt_CGST,0) end as CGST,  "
                        + " case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') then  0-isNull(sbd.oh_Amt_IGST,0) else  isNull(sbd.oh_Amt_IGST,0) end as IGST,  "
                        + " case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') then  0-isNull(sbd.oh_Amt_UGST,0) else  isNull(sbd.oh_Amt_UGST,0) end as UGST,  "
                        + " case when vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') then  0-isNull(sbd.oh_Amt_Net,0) else  isNull(sbd.oh_Amt_Net,0) end as netAmount "
                        + " FROM saleBill sb  "
                        + " LEFT JOIN saleBillDetail sbd ON sbd.docId = sb.docId  "
                        + " LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type  "
                        + " LEFT JOIN subgroup sg ON sg.subCode = sb.subCode  "
                        + " LEFT JOIN itemMast im ON im.code = sbd.itemCode  "
                        + " left join itemCat as ic on im.itemCategoryCode=ic.Code "
                        + " left join itemGroup as ig on im.itemGroup=ig.Code "
                        + " left join hsnMast as hsn on im.hsnCode=hsn.Code "
                        + " left join city as c on c.cityCode = sg.cityCode   "
                        + " WHERE 1=1"
                        //                        + "vt.category IN ('" + pvc.PHARMACY_BILL_CASH + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "')  "
                        + " AND ISNULL(sb.isCancelled, 0) =0  " + condition
                        + " ORDER BY sg.name,sb.partyType, sb.v_Date, vt.short_Name, sb.v_No, sb.recId, sbd.v_SNo";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            subQueryTotalTax = " SELECT (ISNULL(sbd.oh_At_SGST,0) + ISNULL(sbd.oh_At_UGST,0) + ISNULL(sbd.oh_At_CGST,0) + ISNULL(sbd.oh_At_IGST,0))  AS taxRate"
                    + ", SUM(CASE WHEN vt.category in( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') THEN 0 - ISNULL(sbd.oh_Amt_Taxable,0) ELSE ISNULL(sbd.oh_Amt_Taxable,0) END) AS taxableAmount"
                    + ", SUM(CASE WHEN vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') THEN 0 - ISNULL(sbd.oh_Amt_SGST,0) ELSE ISNULL(sbd.oh_Amt_SGST,0) END) AS SGST"
                    + ", SUM(CASE WHEN vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') THEN 0 - ISNULL(sbd.oh_Amt_UGST,0) ELSE ISNULL(sbd.oh_Amt_UGST,0) END) AS UGST"
                    + ", SUM(CASE WHEN vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') THEN 0 - ISNULL(sbd.oh_Amt_CGST,0) ELSE ISNULL(sbd.oh_Amt_CGST,0) END) AS CGST"
                    + ", SUM(CASE WHEN vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') THEN 0 - ISNULL(sbd.oh_Amt_IGST,0) ELSE ISNULL(sbd.oh_Amt_IGST,0) END) AS IGST"
                    + ", SUM("
                    + "       (CASE WHEN vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') THEN 0-isNull(sbd.oh_Amt_SGST,0) ELSE isNull(sbd.oh_Amt_SGST,0) END)+"
                    + "       (CASE WHEN vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') THEN 0-isNull(sbd.oh_Amt_CGST,0) ELSE isNull(sbd.oh_Amt_CGST,0) END)+"
                    + "       (CASE WHEN vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') THEN 0-isNull(sbd.oh_Amt_UGST,0) ELSE isNull(sbd.oh_Amt_UGST,0) END)+"
                    + "       (CASE WHEN vt.category in ( '" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_RETURN + "') THEN 0-isNull(sbd.oh_Amt_IGST,0) ELSE isNull(sbd.oh_Amt_IGST,0) END)"
                    + "	 ) as totalTaxAmount"
                    + " FROM   saleBillDetail AS sbd"
                    + " LEFT JOIN salebill AS sb ON sbd.docid=sb.docId"
                    + " LEFT JOIN voucher_type AS vt ON sb.v_type=vt.v_type"
                    + " WHERE 1=1 "
                    //                    + "vt.category IN ('" + pvc.PHARMACY_BILL_CASH + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "')  "
                    + " AND ISNULL(sb.isCancelled, 0) = 0 " + condition
                    + " GROUP BY (ISNULL(sbd.oh_At_SGST,0) + ISNULL(sbd.oh_At_UGST,0) + ISNULL(sbd.oh_At_CGST,0) + ISNULL(sbd.oh_At_IGST,0))";

            psTempSubReport = DmPreparedStatement.prepareStatement(cnTemp, subQueryTotalTax);
            subQueryTotalTax = getParameter(filter, psTempSubReport);
            HashMap hm = new HashMap();

            jasperCompiled = DmServer.compileJasper("Pharmacy_GSTSaleRegisterSummary", "", true);
            hm.put("REPORT_CONNECTION", cnTemp);
            hm.putAll(jasperCompiled.getSubReports());
            hm.put("dmSubReportTotalTax", subQueryTotalTax);

            if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("SPW")) {
                jasperReport = DmServer.compileJasper("Pharmacy_GSTSaleRegisterSummary");
                jasperPrint = DmServer.fillJasper(this, "GST Sale Register ", jasperReport, hm, filter, rsTemp);
            } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("SBW")) {
                jasperReport = DmServer.compileJasper("Pharmacy_GSTSaleRegisterSummaryBillWise");
                jasperPrint = DmServer.fillJasper(this, "GST Sale Register ", jasperReport, hm, filter, rsTemp);
            } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("SSW")) {
                jasperReport = DmServer.compileJasper("Pharmacy_GSTSaleRegisterSummaryStateWise");
                jasperPrint = DmServer.fillJasper(this, "GST Sale Register ", jasperReport, hm, filter, rsTemp);
            } else {
                jasperReport = DmServer.compileJasper("Pharmacy_GSTSaleRegisterDetail");
                jasperPrint = DmServer.fillJasper(this, "GST Sale Register ", jasperReport, hm, filter, rsTemp);
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
            listNode.setSharedKeyFieldCode("SPW");
            listNode.setSharedKeyFieldName("Summary Party Wise");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("SBW");
            listNode.setSharedKeyFieldName("Summary Bill Wise");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("SSW");
            listNode.setSharedKeyFieldName("Summary State Wise");
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
            ////////////////// 17-oct-2022 ///////////////////////////////
//            sqlWithParam.setSQL("SELECT"
//                    + "    vt.v_Type AS code"
//                    + " , vt.description AS name"
//                    + " FROM voucher_Type vt"
//                    + " WHERE vt.category IN ('" + pvc.PHARMACY_BILL_CASH + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "') "
//                    + " AND isActive=1");
//            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                listNode = new KeyFieldExtended();
//                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
//                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
//                lstTemp.add(listNode);
//            }

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("PHBCS");
            listNode.setSharedKeyFieldName("Cash Sale");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("PHCSR");
            listNode.setSharedKeyFieldName("Cash Sale Return");
            lstTemp.add(listNode);
            
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("PHBIL");
            listNode.setSharedKeyFieldName("Credit Sale");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("PHBRT");
            listNode.setSharedKeyFieldName("Credit Sale Return");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(pageing, lstTemp);
            rstTemp.setData(lstTemp);

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
