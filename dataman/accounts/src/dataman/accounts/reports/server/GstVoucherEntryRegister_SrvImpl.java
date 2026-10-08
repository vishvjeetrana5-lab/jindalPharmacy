package dataman.accounts.reports.server;

import dataman.accounts.reports.shared.GstVoucherEntryRegister_Srv;
import dataman.accounts.reports.shared.GstVoucherEntryRegister_Vld;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmIJasperCompiled;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class GstVoucherEntryRegister_SrvImpl extends DmRSSPrivate implements GstVoucherEntryRegister_Srv {

    private LibraryVoucherCategory lvc = new LibraryVoucherCategory();

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
            GstVoucherEntryRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND lm.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND lm.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("vType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  lm.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("vType").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("partyType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND lm.consigneeType IN (" + DmServer.getParamList(":partyType", filter.get("partyType").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  lm.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }
            if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("S")) {
//                        + " , max(lg.hsnCode) as hsnCode "
//                        + " , max(hm.name) as hsn "
                sqlQuery = " SELECT "
                        + "   case when  MAX(lm.consigneeType)='" + DmEnum.DmPartyType.Unregistered.getId() + "' then '" + DmEnum.DmPartyType.Unregistered.getValue() + "'"
                        + "   when MAX(lm.consigneeType)='" + DmEnum.DmPartyType.Composite.getId() + "' then '" + DmEnum.DmPartyType.Composite.getValue() + "' "
                        + "   when MAX(lm.consigneeType)='" + DmEnum.DmPartyType.Registered.getId() + "' then '" + DmEnum.DmPartyType.Registered.getValue() + "' end as partyType "
                        + " , case when MAX(lm.consigneeType)='" + DmEnum.DmPartyType.Unregistered.getId() + "' then '' else MAX(sg.gsTIN) end as gstIN "
                        + " , MAX(lm.subCode) as subCode "
                        + " , MAX(sg.name) as partyName "
                        + " , CASE WHEN isNull(MAX(c.cityName),'') <>''  THEN ' ,'+MAX(c.cityName)  END  as consigneeCity  "
                        + " , lm.docId, MAX(lm.recId) as recId"
                        + " , MAX(lm.v_Date) as v_Date "
                        + " , MAX(l.refNo) as partyBillNo "
                        + " , MAX(l.refDate) as partyBillDate "
                        + " , SUM(lg.grossAmt) as amount "
                        + " , SUM(lg.taxableValue) as taxableAmt "
                        + " , SUM(lg.cgstAmt) as CGST "
                        + " , SUM(lg.sgstAmt) as SGST "
                        + " , SUM(lg.ugstAmt) as UGST "
                        + " , SUM(lg.igstAmt) as IGST "
                        + " , SUM(lg.sgstAmt+lg.ugstAmt) as SGSTUGST "
                        + " , SUM(lg.netAmt) as netAmt "
                        + " FROM ledgerM lm  "
                        + " LEFT JOIN ledgerGst lg ON lg.docId = lm.docId "
                        + " LEFT JOIN hsnMast hm ON hm.code=lg.hsnCode "
                        + " LEFT JOIN (select docId,max(refNo) as refNo,max(refDate) as refDate from ledger GROUP BY docId) l ON l.docId=lm.docId "
                        + " LEFT JOIN voucher_Type vt ON vt.v_Type = lm.v_Type  "
                        + " LEFT JOIN subgroup sg ON sg.subCode = lm.subCode  "
                        + " left join city as c on c.cityCode = sg.cityCode   "
                        + " WHERE "
                        + " vt.category IN ('" + lvc.CREDIT_INVOICE_GST + "', '" + lvc.CREDIT_NOTE_GST + "', '" + lvc.DEBIT_NOTE_GST + "', '" + lvc.DEBIT_INVOICE_GST + "')"
                        + " " + condition
                        + " GROUP BY lm.docId "
                        + " ORDER BY MAX(sg.name),MAX(lm.consigneeType),MAX(lm.v_Date), MAX(vt.short_Name), MAX(lm.v_No), MAX(lm.recId) ";
            } else {
                sqlQuery = " SELECT  "
                        + "   case when  lm.consigneeType='" + DmEnum.DmPartyType.Unregistered.getId() + "' then '" + DmEnum.DmPartyType.Unregistered.getValue() + "' "
                        + "   when lm.consigneeType='" + DmEnum.DmPartyType.Composite.getId() + "' then '" + DmEnum.DmPartyType.Composite.getValue() + "' "
                        + "   when lm.consigneeType='" + DmEnum.DmPartyType.Registered.getId() + "' then '" + DmEnum.DmPartyType.Registered.getValue() + "' end as partyType "
                        + " , case when lm.consigneeType ='" + DmEnum.DmPartyType.Unregistered.getId() + "' then '' else sg.gsTIN end as gstIN "
                        + " , lm.subCode  "
                        + " , sg.name as partyName "
                        + " , CASE WHEN isNull(c.cityName,'') <>''  THEN ', '+c.cityName ELSE '' END  as consigneeCity  "
                        + " , lm.docId "
                        + " , lm.recId "
                        + " , lm.v_Date "
                        + " , l.refNo as partyBillNo "
                        + " , l.refDate as partyBillDate "
                        + " , hsn.hsn "
                        + " , lg.grossAmt as amount "
                        + " , lg.taxableValue as taxableAmt "
                        + " , (ISNULL(lg.sgstPer,0) + ISNULL(lg.ugstPer,0) + ISNULL(lg.cgstPer,0) + ISNULL(lg.igstPer,0))  as taxPer "
                        + " , lg.sgstAmt  as SGST"
                        + " , lg.cgstAmt  as CGST"
                        + " , lg.igstAmt  as IGST"
                        + " , lg.ugstAmt  as UGST"
                        + " , (lg.sgstAmt+lg.ugstAmt) as SGSTUGST "
                        + " , lg.netAmt as netAmount "
                        + " FROM ledgerM lm  "
                        + " LEFT JOIN ledgergst lg ON lg.docId = lm.docId  "
                        + " LEFT JOIN (select docId,max(refNo) as refNo,max(refDate) as refDate from ledger GROUP BY docId) l ON l.docId=lm.docId "
                        + " LEFT JOIN voucher_Type vt ON vt.v_Type = lm.v_Type  "
                        + " LEFT JOIN subgroup sg ON sg.subCode = lm.subCode  "
                        + " LEFT JOIN hsnMast as hsn on lg.hsnCode=hsn.Code "
                        + " LEFT JOIN city as c on c.cityCode = sg.cityCode   "
                        + " WHERE vt.category IN ('" + lvc.CREDIT_INVOICE_GST + "', '" + lvc.CREDIT_NOTE_GST + "', '" + lvc.DEBIT_NOTE_GST + "', '" + lvc.DEBIT_INVOICE_GST + "')"
                        + " " + condition
                        + " ORDER BY sg.name,lm.consigneeType, lm.v_Date, vt.short_Name, lm.v_No, lm.recId, lg.v_SNo ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            subQueryTotalTax = " SELECT "
                    + " (ISNULL(lg.cgstPer,0) + ISNULL(lg.sgstPer,0) + ISNULL(lg.ugstPer,0) + ISNULL(lg.igstPer,0))  AS taxRate "
                    + " , SUM(ISNULL(lg.taxableValue,0)) AS taxableAmount "
                    + " , SUM(ISNULL(lg.cgstAmt,0)) AS CGST "
                    + " , SUM(ISNULL(lg.sgstAmt,0)) AS SGST "
                    + " , SUM(ISNULL(lg.ugstAmt,0)) AS UGST "
                    + " , SUM(ISNULL(lg.igstAmt,0)) AS IGST "
                    + " , SUM(ISNULL(lg.sgstAmt,0)+ISNULL(lg.ugstAmt,0)) AS sgstUgstAmt "
                    + " , SUM( "
                    + "	  (ISNULL(lg.cgstAmt,0)) "
                    + "	+ (ISNULL(lg.sgstAmt,0)) "
                    + "	+ (ISNULL(lg.igstAmt,0)) "
                    + "	+ (ISNULL(lg.ugstAmt,0)) "
                    + "	) as totalTaxAmount "
                    + " FROM ledgerGst lg "
                    + " LEFT JOIN ledgerM lm ON lm.docId = lg.docId "
                    + " LEFT JOIN voucher_type vt ON lm.v_type = vt.v_type "
                    + " WHERE vt.category IN ('" + lvc.CREDIT_INVOICE_GST + "', '" + lvc.CREDIT_NOTE_GST + "', '" + lvc.DEBIT_NOTE_GST + "', '" + lvc.DEBIT_INVOICE_GST + "')"
                    + " GROUP BY (ISNULL(lg.cgstPer,0) + ISNULL(lg.sgstPer,0) + ISNULL(lg.ugstPer,0) + ISNULL(lg.igstPer,0))";

            psTempSubReport = DmPreparedStatement.prepareStatement(cnTemp, subQueryTotalTax);
            subQueryTotalTax = getParameter(filter, psTempSubReport);
            HashMap hm = new HashMap();

            jasperCompiled = DmServer.compileJasper("Ac_GSTVoucherRegisterSummary", "", true);
            hm.put("REPORT_CONNECTION", cnTemp);
            hm.putAll(jasperCompiled.getSubReports());
            hm.put("dmSubReportTotalTax", subQueryTotalTax);

            if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("S")) {
                jasperReport = DmServer.compileJasper("Ac_GSTVoucherRegisterSummary");
                jasperPrint = DmServer.fillJasper(this, "GST Vocuher Register(Summary) ", jasperReport, hm, filter, rsTemp);
            } else {
                jasperReport = DmServer.compileJasper("Ac_GSTVoucherRegisterDetail");
                jasperPrint = DmServer.fillJasper(this, "GST Vocuher Register (Detail) ", jasperReport, hm, filter, rsTemp);
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
        String strSQL;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSQL = " SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
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
                    + " WHERE vt.category IN ('" + lvc.CREDIT_INVOICE_GST + "', '" + lvc.CREDIT_NOTE_GST + "', '" + lvc.DEBIT_NOTE_GST + "', '" + lvc.DEBIT_INVOICE_GST + "')"
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
