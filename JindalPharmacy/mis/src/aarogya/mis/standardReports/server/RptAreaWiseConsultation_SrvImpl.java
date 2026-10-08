package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptAreaWiseConsultation_Vld;
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
import aarogya.mis.standardReports.shared.RptAreaWiseConsultation_Srv;

public class RptAreaWiseConsultation_SrvImpl extends DmRSSPrivate implements RptAreaWiseConsultation_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String category = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String query = "", sqlQuery, reportName = "", condition = "";

        try {
            RptAreaWiseConsultation_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "and (psd.v_Date between :fromDate and :toDate)";

            if (!DmShared.xNull(filter.get("areaType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND zm.code IN (" + DmServer.getParamList(":areaType", filter.get("areaType").getTag()) + " )";
            }
            sqlQuery = "select  pr.recId as regNo, psd.v_Date, format(ps.v_Time, '00.00') as v_Time, vts.short_Name, pu.recId as uhid, pr.dobDisplay,"
                    + "		dep.name as department, org.manualCode as organization, gen.shortName as gender, psd.oh_Amt_Gross, psd.discount, "
                    + "		psd.oh_Amt_Net, psd.discountReason, ps.preparedBy, pu.mobile, ct.cityName, sm.name as stateName, co.name as country, "
                    + "		case when pr.isPatientOldUHID = 0 then 'New' else 'Old' end as patientStatus, psd.quantity, pr.isPatientOldUHID, "
                    + "		case when isnull(pu.patientInitial, '') = '' then pu.patientName else concat(pu.patientInitial, ' ', pu.patientName) end as patient, "
                    + "		case when isnull(pr.guardianInitial, '') = '' then pr.guardianName else concat(pr.guardianInitial, ' ', pr.guardianName) end as guardian, "
                    + "		case when isnull(con.initial, '') = '' then con.name else concat(con.initial, ' ', con.name) end as consultant, "
                    + "		case when isnull(dis.initial, '') = '' then dis.name else concat(dis.initial, ' ', dis.name) end as discountBy, "
                    + "		case when isnull(pu.address1, '') = '' then '' else concat(pu.address1, "
                    + "		case when isnull(pu.address2, '') = '' then '' else concat(', ', pu.address2) end) end as address, zm.name as zone "
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
                    + "		left join zoneMast zm on zm.code = pu.zoneCode "
                    + "		where vt.category in (" + category + ") and pr.site_Code = :siteCode and pr.isCancelled = 0 "
                    + "             and psm.shortName = 'CF' " + condition;

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("s")) {
                query = "select	cityName, zone, sum(quantity) as regCount "
                        + "	from ( " + sqlQuery + ") as t"
                        + "     group by cityName, zone "
                        + "	order by cityName, zone";

            } else if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("d")) {
                query = sqlQuery
                        + "	order by ct.cityName, zm.name, ps.v_Date, vts.description, ps.v_No, ps.recId  ";;
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, query);

            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("areaType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("areaType", DmShared.xNull(filter.get("areaType").getTag()));
            }

            rsTemp = psTemp.executeQuery();

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("s")) {
                reportName = "MIS_AreaWiseConsultationSummary";
            } else if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("d")) {
                reportName = "MIS_AreaWiseConsultationDetail";
            }

            HashMap hm = new HashMap();

            jasperReport = DmServer.compileJasper(reportName);

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("s");
            listNode.setSharedKeyFieldName("Summary");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("d");
            listNode.setSharedKeyFieldName("Detail");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHArea(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT CODE, NAME FROM zonemast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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

}
