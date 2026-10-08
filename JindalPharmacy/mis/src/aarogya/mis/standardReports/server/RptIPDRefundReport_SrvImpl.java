package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptIPDRefundReport_Srv;
import aarogya.mis.standardReports.shared.RptIPDRefundReport_Vld;
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

public class RptIPDRefundReport_SrvImpl extends DmRSSPrivate implements RptIPDRefundReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vrCat = "'" + pvc.IPD_REFUND_RECEIPT + "'" + "," + "'" + pvc.PATIENT_REFUND_RECEIPT + "'" + "," + "'" + pvc.TPA_PATIENT_REFUND_RECEIPT + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, String widgetName, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint = null;
        String sqlQuery = " ", join = "";
        String condition = " ";

        try {
            RptIPDRefundReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = " and (prc.v_Date  between :fromDate and :toDate)";

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
                    + "		left join subGroup con on con.subCode = pr.consultantCode  "
                    + "		where vt.category in (  " + vrCat + "   ) and prc.site_Code = :siteCode and prc.isCancelled = 0  " + condition
                    + "		order by prc.v_Date, vt.short_Name, prc.v_No, prc.recId, cl.v_SNo ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));

            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()));
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()));

            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();

            jasperReport = DmServer.compileJasper("MIS_IPDRefundReport");

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
