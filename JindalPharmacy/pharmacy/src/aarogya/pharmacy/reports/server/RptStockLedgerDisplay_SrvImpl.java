package aarogya.pharmacy.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import aarogya.pharmacy.reports.shared.RptStockLedgerDisplay_Srv;
import aarogya.pharmacy.stockLedger.shared.DefaultValue_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptStockLedgerDisplay_SrvImpl extends DmRSSPrivate implements RptStockLedgerDisplay_Srv {

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, DefaultValue_Mdl defaultValue_Mdl) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", tableName = "";
        HashMap<String, Object> hm = new HashMap<String, Object>();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            tableName = getTable(this);
            sqlQuery = "SELECT temp.recId, temp.v_date,temp.v_Desc,  "
                    + " temp.batchNo, temp.openingValue, "
                    + " temp.receiveQty, temp.receiveValue, temp.issueQty, temp.issueValue, temp.balanceQty, temp.balanceValue, "
                    + " temp.particulars, temp.issueReceiveValue "
                    + " FROM " + tableName + " AS temp "
                    + " order by temp.orderIndex";

            jasperReport = DmServer.compileJasper("Pharmacy_StockLedgerDisplay");
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            hm.put("displayBal", DmShared.xNull(defaultValue_Mdl.getBalanceQty()));
            hm.put("displayOpen", DmShared.xNull(defaultValue_Mdl.getOpeningQty()));
            hm.put("displayRcv", DmShared.xNull(defaultValue_Mdl.getReceiveQty()));
            hm.put("displayIsu", DmShared.xNull(defaultValue_Mdl.getIssueQty()));
            hm.put("balanceValue", DmShared.xNull(defaultValue_Mdl.getBalanceValue()));
            hm.put("titleName", DmShared.xNull(defaultValue_Mdl.getSetting_Mdl().getTitleName()));

            jasperPrint = DmServer.fillJasper(this, "Stock Ledger Report", jasperReport, hm, filter, rsTemp);
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

    private String getTable(DmRSSPrivate rssPrivate) {
        String sessionName;

        sessionName = (rssPrivate.getLocalRequest().getSession().getId()).replaceAll("-", "_");

        return "##t" + sessionName + "_" + "StockLedgerDisplay_PH";
    }

}
