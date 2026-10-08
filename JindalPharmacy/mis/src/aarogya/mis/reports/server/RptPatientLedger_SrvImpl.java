package aarogya.mis.reports.server;

import aarogya.mis.reports.shared.RptPatientLedger_Srv;
import aarogya.mis.reports.shared.RptPatientLedger_Vld;
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

public class RptPatientLedger_SrvImpl extends DmRSSPrivate implements RptPatientLedger_Srv {

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
        String sqlQuery, condition = "";

        try {
            RptPatientLedger_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select   pu.docId, pu.recId, pr.regCount "
                    + "		from patientUHID pu "
                    + "		left join voucher_Type vt on vt.v_Type = pu.v_Type "
                    + "		left join ( "
                    + "			select  count(pr.regDocId) as regCount, pr.uhid, max(pr.regDocId) as regDocId "
                    + "				from patientRegistration pr "
                    + "				left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "				where pr.isCancelled = 0 and vt.category <> '" + vCatUHID + "'"
                    + "				group by pr.uhid"
                    + "		) pr on pr.uhid = pu.docId "
                    + "		where isnull(pu.approvedBy, '') <> '' and pu.isCancelled = 0 and pu.isExpired = 0 "
                    + "			and pr.regDocId is not null and pu.recId = :recId "
                    + "		order by pr.regCount desc, pu.v_Date desc, vt.short_Name, pu.v_No desc, pu.recId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("recId", filter.get("uhid").getText());
//            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                filter.get("uhid").setTag(DmShared.xNull(rsTemp.getString("docId")));
            } else {
                throw new DmException(0, "No record found. Try again.");
            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(filter.get("diagnosticCategory").getTag()).trim().equalsIgnoreCase("")) {
                condition += "and pscm.filterNature=1";
                condition += " and (pscm.code in (" + DmServer.getParamList(":diagnosticCategory", filter.get("diagnosticCategory").getTag()) + "))";
            }

            sqlQuery = "select  max(pu.recId) as uhid, max(pu.mobile) as mobile, max(gen.shortName) as gender, max(pu.dobDisplay) as dobDisplay, "
                    + "		max(ct.cityName) as cityName, max(sm.name) as stateName, max(co.name) as country, max(zm.name) as zone, "
                    + "		max(case when isnull(pu.patientInitial, '') = '' then pu.patientName else concat(pu.patientInitial, ' ', pu.patientName) end) as patient, "
                    + "		max(case when isnull(pu.address1, '') = '' then '' else concat(pu.address1, (case when isnull(pu.address2, '') = '' "
                    + "		then '' else concat(', ', pu.address2) end)) end) as patientAddress, "
                    + "		max(case when t.docDate < :fromDate then 0 else t.docId end) as docId, "
                    + "		max(case when t.docDate < :fromDate then 0 else t.regVType end) as regVType, "
                    + "		max(case when t.docDate < :fromDate then 0 else t.v_SNo end) as v_SNo, "
                    + "		max(case when t.docDate < :fromDate then 0 else t.regDocId end) as regDocId, "
                    + "		max(case when t.docDate < :fromDate then '' else t.regNo end) as regNo, "
                    + "		max(case when t.docDate < :fromDate then '' else t.docNo end) as docNo, "
                    + "		max(case when t.docDate < :fromDate then null else t.docDate end) as docDate, "
                    + "		max(case when t.docDate < :fromDate then '' else t.remark end) as remark,"
                    + "		max(case when t.docDate < :fromDate then 'Opening' else psm.name end) as serviceName,"
                    + "		max(case when t.docDate < :fromDate then 'Opening' else im.name end) as itemName,"
                    + "		max(case when t.docDate < :fromDate then '' else con.name end) as consultantName, "
                    + "		max(case when t.docDate < :fromDate then 'Opening' else (case when t.unionType = 2  "
                    + "		then '" + PaymentSource.CREDIT_TO_REGISTRATION.getEnumName() + "' else ctm.name end) end) as cardTypeName, "
                    + "		sum(case when t.docDate < :fromDate then 0.0 else t.serviceQty end) as serviceQty, "
                    + "		sum(case when t.docDate < :fromDate then t.serviceAmt else 0.0 end) as serviceAmt_OP, "
                    + "		sum(case when t.docDate < :fromDate then t.recAmt else 0.0 end) as recAmt_OP, "
                    + "		sum(case when t.docDate < :fromDate then t.ctrAmt else 0.0 end) as ctrAmt_OP, "
                    + "		sum(case when t.docDate < :fromDate then (isnull(t.serviceAmt,0) - (isnull(t.recAmt, 0) + isnull(t.ctrAmt,0))) else 0.0 end) as opening, "
                    + "		sum(case when t.docDate < :fromDate then 0.0 else t.serviceAmt end) as serviceAmt, "
                    + "		sum(case when t.docDate < :fromDate then 0.0 else t.recAmt end) as recAmt, "
                    + "		sum(case when t.docDate < :fromDate then 0.0 else t.ctrAmt end) as ctrAmt, "
                    + "		sum(isnull(t.serviceAmt,0) - (isnull(t.recAmt, 0) + isnull(t.ctrAmt, 0))) as balance,"
                    + "		max(case when t.category in ('" + pvc.OPD + "', '" + pvc.EMERGENCY_OPD + "', '" + pvc.UHID + "','" + pvc.PAY_CLINIC + "') then 'OPD' else "
                    + "		case when t.category in ('" + pvc.PHARMACY_BILL + "', '" + pvc.PHARMACY_BILL_RETURN + "') then 'Pharmacy' else "
                    + "		case when t.category in ('" + pvc.DAY_CARE + "') then 'Day Care' else case when t.category in ('" + pvc.EMERGENCY + "') then "
                    + "		'Emergency' else t.category end end end end) as moduleType, "
                    + "          max(pscm.code) as diagnosticCategoryCode  "
                    + "		from ( "
                    + "		select	pr.uhid, psd.docId, pr.v_Type as regVType, psd.v_SNo, psd.regDocId, pr.recId as regNo, ps.recId as docNo, "
                    + "			ps.u_EntDt as docTime,psd.v_Date as docDate, psd.serviceCode, 0 as cardTypeCode, 0 as itemCode, "
                    + "			psd.remark as remark, psd.quantity as serviceQty, psd.consultantCode, psd.oh_Amt_Net as serviceAmt, 0 as recAmt, "
                    + "			0 as ctrAmt, 4 as unionType, vt.category "
                    + "			from patientServiceDetail psd "
                    + "			left join patientService ps on ps.docId = psd.DocId "
                    + "			left join patientRegistration pr on pr.regDocId = psd.regDocId "
                    + "			left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "			where pr.isCancelled = 0 and psd.isCancelled = 0 and psd.billingNature <> :billingNature and "
                    + "			psd.site_Code = :siteCode and psd.v_Date <= :toDate and pr.uhid = :uhid "
                    + "		union all "
                    + "		select  pr.uhid, prc.docId, pr.v_Type as regVType, cl.v_SNo, prc.regDocId, pr.recId as regNo, prc.recId as docNo, "
                    + "			prc.u_EntDt as docTime, prc.v_Date as docDate, 0 as serviceCode, cl.cardTypeCode, 0 as itemCode, "
                    + "			prc.remark as remark, 0 as serviceQty, 0 as consultantCode, 0 as serviceAmt, "
                    + "			isnull(cl.amtDr,0) - isnull(cl.amtCr,0) as recAmt, 0 as ctrAmt, 3 as unionType, vt.category "
                    + "			from patientReceipt prc "
                    + "			left join patientRegistration pr on pr.regDocId = prc.regDocId "
                    + "			left join cardLedger cl on cl.docId = prc.docId "
                    + "			left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "			where pr.isCancelled = 0 and prc.isCancelled = 0 and prc.site_Code = :siteCode and prc.v_Date <= :toDate "
                    + "			and pr.uhid = :uhid and prc.lmCode is null "
                    + "		union all "
                    + "		select  pr.uhid, prc.docId, pr.v_Type as regVType, cl.v_SNo, lm.regDocId, pr.recId as regNo, prc.recId as docNo, "
                    + "			prc.u_EntDt as docTime, prc.v_Date as docDate, 0 as serviceCode, cl.cardTypeCode, "
                    + "			0 as itemCode, prc.remark as remark, 0 as serviceQty, 0 as consultantCode, 0 as serviceAmt, "
                    + "			isnull(cl.amtDr,0) - isnull(cl.amtCr,0) as recAmt, 0 as ctrAmt, 3 as unionType, vt.category "
                    + "			from patientReceipt prc "
                    + "			left join ledgerMast lm on lm.code = prc.lmCode	"
                    + "			left join patientRegistration pr on pr.regDocId = lm.regDocId "
                    + "			left join cardLedger cl on cl.docId = prc.docId "
                    + "			left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "			where pr.isCancelled = 0 and prc.isCancelled = 0 and prc.site_Code = :siteCode and prc.v_Date <= :toDate "
                    + "			and pr.uhid = :uhid and prc.regDocId is null "
                    + "		union all "
                    + "		select  pr.uhid, pla.docId, pr.v_Type as regVType, " + pvc.HIDDEN_SERVICES_START_INDEX + " as v_SNo, pla.regDocId, "
                    + "			pr.recId as regNo, pla.recId as docNo, pla.u_EntDt as docTime, pla.v_Date as docDate, "
                    + "			0 as serviceCode, 0 as cardTypeCode, 0 as itemCode, pla.remark as remark, 0 as serviceQty, "
                    + "			0 as consultantCode, 0 as serviceAmt, 0 as recAmt, pla.amount as ctrAmt, 2 as unionType, vt.category "
                    + "			from patientLimitApproval pla "
                    + "			left join patientRegistration pr on pr.regDocId = pla.regDocId "
                    + "			left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "			where pr.isCancelled = 0 and pla.isCancelled = 0 and pla.site_Code = :siteCode and pla.nature = :plaNature "
                    + "			and pla.v_Date <= :toDate and pr.uhid = :uhid "
                    + "		union all "
                    + "		select  pr.uhid, sb.docId, pr.v_Type as regVType, sbd.v_SNo, sb.regDocId, pr.recId as regNo, sb.recId as docNo, "
                    + "			sb.u_EntDt as docTime, sb.v_Date as docDate, 0 as serviceCode, 0 as cardTypeCode, "
                    + "			sbd.itemCode, sb.remark as remark, sbd.quantity as serviceQty, 0 as consultantCode, sbd.oh_Amt_Net as serviceAmt, "
                    + "			0 as recAmt, 0 as ctrAmt, 1 as unionType, vt.category "
                    + "			from saleBill sb "
                    + "			left join patientRegistration pr on pr.regDocId = sb.regDocId "
                    + "			left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "			left join saleBillDetail sbd on sbd.docId = sb.docId "
                    + "			where pr.isCancelled = 0 and sb.isCancelled = 0 and sb.site_Code = :siteCode and sb.v_Date <= :toDate "
                    + "			and vt.category in ('" + pvc.PHARMACY_BILL + "', '" + pvc.PHARMACY_BILL_RETURN + "') and sb.uhid = :uhid "
                    + "		) as t "
                    + "		left join patientServiceMast psm on psm.code = t.serviceCode "
                    + "         left join patientServiceCatMast pscm on pscm.code = psm.categoryCode"
                    + "		left join subGroup con on con.subCode = t.consultantCode "
                    + "		left join cardTypeMast ctm on ctm.code = t.cardTypeCode "
                    + "		left join itemMast im on im.code = t.itemCode "
                    + "		left join patientUHID pu on pu.docId = t.uhid "
                    + "		left join patientUHID2 pu2 on pu2.uhid = t.uhid "
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "		left join city ct on ct.cityCode = pu.cityCode "
                    + "		left join stateMast sm on sm.code = ct.stateCode "
                    + "		left join country co on co.code = ct.countryCode "
                    + "		left join zoneMast zm on zm.code = pu.zoneCode "
                    + "         where 1=1 " + condition
                    + "		group by t.uhid, "
                    + "		(case when t.docDate < :fromDate then 0 else t.docId end), "
                    + "		(case when t.docDate < :fromDate then 0 else t.v_SNo end), "
                    + "		(case when t.docDate < :fromDate then 0 else t.unionType end) "
                    + "		order by max(case when t.docDate < :fromDate then null else t.docDate end), max(t.docTime),"
                    + "		max(case when t.docDate < :fromDate then '' else t.docTime end), "
                    + "		max(case when t.docDate < :fromDate then '' else t.docNo end), "
                    + "		max(case when t.docDate < :fromDate then 0 else t.v_SNo end),"
                    + "		max(case when t.docDate < :fromDate then 0 else t.unionType end)";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()));
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()));
            psTemp.setInt("billingNature", BillingNature.NON_BILLABLE.getId());
            psTemp.setInt("plaNature", PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue());
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("uhid", filter.get("uhid").getTag());
            if (!DmShared.xNull(filter.get("diagnosticCategory").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("diagnosticCategory", filter.get("diagnosticCategory").getTag());
            }
            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();

            jasperReport = DmServer.compileJasper("MIS_PatientLedger");

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDiagnosticCategory(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select code, name from patientServiceCatMast where filterNature=1");
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

}
