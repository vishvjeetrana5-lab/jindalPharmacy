package aarogya.pharmacy.schemeMaster.server;

import aarogya.pharmacy.schemeMaster.shared.Find_Mdl;
import aarogya.pharmacy.schemeMaster.shared.GrdSchemeDetails_Mdl;
import aarogya.pharmacy.schemeMaster.shared.SchemeMaster_Mdl;
import aarogya.pharmacy.schemeMaster.shared.SchemeMaster_Srv;
import aarogya.pharmacy.schemeMaster.shared.SchemeMaster_Vld;
import aarogya.pharmacy.schemeMaster.shared.TxtHMedicine_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SchemeMaster_SrvImpl extends DmRSSPrivate implements SchemeMaster_Srv {

    public SchemeMaster_SrvImpl() {
    }

    @Override
    public DmResult<SchemeMaster_Mdl> getMoveRec(String strKey, String v_Date, String medicineTotalQty) {
        DmResult<SchemeMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        SchemeMaster_Mdl mrTemp = new SchemeMaster_Mdl();
        GrdSchemeDetails_Mdl lstTemp;

        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT "
                    + "      isd.code as itemCode "
                    + "    , im.name as itemName "
                    + "    , imf.allowNegativeStock "
                    + "    , imf.isBarcodeActive "
                    + "    , im.saleUnitConversionRate "
                    + "    , im.purchaseUnitConversionRate "
                    + "    , umfs.decimalPlaces as sUnitDecimalPlaces "
                    + "    , umfp.decimalPlaces as pUnitDecimalPlaces "
                    + "    , umf.decimalPlaces as skuDecimalPlaces "
                    + "    , imf.itsBasedOn "
                    + "    , isd.freeItemCode "
                    + "    , imf.name as freeItemName "
                    + "    , isd.itemUnitCode "
                    + "    , um.unit as itemUnitName "
                    + "    , isd.freeItemUnitCode "
                    + "    , umf.unit as freeitemUnitName "
                    + "    , isd.v_SNo "
                    + "    , isd.schemeOnQty "
                    + "    , isd.schemeFreeQty "
                    + "    , isd.schemeDesc "
                    + "    , CAST(isd.schemeFreeQty / isd.schemeOnQty AS decimal(18, 7)) AS schemeQtyPerItem "
                    + " FROM  "
                    + " itemSaleSchemeDetail isd "
                    + " LEFT JOIN itemMast AS im ON im.code = isd.code "
                    + " LEFT JOIN itemMast AS imf ON imf.code = isd.freeItemCode "
                    + " LEFT JOIN unitmast AS um ON isd.itemUnitCode = um.code "
                    + " LEFT JOIN unitmast AS umf ON isd.freeItemUnitCode = umf.code "
                    + " LEFT JOIN unitmast AS umfs ON umfs.code = imf.saleUnit "
                    + " LEFT JOIN unitmast AS umfp ON umfp.code = imf.purchaseUnit "
                    + " WHERE isd.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {

                lstTemp = new GrdSchemeDetails_Mdl();

                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                mrTemp.setItemUnitCode(DmShared.xNull(rsTemp.getString("itemUnitCode")));
                mrTemp.setItemUnitName(DmShared.xNull(rsTemp.getString("itemUnitName")));

                double freeQty = DmShared.vNull(rsTemp.getString("schemeQtyPerItem")).doubleValue() * DmShared.vNull(medicineTotalQty).doubleValue();
                if (Math.abs(freeQty) >= DmShared.vNull(rsTemp.getString("schemeFreeQty")).doubleValue()) {
                    lstTemp.setFreeQty(DmServer.vNull(freeQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("sUnitDecimalPlaces")).intValue())));
                } else {
                    lstTemp.setFreeQty("0");
                }

                lstTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("freeItemCode")));
                lstTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("freeItemName")));
                lstTemp.setFreeWithItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                lstTemp.setDescription(DmShared.xNull(rsTemp.getString("schemeDesc")));
                lstTemp.setSchemeOnQty(DmServer.vNull(rsTemp.getString("schemeOnQty")));
                lstTemp.setSchemeFreeQty(DmServer.vNull(rsTemp.getString("schemeFreeQty")));
                lstTemp.setFreeItemUnitCode(DmShared.xNull(rsTemp.getString("freeItemUnitCode")));
                lstTemp.setFreeItemUnitName(DmShared.xNull(rsTemp.getString("freeItemUnitName")));
                lstTemp.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                lstTemp.setItsBasedOnFreeItem(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                lstTemp.setSaleUnitConversionRate(DmShared.iNull(rsTemp.getString("saleUnitConversionRate")).toString());
                lstTemp.setPurchaseUnitConversionRate(DmShared.iNull(rsTemp.getString("purchaseUnitConversionRate")).toString());
                lstTemp.setAllowNegativeStockFreeItem(DmShared.iNull(rsTemp.getString("allowNegativeStock")).toString());
                lstTemp.setBarcodeActiveFreeItem(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                lstTemp.setDecimalPlacesPurchaseUnit(DmShared.iNull(rsTemp.getString("pUnitDecimalPlaces")).toString());
                lstTemp.setDecimalPlacesSaleUnit(DmShared.iNull(rsTemp.getString("sUnitDecimalPlaces")).toString());
                lstTemp.setMedicineMdl(getMedicineDetails(DmShared.xNull(rsTemp.getString("freeItemCode")), cnTemp));
                //<editor-fold defaultstate="collapsed" desc="ITS AND SALEACCODE">
                DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult();
                dmrIts = getITS(DmShared.iNull(lstTemp.getSharedKeyFieldCode()).toString(), 0, DmShared.xNull(lstTemp.getItsBasedOnFreeItem()), v_Date, cnTemp);
                lstTemp.getLstITS().addAll(dmrIts.getData());
                lstTemp.setSaleAcCode(dmrIts.getValue1());
//</editor-fold>

                mrTemp.getLstSchemeDetails().add(lstTemp);
            }
            psTemp.close();
            rsTemp.close();

            cnTemp.close();
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

    private DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, double rate, String itsBasedOn, String billDate, Connection cnTemp) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();

        String sqlQuery = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String location = "";
        try {
            sqlQuery = "SELECT ISNULL(sm.isUt,0) AS isUt FROM stateMast sm WHERE sm.numericCode = :numericCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("numericCode", DmServer.getSsnSiteStateNumericCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.xNull(rsTemp.getString("isUT")).equalsIgnoreCase("1")) {
                    location = DmShared.iNull(DmEnum.DmLocation.UnionTerritory.getId()).toString();
                } else {
                    location = DmShared.iNull(DmEnum.DmLocation.IntraState.getId()).toString();
                }
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp = STServer.getITS(itemCode, rate, itsBasedOn, STEnum.ITSType.SALES, DmServer.getSsnSiteCode(this), location, billDate, cnTemp);
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> saveRecord(SchemeMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";

        dmrTemp.setData("");

        try {
            SchemeMaster_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);
            //<editor-fold defaultstate="collapsed" desc="SAVE IN ITEMSALESCHEMEDETAILS">
            sqlQuery = "Delete From itemSaleSchemeDetail Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            int vSNo = 0;
            for (GrdSchemeDetails_Mdl detail_Mdl : mrTemp.getLstSchemeDetails()) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    detail_Mdl.setV_SNo(String.valueOf(++vSNo));
                    sqlQuery = " INSERT into itemSaleSchemeDetail "
                            + " ( "
                            + "   code "
                            + " , v_SNo "
                            + " , itemUnitCode "
                            + " , freeItemCode "
                            + " , freeItemUnitCode "
                            + " , schemeOnQty "
                            + " , schemeFreeQty "
                            + " , schemeDesc "
                            + " ) "
                            + " VALUES "
                            + " ( "
                            + "   :code "
                            + " , :v_SNo "
                            + " , :itemUnitCode "
                            + " , :freeItemCode "
                            + " , :freeItemUnitCode "
                            + " , :schemeOnQty "
                            + " , :schemeFreeQty "
                            + " , :schemeDesc "
                            + " ) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                    psTemp.setString("itemUnitCode", DmShared.foriegnKey(mrTemp.getItemUnitCode()).toString());
                    psTemp.setString("freeItemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("freeItemUnitCode", DmShared.foriegnKey(detail_Mdl.getFreeItemUnitCode()));
                    psTemp.setString("schemeOnQty", DmServer.vNull(detail_Mdl.getSchemeOnQty()));
                    psTemp.setString("schemeFreeQty", DmServer.vNull(detail_Mdl.getSchemeFreeQty()));
                    psTemp.setString("schemeDesc", DmShared.xNull(detail_Mdl.getDescription()));

                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
//</editor-fold>
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (e instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                    dmrTemp.setValue1(((DmException) e).getErrorSource());
                    dmrTemp.setValue2(((DmException) e).getErrorSourceCode());
                }
            } catch (Exception ex) {
            }
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

    @Override
    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicine_Mdl> lstTemp = new ArrayList<TxtHMedicine_Mdl>();
        TxtHMedicine_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1) ";
            condition += " AND im.isActive = 1  ";
            condition += " AND sl.site_Code = :site_Code";

            sqlQuery = " SELECT "
                    + "   im.code "
                    + " , im.name "
                    + " , im.allowNegativeStock "
                    + " , im.isBarcodeActive "
                    + " , im.hsnCode "
                    + " , im.isBestBefore "
                    + " , im.isReturnableItem "
                    + " , im.isSaleRateEditable "
                    + " , im.itemGSTNature "
                    + " , im.itsBasedOn "
                    + " , im.propertyList "
                    + " , im.saleUnitConversionRate "
                    + " , um.unit "
                    + " , im.purchaseUnit "
                    + " , ump.unit purchaseName "
                    + " , um.decimalPlaces AS skuDecimalPlaces "
                    + " , ums.decimalPlaces "
                    + " , im.saleUnit "
                    + " , ums.unit saleUnitName "
                    + " , im.sku "
                    + " , um.unit skuName "
                    + " , hm.hsn "
                    + " , im.purchaseUnitConversionRate "
                    + " , im.saleDiscountPer "
                    + " , im.restrictGroup "
                    + " , im.compositionCode "
                    + " , mcm.name as compositionName "
                    + " , im.manualCode  "
                    + " , im.itemPacking  "
                    + " FROM itemMast im "
                    + " LEFT JOIN itemCat ic on ic.code = im.itemCategoryCode "
                    + " LEFT JOIN itemGroup ig on ig.code = im.itemGroup "
                    + " LEFT JOIN unitMast um on um.code = im.sku "
                    + " LEFT JOIN unitMast ums on ums.code = im.saleUnit "
                    + " LEFT JOIN unitMast ump on ump.code = im.purchaseUnit "
                    + " LEFT JOIN hsnMast hm on hm.code = im.hsnCode "
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN medicineCompoMast mcm on mcm.code = im.compositionCode ";
            sqlQuery += condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue());
                listNode.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                listNode.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                listNode.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                listNode.setHsnName(DmShared.xNull(rsTemp.getString("hsn")));
                listNode.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                listNode.setDiscountPer(DmServer.vNull(rsTemp.getString("saleDiscountPer")));
                listNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnit")));
                listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseName")));
                listNode.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnit")));
                listNode.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                listNode.setConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                listNode.setIsSaleRateEditable(DmServer.vNull(rsTemp.getString("isSaleRateEditable")));
                listNode.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                listNode.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                listNode.setCompositionCode(DmShared.xNull(rsTemp.getString("compositionCode")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCompositionName(DmShared.xNull(rsTemp.getString("compositionName")));
                listNode.setItemPacking(DmShared.xNull(rsTemp.getString("itemPacking")));
                listNode.setReturnableMedicine(DmShared.iNull(rsTemp.getString("isReturnableItem")).intValue() == 1);
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

    private TxtHMedicine_Mdl getMedicineDetails(String medCode, Connection cnTemp) {
        TxtHMedicine_Mdl listNode = new TxtHMedicine_Mdl();
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;

        try {

            condition = " AND (im.filterItemType = :filterItemType OR im.isCommon = 1) ";
            condition += " AND im.isActive = 1  ";
            condition += " AND sl.site_Code = :site_Code";

            sqlQuery = " SELECT "
                    + "   im.code "
                    + " , im.name "
                    + " , im.allowNegativeStock "
                    + " , im.isBarcodeActive "
                    + " , im.hsnCode "
                    + " , im.isBestBefore "
                    + " , im.isReturnableItem "
                    + " , im.isSaleRateEditable "
                    + " , im.itemGSTNature "
                    + " , im.itsBasedOn "
                    + " , im.propertyList "
                    + " , im.saleUnitConversionRate "
                    + " , um.unit "
                    + " , im.purchaseUnit "
                    + " , ump.unit purchaseName "
                    + " , um.decimalPlaces AS skuDecimalPlaces "
                    + " , ums.decimalPlaces "
                    + " , im.saleUnit "
                    + " , ums.unit saleUnitName "
                    + " , im.sku "
                    + " , um.unit skuName "
                    + " , hm.hsn "
                    + " , im.purchaseUnitConversionRate "
                    + " , im.saleDiscountPer "
                    + " , im.restrictGroup "
                    + " , im.compositionCode "
                    + " , mcm.name as compositionName "
                    + " , im.manualCode  "
                    + " , im.itemPacking  "
                    + " FROM itemMast im "
                    + " LEFT JOIN itemCat ic on ic.code = im.itemCategoryCode "
                    + " LEFT JOIN itemGroup ig on ig.code = im.itemGroup "
                    + " LEFT JOIN unitMast um on um.code = im.sku "
                    + " LEFT JOIN unitMast ums on ums.code = im.saleUnit "
                    + " LEFT JOIN unitMast ump on ump.code = im.purchaseUnit "
                    + " LEFT JOIN hsnMast hm on hm.code = im.hsnCode "
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN medicineCompoMast mcm on mcm.code = im.compositionCode "
                    + " WHERE im.code=:code ";
            sqlQuery += condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("code", DmShared.iNull(medCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue());
                listNode.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                listNode.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                listNode.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                listNode.setHsnName(DmShared.xNull(rsTemp.getString("hsn")));
                listNode.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                listNode.setDiscountPer(DmServer.vNull(rsTemp.getString("saleDiscountPer")));
                listNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnit")));
                listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseName")));
                listNode.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnit")));
                listNode.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                listNode.setConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                listNode.setIsSaleRateEditable(DmServer.vNull(rsTemp.getString("isSaleRateEditable")));
                listNode.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                listNode.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                listNode.setCompositionCode(DmShared.xNull(rsTemp.getString("compositionCode")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCompositionName(DmShared.xNull(rsTemp.getString("compositionName")));
                listNode.setItemPacking(DmShared.xNull(rsTemp.getString("itemPacking")));
                listNode.setReturnableMedicine(DmShared.iNull(rsTemp.getString("isReturnableItem")).intValue() == 1);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return listNode;
    }

}
