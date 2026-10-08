package dataman.accounts.reports.server;


import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.accounts.reports.shared.RptTrialBalance_Mdl;
import dataman.accounts.reports.shared.RptTrialBalance_Srv;
import dataman.accounts.trialBalance.shared.TrialBalanceReport_Mdl;

import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
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

public class RptTrialBalance_SrvImpl extends DmRSSPrivate implements RptTrialBalance_Srv {

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, TrialBalanceReport_Mdl balanceReport_Mdl) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        HashMap<String, Object> hm = new HashMap<String, Object>();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            jasperReport = DmServer.compileJasper("AC_TrialBalanceReport");
            hm.put("totalCredit", balanceReport_Mdl.getTotalCredit());
            hm.put("totalDebit",balanceReport_Mdl.getTotalDebit());
            hm.put("totalDifference", balanceReport_Mdl.getTotalDifference());

            jasperPrint = DmServer.fillJasper(this, "Trial Balance", jasperReport, hm, filter, balanceReport_Mdl.getListTrialBalance());
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
