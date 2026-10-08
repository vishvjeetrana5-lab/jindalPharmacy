package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptTaskExecutionReport_Srv;
import aarogya.mis.standardReports.shared.RptTaskExecutionReport_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum.TaskStatus;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptTaskExecutionReport_SrvImpl extends DmRSSPrivate implements RptTaskExecutionReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.IPD + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint;
        String mainQuery, condition = "";
        try {
            RptTaskExecutionReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition += " and (ts.scheduleDate between :fromDate and :toDate) ";

            if (!DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("")) {
                condition += "and ts.isStatus in(" + DmServer.getParamList(":status", filter.get("status").getTag()) + ")";
            }

            if (!DmShared.xNull(filter.get("fromTime").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  ts.scheduleTime >= :fromTime ";
            }
            if (!DmShared.xNull(filter.get("toTime").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  ts.scheduleTime <= :toTime ";
            }

            mainQuery = "select  pr.recId as ipdNo,pu.recId as uhidNo,ts.scheduleDate,ts.scheduleTime ,ts.description as instruction,ts.u_EntDt_LatestLine as excutionDateTime, "
                    + "         case when ts.isStatus=0 then 'Open' when ts.isStatus=1 then 'Close' when ts.isStatus=2 then 'Complete' when ts.isStatus=3 then 'Re-Open' "
                    + "         when ts.isStatus=4 then 'Follow-Up' when   ts.isStatus=5 then 'Cancel' when ts.isStatus=6 then 'Start' end as status, "
                    + "         case when isnull(pu.patientInitial, '') = '' then pu.patientName else concat(pu.patientInitial, ' ', pu.patientName) end as patient, "
                    + "         case when isnull(con.initial, '') = '' then con.name else concat(con.initial, ' ', con.name) end as consultant,ts.regdocId "
                    + "         from taskSchedule ts "
                    + "         left join patientRegistration pr on pr.regDocId = ts.regDocId "
                    + "         left join patientUHID pu on pu.docId = pr.uhid "
                    + "         left join subGroup con on con.subCode = pr.consultantCode "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         left join patientDischarge pd on pd.regDocId = pr.regDocId "
                    + "		where pr.isCancelled = 0 and vt.category in (" + vCat + ")  and pd.regDocId is null and pr.site_Code = :siteCode " + condition
                    + "         order by pr.v_Date,pr.regTime,vt.short_Name,pr.v_No,pr.recId ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, mainQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }

            if (!DmShared.xNull(filter.get("status").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("status", DmShared.xNull(filter.get("status").getTag()));
            }

            if (!DmShared.xNull(filter.get("fromTime").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setString("fromTime", DmShared.xNull(filter.get("fromTime").getText()));
            }
            if (!DmShared.xNull(filter.get("toTime").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setString("toTime", DmShared.xNull(filter.get("toTime").getText()));
            }

            rsTemp = psTemp.executeQuery();
            LinkedHashMap hm = new LinkedHashMap();

            jasperReport = DmServer.compileJasper("MIS_TaskExecutionReport");

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
    public DmResultPageing<List<KeyFieldExtended>> getStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            for (TaskStatus status : TaskStatus.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(status.getId()).toString());
                listNode.setSharedKeyFieldName(status.getValue());
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

}
