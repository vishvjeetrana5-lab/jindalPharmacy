package aarogya.pharmacy.self.server;

import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmEnum.GSTNature;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;

public class Pharmacy_Server {

    public static String getItemCodeCsv(String barCode, String voucherDate, String departmentCode, String siteCode, ItemBatchType batchType, boolean isItemReceive, boolean isMoverecFlag, Connection cnTemp) throws DmException {

        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        String itemCodeCsv = "", condition = "";
        try {
            if (!isMoverecFlag) {
                if (!isItemReceive) {
                    condition += " AND CASE WHEN ISNULL(im.allowNegativeStock,0) = 1 THEN 1 ELSE CASE WHEN ISNULL(ibs.balanceQty,0) > 0 THEN 1 ELSE 0 END END = 1 ";
                }
            }

            if (batchType.getId() == batchType.EXPIRED_BATCH.getId()) {
                condition += "     AND ib.expiryDate <=  :voucherDate ";
            } else if (batchType.getId() == batchType.NON_EXPIRED_BATCH.getId()) {
                condition += "     AND ib.expiryDate >  :voucherDate ";
            }

            sqlQuery = "SELECT STUFF ( "
                    + "    (SELECT ', ' + CAST(ib.itemCode AS VARCHAR) AS [text()] "
                    + "     FROM itemBatch ib "
                    + "     LEFT JOIN itemBatchStock ibs ON ibs.ibCode = ib.code "
                    + "     LEFT JOIN itemMast im ON im.code = ib.itemCode "
                    + "     WHERE ISNULL(ib.barcode, '') = :barCode "
                    + "     AND ISNULL(ibs.departmentCode, :departmentCode) = :departmentCode "
                    + "     AND ISNULL(ibs.site_Code,:siteCode) = :siteCode " + condition
                    //                    + "     AND ib.expiryDate >  :voucherDate "
                    //                    + "     AND ISNULL(ibs.balanceQty,0) > 0  "
                    + "     GROUP BY ib.itemCode "
                    + "     FOR XML PATH(''), TYPE).value('.[1]','nvarchar(max)'), 1,1,'' "
                    + ") AS csvItemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.iNull(siteCode).toString());
            psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            if (batchType.getId() == batchType.EXPIRED_BATCH.getId() || batchType.getId() == batchType.NON_EXPIRED_BATCH.getId()) {
                psTemp.setDate("voucherDate", DmServer.convertDate(voucherDate));
            }

            psTemp.setString("barCode", DmShared.xNull(barCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                itemCodeCsv = DmShared.xNull(rsTemp.getString("csvItemCode"));
            }
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

        return itemCodeCsv;
    }

    public static BarCode_Mdl barCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode, ItemBatchType batchType, boolean isItemReceive, Connection cnTemp, MedicineSaleOn sRateType) throws DmException {

        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        BarCode_Mdl pharmacyBarCode = new BarCode_Mdl();
        String[] tmpKey;
        String itemCsv = "";
        try {
            itemCsv = getItemCodeCsv(DmShared.xNull(barCode), voucherDate, departmentCode, siteCode, batchType, isItemReceive, false, cnTemp);
            tmpKey = DmShared.xNull(itemCsv).split("\\,");
            if (tmpKey.length == 1 && !tmpKey[0].equalsIgnoreCase("")) {
                pharmacyBarCode.setMedicineDetail(getMedicineSearchDetail(tmpKey[0], departmentCode, siteCode, cnTemp));
                pharmacyBarCode.setBatchDetails(getBatchSearchDetail(barCode, pharmacyBarCode.getMedicineDetail().getSharedKeyFieldCode(), voucherDate, departmentCode, siteCode, batchType, isItemReceive, cnTemp, sRateType));
                pharmacyBarCode.setItemCsv(itemCsv);
            } else if (tmpKey.length > 1) {
                pharmacyBarCode.setItemCsv(itemCsv);
            }

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

        return pharmacyBarCode;
    }

    private static MedicineSearch_Mdl getMedicineSearchDetail(String itemCode, String departmentCode, String siteCode, Connection cnTemp) throws DmException {

        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        MedicineSearch_Mdl medicineSearch = null;
        try {
            sqlQuery = "SELECT"
                    + "  im.code"
                    + " , im.name"
                    + " , im.displayName"
                    + " , im.manualCode"
                    + " , im.itemPacking AS pack"
                    + " , im.itemMakeCode AS makeCode"
                    + " , mm.companyName AS makeName"
                    + " , vis.currentStock"
                    + " , im.sku AS skuCode"
                    + " , im.priority "
                    + " , ums.unit AS skuName"
                    + " , ums.decimalPlaces AS skuDecimalPlaces"
                    + " , im.purchaseUnit AS purchaseUnitCode"
                    + " , ump.unit AS purchaseUnitName"
                    + " , ump.decimalPlaces AS purchaseUnitDecimalPlaces"
                    + " , ump.gstUnitCode AS purchaseUnitGstUnitCode"
                    + " , im.purchaseUnitConversionRate"
                    + " , im.saleUnit AS saleUnitCode"
                    + " , uml.unit AS saleUnitName"
                    + " , uml.decimalPlaces AS saleUnitDecimalPlaces"
                    + " , im.saleUnitConversionRate"
                    + " , im.hsnCode"
                    + " , hm.hsn AS hsnName"
                    + " , im.itemGSTNature"
                    + " , im.itsBasedOn"
                    + " , im.isBestBefore"
                    + " , im.isIncludeFreeQtyInSaleRate"
                    + " , im.mrp"
                    + " , im.saleRate"
                    + " , im.purchaseRate"
                    + " , im.schemeOnQty"
                    + " , im.schemeFreeQty"
                    + " , im.saleDiscountPer"
                    + " , im.purchaseDiscountPer"
                    + " , im.propertyList"
                    + " , im.itemCategoryCode AS categoryCode"
                    + " , ic.name AS categoryName"
                    + " , im.itemGroup AS groupCode"
                    + " , ig.name AS groupName"
                    //                    + " , STUFF (cnp.compositionNameWithPowerCsv, 2, 1, '') AS compositionNameWithPowerCsv"
                    //                    + " , ccp.compositionCodeWithPowerCsv"
                    //                    + " , STUFF (ccl.compositionCodeCsv, 1, 1, '') AS compositionCodeCsv"
                    + " , vidi.minStock"
                    + " , vidi.maxStock"
                    + " , vidi.reOrder"
                    + " , vidi.binName"
                    + " , im.manufacturerCode"
                    + " , sm.name AS manufacturerName"
                    + " , im.instruction"
                    + " , im.regionalInstruction"
                    + " , im.restrictGroup AS restrictedGroupCode"
                    + " , rg.name AS restrictedGroupName"
                    + " , im.divisionCode"
                    + " , sd.name AS divisionName"
                    + " , im.compositionCode"
                    + " , mcm.name AS compositionName "
                    + " , im.isSaleRateEditable"
                    + " , im.isReturnableItem"
                    + " , im.allowNegativeStock "
                    + " , im.isDiscountAllow "
                    //                    + " , isd.code AS saleSchemeCode"
                    //                    + " , imTts.itsPurchase"
                    + " , uml.gstUnitCode as saleUnitGstUnitCode"
                    + " FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN itemMakeMast mm ON mm.code = im.itemMakeCode"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code"
                    + " LEFT JOIN unitmast ump ON im.purchaseUnit = ump.code"
                    + " LEFT JOIN unitmast uml ON im.saleUnit = uml.code"
                    + " LEFT JOIN hsnMast hm ON hm.code = im.hsnCode"
                    + " LEFT JOIN itemCat ic ON ic.code = im.itemCategoryCode"
                    + " LEFT JOIN itemGroup ig ON ig.code = im.itemGroup"
                    + " LEFT JOIN subGroup sm ON sm.subCode = im.manufacturerCode"
                    + " LEFT JOIN restrictedGrpMast rg ON rg.code = im.restrictGroup"
                    + " LEFT JOIN subGroup sd ON sd.subCode = im.divisionCode"
                    + " LEFT JOIN medicineCompoMast mcm ON mcm.code = im.compositionCode "
                    + " LEFT JOIN ("
                    + " 	SELECT idi.itemCode, idi.minStock, idi.maxStock, idi.reOrder, idi.binName"
                    + " 	FROM itemDepartmentInfo idi"
                    + " 	WHERE idi.departmentCode = :departmentCode "
                    + "         AND idi.site_Code = :siteCode"
                    + " ) AS vidi ON vidi.itemCode = im.code "
                    + " LEFT JOIN ("
                    + " 	SELECT iStk.itemCode, SUM(iStk.balanceQty) AS currentStock "
                    + "         FROM itemStock iStk"
                    + "         WHERE iStk.site_Code = :siteCode AND iStk.departmentCode = :departmentCode"
                    + "         GROUP BY iStk.itemCode"
                    + " ) AS vis ON im.code = vis.itemCode "
                    + " WHERE im.isActive = 1 "
                    + " AND sl.site_Code = :siteCode "
                    + " AND (im.filterItemType = :filterItemType OR im.isCommon = 1) and im.code = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.iNull(siteCode).toString());
            psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                medicineSearch = new MedicineSearch_Mdl();
                medicineSearch.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                medicineSearch.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                medicineSearch.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                medicineSearch.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                medicineSearch.setPack(DmShared.xNull(rsTemp.getString("pack")));
                medicineSearch.setMakeCode(DmShared.xNull(rsTemp.getString("makeCode")));
                medicineSearch.setMakeName(DmShared.xNull(rsTemp.getString("makeName")));
                medicineSearch.setCurrentStock(DmServer.vNull(DmServer.vNull(rsTemp.getString("currentStock"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue()))));
                medicineSearch.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                medicineSearch.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineSearch.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));
                medicineSearch.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                medicineSearch.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                medicineSearch.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                medicineSearch.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                medicineSearch.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnitCode")));
                medicineSearch.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                medicineSearch.setSaleUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("saleUnitDecimalPlaces")));
                medicineSearch.setSaleUnitConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                medicineSearch.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineSearch.setHsnName(DmShared.xNull(rsTemp.getString("hsnName")));
                medicineSearch.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                GSTNature gstNature = GSTNature.getValueOf(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                medicineSearch.setItemGSTNatureName(gstNature != null ? gstNature.getValue() : "");
                medicineSearch.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                medicineSearch.setIsBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).toString());
                medicineSearch.setIsIncludeFreeQtyInSaleRate(DmServer.vNull(rsTemp.getString("isIncludeFreeQtyInSaleRate")));
                medicineSearch.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                medicineSearch.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                medicineSearch.setPurchaseRate(DmServer.vNull(rsTemp.getString("purchaseRate"), "0.00"));
                medicineSearch.setSchemeOnQty(DmServer.vNull(rsTemp.getString("schemeOnQty")));
                medicineSearch.setSchemeFreeQty(DmServer.vNull(rsTemp.getString("schemeFreeQty")));
                medicineSearch.setSaleDiscountPer(DmServer.vNull(rsTemp.getString("saleDiscountPer")));
                medicineSearch.setPurchaseDiscountPer(DmServer.vNull(rsTemp.getString("purchaseDiscountPer")));
                medicineSearch.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                medicineSearch.setCategoryCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                medicineSearch.setCategoryName(DmShared.xNull(rsTemp.getString("categoryName")));
                medicineSearch.setGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                medicineSearch.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                medicineSearch.setRestrictedGroupCode(DmShared.xNull(rsTemp.getString("restrictedGroupCode")));
                medicineSearch.setRestrictedGroupName(DmShared.xNull(rsTemp.getString("restrictedGroupName")));
                medicineSearch.setDivisionCode(DmShared.xNull(rsTemp.getString("divisionCode")));
                medicineSearch.setDivisionName(DmShared.xNull(rsTemp.getString("divisionName")));
                medicineSearch.setMinStock(DmShared.xNull(rsTemp.getString("minStock")));
                medicineSearch.setMaxStock(DmShared.xNull(rsTemp.getString("maxStock")));
                medicineSearch.setReOrder(DmShared.xNull(rsTemp.getString("reOrder")));
                medicineSearch.setBinName(DmShared.xNull(rsTemp.getString("binName")));
//                medicineSearch.setCompositionCodeCsv(DmShared.xNull(rsTemp.getString("compositionCodeCsv")).trim());
//                medicineSearch.setCompositionCodeWithPowerCsv(DmShared.xNull(rsTemp.getString("compositionCodeWithPowerCsv")).trim());
//                medicineSearch.setCompositionNameWithPowerCsv(DmShared.xNull(rsTemp.getString("compositionNameWithPowerCsv")).trim());
                medicineSearch.setManufacturerCode(DmShared.xNull(rsTemp.getString("manufacturerCode")));
                medicineSearch.setManufacturerName(DmShared.xNull(rsTemp.getString("manufacturerName")));
                medicineSearch.setInstruction(DmShared.xNull(rsTemp.getString("instruction")));
                medicineSearch.setRegionalInstruction(DmShared.xNull(rsTemp.getString("regionalInstruction")));
//                medicineSearch.setMatchtStatus(DmShared.iNull(rsTemp.getString("matchStatus")).toString());
                medicineSearch.setCompositionCode(DmShared.xNull(rsTemp.getString("compositionCode")));
                medicineSearch.setCompositionName(DmShared.xNull(rsTemp.getString("compositionName")));
                medicineSearch.setIsSaleRateEditable(DmShared.iNull(rsTemp.getString("isSaleRateEditable")).toString());
                medicineSearch.setIsReturnableItem(DmShared.iNull(rsTemp.getString("isReturnableItem")).toString());
                medicineSearch.setIsAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).toString());
                medicineSearch.setIsDiscountAllow(DmShared.iNull(rsTemp.getString("isDiscountAllow")).toString());
//                medicineSearch.setSaleSchemeCode(DmShared.xNull(rsTemp.getString("saleSchemeCode")));
                medicineSearch.setPurchaseUnitGstUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitGstUnitCode")));
                medicineSearch.setSaleUnitGstUnitCode(DmShared.xNull(rsTemp.getString("saleUnitGstUnitCode")));
//                medicineSearch.setItsPurchase(DmShared.xNull(rsTemp.getString("itsPurchase")));

                double margin = 0, costRate = 0, marginPer = 0;
                if (DmShared.vNull(rsTemp.getString("schemeOnQty")).doubleValue() > 0 || DmShared.vNull(rsTemp.getString("schemeFreeQty")).doubleValue() > 0) {
                    costRate = (DmShared.vNull(rsTemp.getString("schemeOnQty")).doubleValue() * DmShared.vNull(rsTemp.getString("purchaseRate")).doubleValue())
                            / (DmShared.vNull(rsTemp.getString("schemeOnQty")).doubleValue() + DmShared.vNull(rsTemp.getString("schemeFreeQty")).doubleValue());
                    margin = DmShared.vNull(rsTemp.getString("saleRate")).doubleValue() - costRate;
                }
                if (DmShared.vNull(rsTemp.getString("saleRate")).doubleValue() > 0) {
                    marginPer = DmShared.vNull(DmShared.vNull(margin).doubleValue() * 100) / DmShared.vNull(rsTemp.getString("saleRate")).doubleValue();
                }
                medicineSearch.setMarginPercent(DmServer.vNull(marginPer, "0.00"));
                medicineSearch.setPriority(DmShared.iNull(rsTemp.getString("priority")).toString());

                // lstTemp.add(medicineSearch);
            }

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

        return medicineSearch;
    }

    private static BatchNo_Mdl getBatchSearchDetail(String barCode, String itemCode, String voucherDate, String departmentCode, String siteCode, ItemBatchType batchType, boolean isItemReceive, Connection cnTemp, MedicineSaleOn sRateType) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", condition = "";
        BatchNo_Mdl batchMdl = null;
        String[] tmpKey = null;

        try {
            if (!isItemReceive) {
                condition += " AND CASE WHEN ISNULL(im.allowNegativeStock,0) = 1 THEN 1 ELSE CASE WHEN ISNULL(ibs.balanceQty,0) > 0 THEN 1 ELSE 0 END END = 1 ";
            }

            if (batchType.getId() == batchType.EXPIRED_BATCH.getId()) {
                condition += "     AND ib.expiryDate <=  :voucherDate ";
            } else if (batchType.getId() == batchType.NON_EXPIRED_BATCH.getId()) {
                condition += "     AND ib.expiryDate >  :voucherDate ";
            }

            sqlQuery = "SELECT STUFF ( "
                    + "    (SELECT ', ' + CAST(ib.code AS VARCHAR) AS [text()] "
                    + "     FROM itemBatch ib "
                    + "     LEFT JOIN itemBatchStock ibs ON ibs.ibCode = ib.code "
                    + "     LEFT JOIN itemMast im ON im.code = ib.itemCode "
                    + "     WHERE ISNULL(ib.barcode,'') = :barCode "
                    + "     AND ISNULL(ibs.departmentCode, :departmentCode) = :departmentCode "
                    + "     AND ISNULL(ibs.site_Code, :siteCode) = :siteCode " + condition
                    //                    + "	AND ib.expiryDate > :voucherDate "
                    //                    + "	AND ISNULL(ibs.balanceQty,0) > 0  "
                    + "     AND ib.itemCode = :itemCode "
                    + "     FOR XML PATH(''), TYPE).value('.[1]','nvarchar(max)'), 1,1,'' "
                    + ") AS csvIbCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("barCode", DmShared.xNull(barCode));
            psTemp.setString("siteCode", DmShared.iNull(siteCode).toString());
            psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            if (batchType.getId() == batchType.EXPIRED_BATCH.getId() || batchType.getId() == batchType.NON_EXPIRED_BATCH.getId()) {
                psTemp.setDate("voucherDate", DmServer.convertDate(voucherDate));
            }
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                tmpKey = DmShared.xNull(rsTemp.getString("csvIbCode")).split("\\,");
            }
            psTemp.close();
            rsTemp.close();

            if (tmpKey != null && tmpKey.length == 1 && !tmpKey[0].equalsIgnoreCase("")) {
                sqlQuery = " SELECT ib.code, "
                        + " ib.batchNo , ibs.balanceQty "
                        + " , ib.expiryDate, "
                        + " ib.manufacturingDate, "
                        + " ib.mrp, "
                        + " ib.saleRate, "
                        + " ib.skuSaleRate, "
                        + " ib.skuMrp, "
                        + " um.unit as skuName, "
                        + " DATEDIFF(day, ib.manufacturingDate, ib.expiryDate) AS bestBeforeDays, "
                        + " im.sku as skuCode, "
                        + " im.purchaseUnitConversionRate , "
                        + " idi.reOrder , "
                        + " im.saleUnitConversionRate , "
                        + " idi.binName,"
                        + " idi.minStock, "
                        + " um.decimalPlaces, "
                        + " ump.decimalPlaces as pUnitDecimalPlaces , "
                        + " ums.decimalPlaces as sUnitDecimalPlaces  ,"
                        + " ib.barcode "
                        + " FROM itemBatch ib "
                        + " LEFT JOIN itemBatchStock ibs ON ibs.ibCode = ib.code "
                        + " LEFT JOIN itemMast im ON im.code = ib.itemCode "
                        + " LEFT JOIN unitMast um ON um.code = im.sku "
                        + " LEFT JOIN unitMast ump ON ump.code = im.purchaseUnit "
                        + " LEFT JOIN unitMast ums ON ums.code = im.saleUnit "
                        + " LEFT JOIN itemDepartmentInfo idi ON idi.itemCode = ib.itemCode AND idi.departmentCode = ISNULL(ibs.departmentCode,:departmentCode)"
                        + " AND idi.site_Code = ISNULL(ibs.site_Code,:siteCode) "
                        + " WHERE ib.itemCode = :itemCode "
                        + " AND ISNULL(ibs.departmentCode,:departmentCode) = :departmentCode "
                        + " AND ISNULL(ibs.site_Code,:siteCode) = :siteCode   "
                        + " AND ib.code = :ibCode";
                if (batchType.getId() == batchType.EXPIRED_BATCH.getId()) {
                    sqlQuery += "     AND ib.expiryDate <=  :voucherDate ";
                } else if (batchType.getId() == batchType.NON_EXPIRED_BATCH.getId()) {
                    sqlQuery += "     AND ib.expiryDate >  :voucherDate ";
                }
                if (!isItemReceive) {
                    sqlQuery += " AND CASE WHEN ISNULL(im.allowNegativeStock,0) = 1 THEN 1 ELSE CASE WHEN ISNULL(ibs.balanceQty,0) > 0 THEN 1 ELSE 0 END END = 1 ";
                }

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("siteCode", DmShared.iNull(siteCode).toString());
                psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
                psTemp.setString("itemCode", DmShared.xNull(itemCode));
                psTemp.setString("ibCode", DmShared.xNull(tmpKey[0]));
                if (batchType.getId() == batchType.EXPIRED_BATCH.getId() || batchType.getId() == batchType.NON_EXPIRED_BATCH.getId()) {
                    psTemp.setDate("voucherDate", DmServer.convertDate(voucherDate));
                }
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    batchMdl = new BatchNo_Mdl();
                    batchMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                    batchMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                    batchMdl.setIbCode(DmShared.xNull(rsTemp.getString("code")));
                    batchMdl.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                    batchMdl.setBinName(DmShared.xNull(rsTemp.getString("binName")));
                    batchMdl.setBalanceQty(DmServer.vNull(rsTemp.getString("balanceQty")));
                    batchMdl.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                    batchMdl.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                    batchMdl.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                    batchMdl.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                    batchMdl.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                    batchMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                    batchMdl.setMinStock(DmServer.vNull(rsTemp.getString("minStock")));
                    batchMdl.setReOrderLevel(DmServer.vNull(rsTemp.getString("reOrder")));
                    batchMdl.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));
                    batchMdl.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                    batchMdl.setDecimalPlaces(DmServer.vNull(rsTemp.getString("decimalPlaces")));
                    batchMdl.setBestBeforeDays(DmServer.vNull(rsTemp.getString("bestBeforeDays")));

                    if (sRateType == MedicineSaleOn.COST_PRICE) {
                        batchMdl.setSkuLandedRate(DmServer.vNull(STServer.getSkuLandedRate(DmShared.iNull(itemCode).toString(), rsTemp.getString("code"), DmShared.iNull(departmentCode).toString(), DmShared.iNull(siteCode).toString(), voucherDate, cnTemp), "0.00"));
                    } else {
                        batchMdl.setSkuLandedRate("0");
                    }

                    if (DmServer.convertDate(DmShared.dNull(rsTemp.getDate("expiryDate"))).getTime() <= DmServer.convertDate(voucherDate).getTime()) {
                        batchMdl.setExpiredIbCode(true);
                    } else {
                        batchMdl.setExpiredIbCode(false);
                    }
                    double pQty = DmShared.vNull(rsTemp.getString("balanceQty")) / DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                    double sQty = DmShared.vNull(rsTemp.getString("balanceQty")) / DmShared.vNull(rsTemp.getString("saleUnitConversionRate"));
                    batchMdl.setBalanceQtyInPurcUnit(DmServer.vNull(pQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("pUnitDecimalPlaces")).intValue())));
                    batchMdl.setBalanceQtyInSaleUnit(DmServer.vNull(sQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("sUnitDecimalPlaces")).intValue())));
                    batchMdl.setBarCode(DmShared.xNull(rsTemp.getString("barcode")));

                }

            }

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

        return batchMdl;
    }

}
