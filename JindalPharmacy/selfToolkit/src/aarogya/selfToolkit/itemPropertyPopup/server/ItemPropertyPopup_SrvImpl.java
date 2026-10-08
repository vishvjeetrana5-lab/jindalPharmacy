package aarogya.selfToolkit.itemPropertyPopup.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemPropertyPopup_SrvImpl extends DmRSSPrivate implements ItemPropertyPopup_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHColour(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        String sqlQuery = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  code, name from itemColourMast ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSize(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        String sqlQuery = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  code, name from itemSizeMast ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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

    @Override
    public DmResultPageing<List<BatchNo_Mdl>> getBatchNo(String itemCode, boolean isIbCodeRequired, boolean isOpening, ItemBatchType batchType, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<BatchNo_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<BatchNo_Mdl> lstTemp = new ArrayList<>();
        BatchNo_Mdl listNode;
        ItemProperty_Mdl prptyMdl;
        String sqlQuery;
        int i = 1;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   ib.batchNo"
                    + " , ib.mrp"
                    + " , ib.expiryDate"
                    + " , ib.manufacturingDate"
                    + " , ib.colourCode"
                    + " , ib.sizeCode";
            if (isIbCodeRequired == false) {
                sqlQuery += " , MAX(um.unit) AS skuName"
                        + " , MAX(icm.name) AS colour"
                        + " , MAX(ism.name) AS size"
                        + " , MAX(ib.skuMrp) AS skuMrp";
            } else {
                sqlQuery += " , ib.code"
                        + " , um.unit AS skuName"
                        + " , icm.name AS colour"
                        + " , ism.name AS size"
                        + " , ib.skuMrp ";
            }
            sqlQuery += " FROM itemBatch ib"
                    + " LEFT JOIN itemColourMast icm ON ib.colourCode = icm.code"
                    + " LEFT JOIN itemSizeMast ism ON ib.sizeCode = ism.code"
                    + " LEFT JOIN itemMast im ON im.code = ib.itemCode "
                    + " LEFT JOIN unitMast um ON um.code = im.sku"
                    + " WHERE ib.itemCode = :itemCode"
                    + " AND ISNULL(ib.batchNo, '') <> ''";
//            if (!isOpening) {
//                sqlQuery += " AND ISNULL(ib.expiryDate, CAST(:currentDate AS DATE)) >= CAST(:currentDate AS DATE) ";
//            }
            if (batchType.getId() == ItemBatchType.EXPIRED_BATCH.getId()) {
                sqlQuery += " AND ib.expiryDate <= :v_Date ";
            } else if (batchType.getId() == ItemBatchType.NON_EXPIRED_BATCH.getId()) {
                sqlQuery += " AND ib.expiryDate > :v_Date ";
            }
            if (isIbCodeRequired == false) {
                sqlQuery += " GROUP BY ib.batchNo, ib.mrp, ib.expiryDate, ib.manufacturingDate, ib.colourCode, ib.sizeCode";
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("itemCode", DmShared.xNull(itemCode));
            if (batchType.getId() == ItemBatchType.EXPIRED_BATCH.getId()
                    || batchType.getId() == ItemBatchType.NON_EXPIRED_BATCH.getId()) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(DmServer.getServerDateTime()[0]));
            }

//            if (!isOpening) {
//                sqlWithParam.setTimestamp("currentDate", DmServer.getServerTimestamp());
//            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new BatchNo_Mdl();
                if (isIbCodeRequired == false) {
                    listNode.setSharedKeyFieldCode(DmShared.xNull(String.valueOf(i++)));
                } else {
                    listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                }
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setColour(DmShared.xNull(rsTemp.getString("colour")));
                listNode.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                listNode.setSize(DmShared.xNull(rsTemp.getString("size")));
                listNode.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                listNode.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                listNode.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                listNode.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp"), DmShared.getNumberFormat(7)));

                prptyMdl = new ItemProperty_Mdl();
                prptyMdl.setColourName(DmShared.xNull(rsTemp.getString("colour")));
                prptyMdl.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                prptyMdl.setSizeName(DmShared.xNull(rsTemp.getString("size")));
                prptyMdl.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                prptyMdl.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                prptyMdl.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));

                listNode.setProperties(getPropertiesCsv(prptyMdl));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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

    public String getPropertiesCsv(ItemProperty_Mdl property_Mdl) {

        String csvString = "";

        if (!DmShared.xNull(property_Mdl.getColourName()).equalsIgnoreCase("")) {
            if (!DmShared.xNull(csvString).equalsIgnoreCase("")) {
                csvString += "; ";
            }
            csvString += DmShared.xNull(property_Mdl.getColourName());
        }

        if (!DmShared.xNull(property_Mdl.getSizeName()).equalsIgnoreCase("")) {
            if (!DmShared.xNull(csvString).equalsIgnoreCase("")) {
                csvString += "; ";
            }
            csvString += DmShared.xNull(property_Mdl.getSizeName());
        }

        if (DmShared.vNull(property_Mdl.getMrp()) != 0) {
            if (!DmShared.xNull(csvString).equalsIgnoreCase("")) {
                csvString += "; ";
            }
            csvString += DmShared.xNull(property_Mdl.getMrp());
        }

        return csvString;
    }

    @Override
    public DmResult<String> getIbCode(ItemProperty_Mdl mdl) {
        DmResult<String> dmrTemp = new DmResult();
        String ibCode = "", sqlQuery;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        ItemBatch_Mdl ibm;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            ibm = new ItemBatch_Mdl();
            ibm.setCode("");
            ibm.setIpm(mdl);
//            ibCode = ItemBatch_Server.getItemBatch(ibm, "", cnTemp, this);
            ibCode = ItemBatch_Server.createItemBatch(ibm, cnTemp, this);

            cnTemp.close();
            dmrTemp.setData(ibCode);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return dmrTemp;
    }

}
