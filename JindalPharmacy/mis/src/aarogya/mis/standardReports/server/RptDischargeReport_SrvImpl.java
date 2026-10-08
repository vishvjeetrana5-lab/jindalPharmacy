package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
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

public class RptDischargeReport_SrvImpl extends DmRSSPrivate implements RptDischargeReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vrCatIPDBill = "'" + pvc.IPD_BILL + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", groupName = "", groupCode = "", orderBy = "";
        String condition = "", caption = "Consultant";

        try {
            RptDischargeReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition += " and (pd.dischargeDate between :fromDate and :toDate)";

            if (!DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("")) {

                condition += " and pr2.dischargeStatus IN (" + DmServer.getParamList(":dischargeStatus", filter.get("status").getTag()) + ")";
            }

            if (!DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("")) {
                switch (RptDischargeReport_Vld.ReportType.ReportValue(DmShared.iNull(filter.get("type").getTag()).intValue())) {
                    case BillWise:
                        groupCode = "sb.billDate";
                        groupName = "format(sb.billDate, 'dd-MMM-yyyy')";
                        orderBy = "";
                        break;
                    case ConsultantWise:
                        groupCode = "pr.consultantCode";
                        groupName = "(case when isnull(con.initial,'') = '' then con.name else concat(con.initial, ' ', con.name) end)";
                        caption = "Department";
                        orderBy = "groupName,";
                        break;

                    case DepartmentWise:
                        groupCode = "pr.departmentCode";
                        groupName = "dep.name";
                        orderBy = "groupName,";
                        break;

                }

            }

            sqlQuery = "select	" + groupCode + " as groupCode, " + groupName + " as groupName, pr.v_Date, pr.recId as regNo, pr.dobDisplay, "
                    + "         pr.guardianName, pu.recId as uhidNo, pu.patientName, pu.mobile, pd.dischargeDate, rm.roomNo, sb.billNo, sb.billDate, "
                    + "         gen.shortName as gender, prc.netReceiptAmount, psd.discount, org.manualCode as organization, "
                    + "		case when isnull(con.initial,'') = '' then con.name else concat(con.initial, ' ', con.name) end as consultant, "
                    + "		case when sb.regDocId is null then psd.netAmount else sb.netAmount end as netAmount, "
                    + "		round((case when sb.regDocId is null then isnull(psd.netAmount, 0) else isnull(sb.netAmount, 0) end) - isnull(prc.netReceiptAmount, 0), 2) as balance, "
                    + "         case when gen.shortName = 'M' then 1 else 0 end as maleCount, "
                    + "		case when gen.shortName = 'F' then 1 else 0 end as femaleCount, "
                    + "		case when pd.regDocId is null then 0 else 1 end as discharge,"
                    + "         dbo.getAdmitDays(pd.regDocId) as noOfDays, "
                    + "		(stuff((select ',' +  Max(icdx.name)+'' as icdx "
                    + "                     from patientPrescription pp "
                    + "                     left join patientPrescriptionICDX ppICDX on pp.docId = ppICDX.docId  "
                    + "                     LEFT JOIN icdxMast icdx ON icdx.code = ppICDX.icdxCode  "
                    + "                     where pp.regDocId = pr.regDocId  "
                    + "                     ORDER BY Max(icdx.name)  FOR XML PATH(''), TYPE ).value('.[1]','nvarchar(max)'), 1,1,'')) as icdx, "
                    + "		case pr2.dischargeStatus when " + DischargeStatus.BIRTH.getId() + " then '" + DischargeStatus.BIRTH.getValue() + "' "
                    + "                                  when " + DischargeStatus.DNTU.getId() + " then '" + DischargeStatus.DNTU.getValue() + "'"
                    + "                                  when " + DischargeStatus.DOR.getId() + " then '" + DischargeStatus.DOR.getValue() + "'"
                    + "                                  when " + DischargeStatus.EXPIRED.getId() + " then '" + DischargeStatus.EXPIRED.getValue() + "'"
                    + "                                  when " + DischargeStatus.IMPROVED.getId() + " then '" + DischargeStatus.IMPROVED.getValue() + "'"
                    + "                                  when " + DischargeStatus.LAMA.getId() + " then '" + DischargeStatus.LAMA.getValue() + "'"
                    + "                                  when " + DischargeStatus.NO_CHANGE.getId() + " then '" + DischargeStatus.NO_CHANGE.getValue() + "'"
                    + "                                  when " + DischargeStatus.RECOVERED.getId() + " then '" + DischargeStatus.RECOVERED.getValue() + "'"
                    + "                                  when " + DischargeStatus.RECOVERED.getId() + " then '" + DischargeStatus.REFERRED.getValue() + "'"
                    + "                                  when " + DischargeStatus.TRANSFERRED.getId() + " then '" + DischargeStatus.TRANSFERRED.getValue() + "'"
                    + "             end as dischargeStatus "
                    + "		from patientDischarge pd "
                    + "		left join patientRegistration pr on pr.regDocId = pd.regDocId "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join patientRegistration2 pr2 on pr2.regDocId = pr.regDocId "
                    + "		left join patientUHID pu on pu.docId = pr.uhid "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pu.docId "
                    + "		left join roomMast rm on rm.code = pr.roomCode "
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "		left join subGroup org on org.subCode = pr.organizationCode "
                    + "		left join subGroup ref on ref.subCode = pr.referredBy "
                    + "		left join subGroup con on con.subCode = pr.consultantCode "
                    + "		left join subGroup dep on dep.subCode = pr.departmentCode "
                    + "		left join ( "
                    + "                     select  sb.regDocId, sb.recId as billNo, sb.v_Date as billDate, sb.oh_Amt_Net as netAmount,  "
                    + "                             sb.v_No, vt.short_Name "
                    + "                             from saleBill sb  "
                    + "                             left join voucher_Type vt on vt.v_Type = sb.v_Type  "
                    + "                             where isnull(sb.approvedBy, '') <> '' and vt.category in (" + vrCatIPDBill + ") "
                    + "                                 and sb.regDocId <> sb.docId  and sb.site_Code = :siteCode "
                    + "			) sb on sb.regDocId = pd.regDocId "
                    + "		left join ( "
                    + "                     select  psd.regDocId, sum(psd.discount) as discount, sum(psd.oh_Amt_Net) as netAmount  "
                    + "                             from patientServiceDetail psd "
                    + "                             where psd.site_Code = :siteCode and psd.isCancelled = 0 and psd.billingNature <> :billingNature "
                    + "                             group by regDocId "
                    + "			) psd on psd.regDocId = pr.regDocId  "
                    + "		left join ( "
                    + "                     select  prc.regDocId, sum(prc.amount) as netReceiptAmount "
                    + "                             from patientReceipt prc  "
                    + "                             where prc.site_Code = :siteCode and prc.isCancelled = 0  "
                    + "                             group by prc.regDocId "
                    + "			) prc on prc.regDocId = pr.regDocId "
                    + "		where pr.site_Code = :siteCode  " + condition
                    + "		order by " + orderBy + " sb.billDate, sb.short_Name, sb.v_No, sb.billNo, "
                    + "  vt.short_Name, pr.v_No, pr.recId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setInt("billingNature", BillingNature.NON_BILLABLE.getId());
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("dischargeStatus", DmShared.xNull(filter.get("status").getTag()), true);
            }

            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();

            jasperReport = DmServer.compileJasper("MIS_DischargePatientStatusReport");
            jasperPrint = DmServer.fillJasper(this, DmShared.xNull(filter.get("type").getText()) + " " + widgetName, jasperReport, hm, filter, rsTemp);

            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
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
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DischargeStatus status : DischargeStatus.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(status.getId()).toString());
                listNode.setSharedKeyFieldName(status.getValue());
                lstTemp.add(listNode);
            }

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Bill No. Wise");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Consultant Wise");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("3");
            listNode.setSharedKeyFieldName("Department Wise");
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
