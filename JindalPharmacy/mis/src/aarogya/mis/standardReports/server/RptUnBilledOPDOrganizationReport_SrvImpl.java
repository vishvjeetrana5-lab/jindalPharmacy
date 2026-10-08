package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.*;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum.BillingNature;
import aarogya.selfToolkit.stStatic.shared.STEnum.PaymentSource;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.*;
import dataman.dmBase.self.server.DmIJasperCompiled;
import dataman.dmBase.self.shared.*;
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

public class RptUnBilledOPDOrganizationReport_SrvImpl extends DmRSSPrivate implements RptUnBilledOPDOrganizationReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vrCatOPD = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "'";
    private final String vrCatDayC = "'" + pvc.DAY_CARE + "'";
    private final String vrCatLab = "'" + pvc.LAB_REGISTRATION + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery, queryPLA = "", queryIPD = "", vrCatPLA = "", vrCatPr = "";
        String conditionPLA, conditionIPD, condition = "";
        HashMap hm = new HashMap();

        try {
            RptUnBilledOPDOrganizationReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            conditionIPD = " and (pr.v_Date between :fromDate and :toDate)";
            conditionPLA = " and (pla.v_Date between :fromDate and :toDate)";
            if (DmShared.xNull(filter.get("reportFor").getTag()).trim().contains(pvc.OPD)) {
                vrCatPLA = vrCatOPD;
            }
            if (DmShared.xNull(filter.get("reportFor").getTag()).trim().contains(pvc.DAY_CARE)) {
                vrCatPLA = vrCatDayC;
            }
            if (DmShared.xNull(filter.get("reportFor").getTag()).trim().contains(pvc.LAB_REGISTRATION)) {
                vrCatPLA = vrCatLab;
            }

            if (DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("")) {
                vrCatPLA = vrCatLab + "," + vrCatDayC + "," + vrCatOPD;

            }

            if (!DmShared.xNull(filter.get("organization").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and tbl.organizationCode in (" + DmServer.getParamList(":subCode", filter.get("organization").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("uhid").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and tbl.uhid in (" + DmServer.getParamList(":uhid", filter.get("uhid").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("")) {
                conditionPLA += " and vt.category in (" + vrCatPLA + " )";
                conditionIPD += " and vt.category in (" + vrCatPr + " )";
            }

            if (DmShared.xNull(filter.get("reportFor").getTag()).trim().contains(pvc.OPD)
                    || DmShared.xNull(filter.get("reportFor").getTag()).trim().contains(pvc.DAY_CARE)
                    || DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("")
                    || DmShared.xNull(filter.get("reportFor").getTag()).trim().contains(pvc.LAB_REGISTRATION)) {
                queryPLA = "select  pr.regDocId, max(pr.v_Date) as v_Date, max(pr.recId) as regNo, max(pr.dobDisplay) as dobDisplay, "
                        + "         max(pr.v_No) as v_No, max(pla.vrCategory) as vrCategory, max(pr.organizationCode) as organizationCode, "
                        + "         max(pr.departmentCode) as departmentCode, max(pr.consultantCode) as consultantCode, max(pr.uhid) as uhid, "
                        + "         max(pr.regTime) as regTime, max(pla.vShortName) as short_Name, sum(psd.oh_Amt_Net) as oh_Amt_Net "
                        + "         from ( "
                        + "             select  pla.regDocId, max(vt.short_Name) as vShortName, max(vt.category) as vrCategory "
                        + "             from patientLimitApproval pla "
                        + "             left join voucher_type vt on pla.v_type = vt.v_Type "
                        + "             where pla.nature = :naturePLA and pla.isCancelled = 0 and pla.site_Code = :siteCode " + conditionPLA
                        + "             group by pla.regDocId"
                        + "		) as pla "
                        + "		left join patientServiceDetail psd on psd.regDocId = pla.regDocId"
                        + "		left join saleBill sb on sb.docId = psd.sbDocId "
                        + "		left join patientRegistration pr on pr.regDocId = pla.regDocId "
                        + "		where isNull(sb.approvedBy,'') = '' and psd.billingNature <> :billingNature "
                        + "             and psd.isCancelled = 0 and psd.site_Code = :siteCode "
                        + "		group by pr.regDocId "
                        + "             having sum(psd.oh_Amt_Net) <> 0 ";
            }
            if (DmShared.xNull(filter.get("reportFor").getTag()).trim().contains(pvc.EMERGENCY)
                    || DmShared.xNull(filter.get("reportFor").getTag()).trim().contains(pvc.IPD)
                    || DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("")) {
                queryIPD = " select  pr.regDocId, max(pr.v_Date) as v_Date, max(pr.recId) as regNo, max(pr.dobDisplay) as dobDisplay, "
                        + "          max(pr.v_No) as v_No, max(vt.category) as vrCategory, max(pr.organizationCode) as organizationCode, "
                        + "          max(pr.departmentCode) as departmentCode, max(pr.consultantCode) as consultantCode, max(pr.uhid) as uhid, "
                        + "          max(pr.regTime) as regTime, max(vt.short_Name) as short_Name, sum(psd.oh_Amt_Net) as oh_Amt_Net "
                        + "          from patientRegistration pr "
                        + "          left join voucher_type vt on pr.v_type = vt.v_Type "
                        + "          left join patientServiceDetail psd on psd.regDocId = pr.regDocId "
                        + "          left join saleBill sb on sb.docId = psd.sbDocId "
                        + "          left join organizationMast om on om.subCode = pr.organizationCode "
                        + "          where isNull(sb.approvedBy,'') = '' and psd.isCancelled = 0 and psd.site_Code = :siteCode "
                        + "          and psd.billingNature <> :billingNature and om.isTPA = 1 " + conditionIPD
                        + "          group by pr.regDocId "
                        + "          having sum(psd.oh_Amt_net) <> 0 ";
            }

            sqlQuery = "select  tbl.regDocId, tbl.v_Date, tbl.regNo, tbl.dobDisplay, pu.patientName, pu.recId as uhidNo, org.name as organization, "
                    + "         con.name as consultant, gen.shortName as gender, dep.name as department, tbl.oh_Amt_Net,pu.address1,pu.guardianName "
                    + "		from ( " + queryPLA;
            if (!queryPLA.trim().equalsIgnoreCase("") && !queryIPD.trim().equalsIgnoreCase("")) {
                sqlQuery += " union all ";
            }
            sqlQuery += queryIPD
                    + " ) as tbl "
                    + "	left join patientUHID pu on pu.docId = tbl.uhid "
                    + "	left join patientUHID2 pu2 on pu2.uhid = tbl.uhid "
                    + "	left join subGroup org on org.subCode = tbl.organizationCode "
                    + "	left join subGroup con on con.subCode = tbl.consultantCode "
                    + "	left join subGroup dep on dep.subCode = tbl.departmentCode "
                    + "	left join hl7Mast gen on gen.code = pu2.genderCode "
                    + " where 1 = 1 " + condition
                    + "	order by org.name, tbl.v_Date, tbl.regTime, tbl.short_Name, tbl.v_No, tbl.regNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.xNull(filter.get("reportFor").getTag()).trim().contains(pvc.OPD)
                    || DmShared.xNull(filter.get("reportFor").getTag()).trim().contains(pvc.DAY_CARE)
                    || DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("")
                    || DmShared.xNull(filter.get("reportFor").getTag()).trim().contains(pvc.LAB_REGISTRATION)) {
                psTemp.setInt("naturePLA", PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue());
            }
            psTemp.setInt("billingNature", BillingNature.NON_BILLABLE.getId());
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this), true);
            if (!DmShared.xNull(filter.get("organization").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(filter.get("organization").getTag()), true);
            }
            if (!DmShared.xNull(filter.get("uhid").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("uhid", DmShared.xNull(filter.get("uhid").getTag()), true);
            }
            rsTemp = psTemp.executeQuery();
            DmIJasperCompiled compileJasper = DmServer.compileJasper("MIS_RptUnBilledOPDOrganizationRegister", "", false);
            jasperReport = compileJasper.getReport();
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
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select om.subCode, sg.name "
                    + " from organizationMast om "
                    + " left join subGroup sg on sg.subCode = om.subCode "
                    + " left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + " where sg.isActive = 1 and sgsl.site_Code = :siteCode and om.isTPA = 1 ");
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

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportFor(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.OPD);
            listNode.setSharedKeyFieldName(pvc.OPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.DAY_CARE);
            listNode.setSharedKeyFieldName("Day Care");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.LAB_REGISTRATION);
            listNode.setSharedKeyFieldName("Diagnostic");
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

    @Override
    public DmResultPageing<List<TxtHUHID_Mdl>> getTxtHUHID(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHUHID_Mdl>> rstTemp = new DmResultPageing<List<TxtHUHID_Mdl>>();
        List<TxtHUHID_Mdl> lstTemp = new ArrayList<TxtHUHID_Mdl>();
        TxtHUHID_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select  pu.docId, pu.recId, pu.patientName, pu.guardianName, pu.dobDisplay, "
                    + "		gen.name as gender, pu.mobile, pu.v_Date, pu.v_No, vt.short_Name "
                    + "		from patientUHID pu "
                    + "		left join voucher_Type vt on vt.v_Type = pu.v_Type "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pu.docId "
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "		where pu.isCancelled = 0 and isnull(pu.approvedBy, '') <> '' and pu.isExpired = 0 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHUHID_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setPatient(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setGuardian(DmShared.xNull(rsTemp.getString("guardianName")));
                listNode.setDisplayDOB(DmShared.xNull(rsTemp.getString("dobDisplay")));
                listNode.setGender(DmShared.xNull(rsTemp.getString("gender")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
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
