package aarogya.mis.standardReports.server;

import aarogya.mis.reports.shared.RptBirthdayAnniv_Vld;
import aarogya.mis.standardReports.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptRoomCategoryWiseIPDReport_SrvImpl extends DmRSSPrivate implements RptRoomCategoryWiseIPDReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

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
            RptBirthdayAnniv_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "and (sb.v_Date between :fromDate and :toDate)";

            sqlQuery = "declare @totalBIllAmt float  "
                    + " set @totalBIllAmt =(select sum(tbl.billAmount) from(  select  round(sum(psd.oh_Amt_Net),2) as billAmount   "
                    + "		from saleBill sb "
                    + "		left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "		left join patientServiceDetail psd on psd.sbDocId = sb.docId "
                    + "		left join roomMast rm on rm.code = psd.roomCode "
                    + "		left join roomCatMast rcm on rcm.code = rm.roomCategoryCode "
                    + "		where sb.isCancelled = 0 and isnull(sb.approvedBy, '') <> '' and vt.category in ('IPDBL')"
                    + "	        and sb.docId <> sb.regDocId and psd.roomCode is not null and rcm.roomType = 'Room'" + condition
                    + "		group by rm.roomCategoryCode ) as tbl) "
                    + "  select rm.roomCategoryCode, max(rcm.name) as roomCategory, round(sum(psd.oh_Amt_Net),2) as billAmount  , round(((sum(psd.oh_Amt_Net)/@totalBIllAmt)*100),2) as billPercentage "
                    + "		from saleBill sb "
                    + "		left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "		left join patientServiceDetail psd on psd.sbDocId = sb.docId "
                    + "		left join roomMast rm on rm.code = psd.roomCode "
                    + "		left join roomCatMast rcm on rcm.code = rm.roomCategoryCode "
                    + "		where sb.isCancelled = 0 and isnull(sb.approvedBy, '') <> '' and vt.category in ('IPDBL')"
                    + "	        and sb.docId <> sb.regDocId and psd.roomCode is not null and rcm.roomType = 'Room'" + condition
                    + "		group by rm.roomCategoryCode"
                    + "		order by sum(psd.oh_Amt_Net) desc  " ;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();
            jasperReport = DmServer.compileJasper("MIS_RoomCategoryWiseIPDReport");
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

}
