package aarogya.mis.reports.server;

import aarogya.mis.reports.shared.RptPatientWalletLedger_Model;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import aarogya.mis.reports.shared.TxtHCardReg_Mdl;
import aarogya.mis.reports.shared.RptPatientWalletLedger_Srv;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptPatientWalletLedger_SrvImpl extends DmRSSPrivate implements RptPatientWalletLedger_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT Sm.Code, Sm.Name FROM Sitemast Sm "
                    + "WHERE CODE IN (" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ") ");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIncludeOpening(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Y");
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
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, String widgetName, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", condition = "", mainQuery, conditionOP = "";

        try {
            RptPatientWalletLedger_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  cl.v_Date >= :fromDate ";
                conditionOP += " AND cl.v_Date < :fromDate";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  cl.v_Date <= :toDate ";

            }

            if (!DmShared.xNull(filter.get("cardReg").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  cr.recId IN (" + DmServer.getParamList(":cardReg", filter.get("cardReg").getTag()) + " ) ";
                conditionOP += " AND  cr.recId IN (" + DmServer.getParamList(":cardReg", filter.get("cardReg").getTag()) + " ) ";
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND cl.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
                conditionOP += " AND cl.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            } else {
                condition += " AND cl.site_Code IN (" + DmServer.getParamList(":site_Code ", DmServer.getSsnSiteList(this)) + " )";
                conditionOP += " AND cl.site_Code IN (" + DmServer.getParamList(":site_Code ", DmServer.getSsnSiteList(this)) + " )";
            }
            if (DmShared.xNull(filter.get("includeOpening").getTag()).trim().equalsIgnoreCase("")) {
                filter.get("includeOpening").setTag("Y");
            }

            //======= For Opening Balance =========
            sqlQuery += " select  cr.recId as cardRegNo, cl.v_Date,vt.description, cl.docId, cl.v_SNo, cl.v_Type, vt.short_Name as vrShortName,"
                    + "		case when isnull(cl.recid, '') = '' then prc.recId else cl.recId end as receiptNo, "
                    + "		case when isnull(cl.recid, '') = '' then prc.u_EntDt else grc.u_entDt end as u_entDt, cl.subCode, "
                    + "		case when isnull(sg.initial, '') = '' then sg.name else concat(sg.initial, ' ', sg.name) end as account,"
                    + "		case when cl.v_Date < :fromDate then (isnull(cl.amtDr, 0) - isnull(cl.amtCr, 0)) else 0 end as openingBalance,"
                    + "		case when (cl.v_Date between :fromDate and :toDate) then isnull(cl.amtDr, 0)  else 0 end as amtDr,"
                    + "		case when (cl.v_Date between :fromDate and :toDate) then isnull(cl.amtCr, 0) else 0 end as amtCr,"
                    + "		cl.narration, cl.cardRegDocId, cl.cardNo, cl.cardHolderName, cl.cardAuthorization, cl.cardDate, cl.site_Code, "
                    + "		cl.isCancelled, cr.expiryDate, cr.address, cr.mobile, cr.eMail, ctm.name as paymentMode,"
                    + "		case when prc.regDocId is null then lm.manualCode else pr.recid end as regNo, "
                    + "		case when isnull(cr.initialPrefix, '') = '' then cr.personName else concat(cr.initialPrefix, ' ', cr.personName) end as personName "
                    + "		from cardRegistration cr"
                    + "		left join cardLedger cl on cl.cardRegDocId = cr.docId"
                    + "		left join patientReceipt prc on prc.docId = cl.docId "
                    + "		left join generalReceipt  grc on grc.docId = cl.docId"
                    + "		left join cardTypeMast ctm on ctm.code = cl.cardTypeCode "
                    + "		left join voucher_Type vt on vt.v_Type = cl.v_Type "
                    + "		left join subGroup sg on sg.subCode = cl.subCode "
                    + "		left join patientRegistration pr on pr.regDocId = prc.regDocid "
                    + "		left join ledgerMast lm on lm.code = prc.lmCode"
                    + "		where cl.isCancelled = 0 ";
            sqlQuery += condition;

            if (DmShared.xNull(filter.get("includeOpening").getTag().trim()).equalsIgnoreCase("Y")) {
                sqlQuery += " UNION ALL";

                sqlQuery += " select   max(cr.recId) as cardRegNo, null as v_Date, 'Opening Balance' as description, 0 as docId, 0 as v_SNo,  0 as v_Type, '' as vrShortName,"
                        + "		 '' as receiptNo, null as u_entDt, 0 as subCode, '' as account,  sum(isnull(cl.amtDr, 0) - isnull(cl.amtCr, 0)) as openingBalance, 0 as amtDr, "
                        + "		 0 as amtCr, '' as narration, 0 as cardRegDocId, '' as cardNo, '' as cardHolderName, '' as cardAuthorization, null as cardDate, "
                        + "		 0 as site_Code, 0 as isCancelled, null as expiryDate, '' as address, '' as mobile, '' as eMail, '' as paymentMode, "
                        + "		'' as regNo, "
                        + "		max(case when isnull(cr.initialPrefix, '') = '' then cr.personName else concat(cr.initialPrefix, ' ', cr.personName) end) as personName 	"
                        + "		from cardRegistration cr		"
                        + "		left join cardLedger cl on cl.cardRegDocId = cr.docId	"
                        + "		left join voucher_Type vt on vt.v_Type = cl.v_Type 		"
                        + "		left join subGroup sg on sg.subCode = cl.subCode "
                        + "		where cl.isCancelled = 0 ";
                sqlQuery += conditionOP;
                sqlQuery += "group by cl.cardRegDocId"
                        + " having sum(isnull(cl.amtDr, 0) - isnull(cl.amtCr, 0)) <> 0 ";
//                sqlQuery += " select  cr.recId as cardRegNo, cl.v_Date, 'Opening Balance' as description, 0 as docId, 0 as v_SNo,  0 as v_Type, '' as vrShortName,"
//                        + "		case when isnull(cl.recid, '') = '' then prc.recId else cl.recId end as receiptNo, 0 as subCode, "
//                        + "		case when isnull(sg.initial, '') = '' then sg.name else concat(sg.initial, ' ', sg.name) end as account,"
//                        + "		(isnull(cl.amtDr, 0) - isnull(cl.amtCr, 0)) as openingBalance,"
//                        + "		0 as amtDr,"
//                        + "		0 as amtCr,"
//                        + "		'' as narration, 0 as cardRegDocId, '' as cardNo, '' as cardHolderName, '' as cardAuthorization, null as cardDate, 0 as site_Code, "
//                        + "		0 as isCancelled, null as expiryDate, '' as address, '' as mobile, '' as eMail, '' as paymentMode,"
//                        + "		case when prc.regDocId is null then lm.manualCode else pr.recid end as regNo, "
//                        + "		case when isnull(cr.initialPrefix, '') = '' then cr.personName else concat(cr.initialPrefix, ' ', cr.personName) end as personName "
//                        + "		from cardRegistration cr"
//                        + "		left join cardLedger cl on cl.cardRegDocId = cr.docId"
//                        + "		left join patientReceipt prc on prc.docId = cl.docId "
//                        + "		left join generalReceipt  grc on grc.docId = cl.docId"
//                        + "		left join cardTypeMast ctm on ctm.code = cl.cardTypeCode "
//                        + "		left join voucher_Type vt on vt.v_Type = cl.v_Type "
//                        + "		left join subGroup sg on sg.subCode = cl.subCode "
//                        + "		left join patientRegistration pr on pr.regDocId = prc.regDocid "
//                        + "		left join ledgerMast lm on lm.code = prc.lmCode"
//                        + "		where cl.isCancelled = 0 ";
//                sqlQuery += conditionOP;
            }

            mainQuery = "select * from (" + sqlQuery + ") as tbl order by tbl.cardRegNo, tbl.v_Date, tbl.u_EntDt, tbl.v_SNo ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, mainQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            jasperReport = DmServer.compileJasper("MIS_PatientWalletLedger");
            jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, null, filter, rsTemp);
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
            if (!DmShared.dNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.dNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }

            if (!DmShared.xNull(filter.get("cardReg").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("cardReg", DmShared.xNull(filter.get("cardReg").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            } else {
                psTemp.setStringList("site_Code", DmServer.getSsnSiteList(this));
            }
            if (!DmShared.xNull(filter.get("includeOpening").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("includeOpening", DmShared.xNull(filter.get("includeOpening").getTag()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<TxtHCardReg_Mdl>> getTxtHCardNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCardReg_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCardReg_Mdl> lstTemp = new ArrayList<>();
        TxtHCardReg_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition, sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select pu.recId as uhidName, cr.recId as regNo ,cr.personName,cr.mobile,cr.uhid from cardRegistration cr"
                    + " left join patientUhid pu on pu.docid=cr.uhid";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCardReg_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setPersonName(DmShared.xNull(rsTemp.getString("personName")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setUhidNo(DmShared.xNull(rsTemp.getString("uhidName")));
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
}
