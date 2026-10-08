package aarogya.pharmacy.addBatchSingle.server;

import aarogya.pharmacy.addBatchSingle.shared.*;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AddBatchSingle_SrvImpl extends DmRSSPrivate implements AddBatchSingle_Srv {

    @Override
    public DmResultPageing<List<TxtHBatch_Mdl>> getTxtHBatchNo(String itemCode, String departmentCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBatch_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHBatch_Mdl> lstTemp = new ArrayList<TxtHBatch_Mdl>();
        TxtHBatch_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = getItemBatchSql(true);
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("itemCode", itemCode);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("departmentCode", DmShared.xNull(departmentCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBatch_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("ibCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                listNode.setIsBarcodeActive(DmShared.xNull(rsTemp.getString("isBarcodeActive")));
                listNode.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.setRowId(DmShared.xNull(rsTemp.getString("rowId")));
                listNode.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                listNode.setBestBeforeDays(DmServer.vNull(rsTemp.getString("bestBeforeDays"), "0"));
                listNode.setQty(DmServer.vNull(rsTemp.getString("balanceQty"), ""));
                listNode.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));
                listNode.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                listNode.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                listNode.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
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

    private String getItemBatchSql(boolean isMainSql) {
        String sqlQuery, sqlQuery_SelectedBatches = "", balanceQuantityField = "", returnValue = "";

        sqlQuery = "SELECT "
                + " ib.code as ibCode, ib.expiryDate, "
                + " ib.batchNo, ib.barcode, im.isBarcodeActive, ib.saleRate, ib.mrp, ib.skuSaleRate, ib.skuMrp, "
                + " ib.itemCode, im.name as medName, ib.manufacturingDate, "
                + " DATEDIFF(day,ib.manufacturingDate , ib.expiryDate) AS bestBeforeDays, "
                + " ISNULL(im.allowNegativeStock,0) AS allowNegativeStock,"
                + " im.saleUnitConversionRate, um.decimalPlaces as skuDecimalPlaces, ums.decimalPlaces ";
        if (sqlQuery_SelectedBatches.trim().equals("")) {
            balanceQuantityField = " ISNULL(ibs.balanceQty,0) ";
        } else {
            balanceQuantityField = " (ISNULL(ibs.balanceQty,0) - ISNULL(vsb.issueQty,0)) ";
        }
        sqlQuery += ", " + balanceQuantityField + " AS balanceQty";
        if (isMainSql) {
            sqlQuery += ", ROW_NUMBER() OVER (ORDER BY ib.expiryDate, ib.batchNo, ISNULL(ibs.balanceQty,0) DESC) AS rowId ";
        } else {
            sqlQuery += ", SUM(" + balanceQuantityField + ") OVER (ORDER BY ib.expiryDate, ib.batchNo, ISNULL(ibs.balanceQty,0) DESC) AS runningTotal ";
        }
        sqlQuery += " FROM itemBatch ib "
                + " LEFT JOIN itemBatchStock ibs on ib.code = ibs.ibCode "
                + " LEFT JOIN itemMast im on im.code = ib.itemCode "
                + " LEFT JOIN unitMast um on um.code = im.sku "
                + " LEFT JOIN unitMast ums on ums.code = im.saleUnit ";
        if (!sqlQuery_SelectedBatches.trim().equals("")) {
            sqlQuery += " LEFT JOIN ("
                    + "                 SELECT sb.ibCode, SUM(sb.issueQty) AS issueQty"
                    + "                 FROM (" + sqlQuery_SelectedBatches + ") AS sb "
                    + "                 GROUP BY sb.ibCode "
                    + "             ) AS vsb ON vsb.ibCode = ib.code ";
        }
        sqlQuery += " WHERE ib.itemCode = :itemCode "
                + " AND isnull(ibs.site_Code,:site_Code) = :site_Code "
                + " AND isnull(ibs.departmentCode,:departmentCode) = :departmentCode ";
        sqlQuery += " AND CASE WHEN ISNULL(im.allowNegativeStock,0) = 1 THEN 1 ELSE CASE WHEN " + balanceQuantityField + " > 0 THEN 1 ELSE 0 END END = 1 ";
        if (!isMainSql) {
            returnValue = sqlQuery;
            returnValue += " ORDER BY ib.expiryDate, ib.batchNo, ISNULL(ibs.balanceQty,0) DESC ";
            returnValue += " OFFSET :offset ROWS ";
        } else {
            returnValue = sqlQuery;
        }
        return returnValue;
    }

    @Override
    public DmResult<TxtHBatch_Mdl> saveNewBatch(Batch_Mdl batchMdl) {
        DmResult<TxtHBatch_Mdl> dmrTemp = new DmResult<TxtHBatch_Mdl>();
        TxtHBatch_Mdl mrTemp = new TxtHBatch_Mdl();
        Connection cnTemp = null;
        try {
            AddBatchSingle_Vld.chkSave(batchMdl);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            mrTemp.setSharedKeyFieldCode(ItemBatch_Server.getItemBatch(batchMdl, departmentCode, cnTemp, this));
            mrTemp.setSharedKeyFieldCode(ItemBatch_Server.createItemBatch(batchMdl, cnTemp, this));
            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() > 0) {
                mrTemp.setSharedKeyFieldName(batchMdl.getIpm().getBatchNo());
                mrTemp.setExpDate(batchMdl.getIpm().getExpiryDate());
                mrTemp.setItemCode(batchMdl.getIpm().getItemCode());
                mrTemp.setMfgDate(batchMdl.getIpm().getManufacturingDate());
                mrTemp.setMrp(batchMdl.getIpm().getMrp());
                mrTemp.setSkuMrp(batchMdl.getIpm().getSkuMrp());
                mrTemp.setSaleRate(batchMdl.getSaleRate());
                mrTemp.setSkuSaleRate(batchMdl.getSkuSaleRate());
                dmrTemp.setData(mrTemp);
                dmrTemp.setStatus(true);
                cnTemp.close();
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

}
