package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptCategoryWiseServiceReport_Srv;
import aarogya.mis.standardReports.shared.RptCategoryWiseServiceReport_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptCategoryWiseServiceReport_SrvImpl extends DmRSSPrivate implements RptCategoryWiseServiceReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", reportName = "", condition = "", mainQuery;

        try {
            RptCategoryWiseServiceReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            condition = " and (psd.v_Date between :fromDate and :toDate) ";

            if (!DmShared.xNull(filter.get("serviceCategory").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND psm.categoryCode IN(" + DmServer.getParamList(":serviceCategory", filter.get("serviceCategory").getTag()) + " )";
            }

            mainQuery = "select	ps.docId, ps.recId, ps.v_Date, vt.description, psd.v_Type, pr.recId as regNo, pu.recId as uhid, "
                    + "         psd.discountReason, psd.oh_Amt_Net, "
                    + "         case when isnull(pu.patientInitial,'') = '' then pu.patientName else concat(pu.patientInitial,' ', pu.patientName) end as patientName,        "
                    + "         case when isnull(dis.initial,'') = '' then dis.name else concat(dis.initial,' ', dis.name) end  as discountBy, "
                    + "         case when org.manualCode = 'CP' then '' else org.manualCode end as orgShortName, psd.serviceCode, "
                    + "         concat(con.initial, ' ', con.name) consultant, ps.preparedBy, psm.name as serviceName, psd.grossAmount, psd.discount, "
                    + "         pr.dobDisplay, psd.rate, psd.quantity, gen.shortName gender, psd.consultantCode as consultantCode, "
                    + "         pscm.name as serviceCategory "
                    + "         from patientService ps "
                    + "         left join voucher_Type vt on vt.v_Type = ps.v_Type "
                    + "         left join patientServiceDetail psd on psd.docId = ps.docId "
                    + "         left join patientRegistration pr on pr.regDocId = psd.regDocId 	"
                    + "         left join patientUHID pu on pu.docId = pr.uhid 		"
                    + "         left join patientUHID2 pu2 on pu2.uhid = pr.uhid 		"
                    + "         left join subGroup org on org.subCode = pr.organizationCode "
                    + "         left join subGroup dis on dis.subCode = psd.discountGivenBy "
                    + "         left join subGroup con on con.subCode = psd.consultantCode "
                    + "         left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "         left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "         left join hl7mast gen on gen.code = pu2.genderCode "
                    + "         where vt.category in ('" + pvc.OPD_SERVICE + "') and psd.isCancelled = 0 "
                    + "             and ps.site_Code = :siteCode and psm.shortName <> 'CF' " + condition;

            if (DmShared.xNull(filter.get("reportView").getTag()).trim().equalsIgnoreCase("s")) {
                sqlQuery = "select max (ps.serviceCode) as serviceCode ,max(ps.serviceCategory) as serviceCategory, "
                        + "     max(ps.serviceName) as serviceName, max(ps.consultant) as consultant, sum(ps.quantity) as quantity, sum(ps.grossAmount) as grossAmount,"
                        + "     sum(isnull(ps.discount,0)) as discount, sum(ps.oh_Amt_Net) as oh_Amt_Net "
                        + "     from (" + mainQuery + ") ps "
                        + "     group by  ps.serviceCategory, ps.serviceName, ps.consultantCode "
                        + "     order by  ps.serviceCategory,ps.serviceName, ps.consultantCode   ";
            } else if (DmShared.xNull(filter.get("reportView").getTag()).trim().equalsIgnoreCase("d")) {
                sqlQuery = mainQuery + " order by ps.v_Date, vt.description, ps.v_No, ps.recId ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }

            if (!DmShared.xNull(filter.get("serviceCategory").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("serviceCategory", DmShared.xNull(filter.get("serviceCategory").getTag()), true);
            }

            rsTemp = psTemp.executeQuery();
            if (DmShared.xNull(filter.get("reportView").getTag()).trim().equalsIgnoreCase("s")) {
                reportName = "MIS_CategoryWiseServiceSummary";
            } else if (DmShared.xNull(filter.get("reportView").getTag()).trim().equalsIgnoreCase("d")) {
                reportName = "MIS_CategoryWiseServiceDetail";
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportView(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Summary");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Detail");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select  pscm.code, pscm.name "
                    + "		from patientServiceApplicableTo psat "
                    + "		left join patientServiceCatMast pscm on pscm.code = psat.serviceCategoryCode "
                    + "		left join voucher_Type vt on vt.v_Type = psat.v_Type "
                    + "		where vt.category in ('" + pvc.OPD + "','" + pvc.OPD_SERVICE + "', '" + pvc.EMERGENCY_OPD + "', '" + pvc.OPD_SERVICE_CANCELLATION + "','" + pvc.PAY_CLINIC + "') ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
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
