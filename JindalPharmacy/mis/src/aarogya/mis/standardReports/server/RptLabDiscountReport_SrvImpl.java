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

public class RptLabDiscountReport_SrvImpl extends DmRSSPrivate implements RptLabDiscountReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vrCat = "'" + pvc.LAB_REGISTRATION + "'" + "," + "'" + pvc.LAB_DISCOUNT + "'";

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

            condition = " and  (ps.v_Date between :fromDate and :toDate)";

            sqlQuery = "select  max(ps.v_Date) as v_Date, max(ps.recId) as recId, max(pu.patientName) as patientName, round(sum(psd.discount),2) as discount,  "
                    + "		max(case when isnull(dis.initial, '') = '' then dis.name else concat(dis.initial, ' ', dis.name) end) as discountBy, "
                    + "		sum(psd.oh_Amt_Net) as oh_Amt_Net, max(ps.preparedBy) as preparedBy "
                    + "		from patientService ps  "
                    + "		left join voucher_Type vt on vt.v_Type = ps.v_Type  "
                    + "		left join patientServiceDetail psd on psd.DocId = ps.docId  "
                    + "		left join patientServiceMast psm on psm.code = psd.serviceCode  "
                    + "		left join subGroup dis on dis.subCode = psd.discountGivenBy  "
                    + "		left join patientRegistration pr on pr.regDocId = psd.regDocId  "
                    + "		left join patientUHID pu on pu.docId = pr.uhid  "
                    + "		where isnull(ps.isCancelled, 0) = 0 and ps.site_Code = 1 and vt.category in ( " + vrCat + " )  "
                    + "             and isnull(psd.discount, '') <> 0  and ps.site_Code = :siteCode " + condition
                    + "		group by ps.DocId, psd.discountGivenBy  "
                    + "		order by max(ps.v_Date), max(ps.v_Time), max(vt.short_Name), max(ps.v_No), max(ps.recId) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            rsTemp = psTemp.executeQuery();

            LinkedHashMap hm = new LinkedHashMap();

            jasperReport = DmServer.compileJasper("MIS_LabDiscountReport");
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
