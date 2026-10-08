package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmBase.self.server.DmIJasperCompiled;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import dataman.accounts.reports.shared.RptBankReconciliationPrint_Mdl;
import dataman.accounts.reports.shared.RptBankReconciliation_Srv;

public class RptBankReconciliation_SrvImpl extends DmRSSPrivate implements RptBankReconciliation_Srv {

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, List<RptBankReconciliationPrint_Mdl> grdBankReconciliationEntry_Mdls) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        HashMap<String, Object> hm = new HashMap<String, Object>();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            DmIJasperCompiled compileJasper = DmServer.compileJasper("AC_BankReconcilation", "", false);
            jasperReport = compileJasper.getReport();

            jasperPrint = DmServer.fillJasper(this, "Bank Reconciliation Report", jasperReport, hm, filter, grdBankReconciliationEntry_Mdls);
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
