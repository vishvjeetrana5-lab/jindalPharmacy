package aarogya.pharmacy.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import aarogya.pharmacy.reports.shared.RptStockBatch_Srv;
import aarogya.pharmacy.stockBatch.shared.DefaultValue_Mdl;
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

public class RptStockBatch_SrvImpl extends DmRSSPrivate implements RptStockBatch_Srv {

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
            sqlQuery = " SELECT temp.orderIndex,temp.batchNo, temp.expiryDate,  temp.mrp, temp.saleRate "
                    + " , temp.openingStock, temp.openingValue, temp.purchaseQty, temp.otherReceiveQty"
                    + " , temp.saleQty, temp.otherIssueQty, temp.closingStock, temp.closingValue "
                    + " FROM " + tableName + " AS temp "
                    + " order by temp.orderIndex";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            jasperReport = DmServer.compileJasper("Pharmacy_StockBatchDisplay");

            hm.put("totalOpnValue", DmShared.xNull(defaultValue_Mdl.getDisplayOpeningValue()));
            hm.put("totalBalValue", DmShared.xNull(defaultValue_Mdl.getDisplayBalanceValue()));
            hm.put("titleName", DmShared.xNull(defaultValue_Mdl.getSetting_Mdl().getTitleName()));

            jasperPrint = DmServer.fillJasper(this, "Stock Batch Report", jasperReport, hm, filter, rsTemp);
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

        return "##t" + sessionName + "_" + "StockBatchDisplay_PH";
    }

}
