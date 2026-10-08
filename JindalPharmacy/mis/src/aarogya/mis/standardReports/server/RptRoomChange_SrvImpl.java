package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptRoomChange_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import aarogya.mis.standardReports.shared.RptRoomChange_Srv;

public class RptRoomChange_SrvImpl extends DmRSSPrivate implements RptRoomChange_Srv {

    private final String vCat = "'" + new ProjectVoucherCategory().IPD + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint = null;
        String sqlQuery = " ", join = "";
        String condition = " ";
        try {
            RptRoomChange_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = " and (rah.allotedDate between :fromDate and :toDate)";

            sqlQuery = "select  rah.regDocId, rah.v_SNo, rah.allotedDate, format(rah.allotedTime, '00.00') as allotedTime, rah.status,"
                    + "		pr.v_Date as admitDate, pr.recId as regNo, pu.recId as uhid, pu.patientName, pr.guardianName,"
                    + "		alt.allotedRoom, rm.roomNo roomHistory"
                    + "		from roomAllotmentHistory rah "
                    + "		left join roomMast rm on rm.code = rah.roomCode "
                    + "		left join roomCatMast rcm on rcm.code = rm.roomCategoryCode "
                    + "		left join patientRegistration pr on pr.regDocId = rah.regDocId "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join patientUHID pu on pu.docId = pr.uhid "
                    + "		left join ( "
                    + "			select  rah.regDocId, rm.roomNo as allotedRoom"
                    + "				from roomAllotmentHistory rah"
                    + "				left join roomMast rm on rm.code = rah.roomCode "
                    + "				left join roomCatMast rcm on rcm.code = rm.roomCategoryCode "
                    + "				where v_Sno = 1 and rcm.roomType = 'Room'"
                    + "		) alt on alt.regDocId = pr.regDocId "
                    + "		where rcm.roomType = 'Room' and vt.category in (" + vCat + ") "
                    + "             and rah.isSystemDefine = 0 and pr.site_Code = :siteCode " + condition
                    + "		order by rah.allotedDate, rah.allotedTime, vt.short_Name, pr.v_No, pr.recId ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()));
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()));
            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();
            jasperReport = DmServer.compileJasper("MIS_RoomChangeReport");
            jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, hm, filter, rsTemp);

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

}
