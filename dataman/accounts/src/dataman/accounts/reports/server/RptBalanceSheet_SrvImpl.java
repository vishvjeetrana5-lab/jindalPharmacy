package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.accounts.balanceSheet.shared.BalanceSheetReport_Mdl;
import dataman.accounts.reports.shared.RptBalanceSheet_Srv;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptBalanceSheet_SrvImpl extends DmRSSPrivate implements RptBalanceSheet_Srv {

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, BalanceSheetReport_Mdl balanceReport_Mdl) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        HashMap<String, Object> hm = new HashMap<String, Object>();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            jasperReport = DmServer.compileJasper("AC_BalanceSheetReport");
            hm.put("totalLiabilitiesAmount", balanceReport_Mdl.getTotalLiabilitiesAmount());
            hm.put("totalAssetsAmount",balanceReport_Mdl.getTotalAssetsAmount());
            hm.put("totalDifference", balanceReport_Mdl.getTotalDifference());

            jasperPrint = DmServer.fillJasper(this, "Balance Sheet", jasperReport, hm, filter, balanceReport_Mdl.getListBalanceSheet());
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
