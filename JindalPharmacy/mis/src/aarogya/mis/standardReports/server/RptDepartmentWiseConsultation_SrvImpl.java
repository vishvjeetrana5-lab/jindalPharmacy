package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptDepartmentWiseConsultation_Srv;
import aarogya.mis.standardReports.shared.RptDepartmentWiseConsultation_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
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

public class RptDepartmentWiseConsultation_SrvImpl extends DmRSSPrivate implements RptDepartmentWiseConsultation_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "'";

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
            RptDepartmentWiseConsultation_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "and (psd.v_Date between :fromDate and :toDate)";

            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                condition += "and pr.departmentCode in(" + DmServer.getParamList(":departmentCode", filter.get("department").getTag()) + ")";
            }

            sqlQuery = "select  pr.recId as regNo, psd.v_Date, format(ps.v_Time, '00.00') as v_Time, vts.short_Name, pu.recId as uhid, pr.dobDisplay, "
                    + "		dep.name as department, org.manualCode as organization, gen.shortName as gender, psd.oh_Amt_Gross, psd.discount, "
                    + "		psd.oh_Amt_Net, psd.discountReason, ps.preparedBy, pu.mobile, ct.cityName, sm.name as stateName, co.name as country, "
                    + "		case when pr.isPatientOldUHID = 0 then 'New' else 'Old' end as patientStatus, psd.quantity, pr.isPatientOldUHID, "
                    + "		case when isnull(pu.patientInitial, '') = '' then pu.patientName else concat(pu.patientInitial, ' ', pu.patientName) end as patient, "
                    + "		case when isnull(con.initial, '') = '' then con.name else concat(con.initial, ' ', con.name) end as consultant, "
                    + "		case when isnull(dis.initial, '') = '' then dis.name else concat(dis.initial, ' ', dis.name) end as discountBy, "
                    + "		case when isnull(pu.address1, '') = '' then '' else concat(pu.address1, "
                    + "		case when isnull(pu.address2, '') = '' then '' else concat(', ', pu.address2) end) end as address "
                    + "		from patientRegistration pr "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join patientUHID pu on pu.docId = pr.uhid "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pr.uhid "
                    + "		left join patientServiceDetail psd on psd.docId = pr.regDocId "
                    + "		left join patientService ps on ps.DocId = psd.DocId "
                    + "		left join voucher_Type vts on vts.v_Type = ps.v_Type "
                    + "		left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "		left join subGroup org on org.subCode = pr.organizationCode "
                    + "		left join subGroup dep on dep.subCode = pr.departmentCode "
                    + "		left join subGroup con on con.subCode = pr.consultantCode "
                    + "		left join subGroup dis on dis.subCode = psd.discountGivenBy "
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "		left join city ct on ct.cityCode = pu.cityCode "
                    + "		left join stateMast sm on sm.code = ct.stateCode "
                    + "		left join country co on co.code = ct.countryCode "
                    + "		where vt.category in (" + vCat + ") and pr.site_Code = :siteCode and pr.isCancelled = 0 "
                    + "             and psm.shortName = 'CF' " + condition
                    + "		order by dep.name, ps.v_Date, ps.v_Time, vts.short_Name, ps.v_No, ps.recId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(filter.get("department").getTag()), true);
            }

            rsTemp = psTemp.executeQuery();
            HashMap hm = new HashMap();

            jasperReport = DmServer.compileJasper("MIS_DepartmentWiseConsultation");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select  dm.subCode, sg.name "
                    + "		from departmentMast dm "
                    + "		left join subGroup sg on sg.subCode = dm.subCode "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + "		where sgsl.site_Code = :siteCode and sg.isActive = 1");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
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

}
