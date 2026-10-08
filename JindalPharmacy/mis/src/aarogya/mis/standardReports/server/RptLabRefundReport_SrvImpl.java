package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmBase.self.shared.*;
import java.sql.*;
import java.util.*;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptLabRefundReport_SrvImpl extends DmRSSPrivate implements RptLabRefundReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vrCat = "'" + pvc.LAB_REFUND_RECEIPT + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery;
        String condition;

        try {
            RptLabDiscountReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "and (prc.v_Date between  :fromDate  and  :toDate)";

            sqlQuery = "select  prc.docId, prc.recId as recNo, prc.v_Date, cl.amtDr, ctm.name, pu.patientName, gen.shortName as gender,  "
                    + "		pr.dobDisplay, pr.recId as regNo, prc.preparedBy, prc.remark, pu.recId as uhid,  "
                    + "		case when isnull(con.initial, '') = '' then con.name else concat(con.initial, ' ', con.name) end as consultant "
                    + "		from patientReceipt prc  "
                    + "		left join voucher_Type vt on vt.v_Type = prc.v_Type  "
                    + "		left join patientRegistration pr on pr.regDocId = prc.regDocId  "
                    + "		left join patientUHID pu on pu.docId = pr.uhid  "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pu.docId  "
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode  "
                    + "		left join cardLedger cl on cl.docId = prc.docId  "
                    + "		left join cardTypeMast ctm on ctm.code = cl.cardTypeCode  "
                    + "		left join subGroup con on con.subCode = pr.referredBy  "
                    + "		where vt.category in (  " + vrCat + " ) and prc.site_Code = :siteCode  "
                    + "			and prc.isCancelled = 0   " + condition
                    + "		order by prc.v_Date, vt.short_Name, prc.v_No, prc.recId, cl.v_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            rsTemp = psTemp.executeQuery();
            LinkedHashMap hm = new LinkedHashMap();

            jasperReport = DmServer.compileJasper("MIS_LabRefundReport");
            jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, hm, filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);
            cnTemp.close();

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
