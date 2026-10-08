package aarogya.mis.reports.server;

import aarogya.mis.reports.shared.RptPatientSummary_Srv;
import aarogya.mis.reports.shared.RptPatientSummary_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
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

public class RptPatientSummary_SrvImpl extends DmRSSPrivate implements RptPatientSummary_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCatUHID = pvc.UHID;

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery, conditionIn = "", conditionOut = "", havingClause = "";
        RptPatientSummary_Vld.ReportView reportView;
        try {
            RptPatientSummary_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("organization").getTag()).trim().equalsIgnoreCase("")) {
                conditionOut += " and (pu.organizationCode in (" + DmServer.getParamList(":organization", filter.get("organization").getTag()) + "))";
            }

//            if (!DmShared.xNull(filter.get("referredBy").getTag()).trim().equalsIgnoreCase("")) {
//                conditionIn += " and (pr.referredBy in (" + DmServer.getParamList(":referredBy", filter.get("referredBy").getTag()) + "))";
//            }
            if (!DmShared.xNull(filter.get("area").getTag()).trim().equalsIgnoreCase("")) {
                conditionOut += " and (pu.zoneCode in (" + DmServer.getParamList(":area", filter.get("area").getTag()) + "))";
            }

            reportView = RptPatientSummary_Vld.ReportView.getByText(filter.get("status").getTag());
            switch (reportView) {
                case BOTH:
                    havingClause = " having round(sum((isnull(t.serviceAmt,0) - isnull(t.recAmt, 0)) + isnull(t.ctrAmt,0)),2) <> 0 ";
                    break;
                case PENDING:
                    havingClause = " having round(sum((isnull(t.serviceAmt,0) - isnull(t.recAmt, 0)) + isnull(t.ctrAmt,0)),2) > 0 ";
                    break;
                case TO_REFUND:
                    havingClause = " having round(sum((isnull(t.serviceAmt,0) - isnull(t.recAmt, 0)) + isnull(t.ctrAmt,0)),2) < 0 ";
                    break;
            }

            sqlQuery = "select  max(pu.recId) as uhid, max(pu.mobile) as mobile, max(gen.shortName) as gender, max(pu.dobDisplay) as dobDisplay, "
                    + "		max(ct.cityName) as cityName, max(sm.name) as stateName, max(co.name) as country, max(zm.name) as zone, "
                    + "		max(org.name) as organization, "
                    + "		max(case when isnull(ref.initial, '') = '' then ref.name else concat(ref.initial, ' ', ref.name) end) as referredBy, "
                    + "		max(case when isnull(pu.patientInitial, '') = '' then pu.patientName else concat(pu.patientInitial, ' ', pu.patientName) end) as patient, "
                    + "		max(case when isnull(pu.address1, '') = '' then '' else concat(pu.address1, (case when isnull(pu.address2, '') = '' "
                    + "		then '' else concat(', ', pu.address2) end)) end) as patientAddress, "
                    + "		round(sum(case when t.docDate < :fromDate then (isnull(t.serviceAmt,0) - (isnull(t.recAmt, 0)+ isnull(t.ctrAmt,0))) else 0.0 end),2) as opening, "
                    + "		round(sum(case when t.docDate < :fromDate then 0.0 else t.serviceAmt end),2) as serviceAmt, "
                    + "		round(sum(case when t.docDate < :fromDate then 0.0 else t.recAmt end),2) as recAmt, "
                    + "		round(sum(case when t.docDate < :fromDate then 0.0 else t.ctrAmt end),2) as ctrAmt, "
                    + "		round(sum(isnull(t.serviceAmt,0) - (isnull(t.recAmt, 0)+ isnull(t.ctrAmt,0))),2) as balance "
                    + "		from ( "
                    + "		select	pr.uhid, psd.v_Date as docDate, pr.referredBy, pr.organizationCode, psd.oh_Amt_Net as serviceAmt,0 as recAmt, "
                    + "			0 as ctrAmt, 4 as unionType "
                    + "			from patientServiceDetail psd "
                    + "			left join patientService ps on ps.docId = psd.DocId "
                    + "			left join patientRegistration pr on pr.regDocId = psd.regDocId "
                    + "			where pr.isCancelled = 0 and psd.isCancelled = 0 and psd.billingNature <> :billingNature and psd.v_Date <= :toDate "
                    + "			and psd.site_Code = :siteCode "
                    + "		union all "
                    + "		select  pr.uhid, prc.v_Date as docDate, pr.referredBy, pr.organizationCode, 0 as serviceAmt, "
                    + "			isnull(cl.amtDr,0) - isnull(cl.amtCr,0) as recAmt, 0 as ctrAmt, 3 as unionType  "
                    + "			from patientReceipt prc "
                    + "			left join patientRegistration pr on pr.regDocId = prc.regDocId "
                    + "			left join cardLedger cl on cl.docId = prc.docId "
                    + "			where pr.isCancelled = 0 and prc.isCancelled = 0 and prc.site_Code = :siteCode and prc.v_Date <= :toDate "
                    + "			and prc.lmCode is null "
                    + "		union all "
                    + "		select  pr.uhid, prc.v_Date as docDate, pr.referredBy, pr.organizationCode, 0 as serviceAmt, "
                    + "			isnull(cl.amtDr,0) - isnull(cl.amtCr,0) as recAmt, 0 as ctrAmt, 3 as unionType  "
                    + "			from patientReceipt prc "
                    + "			left join ledgerMast lm on lm.code = prc.lmCode "
                    + "			left join patientRegistration pr on pr.regDocId = lm.regDocId "
                    + "			left join cardLedger cl on cl.docId = prc.docId "
                    + "			where pr.isCancelled = 0 and prc.isCancelled = 0 and prc.site_Code = :siteCode and prc.v_Date <= :toDate "
                    + "			and prc.regDocId is null "
                    + "		union all "
                    + "		select  pr.uhid, pla.v_Date as docDate, pr.referredBy, pr.organizationCode, 0 as serviceAmt, 0 as recAmt, "
                    + "			pla.amount as ctrAmt, 2 as unionType  "
                    + "			from patientLimitApproval pla "
                    + "			left join patientRegistration pr on pr.regDocId = pla.regDocId "
                    + "			where pr.isCancelled = 0 and pla.isCancelled = 0 and pla.site_Code = :siteCode and pla.nature = :plaNature and pla.v_Date <= :toDate "
                    + "		union all "
                    + "		select  pr.uhid, sb.v_Date as docDate, pr.referredBy, pr.organizationCode, sbd.oh_Amt_Net as serviceAmt, 0 as recAmt, "
                    + "			0 as ctrAmt, 1 as unionType "
                    + "			from saleBill sb "
                    + "			left join patientRegistration pr on pr.regDocId = sb.regDocId "
                    + "			left join saleBillDetail sbd on sbd.docId = sb.docId "
                    + "			left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "			where pr.isCancelled = 0 and sb.isCancelled = 0 and sb.site_Code = :siteCode and sb.v_Date <= :toDate "
                    + "			and vt.category in ('" + pvc.PHARMACY_BILL + "', '" + pvc.PHARMACY_BILL_RETURN + "')  "
                    + "		) as t "
                    + "		left join subGroup ref on ref.subCode = t.referredBy "
                    + "		left join patientUHID pu on pu.docId = t.uhid "
                    + "		left join patientUHID2 pu2 on pu2.uhid = t.uhid "
                    + "		left join subGroup org on org.subCode = pu.organizationCode "
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "		left join city ct on ct.cityCode = pu.cityCode "
                    + "		left join stateMast sm on sm.code = ct.stateCode "
                    + "		left join country co on co.code = ct.countryCode "
                    + "		left join zoneMast zm on zm.code = pu.zoneCode "
                    + "		where pu.isCancelled = 0 and isnull(pu.approvedBy, '') <> '' and pu.site_Code = :siteCode and pu.isExpired = 0 " + conditionOut
                    + "		group by t.uhid " + havingClause
                    + "		order by max(org.name), max(pu.v_Date), max(pu.v_No), max(pu.recId) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()));
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()));
            psTemp.setInt("billingNature", BillingNature.NON_BILLABLE.getId());
            psTemp.setInt("plaNature", PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue());
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            if (!DmShared.xNull(filter.get("organization").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("organization", filter.get("organization").getTag());
            }
//            if (!DmShared.xNull(filter.get("referredBy").getTag()).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("referredBy", filter.get("referredBy").getTag());
//            }
            if (!DmShared.xNull(filter.get("area").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("area", filter.get("area").getTag());
            }
            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();

            jasperReport = DmServer.compileJasper("MIS_PatientSummary");

            jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, hm, filter, rsTemp);

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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOrganization(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  om.subCode, sg.name"
                    + "		from organizationMast om "
                    + "		left join subGroup sg on sg.subCode = om.subCode "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = om.subCode "
                    + "		where sg.isActive = 1 and sgsl.site_Code = :siteCode ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHArea(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        rstTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select code, name from zoneMast");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReferredBy(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        rstTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  cm.subCode, sg.name "
                    + "		from consultantMast cm  "
                    + "		left join subGroup sg on sg.subCode = cm.subCode  "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = cm.subCode  "
                    + "		where sg.isActive = 1 and sgsl.site_Code = :siteCode ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        try {
            for (RptPatientSummary_Vld.ReportView reportView : RptPatientSummary_Vld.ReportView.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(reportView.getText());
                listNode.setSharedKeyFieldName(reportView.getText());
                lstTemp.add(listNode);
            }

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
