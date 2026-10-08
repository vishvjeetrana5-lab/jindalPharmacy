package aarogya.selfToolkit.D365_To_His_DataSync.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.HsnMaster_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.ItemCategory_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.ItemGroup_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.ItemMakeMast_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.ItemMaterialCatMast_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.ItemMaterialType_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.ItemModelMaster_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.ItemRestrictedGroup_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.ItemSpeciality_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.ItemSubCategoryMaster_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.MedicineMaster_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.Strength_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.UnitMaster_Mdl;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemMasterSync {

    private static final String vCat = new ProjectVoucherCategory().MANUFACTURER_MAST;

    public static void syncItemMaster(Connection cnTemp, Connection stagingCnTemp, String transactioDB) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<MedicineMaster_Mdl> newList = new ArrayList<>();
        List<MedicineMaster_Mdl> updateList = new ArrayList<>();
        String sqlQuery = "";
        try {

            /*=================Sync Subgroup======================*/
            syncParents(cnTemp, stagingCnTemp);
            System.out.println("All Patent Are Sync");
//            <editor-fold defaultstate="collapsed" desc="Fetch item from staging Master">
            /*=================Get item From Staging Database======================*/
            sqlQuery = "SELECT * \n"
                    + "FROM (\n"
                    + "    SELECT \n"
                    + "        im.code, im.name,\n"
                    + "        im.displayName,\n"
                    + "        im.manualCode,\n"
                    + "        im.SKUName,\n"
                    + "        im.purchaseUnitName,\n"
                    + "        im.saleUnitName,\n"
                    + "        im.itemPacking,\n"
                    + "        im.purchaseUnitConversionRate,\n"
                    + "        im.saleUnitConversionRate,\n"
                    + "        im.hsnCode,\n"
                    + "        im.modelName,\n"
                    + "        im.Model,\n"
                    + "        im.strengthName,\n"
                    + "        im.Strength,\n"
                    + "        im.propertyList,\n"
                    + "        im.ItemCategoryCode,\n"
                    + "        im.subCategoryCodeName,\n"
                    + "        im.MaterialCategoryCode,\n"
                    + "        im.MaterialTypeCode,\n"
                    + "        im.marketingCompanyName,\n"
                    + "        im.itemGroupName,\n"
                    + "        im.ItemMakeCode,\n"
                    + "        im.filterItemType,\n"
                    + "        im.itemType,\n"
                    + "        im.ManufacturerCode as ManufacturerCodeName,\n"
                    + "        im.isActive,\n"
                    + "        im.isBarcodeActive,\n"
                    + "        im.isConsignment,\n"
                    + "        im.isNarcotics,\n"
                    + "        im.isReturnableItem,\n"
                    + "        im.isSaleRateEditable,\n"
                    + "        im.isIncludeFreeQtyInSaleRate,\n"
                    + "        im.isDiscountAllow,\n"
                    + "        im.isQuotationMandatory,\n"
                    + "        im.allowMRPDiscPattern,\n"
                    + "        im.marginRateFix,\n"
                    + "        im.remark,\n"
                    + "        im.AllowNegativeStock,\n"
                    + "        im.ItemGSTNature,\n"
                    + "        im.mrp,\n"
                    + "        im.SaleRate,\n"
                    + "        im.PurchaseRate,\n"
                    + "        im.SchemeOnQty,\n"
                    + "        im.SaleDiscountPer,\n"
                    + "        im.PurchaseDiscountPer,\n"
                    + "        im.IsLifeSaving,\n"
                    + "        im.IsHighValue,\n"
                    + "        im.IsFlowThrough,\n"
                    + "        im.IsBilledItem,\n"
                    + "        im.DivisionCode,\n"
                    + "        im.DivisionName,\n"
                    + "        im.TaxPerc,\n"
                    + "        im.RegionalInstruction,\n"
                    + "        im.SchemeFreeQty,\n"
                    + "        im.D365_Status,\n"
                    + "\n"
                    + "        -- Joined Codes\n"
                    + "        u.code AS purchaseUnitCode,\n"
                    + "        su.code AS saleUnitCode,\n"
                    + "        h.code AS hsnMasterCode,\n"
                    + "        ig.code AS itemGroupCode,\n"
                    + "        imk.code AS itemMakeCodeRef,\n"
                    + "        mdl.code AS modelCode,\n"
                    + "        str.code AS strengthCode,\n"
                    + "        subc.code AS subCategoryCodeRef,\n"
                    + "        ic.code AS itemCategoryCodeRef,\n"
                    + "        mt.code AS materialTypeCodeRef,\n"
                    + "        mc.code AS materialCategoryCodeRef,\n"
                    + "        sp.code AS specialityCode,\n"
                    + "        msc.code AS medicineSubCategoryCode,\n"
                    + "        rg.code AS restrictGroupcode,\n"
                    + "        sg.subcode as ManufacturerCode,\n"
                    + "        sk.code as skuUnitCode,\n"
                    + "\n"
                    + "        ROW_NUMBER() OVER (\n"
                    + "            PARTITION BY im.manualCode \n"
                    + "            ORDER BY im.D365_Timestamp DESC\n"
                    + "        ) AS rn\n"
                    + "\n"
                    + "    FROM D365_ItemMasterData im\n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.unitmast  u WITH (NOLOCK)\n"
                    + "        ON u.D365_Code = im.purchaseUnit   collate database_default \n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.unitmast sk WITH (NOLOCK)\n"
                    + "        ON sk.D365_Code = im.SKUName collate database_default \n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.unitmast su WITH (NOLOCK)\n"
                    + "        ON su.D365_Code = im.saleUnit  collate database_default\n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.unitmast uip WITH (NOLOCK)\n"
                    + "        ON uip.D365_Code = im.ItemPacking  collate database_default \n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.hsnMast h WITH (NOLOCK)\n"
                    + "        ON h.D365_code = im.hsnCode  collate database_default \n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.itemGroup ig WITH (NOLOCK)\n"
                    + "        ON ig.D365_Code = im.itemGroup collate database_default \n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.itemMakeMast imk WITH (NOLOCK)\n"
                    + "        ON imk.D365_Code = im.ItemMakeCode collate database_default \n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.itemModelMast mdl  WITH (NOLOCK)\n"
                    + "        ON mdl.D365_Code = im.Model  collate database_default\n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.itemStrengthMast str WITH (NOLOCK)\n"
                    + "        ON str.D365_Code = im.Strength  collate database_default\n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.medicineSubCategoryMast  subc WITH (NOLOCK)\n"
                    + "        ON subc.D365_Code = im.subCategoryCode  collate database_default\n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.itemCat  ic WITH (NOLOCK)\n"
                    + "        ON ic.D365_Code = im.ItemCategoryCode collate database_default \n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.restrictedGrpMast  rg WITH (NOLOCK)\n"
                    + "        ON rg.D365_Code = im.RestrictGroup  collate database_default\n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.materialTypeMast  mt WITH (NOLOCK)\n"
                    + "        ON mt.D365_Code = im.MaterialTypeCode collate database_default\n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.materialCatMast  mc  WITH (NOLOCK)\n"
                    + "        ON mc.D365_Code = im.MaterialCategoryCode  collate database_default\n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.itemSpecialityMast  sp WITH (NOLOCK)\n"
                    + "        ON sp.D365_Code = im.ItemSpecialityCode collate database_default\n"
                    + "\n"
                    + "    LEFT JOIN " + transactioDB + ".dbo.medicineSubCategoryMast msc  WITH (NOLOCK)\n"
                    + "        ON msc.D365_Code = im.SubCategoryCode  collate database_default \n"
                    + "LEFT JOIN " + transactioDB + ".dbo.subGroup sg  WITH (NOLOCK)\n"
                    + "    ON sg.D365_Code = im.ManufacturerCode   collate database_default "
                    + "\n"
                    + "    WHERE im.D365_Status IN ('New','Update')\n"
                    + "      AND im.jp_TimeStamp IS NULL\n"
                    + "\n"
                    + ") t\n"
                    + "WHERE rn = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            // ================= MAP DATA =================
            while (rsTemp.next()) {

                MedicineMaster_Mdl mdl = new MedicineMaster_Mdl();

                // ===== BASIC =====
                mdl.setD365Code(DmShared.xNull(rsTemp.getString("code")));
                mdl.setName(DmShared.xNull(rsTemp.getString("name")));
                mdl.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                mdl.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                mdl.setSkuName(DmShared.xNull(rsTemp.getString("SKUName")));
                mdl.setSkuCode(DmShared.xNull(rsTemp.getString("skuUnitCode")));

                mdl.setPropertyList(DmShared.xNull(rsTemp.getString("PropertyList")));
                mdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                mdl.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                mdl.setItemPacking(DmShared.xNull(rsTemp.getString("itemPacking")));

                mdl.setPurchaseUnitConversionRate(DmShared.xNull(rsTemp.getString("purchaseUnitConversionRate")));
                mdl.setSaleUnitConversionRate(DmShared.xNull(rsTemp.getString("saleUnitConversionRate")));

                mdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));

                mdl.setModelName(DmShared.xNull(rsTemp.getString("modelName")));
                mdl.setModel(DmShared.xNull(rsTemp.getString("Model")));

                mdl.setStrengthName(DmShared.xNull(rsTemp.getString("strengthName")));
                mdl.setStrength(DmShared.xNull(rsTemp.getString("Strength")));

                mdl.setItemCategoryCode(DmShared.xNull(rsTemp.getString("ItemCategoryCode")));
                mdl.setSubCategoryCodeName(DmShared.xNull(rsTemp.getString("subCategoryCodeName")));

                mdl.setMaterialCategoryCode(DmShared.xNull(rsTemp.getString("MaterialCategoryCode")));
                mdl.setMaterialTypeCode(DmShared.xNull(rsTemp.getString("MaterialTypeCode")));

                mdl.setMarketingCompanyName(DmShared.xNull(rsTemp.getString("marketingCompanyName")));
                mdl.setItemGroupName(DmShared.xNull(rsTemp.getString("itemGroupName")));
                mdl.setItemMakeCode(DmShared.xNull(rsTemp.getString("ItemMakeCode")));

                mdl.setFilterItemType(DmShared.xNull(rsTemp.getString("filterItemType")));
                mdl.setItemType(DmShared.xNull(rsTemp.getString("itemType")));

                mdl.setManufacturerCode(DmShared.xNull(rsTemp.getString("ManufacturerCode")));

                // ===== FLAGS =====
                mdl.setIsActive(DmShared.xNull(rsTemp.getString("isActive")));
                mdl.setIsBarcodeActive(DmShared.xNull(rsTemp.getString("isBarcodeActive")));
                mdl.setIsConsignment(DmShared.xNull(rsTemp.getString("isConsignment")));
                mdl.setIsNarcotics(DmShared.xNull(rsTemp.getString("isNarcotics")));
                mdl.setIsReturnableItem(DmShared.xNull(rsTemp.getString("isReturnableItem")));
                mdl.setIsSaleRateEditable(DmShared.xNull(rsTemp.getString("isSaleRateEditable")));
                mdl.setIsIncludeFreeQtyInSaleRate(DmShared.xNull(rsTemp.getString("isIncludeFreeQtyInSaleRate")));
                mdl.setIsDiscountAllow(DmShared.xNull(rsTemp.getString("isDiscountAllow")));
                mdl.setIsQuotationMandatory(DmShared.xNull(rsTemp.getString("isQuotationMandatory")));
                mdl.setAllowMRPDiscPattern(DmShared.xNull(rsTemp.getString("allowMRPDiscPattern")));
                mdl.setMarginRateFix(DmShared.xNull(rsTemp.getString("marginRateFix")));

                mdl.setRemark(DmShared.xNull(rsTemp.getString("remark")));

                // ===== JOINED (HIS MASTER CODES) =====
                mdl.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                mdl.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnitCode")));
                mdl.setHsnMasterCode(DmShared.xNull(rsTemp.getString("hsnMasterCode")));
                mdl.setItemGroupCode(DmShared.xNull(rsTemp.getString("itemGroupCode")));
                mdl.setItemMakeCodeRef(DmShared.xNull(rsTemp.getString("itemMakeCodeRef")));
                mdl.setModelCode(DmShared.xNull(rsTemp.getString("modelCode")));
                mdl.setStrengthCode(DmShared.xNull(rsTemp.getString("strengthCode")));
                mdl.setSubCategoryCodeRef(DmShared.xNull(rsTemp.getString("subCategoryCodeRef")));
                mdl.setItemCategoryCodeRef(DmShared.xNull(rsTemp.getString("itemCategoryCodeRef")));
                mdl.setMaterialTypeCodeRef(DmShared.xNull(rsTemp.getString("materialTypeCodeRef")));
                mdl.setMaterialCategoryCodeRef(DmShared.xNull(rsTemp.getString("materialCategoryCodeRef")));
                mdl.setSpecialityCode(DmShared.xNull(rsTemp.getString("specialityCode")));
                mdl.setMedicineSubCategoryCode(DmShared.xNull(rsTemp.getString("medicineSubCategoryCode")));

                // ===== EXTRA FIELDS =====
                mdl.setAllowNegativeStock(DmShared.xNull(rsTemp.getString("AllowNegativeStock")));
                mdl.setItemGSTNature(DmShared.xNull(rsTemp.getString("ItemGSTNature")));
//                mdl.setIsIndentMandatory(DmShared.xNull(rsTemp.getString("IsIndentMandatory")));
//                mdl.setIsCommon(DmShared.xNull(rsTemp.getString("IsCommon")));
                mdl.setMRP(DmShared.xNull(rsTemp.getString("MRP")));
                mdl.setSaleRate(DmShared.xNull(rsTemp.getString("SaleRate")));
                mdl.setPurchaseRate(DmShared.xNull(rsTemp.getString("PurchaseRate")));
                mdl.setSchemeOnQty(DmShared.xNull(rsTemp.getString("SchemeOnQty")));
                mdl.setSchemeFreeQty(DmShared.xNull(rsTemp.getString("SchemeFreeQty")));
                mdl.setPurchaseDiscountPer(DmShared.xNull(rsTemp.getString("PurchaseDiscountPer")));
                mdl.setSaleDiscountPer(DmShared.xNull(rsTemp.getString("SaleDiscountPer")));
                mdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("RestrictGroupcode")));
                mdl.setIsLifeSaving(DmShared.xNull(rsTemp.getString("IsLifeSaving")));
                mdl.setIsHighValue(DmShared.xNull(rsTemp.getString("IsHighValue")));
                mdl.setIsFlowThrough(DmShared.xNull(rsTemp.getString("IsFlowThrough")));
                mdl.setIsBilledItem(DmShared.xNull(rsTemp.getString("IsBilledItem")));
                mdl.setDivisionCode(DmShared.xNull(rsTemp.getString("DivisionCode")));
                mdl.setDivisionName(DmShared.xNull(rsTemp.getString("DivisionName")));
                mdl.setRegionalInstruction(DmShared.xNull(rsTemp.getString("RegionalInstruction")));
                mdl.setTaxPerc(DmShared.xNull(rsTemp.getString("TaxPerc")));
                mdl.setD365Status(DmShared.xNull(rsTemp.getString("D365_Status")));

                // ===== SPLIT LOGIC =====
                if ("NEW".equalsIgnoreCase(mdl.getD365Status())) {
                    newList.add(mdl);
                } else if ("UPDATE".equalsIgnoreCase(mdl.getD365Status())) {
                    updateList.add(mdl);
                }

            }
            psTemp.close();
            rsTemp.close();
            //</editor-fold>
            String itemCode = "";

            int siteCount = DmShared.iNull(DmServer.executeScalar("select count(code) as siteCount from siteMast ", cnTemp)).intValue();

//            <editor-fold defaultstate="collapsed" desc="Save New item Master">
            for (MedicineMaster_Mdl list : newList) {

                if (!list.getDivisionName().equalsIgnoreCase("")) {
                    if (DmShared.xNull(DmServer.executeScalar("select subcode from subgroup where name = '" + DmShared.xNull(list.getDivisionName()) + "' ", cnTemp)).equalsIgnoreCase("")) {
                        syncDivisionMast(cnTemp, stagingCnTemp, list.getDivisionName(), list.getDivisionCode());
                    }
                }

                /*===============Check code hai ki nahi===============*/
                itemCode = DmShared.xNull(DmServer.executeScalar("select code from itemmast where D365_code = '" + DmShared.xNull(list.getD365Code()) + "' ", cnTemp));
                if (itemCode.equals("")) {
                    list.setSharedKeyFieldCode(DmServer.getSerialNoBigInt("1", "itemMast", cnTemp));
                    sqlQuery = "select code,v_SNo,its_Code_Purchase,its_Code_PurchaseRet,\n"
                            + "its_Code_Sales,its_Code_SalesRet from hsnITSDetail where code = '" + list.getHsnMasterCode() + "' ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        list.setIts_Code_Purchase(DmShared.xNull(rsTemp.getString("its_Code_Purchase")));
                        list.setIts_Code_PurchaseRet(DmShared.xNull(rsTemp.getString("its_Code_PurchaseRet")));
                        list.setIts_Code_Sales(DmShared.xNull(rsTemp.getString("its_Code_Sales")));
                        list.setIts_Code_SalesRet(DmShared.xNull(rsTemp.getString("its_Code_SalesRet")));
                    }
                    psTemp.close();
                    rsTemp.close();

                    sqlQuery = "INSERT INTO itemmast (CODE, NAME, itemGroup, itemType,filterItemType, sku, "
                            + " preparedBY, u_EntDt, transferDt, itemMakeCode, isActive, itemCategoryCode,"
                            + " manualCode, hsnCode,isBestBefore,isLifeSaving,isHighValue,isFlowThrough,  "
                            + " isNarcotics,isConsignment,isReturnableItem,isBilledItem,PropertyList,itemGSTNature,purchaseUnit,saleUnit,"
                            + " purchaseUnitConversionRate,saleUnitConversionRate,allowNegativeStock,displayName,isSaleRateEditable,"
                            + " isIncludeFreeQtyInSaleRate,"
                            + " isDiscountAllow,isCommon,instruction,regionalInstruction,itempacking,manufacturerCode,divisionCode"
                            + " , mrp , saleRate , purchaseRate , schemeOnQty , schemeFreeQty  , isIndentMandatory "
                            + " , isQuotationMandatory , isBarcodeActive , purchaseDiscountPer , saleDiscountPer , priority , preparedDate "
                            + " ,materialCategoryCode,model,strength, allowMRPDiscPattern, "
                            + " subCode ,\n"
                            + " grIrAc,ConsumptionAc,cogsAc ,subCategoryCode ,marginRateFix,D365_code) "
                            + "\n"
                            + " VALUES (:code, :name, :itemGroup, :itemType,:filterItemType,:sku, "
                            + " :preparedBY, :uEntDt, NULL, :itemMakeCode, :isActive,  :itemCategoryCode,"
                            + " :manualCode,:hsnCode,:isBestBefore,:isLifeSaving,:isHighValue,:isFlowThrough, "
                            + " :isNarcotics,:isConsignment,:isReturnableItem,:isBilledItem,:propertyList,:itemGSTNature,:purchaseUnit,:saleUnit,"
                            + " :purchaseUnitConversionRate,:saleUnitConversionRate,:allowNegativeStock,:displayName,:isSaleRateEditable,"
                            + " :isIncludeFreeQtyInSaleRate,:isDiscountAllow,:isCommon,"
                            + " :instruction,:regionalInstruction,:itempacking,:manufacturerCode,:divisionCode"
                            + " , :mrp , :saleRate , :purchaseRate , :schemeOnQty , :schemeFreeQty  , :isIndentMandatory "
                            + " , :isQuotationMandatory , :isBarcodeActive , :purchaseDiscountPer , :saleDiscountPer , :priority , :uEntDt "
                            + " ,:materialCategoryCode,:model,:strength, :allowMRPDiscPattern, "
                            + " :subCode"
                            + " ,:grIrAc,:ConsumptionAc,:cogsAc ,:subCategoryCode ,:marginRateFix,:D365_code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(list.getSharedKeyFieldCode()));
                    psTemp.setString("name", DmShared.xNull(list.getName()));
                    psTemp.setString("itemGroup", DmShared.foriegnKey(list.getItemGroupCode()));
                    psTemp.setString("manualCode", list.getManualCode());
                    psTemp.setString("itemType", DmShared.foriegnKey(list.getItemType()));
                    psTemp.setString("filterItemType", DmShared.foriegnKey(list.getFilterItemType()));
                    psTemp.setString("sku", DmShared.foriegnKey(list.getSkuCode()));
                    psTemp.setString("itemMakeCode", DmShared.foriegnKey(list.getItemMakeCodeRef()));
                    psTemp.setString("isActive", DmShared.xNull(list.getIsActive()));
                    psTemp.setString("itemCategoryCode", DmShared.foriegnKey(list.getItemCategoryCodeRef()));
                    psTemp.setString("hsnCode", DmShared.foriegnKey(list.getHsnMasterCode()));
                    psTemp.setString("isBestBefore", DmShared.xNull("0"));
                    psTemp.setString("isLifeSaving", DmShared.xNull(list.getIsLifeSaving()));
                    psTemp.setString("isHighValue", DmShared.xNull(list.getIsHighValue()));
                    psTemp.setString("isFlowThrough", DmShared.xNull(list.getIsFlowThrough()));
                    psTemp.setString("isNarcotics", DmShared.xNull(list.getIsNarcotics()));
                    psTemp.setString("isIndentMandatory", DmShared.xNull(list.getIsIndentMandatory()));
                    psTemp.setString("allowMRPDiscPattern", DmShared.xNull(list.getAllowMRPDiscPattern()));
                    psTemp.setString("isQuotationMandatory", DmShared.xNull(list.getIsQuotationMandatory()));
                    psTemp.setString("isBarcodeActive", DmShared.xNull(list.getIsBarcodeActive()));
                    psTemp.setString("isConsignment", DmShared.xNull(list.getIsConsignment()));
                    psTemp.setString("isReturnableItem", DmShared.xNull(list.getIsReturnableItem()));
                    psTemp.setString("isBilledItem", DmShared.xNull(list.getIsBilledItem()));
                    psTemp.setString("PropertyList", DmShared.xNull(list.getPropertyList()));
                    psTemp.setString("preparedBY", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("itemGSTNature", DmShared.xNull(list.getItemGSTNature()));
                    psTemp.setString("isDiscountAllow", DmShared.xNull(list.getIsDiscountAllow()));
                    psTemp.setString("isCommon", DmShared.xNull(list.getIsCommon()));
                    psTemp.setString("purchaseUnit", DmShared.xNull(list.getPurchaseUnitCode()));
                    psTemp.setString("saleUnit", DmShared.xNull(list.getSaleUnitCode()));
                    psTemp.setString("purchaseUnitConversionRate", DmServer.vNull(list.getPurchaseUnitConversionRate()));
                    psTemp.setString("saleUnitConversionRate", DmServer.vNull(list.getSaleUnitConversionRate()));
                    psTemp.setString("allowNegativeStock", DmShared.xNull(list.getAllowNegativeStock()));
                    psTemp.setString("isSaleRateEditable", DmShared.xNull(list.getIsSaleRateEditable()));
                    psTemp.setString("isIncludeFreeQtyInSaleRate", DmShared.xNull(list.getIsIncludeFreeQtyInSaleRate()));
                    psTemp.setString("displayName", DmShared.xNull(list.getDisplayName()));
                    psTemp.setString("instruction", DmShared.xNull(list.getInstruction()));
                    psTemp.setString("regionalInstruction", DmShared.xNull(list.getRegionalInstruction()));
                    psTemp.setString("itempacking", DmShared.xNull(list.getItemPacking()));
                    psTemp.setString("manufacturerCode", DmShared.foriegnKey(list.getManufacturerCode()));
                    psTemp.setString("divisionCode", DmShared.foriegnKey(list.getDivisionCode()));
                    psTemp.setString("mrp", DmServer.vNull(list.getMRP(), "0.00"));
                    psTemp.setString("saleRate", DmServer.vNull(list.getSaleRate(), "0.00"));
                    psTemp.setString("purchaseRate", DmServer.vNull(list.getPurchaseRate(), "0.00"));
                    psTemp.setString("schemeFreeQty", DmServer.vNull(list.getSchemeFreeQty()));
                    psTemp.setString("schemeOnQty", DmServer.vNull(list.getSchemeOnQty()));
                    psTemp.setString("purchaseDiscountPer", DmServer.vNull(list.getPurchaseDiscountPer(), "0.00"));
                    psTemp.setString("saleDiscountPer", DmServer.vNull(list.getSaleDiscountPer(), "0.00"));
                    psTemp.setString("priority", ("0"));
                    psTemp.setString("materialCategoryCode", DmShared.foriegnKey(list.getMaterialCategoryCodeRef()));
                    psTemp.setString("model", DmShared.foriegnKey(list.getModelCode()));
                    psTemp.setString("strength", DmShared.foriegnKey(list.getStrengthCode()));
                    psTemp.setString("subCode", DmServer.executeScalar(" select * from subGroup where name ='GENERAL INVENTORY A/C' ", cnTemp));
                    psTemp.setString("grIrAc", null);
                    psTemp.setString("ConsumptionAc", null);
                    psTemp.setString("cogsAc", null);
                    psTemp.setString("subCategoryCode", DmShared.foriegnKey(list.getSubCategoryCodeRef()));
                    psTemp.setString("marginRateFix", DmShared.xNull(list.getMarginRateFix()));
                    psTemp.setString("D365_code", DmShared.xNull(list.getD365Code()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "insert into itemITSDetail (code ,v_SNo,its_Code_Purchase,its_Code_PurchaseRet,"
                            + "                  its_Code_Sales,its_Code_SalesRet) "
                            + "         		values(:code ,:v_SNo,:its_Code_Purchase,:its_Code_PurchaseRet,"
                            + "                 :its_Code_Sales,:its_Code_SalesRet) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", list.getSharedKeyFieldCode());
                    psTemp.setString("v_SNo", "1");
                    psTemp.setString("its_Code_Purchase", DmShared.foriegnKey(list.getIts_Code_Purchase()));
                    psTemp.setString("its_Code_PurchaseRet", DmShared.foriegnKey(list.getIts_Code_PurchaseRet()));
                    psTemp.setString("its_Code_Sales", DmShared.foriegnKey(list.getIts_Code_Sales()));
                    psTemp.setString("its_Code_SalesRet", DmShared.foriegnKey(list.getIts_Code_SalesRet()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    for (int i = 1; i <= siteCount; i++) {
                        sqlQuery = "insert into itemSiteList (code ,site_Code) "
                                + " values(:code ,:site_Code) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", list.getSharedKeyFieldCode());
                        psTemp.setString("site_Code", DmShared.iNull(i).toString());
                        psTemp.executeUpdate();
                        psTemp.close();
                    }

                }

                /*============================update isStatus into Staging======================*/
                sqlQuery = " update D365_ItemMasterData set \n"
                        + " jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                        + " jp_status = 'Success'\n"
                        + " where code = :code "
                        + " and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(list.getD365Code()));
                psTemp.setString("jp_Code", DmShared.iNull(list.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();

            }
            //</editor-fold>
//            <editor-fold defaultstate="collapsed" desc="Save update item Master">
            for (MedicineMaster_Mdl list : updateList) {

                if (!DmShared.xNull(list.getDivisionName()).equalsIgnoreCase("")) {

                    list.setDivisionCode(DmShared.xNull(DmServer.executeScalar("select subcode from subgroup where name = '" + DmShared.xNull(list.getDivisionName()) + "' ", cnTemp)));

                    if (DmShared.xNull(list.getDivisionCode()).equalsIgnoreCase("")) {
                        list.setDivisionCode(DmShared.xNull(syncDivisionMast(cnTemp, stagingCnTemp, list.getDivisionName(), list.getDivisionCode())));
                    }
                }


                /*===============Check code hai ki nahi===============*/
                itemCode = DmShared.xNull(DmServer.executeScalar("select code from itemmast where D365_code = '" + DmShared.xNull(list.getD365Code()) + "' ", cnTemp));

                sqlQuery = "select code,v_SNo,its_Code_Purchase,its_Code_PurchaseRet,\n"
                        + "its_Code_Sales,its_Code_SalesRet from hsnITSDetail where code = '" + list.getHsnMasterCode() + "' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    list.setIts_Code_Purchase(DmShared.xNull(rsTemp.getString("its_Code_Purchase")));
                    list.setIts_Code_PurchaseRet(DmShared.xNull(rsTemp.getString("its_Code_PurchaseRet")));
                    list.setIts_Code_Sales(DmShared.xNull(rsTemp.getString("its_Code_Sales")));
                    list.setIts_Code_SalesRet(DmShared.xNull(rsTemp.getString("its_Code_SalesRet")));
                }
                psTemp.close();
                rsTemp.close();
                if (itemCode.equals("")) {
                    list.setSharedKeyFieldCode(DmServer.getSerialNoBigInt("1", "itemMast", cnTemp));

                    sqlQuery = "INSERT INTO itemmast (CODE, NAME, itemGroup, itemType,filterItemType, sku, "
                            + " preparedBY, u_EntDt, transferDt, itemMakeCode, isActive, itemCategoryCode,"
                            + " manualCode, hsnCode,isBestBefore,isLifeSaving,isHighValue,isFlowThrough,  "
                            + " isNarcotics,isConsignment,isReturnableItem,isBilledItem,PropertyList,itemGSTNature,purchaseUnit,saleUnit,"
                            + " purchaseUnitConversionRate,saleUnitConversionRate,allowNegativeStock,displayName,isSaleRateEditable,"
                            + " isIncludeFreeQtyInSaleRate,"
                            + " isDiscountAllow,isCommon,instruction,regionalInstruction,itempacking,manufacturerCode,divisionCode"
                            + " , mrp , saleRate , purchaseRate , schemeOnQty , schemeFreeQty  , isIndentMandatory "
                            + " , isQuotationMandatory , isBarcodeActive , purchaseDiscountPer , saleDiscountPer , priority , preparedDate "
                            + " ,materialCategoryCode,model,strength, allowMRPDiscPattern, "
                            + " subCode ,\n"
                            + " grIrAc,ConsumptionAc,cogsAc ,subCategoryCode ,marginRateFix,D365_code) "
                            + "\n"
                            + " VALUES (:code, :name, :itemGroup, :itemType,:filterItemType,:sku, "
                            + " :preparedBY, :uEntDt, NULL, :itemMakeCode, :isActive,  :itemCategoryCode,"
                            + " :manualCode,:hsnCode,:isBestBefore,:isLifeSaving,:isHighValue,:isFlowThrough, "
                            + " :isNarcotics,:isConsignment,:isReturnableItem,:isBilledItem,:propertyList,:itemGSTNature,:purchaseUnit,:saleUnit,"
                            + " :purchaseUnitConversionRate,:saleUnitConversionRate,:allowNegativeStock,:displayName,:isSaleRateEditable,"
                            + " :isIncludeFreeQtyInSaleRate,:isDiscountAllow,:isCommon,"
                            + " :instruction,:regionalInstruction,:itempacking,:manufacturerCode,:divisionCode"
                            + " , :mrp , :saleRate , :purchaseRate , :schemeOnQty , :schemeFreeQty  , :isIndentMandatory "
                            + " , :isQuotationMandatory , :isBarcodeActive , :purchaseDiscountPer , :saleDiscountPer , :priority , :uEntDt "
                            + " ,:materialCategoryCode,:model,:strength, :allowMRPDiscPattern, "
                            + " :subCode"
                            + " ,:grIrAc,:ConsumptionAc,:cogsAc ,:subCategoryCode ,:marginRateFix,:D365_code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(list.getSharedKeyFieldCode()));
                    psTemp.setString("name", DmShared.xNull(list.getName()));
                    psTemp.setString("itemGroup", DmShared.foriegnKey(list.getItemGroupCode()));
                    psTemp.setString("manualCode", list.getManualCode());
                    psTemp.setString("itemType", DmShared.foriegnKey(list.getItemType()));
                    psTemp.setString("filterItemType", DmShared.foriegnKey(list.getFilterItemType()));
                    psTemp.setString("sku", DmShared.foriegnKey(list.getSkuCode()));
                    psTemp.setString("itemMakeCode", DmShared.foriegnKey(list.getItemMakeCodeRef()));
                    psTemp.setString("isActive", DmShared.xNull(list.getIsActive()));
                    psTemp.setString("itemCategoryCode", DmShared.foriegnKey(list.getItemCategoryCodeRef()));
                    psTemp.setString("hsnCode", DmShared.foriegnKey(list.getHsnMasterCode()));
                    psTemp.setString("isBestBefore", DmShared.xNull("0"));
                    psTemp.setString("isLifeSaving", DmShared.xNull(list.getIsLifeSaving()));
                    psTemp.setString("isHighValue", DmShared.xNull(list.getIsHighValue()));
                    psTemp.setString("isFlowThrough", DmShared.xNull(list.getIsFlowThrough()));
                    psTemp.setString("isNarcotics", DmShared.xNull(list.getIsNarcotics()));
                    psTemp.setString("isIndentMandatory", DmShared.xNull(list.getIsIndentMandatory()));
                    psTemp.setString("allowMRPDiscPattern", DmShared.xNull(list.getAllowMRPDiscPattern()));
                    psTemp.setString("isQuotationMandatory", DmShared.xNull(list.getIsQuotationMandatory()));
                    psTemp.setString("isBarcodeActive", DmShared.xNull(list.getIsBarcodeActive()));
                    psTemp.setString("isConsignment", DmShared.xNull(list.getIsConsignment()));
                    psTemp.setString("isReturnableItem", DmShared.xNull(list.getIsReturnableItem()));
                    psTemp.setString("isBilledItem", DmShared.xNull(list.getIsBilledItem()));
                    psTemp.setString("PropertyList", DmShared.xNull(list.getPropertyList()));
                    psTemp.setString("preparedBY", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("itemGSTNature", DmShared.xNull(list.getItemGSTNature()));
                    psTemp.setString("isDiscountAllow", DmShared.xNull(list.getIsDiscountAllow()));
                    psTemp.setString("isCommon", DmShared.xNull(list.getIsCommon()));
                    psTemp.setString("purchaseUnit", DmShared.xNull(list.getPurchaseUnitCode()));
                    psTemp.setString("saleUnit", DmShared.xNull(list.getSaleUnitCode()));
                    psTemp.setString("purchaseUnitConversionRate", DmServer.vNull(list.getPurchaseUnitConversionRate()));
                    psTemp.setString("saleUnitConversionRate", DmServer.vNull(list.getSaleUnitConversionRate()));
                    psTemp.setString("allowNegativeStock", DmShared.xNull(list.getAllowNegativeStock()));
                    psTemp.setString("isSaleRateEditable", DmShared.xNull(list.getIsSaleRateEditable()));
                    psTemp.setString("isIncludeFreeQtyInSaleRate", DmShared.xNull(list.getIsIncludeFreeQtyInSaleRate()));
                    psTemp.setString("displayName", DmShared.xNull(list.getDisplayName()));
                    psTemp.setString("instruction", DmShared.xNull(list.getInstruction()));
                    psTemp.setString("regionalInstruction", DmShared.xNull(list.getRegionalInstruction()));
                    psTemp.setString("itempacking", DmShared.xNull(list.getItemPacking()));
                    psTemp.setString("manufacturerCode", DmShared.foriegnKey(list.getManufacturerCode()));
                    psTemp.setString("divisionCode", DmShared.foriegnKey(list.getDivisionCode()));
                    psTemp.setString("mrp", DmServer.vNull(list.getMRP(), "0.00"));
                    psTemp.setString("saleRate", DmServer.vNull(list.getSaleRate(), "0.00"));
                    psTemp.setString("purchaseRate", DmServer.vNull(list.getPurchaseRate(), "0.00"));
                    psTemp.setString("schemeFreeQty", DmServer.vNull(list.getSchemeFreeQty()));
                    psTemp.setString("schemeOnQty", DmServer.vNull(list.getSchemeOnQty()));
                    psTemp.setString("purchaseDiscountPer", DmServer.vNull(list.getPurchaseDiscountPer(), "0.00"));
                    psTemp.setString("saleDiscountPer", DmServer.vNull(list.getSaleDiscountPer(), "0.00"));
                    psTemp.setString("priority", ("0"));
                    psTemp.setString("materialCategoryCode", DmShared.foriegnKey(list.getMaterialCategoryCodeRef()));
                    psTemp.setString("model", DmShared.foriegnKey(list.getModelCode()));
                    psTemp.setString("strength", DmShared.foriegnKey(list.getStrengthCode()));
                    psTemp.setString("subCode", "1000100000000001496");
                    psTemp.setString("grIrAc", "1000100000000001498");
                    psTemp.setString("ConsumptionAc", "1000200000000000126");
                    psTemp.setString("cogsAc", "1000100000000000555");
                    psTemp.setString("subCategoryCode", DmShared.foriegnKey(list.getSubCategoryCodeRef()));
                    psTemp.setString("marginRateFix", DmShared.xNull(list.getMarginRateFix()));
                    psTemp.setString("D365_code", DmShared.xNull(list.getD365Code()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "insert into itemITSDetail (code ,v_SNo,its_Code_Purchase,its_Code_PurchaseRet,"
                            + "                  its_Code_Sales,its_Code_SalesRet) "
                            + "         		values(:code ,:v_SNo,:its_Code_Purchase,:its_Code_PurchaseRet,"
                            + "                 :its_Code_Sales,:its_Code_SalesRet) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", list.getSharedKeyFieldCode());
                    psTemp.setString("v_SNo", "1");
                    psTemp.setString("its_Code_Purchase", DmShared.foriegnKey(list.getIts_Code_Purchase()));
                    psTemp.setString("its_Code_PurchaseRet", DmShared.foriegnKey(list.getIts_Code_PurchaseRet()));
                    psTemp.setString("its_Code_Sales", DmShared.foriegnKey(list.getIts_Code_Sales()));
                    psTemp.setString("its_Code_SalesRet", DmShared.foriegnKey(list.getIts_Code_SalesRet()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    for (int i = 1; i <= siteCount; i++) {
                        sqlQuery = "insert into itemSiteList (code ,site_Code) "
                                + " values(:code ,:site_Code) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", list.getSharedKeyFieldCode());
                        psTemp.setString("site_Code", DmShared.iNull(i).toString());
                        psTemp.executeUpdate();
                        psTemp.close();
                    }

                    /*============================update isStatus into Staging======================*/
                    sqlQuery = " update D365_ItemMasterData set \n"
                            + " jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                            + " jp_status = 'Success'\n"
                            + " where code = :code "
                            + " and jp_TimeStamp is null";
                    psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(list.getD365Code()));
                    psTemp.setString("jp_Code", DmShared.iNull(list.getSharedKeyFieldCode()).toString());
                    psTemp.executeUpdate();
                    psTemp.close();
                } else {

                    if (DmShared.xNull(checkItem(list.getSharedKeyFieldCode(), cnTemp)).equalsIgnoreCase("1")) {
                        sqlQuery = " update D365_ItemMasterData set \n"
                                + " jp_TimeStamp = GETDATE() ,\n"
                                + " jp_status = 'Item is Used'\n"
                                + " where code = :code ";
                        psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(list.getD365Code()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }

                    sqlQuery = "UPDATE itemmast SET "
                            + "  CODE = :code, "
                            + "  NAME = :name, "
                            + "  displayName = :displayName, "
                            + "  itemGroup = :itemGroup, "
                            + "  itemType = :itemType, "
                            + "  filterItemType = :filterItemType, "
                            + "  isDiscountAllow = :isDiscountAllow, "
                            + "  isCommon = :isCommon, "
                            + "  sku = :sku, "
                            + "  u_Name = :username, "
                            + "  u_EntDt = :uEntDt, "
                            + "  transferDt = NULL, "
                            + "  itemMakeCode = :itemMakeCode, "
                            + "  isActive = :isActive, "
                            + "  itemCategoryCode = :itemCategoryCode,  "
                            + "  hsnCode = :hsnCode,  "
                            + "  manualCode = :manualCode,  "
                            + "  isBestBefore = :isBestBefore, "
                            + "  isLifeSaving = :isLifeSaving, "
                            + "  isHighValue = :isHighValue, "
                            + "  isFlowThrough = :isFlowThrough, "
                            + "  isNarcotics = :isNarcotics, "
                            + "  isIndentMandatory = :isIndentMandatory, "
                            + "  isQuotationMandatory = :isQuotationMandatory, "
                            + "  isConsignment = :isConsignment, "
                            + "  isReturnableItem = :isReturnableItem, "
                            + "  isBilledItem = :isBilledItem, "
                            + "  itemGSTNature=:itemGSTNature,"
                            + "  purchaseUnit=:purchaseUnit,"
                            + "  saleUnit=:saleUnit,"
                            + "  purchaseUnitConversionRate=:purchaseUnitConversionRate,"
                            + "  saleUnitConversionRate=:saleUnitConversionRate,"
                            + "  isSaleRateEditable=:isSaleRateEditable,"
                            + "  isIncludeFreeQtyInSaleRate=:isIncludeFreeQtyInSaleRate,"
                            + "  allowNegativeStock=:allowNegativeStock, "
                            + "  instruction=:instruction, "
                            + "  regionalInstruction=:regionalInstruction, "
                            + "  manufacturerCode = :manufacturerCode, "
                            + "  divisionCode = :divisionCode, "
                            + "  mrp = :mrp, "
                            + "  saleRate = :saleRate, "
                            + "  purchaseRate = :purchaseRate, "
                            + "  schemeOnQty = :schemeOnQty, "
                            + "  schemeFreeQty = :schemeFreeQty, "
                            + "  purchaseDiscountPer = :purchaseDiscountPer, "
                            + "  saleDiscountPer = :saleDiscountPer, "
                            + "  itempacking=:itempacking, "
                            + "  materialCategoryCode = :materialCategoryCode, "
                            + "  model = :model, "
                            + "  strength=:strength, "
                            + "  subCategoryCode = :subCategoryCode, "
                            + "  allowMRPDiscPattern = :allowMRPDiscPattern, "
                            + "  marginRateFix = :marginRateFix "
                            + " WHERE CODE = :CODE ;";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(itemCode));
                    psTemp.setString("name", DmShared.xNull(list.getName()));
                    psTemp.setString("displayName", DmShared.xNull(list.getDisplayName()));
                    psTemp.setString("itemGroup", DmShared.foriegnKey(list.getItemGroupCode()));
                    psTemp.setString("manualCode", list.getManualCode());
                    psTemp.setString("itemType", DmShared.foriegnKey(list.getItemType()));
                    psTemp.setString("filterItemType", DmShared.foriegnKey(list.getFilterItemType()));
                    psTemp.setString("sku", DmShared.foriegnKey(list.getSkuCode()));
                    psTemp.setString("itemMakeCode", DmShared.foriegnKey(list.getItemMakeCodeRef()));
                    psTemp.setString("isActive", DmShared.xNull(list.getIsActive()));
                    psTemp.setString("itemCategoryCode", DmShared.foriegnKey(list.getItemCategoryCodeRef()));
                    psTemp.setString("hsnCode", DmShared.foriegnKey(list.getHsnMasterCode()));
                    psTemp.setString("isBestBefore", DmShared.xNull("0"));
                    psTemp.setString("isLifeSaving", DmShared.xNull(list.getIsLifeSaving()));
                    psTemp.setString("isHighValue", DmShared.xNull(list.getIsHighValue()));
                    psTemp.setString("isFlowThrough", DmShared.xNull(list.getIsFlowThrough()));
                    psTemp.setString("isNarcotics", DmShared.xNull(list.getIsNarcotics()));
                    psTemp.setString("isIndentMandatory", DmShared.xNull(list.getIsIndentMandatory()));
                    psTemp.setString("allowMRPDiscPattern", DmShared.xNull(list.getAllowMRPDiscPattern()));
                    psTemp.setString("isQuotationMandatory", DmShared.xNull(list.getIsQuotationMandatory()));
                    psTemp.setString("isConsignment", DmShared.xNull(list.getIsConsignment()));
                    psTemp.setString("isReturnableItem", DmShared.xNull(list.getIsReturnableItem()));
                    psTemp.setString("isBilledItem", DmShared.xNull(list.getIsBilledItem()));
                    psTemp.setString("username", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("schemeOnQty", DmServer.vNull(list.getSchemeOnQty()));
                    psTemp.setString("subCategoryCode", DmShared.foriegnKey(list.getSubCategoryCodeRef()));
                    psTemp.setString("marginRateFix", DmShared.xNull(list.getMarginRateFix()));//                    12
                    psTemp.setString("itemGSTNature", DmShared.xNull(list.getItemGSTNature()));
                    psTemp.setString("isDiscountAllow", DmShared.xNull(list.getIsDiscountAllow()));
                    psTemp.setString("isCommon", DmShared.xNull(list.getIsCommon()));
                    psTemp.setString("purchaseUnit", DmShared.xNull(list.getPurchaseUnitCode()));
                    psTemp.setString("saleUnit", DmShared.xNull(list.getSaleUnitCode()));
                    psTemp.setString("purchaseUnitConversionRate", DmServer.vNull(list.getPurchaseUnitConversionRate()));
                    psTemp.setString("saleUnitConversionRate", DmServer.vNull(list.getSaleUnitConversionRate()));
                    psTemp.setString("allowNegativeStock", DmShared.xNull(list.getAllowNegativeStock()));
                    psTemp.setString("isSaleRateEditable", DmShared.xNull(list.getIsSaleRateEditable()));
                    psTemp.setString("isIncludeFreeQtyInSaleRate", DmShared.xNull(list.getIsIncludeFreeQtyInSaleRate()));
                    psTemp.setString("instruction", DmShared.xNull(list.getInstruction()));
                    psTemp.setString("regionalInstruction", DmShared.xNull(list.getRegionalInstruction()));
                    psTemp.setString("itempacking", DmShared.xNull(list.getItemPacking()));
                    psTemp.setString("manufacturerCode", DmShared.foriegnKey(list.getManufacturerCode()));
                    psTemp.setString("divisionCode", DmShared.foriegnKey(list.getDivisionCode()));
                    psTemp.setString("mrp", DmServer.vNull(list.getMRP(), "0.00"));
                    psTemp.setString("saleRate", DmServer.vNull(list.getSaleRate(), "0.00"));
                    psTemp.setString("purchaseRate", DmServer.vNull(list.getPurchaseRate(), "0.00"));
                    psTemp.setString("schemeFreeQty", DmServer.vNull(list.getSchemeFreeQty()));
                    psTemp.setString("purchaseDiscountPer", DmServer.vNull(list.getPurchaseDiscountPer(), "0.00"));
                    psTemp.setString("saleDiscountPer", DmServer.vNull(list.getSaleDiscountPer(), "0.00"));
                    psTemp.setString("materialCategoryCode", DmShared.foriegnKey(list.getMaterialCategoryCodeRef()));
                    psTemp.setString("model", DmShared.foriegnKey(list.getModelCode()));
                    psTemp.setString("strength", DmShared.foriegnKey(list.getStrengthCode()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    // del frm hsnitsdetl
                    sqlQuery = "Delete From itemITSDetail Where code= :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(itemCode).toString());
                    psTemp.executeUpdate();

                    sqlQuery = "insert into itemITSDetail (code ,v_SNo,its_Code_Purchase,its_Code_PurchaseRet,"
                            + "                  its_Code_Sales,its_Code_SalesRet) "
                            + "         		values(:code ,:v_SNo,:its_Code_Purchase,:its_Code_PurchaseRet,"
                            + "                 :its_Code_Sales,:its_Code_SalesRet) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(itemCode).toString());
                    psTemp.setString("v_SNo", "1");
                    psTemp.setString("its_Code_Purchase", DmShared.foriegnKey(list.getIts_Code_Purchase()));
                    psTemp.setString("its_Code_PurchaseRet", DmShared.foriegnKey(list.getIts_Code_PurchaseRet()));
                    psTemp.setString("its_Code_Sales", DmShared.foriegnKey(list.getIts_Code_Sales()));
                    psTemp.setString("its_Code_SalesRet", DmShared.foriegnKey(list.getIts_Code_SalesRet()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    /*============================update isStatus into Staging======================*/
                    sqlQuery = " update D365_ItemMasterData set \n"
                            + " jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                            + " jp_status = 'Success'\n"
                            + " where code = :code "
                            + " and jp_TimeStamp is null";
                    psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(list.getD365Code()));
                    psTemp.setString("jp_Code", DmShared.iNull(itemCode).toString());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            //</editor-fold>
            System.out.println("Item Master Sync ");
        } catch (Exception e) {
            throw e;
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
    }

    //<editor-fold defaultstate="collapsed" desc="Sync All Master">
    private static void syncParents(Connection cnTemp, Connection stagingCnTemp) throws Exception {
        try {

            syncUnitMast(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync unitMaster");
            syncHsnMast(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync HsnMast");
            syncitemGroup(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync itemGroup");
            syncItemMakeMast(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync ItemMakeMast");
            syncModelMast(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync ModelMast");
            syncStrengthMast(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync StrengthMast");
            syncMedicineSubCategoryMast(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync MedicineSubCategoryMast");
            syncitemCat(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync itemCat");
            syncRestrictedGrpMast(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync RestrictedGrpMast");
            syncMaterialTypeMast(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync MaterialTypeMast");
            syncMaterialCatMast(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync MaterialCatMast");
            syncItemSpecialityMast(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync ItemSpecialityMast");
            syncManufacturerMast(cnTemp, stagingCnTemp);
            System.out.println("Complete Sync ManufacturerMast");
        } catch (Exception e) {
            throw e;
        } finally {
            try {

            } catch (Exception e) {
            }
        }
    }

    private static void syncUnitMast(Connection cnTemp, Connection stagingCnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int itemCatCode = 0;
        try {

            /*===============fetch new data and updated data from staging===============*/
            List<UnitMaster_Mdl> newUnitMaster = new ArrayList<>();
            List<UnitMaster_Mdl> updateUnitMaster = new ArrayList<>();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_unitMast "
                    + " WHERE D365_status IN ('Update','New') and jp_TimeStamp is null"
                    + ") t WHERE rn = 1";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                UnitMaster_Mdl list = new UnitMaster_Mdl();
                list.setCode(rsTemp.getString("code"));
                list.setUnit(rsTemp.getString("unit"));
                list.setDecimalPlaces(rsTemp.getString("decimalPlaces"));
                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newUnitMaster.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateUnitMaster.add(list);
                }

            }
            psTemp.close();
            rsTemp.close();
            for (UnitMaster_Mdl listTemp : newUnitMaster) {
                /*===============Check code hai ki nahi===============*/
                itemCatCode = DmShared.iNull(DmServer.executeScalar("select code from unitMast where D365_code = '" + DmShared.xNull(listTemp.getCode()) + "' ", cnTemp)).intValue();
                /*===============Insert subGroup data into His===============*/
                if (itemCatCode == 0) {
                    listTemp.setHisCode(DmServer.getSerialNoInteger("1", "unitmast", cnTemp));
                    sqlQuery = "Insert Into unitmast(code ,unit,decimalplaces, "
                            + " preparedBy,u_EntDt,d365_Code) Values  "
                            + "(:code ,:name,:decimalplaces,:preparedBy,:uEntDt,:d365_code) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHisCode()));
                    psTemp.setString("d365_code", DmShared.xNull(listTemp.getCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getUnit()));
                    psTemp.setString("decimalplaces", DmShared.xNull(listTemp.getDecimalPlaces()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();

                }

                sqlQuery = "UPDATE D365_unitMast\n"
                        + "SET jp_TimeStamp = GETDATE(),\n"
                        + "jp_status = 'Success',\n"
                        + "jp_Code = :jp_Code\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("jp_Code", DmShared.xNull(listTemp.getHisCode()));
                psTemp.executeUpdate();
                psTemp.close();
            }

            for (UnitMaster_Mdl listTemp : updateUnitMaster) {
                /*===============Check code hai ki nahi===============*/
                itemCatCode = DmShared.iNull(DmServer.executeScalar("select code from unitMast where D365_code = '" + DmShared.xNull(listTemp.getCode()) + "' ", cnTemp)).intValue();
                /*===============Insert subGroup data into His===============*/
                if (itemCatCode == 0) {
                    listTemp.setHisCode(DmServer.getSerialNoInteger("1", "unitmast", cnTemp));
                    sqlQuery = "Insert Into unitmast(code ,unit,decimalplaces, "
                            + " preparedBy,u_EntDt) Values  "
                            + "(:code ,:name,:decimalplaces,:preparedBy,:uEntDt) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHisCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getUnit()));
                    psTemp.setString("decimalplaces", DmShared.xNull(listTemp.getDecimalPlaces()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

                } else {
                    sqlQuery = "Update unitmast Set "
                            + "unit = :name, "
                            + "decimalplaces = :decimalplaces, "
                            + "U_Name = :u_Name, "
                            + "U_EntDt = :uEntDt "
                            + "Where code = :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(itemCatCode).toString());
                    psTemp.setString("name", DmShared.xNull(listTemp.getUnit()));
                    psTemp.setString("decimalplaces", DmShared.xNull(listTemp.getDecimalPlaces()));
                    psTemp.setString("u_Name", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                }
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE D365_unitMast\n"
                        + "SET jp_TimeStamp = GETDATE(),\n"
                        + "jp_status = 'Success',\n"
                        + "jp_Code = :jp_Code\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("jp_Code", DmShared.xNull(listTemp.getHisCode()));
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
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
    }

    private static void syncHsnMast(Connection cnTemp, Connection stagingCnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int hsnCodeExist = 0;

        try {

            /*===============fetch new data and updated data from staging===============*/
            List<HsnMaster_Mdl> newHsnList = new ArrayList<>();
            List<HsnMaster_Mdl> updateHsnList = new ArrayList<>();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_hsnMast "
                    + " WHERE D365_status IN ('Update','New') and jp_TimeStamp is null "
                    + ") t WHERE rn = 1";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                HsnMaster_Mdl list = new HsnMaster_Mdl();
                list.setCode(rsTemp.getString("code"));
                list.setName(rsTemp.getString("name"));
                list.setHsn(rsTemp.getString("hsn"));
                list.setNature(rsTemp.getString("nature"));
                list.setItemGSTNature(rsTemp.getString("itemGSTNature"));
                list.setPurchaseITS(DmServer.executeScalar("select code from its where name = '" + DmShared.xNull(rsTemp.getString("purchaseITS")) + "' ", cnTemp));
                list.setPurchaseReturnITS(DmServer.executeScalar("select code from its where name = '" + DmShared.xNull(rsTemp.getString("purchaseReturnITS")) + "' ", cnTemp));
                list.setSaleITS(DmServer.executeScalar("select code from its where name = '" + DmShared.xNull(rsTemp.getString("saleITS")) + "' ", cnTemp));
                list.setSaleReturnITS(DmServer.executeScalar("select code from its where name = '" + DmShared.xNull(rsTemp.getString("saleReturnITS")) + "' ", cnTemp));

                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newHsnList.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateHsnList.add(list);
                }

            }
            psTemp.close();
            rsTemp.close();

            /*===============Insert New Records===============*/
            for (HsnMaster_Mdl listTemp : newHsnList) {

                hsnCodeExist = DmShared.iNull(DmServer.executeScalar("select code from hsnmast where D365_code = '" + DmShared.xNull(listTemp.getCode()) + "' ", cnTemp)).intValue();

                if (hsnCodeExist == 0) {
                    listTemp.setHisCode(DmShared.iNull(DmServer.getSerialNoInteger("1", "hsnMast", cnTemp)).toString());

                    sqlQuery = "Insert Into hsnMast(code, name, hsn, itsBasedOn, itemGSTNature, preparedBy, u_EntDt, nature,d365_code) "
                            + " values (:code, :name, :hsn, :itsBasedOn, :itemGSTNature, :preparedBy, :uEntDt, :nature,:d365_code)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", listTemp.getHisCode());
                    psTemp.setString("d365_code", listTemp.getCode());
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("hsn", DmShared.iNull(listTemp.getHsn()).toString());
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setString("itsBasedOn", DmShared.xNull("0"));
                    psTemp.setString("itemGSTNature", DmShared.xNull(listTemp.getItemGSTNature()));
                    psTemp.setString("nature", DmShared.xNull("HSN".equalsIgnoreCase(listTemp.getNature()) ? "1" : "0"));
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();

                    // del frm hsnitsdetl
                    sqlQuery = "Delete From hsnITSDetail Where code= :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(hsnCodeExist).toString());
                    psTemp.executeUpdate();

                    //inser int hsnitsdtl
                    sqlQuery = "Insert Into hsnITSDetail(code ,v_SNo,rateFrom,rateUpto,its_Code_Purchase,its_Code_PurchaseRet,"
                            + " its_Code_Sales,its_Code_SalesRet) Values  "
                            + "(:code ,:v_SNo,:rateFrom,:rateUpto,:its_Code_Purchase,:its_Code_PurchaseRet,"
                            + " :its_Code_Sales,:its_Code_SalesRet) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", listTemp.getHisCode());
                    psTemp.setString("v_SNo", DmShared.iNull("1").toString());
                    psTemp.setString("rateFrom", DmShared.iNull("0").toString());
                    psTemp.setString("rateUpto", DmShared.iNull("0").toString());
                    psTemp.setString("its_Code_Purchase", DmShared.xNull(listTemp.getPurchaseITS()));
                    psTemp.setString("its_Code_PurchaseRet", DmShared.xNull(listTemp.getPurchaseReturnITS()));
                    psTemp.setString("its_Code_Sales", DmShared.xNull(listTemp.getSaleITS()));
                    psTemp.setString("its_Code_SalesRet", DmShared.xNull(listTemp.getSaleReturnITS()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
                sqlQuery = "UPDATE D365_hsnMast\n"
                        + "SET jp_TimeStamp = GETDATE(),\n"
                        + "jp_Code = :jp_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHisCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*===============Update Records===============*/
            for (HsnMaster_Mdl listTemp : updateHsnList) {

                hsnCodeExist = DmShared.iNull(DmServer.executeScalar("select code from hsnmast where D365_code = '" + DmShared.xNull(listTemp.getCode()) + "' ", cnTemp)).intValue();

                if (hsnCodeExist == 0) {
                    listTemp.setHisCode(DmShared.iNull(DmServer.getSerialNoInteger("1", "hsnMast", cnTemp)).toString());

                    sqlQuery = "Insert Into hsnMast(code, name, hsn, itsBasedOn, itemGSTNature, preparedBy, u_EntDt, nature,d365_code) "
                            + " values (:code, :name, :hsn, :itsBasedOn, :itemGSTNature, :u_Name, :uEntDt, :nature,:d365_code)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHisCode()));
                    psTemp.setString("d365_code", DmShared.xNull(listTemp.getCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("hsn", DmShared.iNull(listTemp.getHsn()).toString());
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setString("itsBasedOn", DmShared.xNull("0"));
                    psTemp.setString("itemGSTNature", DmShared.xNull(listTemp.getItemGSTNature()));
                    psTemp.setString("nature", DmShared.xNull("HSN".equalsIgnoreCase(listTemp.getNature()) ? "1" : "0"));
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();

                    // del frm hsnitsdetl
                    sqlQuery = "Delete From hsnITSDetail Where code= :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(hsnCodeExist).toString());
                    psTemp.executeUpdate();
                    psTemp.close();

                    //inser int hsnitsdtl
                    sqlQuery = "Insert Into hsnITSDetail(code ,v_SNo,rateFrom,rateUpto,its_Code_Purchase,its_Code_PurchaseRet,"
                            + " its_Code_Sales,its_Code_SalesRet) Values  "
                            + "(:code ,:v_SNo,:rateFrom,:rateUpto,:its_Code_Purchase,:its_Code_PurchaseRet,"
                            + " :its_Code_Sales,:its_Code_SalesRet) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(DmServer.getSerialNoInteger("1", "hsnMast", cnTemp)));
                    psTemp.setString("v_SNo", DmShared.iNull("1").toString());
                    psTemp.setString("rateFrom", DmShared.iNull("0").toString());
                    psTemp.setString("rateUpto", DmShared.iNull("0").toString());
                    psTemp.setString("its_Code_Purchase", DmShared.xNull(listTemp.getPurchaseITS()));
                    psTemp.setString("its_Code_PurchaseRet", DmShared.xNull(listTemp.getPurchaseReturnITS()));
                    psTemp.setString("its_Code_Sales", DmShared.xNull(listTemp.getSaleITS()));
                    psTemp.setString("its_Code_SalesRet", DmShared.xNull(listTemp.getSaleReturnITS()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
                sqlQuery = "UPDATE D365_hsnMast\n"
                        + "SET jp_TimeStamp = GETDATE(),\n"
                        + "jp_Code = :jp_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHisCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();

            }

        } catch (Exception e) {
            throw e;
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
    }

    private static void syncitemGroup(Connection cnTemp, Connection stagingCnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int itemGroupCode = 0;

        try {

            /*=============== Fetch New and Updated Records ===============*/
            List<ItemGroup_Mdl> newItemGroup = new ArrayList<>();
            List<ItemGroup_Mdl> updateItemGroup = new ArrayList<>();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_itemGroup "
                    + " WHERE D365_status IN ('Update','New') "
                    + "   AND jp_TimeStamp IS NULL "
                    + ") t WHERE rn = 1";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                ItemGroup_Mdl list = new ItemGroup_Mdl();
                list.setCode(rsTemp.getString("code"));
                list.setName(rsTemp.getString("name"));
                list.setFilterItemType(rsTemp.getString("filterItemType"));

                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newItemGroup.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateItemGroup.add(list);
                }

            }

            rsTemp.close();
            psTemp.close();

            /*=============== Process New Records ===============*/
            for (ItemGroup_Mdl listTemp : newItemGroup) {

                itemGroupCode = DmShared.iNull(DmServer.executeScalar(
                        "SELECT code FROM itemGroup WHERE D365_CODE = '"
                        + DmShared.xNull(listTemp.getCode()) + "'", cnTemp)).intValue();

                if (itemGroupCode == 0) {
                    listTemp.setHisCode(DmShared.iNull(DmServer.getSerialNoInteger("1", "itemGroup", cnTemp)).toString());
                    sqlQuery = "INSERT INTO itemGroup "
                            + "(code, name, filterItemType, isCommon, preparedBy, u_EntDt,d365_code) "
                            + "VALUES "
                            + "(:code, :name, :filterItemType, :isCommon, :preparedBy, :uEntDt,:d365_code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHisCode()));
                    psTemp.setString("d365_code", DmShared.xNull(listTemp.getCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("filterItemType", DmShared.xNull(listTemp.getFilterItemType()));
                    psTemp.setString("isCommon", "0");
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

                    psTemp.executeUpdate();
                    psTemp.close();
                }
                sqlQuery = "UPDATE D365_itemGroup\n"
                        + "SET jp_TimeStamp = GETDATE(),\n"
                        + "His_Code = :His_Code,"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("His_Code", DmShared.iNull(listTemp.getHisCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*=============== Process Update Records ===============*/
            for (ItemGroup_Mdl listTemp : updateItemGroup) {

                itemGroupCode = DmShared.iNull(DmServer.executeScalar(
                        "SELECT code FROM itemGroup WHERE D365_code = '"
                        + DmShared.xNull(listTemp.getCode()) + "'", cnTemp)).intValue();

                if (itemGroupCode == 0) {
                    listTemp.setHisCode(DmShared.iNull(DmServer.getSerialNoInteger("1", "itemGroup", cnTemp)).toString());
                    /* Insert if not exists */
                    sqlQuery = "INSERT INTO itemGroup "
                            + "(code, name, filterItemType, isCommon, preparedBy, u_EntDt,d365_code) "
                            + "VALUES "
                            + "(:code, :name, :filterItemType, :isCommon, :preparedBy, :uEntDt,:d365_code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHisCode()));
                    psTemp.setString("d365_code", DmShared.xNull(listTemp.getCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("filterItemType", DmShared.xNull(listTemp.getFilterItemType()));
                    psTemp.setString("isCommon", "0");
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

                } else {

                    /* Update existing record */
                    sqlQuery = "UPDATE itemGroup SET "
                            + "name = :name, "
                            + "filterItemType = :filterItemType, "
                            + "u_Name = :u_Name, "
                            + "u_EntDt = :uEntDt "
                            + "WHERE code = :code";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(itemGroupCode).toString());
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("filterItemType", DmShared.xNull(listTemp.getFilterItemType()));
                    psTemp.setString("u_Name", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                }

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE D365_itemGroup\n"
                        + "SET jp_TimeStamp = GETDATE(),\n"
                        + "His_Code = :His_Code,"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("His_Code", DmShared.iNull(listTemp.getHisCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
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
    }

    private static void syncItemMakeMast(Connection cnTemp, Connection stagingCnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int itemMakeCode = 0;

        try {

            /*=============== fetch new and updated data from staging ===============*/
            List<ItemMakeMast_Mdl> newItemMake = new ArrayList<>();
            List<ItemMakeMast_Mdl> updateItemMake = new ArrayList<>();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_itemMakeMast "
                    + " WHERE D365_status IN ('Update','New') "
                    + "   AND jp_TimeStamp IS NULL "
                    + "   AND Segment1 = 'Medicine/Marketing' "
                    + ") t WHERE rn = 1";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                ItemMakeMast_Mdl list = new ItemMakeMast_Mdl();
                list.setCode(rsTemp.getString("code"));
                list.setCompanyName(rsTemp.getString("companyName"));
                list.setFilterItemType(rsTemp.getString("filterItemType"));
                list.setShortName(rsTemp.getString("shortName"));
                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newItemMake.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateItemMake.add(list);
                }

            }

            rsTemp.close();
            psTemp.close();

            /*=============== Process New Records ===============*/
            for (ItemMakeMast_Mdl listTemp : newItemMake) {

                itemMakeCode = DmShared.iNull(DmServer.executeScalar(
                        "SELECT code FROM itemMakeMast WHERE code = '"
                        + DmShared.xNull(listTemp.getHisCode()) + "'", cnTemp)).intValue();

                if (itemMakeCode == 0) {
                    listTemp.setHisCode(DmServer.getSerialNoInteger("1", "itemMakeMast", cnTemp));
                    sqlQuery = "INSERT INTO itemMakeMast "
                            + "(code, companyName, shortName, filterItemType, preparedBy, u_EntDt,d365_code) "
                            + "VALUES "
                            + "(:code, :companyName, :shortName, :filterItemType, :preparedBy, :uEntDt,:d365_code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHisCode()));
                    psTemp.setString("d365_code", DmShared.xNull(listTemp.getCode()));
                    psTemp.setString("companyName", DmShared.xNull(listTemp.getCompanyName()));
                    psTemp.setString("shortName", DmShared.xNull(listTemp.getShortName()));
                    psTemp.setString("filterItemType", DmShared.xNull(listTemp.getFilterItemType()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

                    psTemp.executeUpdate();
                    psTemp.close();
                }

                sqlQuery = "UPDATE D365_itemMakeMast\n"
                        + "SET jp_TimeStamp = GETDATE(),\n"
                        + "His_Code = :His_Code,"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("His_Code", DmShared.iNull(listTemp.getHisCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*=============== Process Update Records ===============*/
            for (ItemMakeMast_Mdl listTemp : updateItemMake) {

                itemMakeCode = DmShared.iNull(DmServer.executeScalar(
                        "SELECT code FROM itemMakeMast WHERE code = '"
                        + DmShared.xNull(listTemp.getHisCode()) + "'", cnTemp)).intValue();

                if (itemMakeCode == 0) {
                    listTemp.setHisCode(DmServer.getSerialNoInteger("1", "itemMakeMast", cnTemp));

                    /* Insert if not exists */
                    sqlQuery = "INSERT INTO itemMakeMast "
                            + "(code, companyName, shortName, filterItemType, preparedBy, u_EntDt,d365_code) "
                            + "VALUES "
                            + "(:code, :companyName, :shortName, :filterItemType, :preparedBy, :uEntDt,:d365_code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHisCode()));
                    psTemp.setString("d365_code", DmShared.xNull(listTemp.getCode()));
                    psTemp.setString("companyName", DmShared.xNull(listTemp.getCompanyName()));
                    psTemp.setString("shortName", DmShared.xNull(listTemp.getShortName()));
                    psTemp.setString("filterItemType", DmShared.xNull(listTemp.getFilterItemType()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

                } else {

                    /* Update existing record */
                    sqlQuery = "UPDATE itemMakeMast SET "
                            + "companyName = :companyName, "
                            + "shortName = :shortName, "
                            + "filterItemType = :filterItemType, "
                            + "u_Name = :u_Name, "
                            + "u_EntDt = :uEntDt "
                            + "WHERE code = :code";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(itemMakeCode).toString());
                    psTemp.setString("companyName", DmShared.xNull(listTemp.getCompanyName()));
                    psTemp.setString("shortName", DmShared.xNull(listTemp.getShortName()));
                    psTemp.setString("filterItemType", DmShared.xNull(listTemp.getFilterItemType()));
                    psTemp.setString("u_Name", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                }

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE D365_itemMakeMast\n"
                        + "SET jp_TimeStamp = GETDATE(),\n"
                        + "His_Code = :His_Code,"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("His_Code", DmShared.iNull(listTemp.getHisCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
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
    }

    private static void syncModelMast(Connection cnTemp, Connection stagingCnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int modelExist = 0;

        try {

            /*===============fetch new & updated data===============*/
            List<ItemModelMaster_Mdl> newModelList = new ArrayList<>();
            List<ItemModelMaster_Mdl> updateModelList = new ArrayList<>();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_itemModelMast "
                    + " WHERE D365_status IN ('Update','New') and jp_TimeStamp is null "
                    + ") t WHERE rn = 1";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                ItemModelMaster_Mdl list = new ItemModelMaster_Mdl();
                list.setModelName(rsTemp.getString("name"));
                list.setModelCode(rsTemp.getString("code"));

                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newModelList.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateModelList.add(list);
                }
            }

            psTemp.close();
            rsTemp.close();
            /*=============== INSERT (NEW) ===============*/
            for (ItemModelMaster_Mdl listTemp : newModelList) {

                modelExist = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from itemModelMast where D365_code = '" + DmShared.xNull(listTemp.getModelCode()) + "'",
                                cnTemp)).intValue();

                if (modelExist == 0) {
                    listTemp.setHis_Code(DmServer.getSerialNoInteger("1", "itemModelMast", cnTemp));

                    sqlQuery = "Insert Into itemModelMast(code, name, preparedBy, u_EntDt,d365_code) "
                            + "Values (:code, :name, :preparedBy, :uEntDt,:d365_code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_Code()));
                    psTemp.setString("d365_code", DmShared.xNull(listTemp.getModelCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getModelName()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

                    psTemp.executeUpdate();
                    psTemp.close();
                }

                /*=============== UPDATE STAGING STATUS ===============*/
                sqlQuery = "UPDATE D365_itemModelMast "
                        + "SET jp_TimeStamp = GETDATE(), jp_status = 'Success',jp_Code = :jp_Code "
                        + "WHERE code = :code AND jp_TimeStamp is null";

                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getModelCode()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHis_Code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*=============== UPDATE (EXISTING) ===============*/
            for (ItemModelMaster_Mdl listTemp : updateModelList) {

                modelExist = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from itemModelMast where D365_code = '" + DmShared.xNull(listTemp.getModelCode()) + "'",
                                cnTemp)).intValue();

                if (modelExist == 0) {
                    listTemp.setHis_Code(DmServer.getSerialNoInteger("1", "itemModelMast", cnTemp));

                    sqlQuery = "Insert Into itemModelMast(code, name, preparedBy, u_EntDt,d365_code) "
                            + "Values (:code, :name, :preparedBy, :uEntDt,:d365_code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_Code()));
                    psTemp.setString("d365_code", DmShared.xNull(listTemp.getModelCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getModelName()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

                } else {

                    sqlQuery = "Update itemModelMast Set "
                            + "name = :name, "
                            + "U_Name = :u_Name, "
                            + "U_EntDt = :uEntDt "
                            + "Where code = :code";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(modelExist).toString());
                    psTemp.setString("name", DmShared.xNull(listTemp.getModelName()));
                    psTemp.setString("u_Name", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                }

                psTemp.executeUpdate();
                psTemp.close();

                /*=============== UPDATE STAGING STATUS ===============*/
                sqlQuery = "UPDATE D365_itemModelMast "
                        + "SET jp_TimeStamp = GETDATE(), jp_status = 'Success',jp_Code = :jp_Code "
                        + "WHERE code = :code AND jp_TimeStamp is null";

                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getModelCode()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHis_Code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
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
    }

    private static void syncStrengthMast(Connection cnTemp, Connection stagingCnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int StrengthExist = 0;

        try {

            /*===============fetch new & updated data===============*/
            List<Strength_Mdl> newStrengthList = new ArrayList<>();
            List<Strength_Mdl> updateStrengthList = new ArrayList<>();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_itemStrengthMast "
                    + " WHERE D365_status IN ('Update','New') and jp_TimeStamp is null "
                    + ") t WHERE rn = 1";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                Strength_Mdl list = new Strength_Mdl();
                list.setStrengthName(rsTemp.getString("name"));
                list.setStrengthCode(rsTemp.getString("code"));

                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newStrengthList.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateStrengthList.add(list);
                }
            }
            psTemp.close();
            rsTemp.close();

            /*=============== INSERT (NEW) ===============*/
            for (Strength_Mdl listTemp : newStrengthList) {

                StrengthExist = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from itemStrengthMast where D365_code = '" + DmShared.xNull(listTemp.getStrengthCode()) + "'",
                                cnTemp)).intValue();

                if (StrengthExist == 0) {
                    listTemp.setHis_Code(DmServer.getSerialNoInteger("1", "itemStrengthMast", cnTemp));
                    sqlQuery = "Insert Into itemStrengthMast(code, name, preparedBy, u_EntDt,d365_Code) "
                            + "Values (:code, :name, :preparedBy, :uEntDt,:d365_Code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_Code()));
                    psTemp.setString("d365_Code", DmShared.xNull(listTemp.getStrengthCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getStrengthName()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

                    psTemp.executeUpdate();
                    psTemp.close();
                }

                /*=============== UPDATE STAGING STATUS ===============*/
                sqlQuery = "UPDATE D365_itemStrengthMast "
                        + "SET jp_TimeStamp = GETDATE(), jp_status = 'Success',jp_Code = :jp_Code "
                        + "WHERE code = :code AND jp_TimeStamp is null";

                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getStrengthCode()));
                psTemp.setString("jp_Code", DmShared.xNull(listTemp.getHis_Code()));
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*=============== UPDATE (EXISTING) ===============*/
            for (Strength_Mdl listTemp : updateStrengthList) {

                StrengthExist = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from itemStrengthMast where D365_code = '" + DmShared.xNull(listTemp.getStrengthCode()) + "'",
                                cnTemp)).intValue();

                if (StrengthExist == 0) {
                    listTemp.setHis_Code(DmServer.getSerialNoInteger("1", "itemStrengthMast", cnTemp));

                    sqlQuery = "Insert Into itemStrengthMast(code, name, preparedBy, u_EntDt,d365_Code) "
                            + "Values (:code, :name, :preparedBy, :uEntDt,d365_Code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_Code()));
                    psTemp.setString("d365_Code", DmShared.xNull(listTemp.getStrengthCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getStrengthName()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

                } else {

                    sqlQuery = "Update itemStrengthMast Set "
                            + "name = :name, "
                            + "U_Name = :u_Name, "
                            + "U_EntDt = :uEntDt "
                            + "Where code = :code";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(StrengthExist).toString());
                    psTemp.setString("name", DmShared.xNull(listTemp.getStrengthName()));
                    psTemp.setString("u_Name", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                }

                psTemp.executeUpdate();
                psTemp.close();

                /*=============== UPDATE STAGING STATUS ===============*/
                sqlQuery = "UPDATE D365_itemStrengthMast "
                        + "SET jp_TimeStamp = GETDATE(), jp_status = 'Success',jp_Code = :jp_Code "
                        + "WHERE code = :code AND jp_TimeStamp is null";

                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getStrengthCode()));
                psTemp.setString("jp_Code", DmShared.xNull(listTemp.getHis_Code()));
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
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
    }

    private static void syncMedicineSubCategoryMast(Connection cnTemp, Connection stagingCnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int subCatCode = 0;

        try {

            /*===============fetch new data and updated data from staging===============*/
            List<ItemSubCategoryMaster_Mdl> newList = new ArrayList<>();
            List<ItemSubCategoryMaster_Mdl> updateList = new ArrayList<>();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_medicineSubCategoryMast "
                    + " WHERE D365_status IN ('Update','New') and jp_TimeStamp is null "
                    + ") t WHERE rn = 1";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                ItemSubCategoryMaster_Mdl list = new ItemSubCategoryMaster_Mdl();

                list.setD365_Code(rsTemp.getString("code"));
                list.setName(rsTemp.getString("name"));

                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newList.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateList.add(list);
                }
            }
            psTemp.close();
            rsTemp.close();

            /*=============== INSERT (NEW) ===============*/
            for (ItemSubCategoryMaster_Mdl listTemp : newList) {

                subCatCode = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from medicineSubCategoryMast where D365_code = '"
                                + DmShared.xNull(listTemp.getD365_Code()) + "' ", cnTemp)).intValue();

                if (subCatCode == 0) {
                    listTemp.setHis_Code(DmServer.getSerialNoInteger("1", "medicineSubCategoryMast", cnTemp));

                    sqlQuery = "Insert Into medicineSubCategoryMast(code, name, preparedBy, u_EntDt, D365_Code) "
                            + "Values (:code, :name, :preparedBy, :uEntDt, :D365_Code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_Code()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("D365_Code", DmShared.xNull(listTemp.getD365_Code()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                sqlQuery = "UPDATE D365_medicineSubCategoryMast\n"
                        + "SET jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getD365_Code()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHis_Code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*=============== UPDATE (EXISTING) ===============*/
            for (ItemSubCategoryMaster_Mdl listTemp : updateList) {

                subCatCode = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from medicineSubCategoryMast where D365_code = '"
                                + DmShared.xNull(listTemp.getD365_Code()) + "' ", cnTemp)).intValue();

                if (subCatCode == 0) {
                    listTemp.setHis_Code(DmServer.getSerialNoInteger("1", "medicineSubCategoryMast", cnTemp));

                    sqlQuery = "Insert Into medicineSubCategoryMast(code, name, preparedBy, u_EntDt, D365_Code) "
                            + "Values (:code, :name, :preparedBy, :uEntDt, :D365_Code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_Code()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("D365_Code", DmShared.xNull(listTemp.getD365_Code()));

                } else {

                    sqlQuery = "Update medicineSubCategoryMast Set "
                            + "name = :name, "
                            + "u_name = :u_Name, "
                            + "u_EntDt = :uEntDt "
                            + "Where code = :code ";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(subCatCode).toString());
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("u_Name", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                }

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE D365_medicineSubCategoryMast\n"
                        + "SET jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getD365_Code()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHis_Code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
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
    }

    private static void syncitemCat(Connection cnTemp, Connection stagingCnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int itemCatCode = 0;

        try {

            /*===============fetch new data and updated data from staging===============*/
            List<ItemCategory_Mdl> newItemCatList = new ArrayList<>();
            List<ItemCategory_Mdl> updateItemCatList = new ArrayList<>();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_itemCat "
                    + " WHERE D365_status IN ('Update','New') and jp_TimeStamp is null "
                    + ") t WHERE rn = 1";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                ItemCategory_Mdl list = new ItemCategory_Mdl();

                list.setCode(rsTemp.getString("code"));
                list.setName(rsTemp.getString("name"));
                list.setSaleRateProffitMargine(rsTemp.getString("saleRateProffitMargine"));
                list.setFilterItemType(rsTemp.getString("filterItemType"));

                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newItemCatList.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateItemCatList.add(list);
                }

            }
            psTemp.close();
            rsTemp.close();

            /*=============== INSERT (NEW) ===============*/
            for (ItemCategory_Mdl listTemp : newItemCatList) {
                itemCatCode = DmShared.iNull(
                        DmServer.executeScalar("select code from itemCat where d365_code = '"
                                + DmShared.xNull(listTemp.getCode()) + "' ", cnTemp)).intValue();

                if (itemCatCode == 0) {
                    listTemp.setHis_code(DmServer.getSerialNoInteger("1", "itemCat", cnTemp));
                    sqlQuery = "Insert Into itemCat(code,d365_code, name, printSrNo, filterItemType, isCommon, preparedBy, u_EntDt, saleRateProffitMargine, allowBifurcation) "
                            + "Values (:code,:d365_code, :name, :printSrNo, :filterItemType, :isCommon, :preparedBy, :uEntDt, :saleRateProffitMargine, :allowBifurcation)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_code()));
                    psTemp.setString("d365_code", DmShared.xNull(listTemp.getCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("printSrNo", "0");
                    psTemp.setString("filterItemType", DmShared.xNull(listTemp.getFilterItemType()));
                    psTemp.setString("isCommon", "0");
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("saleRateProffitMargine", DmShared.xNull(listTemp.getSaleRateProffitMargine()));
                    psTemp.setString("allowBifurcation", "0");
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                sqlQuery = "UPDATE D365_itemCat\n"
                        + "SET jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHis_code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*=============== UPDATE (EXISTING) ===============*/
            for (ItemCategory_Mdl listTemp : updateItemCatList) {

                itemCatCode = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from itemCat where D365_code = '"
                                + DmShared.xNull(listTemp.getCode()) + "' ",
                                cnTemp)).intValue();

                if (itemCatCode == 0) {
                    listTemp.setHis_code(DmServer.getSerialNoInteger("1", "itemCat", cnTemp));
                    sqlQuery = "Insert Into itemCat(code,d365_code, name, printSrNo, filterItemType, isCommon, preparedBy, u_EntDt, saleRateProffitMargine, allowBifurcation) "
                            + "Values (:code,:d365_code, :name, :printSrNo, :filterItemType, :isCommon, :preparedBy, :uEntDt, :saleRateProffitMargine, :allowBifurcation)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_code()));
                    psTemp.setString("d365_code", DmShared.xNull(listTemp.getCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("printSrNo", "0");
                    psTemp.setString("filterItemType", DmShared.xNull(listTemp.getFilterItemType()));
                    psTemp.setString("isCommon", "0");
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("saleRateProffitMargine", DmShared.xNull(listTemp.getSaleRateProffitMargine()));
                    psTemp.setString("allowBifurcation", "0");

                } else {

                    sqlQuery = "Update itemCat Set "
                            + "name = :name, "
                            + "filterItemType = :filterItemType, "
                            + "saleRateProffitMargine = :saleRateProffitMargine, "
                            + "u_Name = :u_Name, "
                            + "u_EntDt = :uEntDt "
                            + "Where code = :code ";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(itemCatCode).toString());
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("filterItemType", DmShared.xNull(listTemp.getFilterItemType()));
                    psTemp.setString("saleRateProffitMargine", DmShared.xNull(listTemp.getSaleRateProffitMargine()));
                    psTemp.setString("u_Name", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                }

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE D365_itemCat\n"
                        + "SET jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHis_code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
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
    }

    private static void syncRestrictedGrpMast(Connection cnTemp, Connection stagingCnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int restrictedGrpCode = 0;

        try {

            /*===============fetch new data and updated data from staging===============*/
            List<ItemRestrictedGroup_Mdl> newList = new ArrayList<>();
            List<ItemRestrictedGroup_Mdl> updateList = new ArrayList<>();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_restrictedGrpMast "
                    + " WHERE D365_status IN ('Update','New') and jp_TimeStamp is null "
                    + ") t WHERE rn = 1";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                ItemRestrictedGroup_Mdl list = new ItemRestrictedGroup_Mdl();

                list.setD365_Code(rsTemp.getString("code"));
                list.setName(rsTemp.getString("name"));

                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newList.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateList.add(list);
                }

            }
            psTemp.close();
            rsTemp.close();

            /*=============== INSERT (NEW) ===============*/
            for (ItemRestrictedGroup_Mdl listTemp : newList) {

                restrictedGrpCode = DmShared.iNull(DmServer.executeScalar(
                        "select code from restrictedGrpMast where D365_code = '"
                        + DmShared.xNull(listTemp.getD365_Code()) + "' ", cnTemp)).intValue();

                if (restrictedGrpCode == 0) {
                    listTemp.setHis_Code(DmServer.getSerialNoInteger("1", "restrictedGrpMast", cnTemp));
                    sqlQuery = "Insert Into restrictedGrpMast(code, name, preparedBy, u_EntDt, D365_Code) "
                            + "Values (:code, :name, :preparedBy, :uEntDt, :D365_Code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_Code()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("D365_Code", DmShared.xNull(listTemp.getD365_Code()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                sqlQuery = "UPDATE D365_restrictedGrpMast\n"
                        + "SET jp_TimeStamp = GETDATE(),jp_Code = :his_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getD365_Code()).toString());
                psTemp.setString("his_Code", DmShared.iNull(listTemp.getHis_Code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*=============== UPDATE (EXISTING) ===============*/
            for (ItemRestrictedGroup_Mdl listTemp : updateList) {

                restrictedGrpCode = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from restrictedGrpMast where D365_code = '"
                                + DmShared.xNull(listTemp.getD365_Code()) + "' ",
                                cnTemp
                        )
                ).intValue();

                if (restrictedGrpCode == 0) {
                    listTemp.setHis_Code(DmServer.getSerialNoInteger("1", "restrictedGrpMast", cnTemp));

                    sqlQuery = "Insert Into restrictedGrpMast(code, name, preparedBy, u_EntDt, D365_Code) "
                            + "Values (:code, :name, :preparedBy, :uEntDt, :D365_Code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_Code()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("D365_Code", DmShared.xNull(listTemp.getD365_Code()));

                } else {

                    sqlQuery = "Update restrictedGrpMast Set "
                            + "name = :name, "
                            + "u_Name = :u_Name, "
                            + "u_EntDt = :uEntDt "
                            + "Where code = :code ";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(restrictedGrpCode).toString());
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("u_Name", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                }

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE D365_restrictedGrpMast\n"
                        + "SET jp_TimeStamp = GETDATE(),jp_Code = :his_Code\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getD365_Code()).toString());
                psTemp.setString("his_Code", DmShared.iNull(listTemp.getHis_Code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
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
    }

    private static void syncMaterialTypeMast(Connection cnTemp, Connection stagingCnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int materialTypeCode = 0;

        try {

            /*===============fetch new data and updated data from staging===============*/
            List<ItemMaterialType_Mdl> newList = new ArrayList<>();
            List<ItemMaterialType_Mdl> updateList = new ArrayList<>();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_materialTypeMast "
                    + " WHERE D365_status IN ('Update','New') and jp_TimeStamp is null"
                    + ") t WHERE rn = 1";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                ItemMaterialType_Mdl list = new ItemMaterialType_Mdl();

                list.setCode(rsTemp.getString("code"));
                list.setName(rsTemp.getString("name"));

                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newList.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateList.add(list);
                }

            }
            psTemp.close();
            rsTemp.close();

            /*=============== INSERT (NEW) ===============*/
            for (ItemMaterialType_Mdl listTemp : newList) {

                materialTypeCode = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from materialTypeMast where D365_code = '"
                                + DmShared.xNull(listTemp.getCode()) + "' ", cnTemp)).intValue();

                if (materialTypeCode == 0) {
                    listTemp.setHis_code(DmServer.getSerialNoInteger("1", "materialTypeMast", cnTemp));
                    sqlQuery = "Insert Into materialTypeMast(code, name, preparedBy, u_EntDt, D365_Code) Values "
                            + "(:code, :name, :preparedBy, :uEntDt, :D365_Code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_code()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("D365_Code", DmShared.xNull(listTemp.getCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                sqlQuery = "UPDATE D365_materialTypeMast\n"
                        + "SET jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHis_code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*=============== UPDATE (EXISTING) ===============*/
            for (ItemMaterialType_Mdl listTemp : updateList) {

                materialTypeCode = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from materialTypeMast where D365_code = '"
                                + DmShared.xNull(listTemp.getCode()) + "' ",
                                cnTemp
                        )
                ).intValue();

                if (materialTypeCode == 0) {
                    listTemp.setHis_code(DmServer.getSerialNoInteger("1", "materialTypeMast", cnTemp));
                    sqlQuery = "Insert Into materialTypeMast(code, name, preparedBy, u_EntDt, D365_Code) Values "
                            + "(:code, :name, :preparedBy, :uEntDt, :D365_Code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_code()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("D365_Code", DmShared.xNull(listTemp.getCode()));

                } else {

                    sqlQuery = "Update materialTypeMast Set "
                            + "name = :name, "
                            + "U_Name = :u_Name, "
                            + "U_EntDt = :uEntDt "
                            + "Where code = :code ";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(materialTypeCode).toString());
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("u_Name", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                }

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE D365_materialTypeMast\n"
                        + "SET jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHis_code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
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
    }

    private static void syncMaterialCatMast(Connection cnTemp, Connection stagingCnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int materialCatCode = 0;

        try {

            /*===============fetch new data and updated data from staging===============*/
            List<ItemMaterialCatMast_Mdl> newList = new ArrayList<>();
            List<ItemMaterialCatMast_Mdl> updateList = new ArrayList<>();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_materialCatMast "
                    + " WHERE D365_status IN ('Update','New') and jp_TimeStamp is null"
                    + ") t WHERE rn = 1";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                ItemMaterialCatMast_Mdl list = new ItemMaterialCatMast_Mdl();

                list.setCode(rsTemp.getString("code"));
                list.setName(rsTemp.getString("name"));
                list.setFilterItemType(rsTemp.getString("filterItemType"));
                list.setIsCommon(rsTemp.getString("isCommon"));

                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newList.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateList.add(list);
                }

            }
            psTemp.close();
            rsTemp.close();

            /*=============== INSERT (NEW) ===============*/
            for (ItemMaterialCatMast_Mdl listTemp : newList) {

                materialCatCode = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from materialCatMast where D365_code = '"
                                + DmShared.xNull(listTemp.getCode()) + "' ",
                                cnTemp
                        )
                ).intValue();

                if (materialCatCode == 0) {
                    listTemp.setHis_code(DmServer.getSerialNoInteger("1", "materialCatMast", cnTemp));
                    sqlQuery = "Insert Into materialCatMast(code, name, filterItemType, isCommon, preparedBy, u_EntDt, D365_Code) "
                            + "Values (:code, :name, :filterItemType, :isCommon, :preparedBy, :uEntDt, :D365_Code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_code()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("filterItemType", DmShared.xNull(listTemp.getFilterItemType()));
                    psTemp.setString("isCommon", DmShared.xNull(listTemp.getIsCommon()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("D365_Code", DmShared.xNull(listTemp.getCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                sqlQuery = "UPDATE D365_materialCatMast\n"
                        + "SET jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHis_code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*=============== UPDATE (EXISTING) ===============*/
            for (ItemMaterialCatMast_Mdl listTemp : updateList) {

                materialCatCode = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from materialCatMast where D365_code = '"
                                + DmShared.xNull(listTemp.getCode()) + "' ",
                                cnTemp
                        )
                ).intValue();

                if (materialCatCode == 0) {
                    listTemp.setHis_code(DmServer.getSerialNoInteger("1", "materialCatMast", cnTemp));
                    sqlQuery = "Insert Into materialCatMast(code, name, filterItemType, isCommon, preparedBy, u_EntDt, D365_Code) "
                            + "Values (:code, :name, :filterItemType, :isCommon, :preparedBy, :uEntDt, :D365_Code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_code()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("filterItemType", DmShared.xNull(listTemp.getFilterItemType()));
                    psTemp.setString("isCommon", DmShared.xNull(listTemp.getIsCommon()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("D365_Code", DmShared.xNull(listTemp.getCode()));

                } else {

                    sqlQuery = "Update materialCatMast Set "
                            + "name = :name, "
                            + "filterItemType = :filterItemType, "
                            + "isCommon = :isCommon, "
                            + "U_Name = :u_Name, "
                            + "U_EntDt = :uEntDt "
                            + "Where code = :code ";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(materialCatCode).toString());
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("filterItemType", DmShared.xNull(listTemp.getFilterItemType()));
                    psTemp.setString("isCommon", DmShared.xNull(listTemp.getIsCommon()));
                    psTemp.setString("u_Name", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                }

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE D365_materialCatMast\n"
                        + "SET jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHis_code()).toString());
                psTemp.executeUpdate();
                psTemp.close();

            }

        } catch (Exception e) {
            throw e;
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
    }

    private static void syncItemSpecialityMast(Connection cnTemp, Connection stagingCnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int itemSpecialityCode = 0;

        try {

            /*===============fetch new data and updated data from staging===============*/
            List<ItemSpeciality_Mdl> newList = new ArrayList<>();
            List<ItemSpeciality_Mdl> updateList = new ArrayList<>();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_itemSpecialityMast "
                    + " WHERE D365_status IN ('Update','New') and jp_TimeStamp is null"
                    + ") t WHERE rn = 1";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                ItemSpeciality_Mdl list = new ItemSpeciality_Mdl();

                list.setCode(rsTemp.getString("code"));
                list.setName(rsTemp.getString("name"));

                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newList.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateList.add(list);
                }

            }
            psTemp.close();
            rsTemp.close();

            /*=============== INSERT (NEW) ===============*/
            for (ItemSpeciality_Mdl listTemp : newList) {

                itemSpecialityCode = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from itemSpecialityMast where D365_code = '"
                                + DmShared.xNull(listTemp.getCode()) + "' ", cnTemp)).intValue();

                if (itemSpecialityCode == 0) {
                    listTemp.setHis_code(DmServer.getSerialNoInteger("1", "itemSpecialityMast", cnTemp));
                    sqlQuery = "Insert Into itemSpecialityMast(code, name, preparedBy, u_EntDt, D365_Code) Values "
                            + "(:code, :name, :preparedBy, :uEntDt, :D365_Code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_code()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("D365_Code", DmShared.xNull(listTemp.getCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                sqlQuery = "UPDATE D365_itemSpecialityMast\n"
                        + "SET jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHis_code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*=============== UPDATE (EXISTING) ===============*/
            for (ItemSpeciality_Mdl listTemp : updateList) {

                itemSpecialityCode = DmShared.iNull(
                        DmServer.executeScalar(
                                "select code from itemSpecialityMast where D365_code = '"
                                + DmShared.xNull(listTemp.getCode()) + "' ", cnTemp)).intValue();

                if (itemSpecialityCode == 0) {
                    listTemp.setHis_code(DmServer.getSerialNoInteger("1", "itemSpecialityMast", cnTemp));
                    sqlQuery = "Insert Into itemSpecialityMast(code, name, preparedBy, u_EntDt, D365_Code) Values "
                            + "(:code, :name, :preparedBy, :uEntDt, :D365_Code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(listTemp.getHis_code()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("D365_Code", DmShared.xNull(listTemp.getCode()));

                } else {

                    sqlQuery = "Update itemSpecialityMast Set "
                            + "name = :name, "
                            + "U_Name = :u_Name, "
                            + "U_EntDt = :uEntDt "
                            + "Where code = :code ";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(itemSpecialityCode).toString());
                    psTemp.setString("name", DmShared.xNull(listTemp.getName()));
                    psTemp.setString("u_Name", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                }

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE D365_itemSpecialityMast\n"
                        + "SET jp_TimeStamp = GETDATE(),jp_Code = :jp_Code,\n"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("jp_Code", DmShared.iNull(listTemp.getHis_code()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
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
    }

    private static void syncManufacturerMast(Connection cnTemp, Connection stagingCnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        ResultSet rsTemp1 = null;
        String sqlQuery = "";
        String itemMakeCode = "";
        CallableStatement csTemp = null;
        String vType = "";
        try {

            /*=============== fetch new and updated data from staging ===============*/
            List<ItemMakeMast_Mdl> newItemMake = new ArrayList<>();
            List<ItemMakeMast_Mdl> updateItemMake = new ArrayList<>();

            sqlQuery = "SELECT vt.v_Type  FROM voucher_type vt WHERE  vt.category = '" + vCat + " ' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT * FROM ( "
                    + " SELECT *, ROW_NUMBER() OVER ( "
                    + "   PARTITION BY code ORDER BY D365_Timestamp DESC "
                    + " ) AS rn "
                    + " FROM D365_itemMakeMast "
                    + " WHERE D365_status IN ('Update','New') "
                    + "   AND jp_TimeStamp IS NULL "
                    + "   AND Segment1 = 'Manufacturer' "
                    + ") t WHERE rn = 1";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                ItemMakeMast_Mdl list = new ItemMakeMast_Mdl();
                list.setCode(rsTemp.getString("code"));
                list.setCompanyName(rsTemp.getString("companyName"));
                list.setFilterItemType(rsTemp.getString("filterItemType"));
                list.setShortName(rsTemp.getString("shortName"));
                list.setHisCode(rsTemp.getString("HIS_Code"));

                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newItemMake.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateItemMake.add(list);
                }

                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<>();

                fieldUDTHashMap.put("manufacturerName",
                        new FieldUDT_Mdl("manufacturerName",
                                DmShared.xNull(rsTemp.getString("companyName")),
                                DmShared.xNull(rsTemp.getString("companyName")).trim()));

                fieldUDTHashMap.put("acGroup", new FieldUDT_Mdl("acGroup", "Manufacturer", "Manufacturer"));

                fieldUDTHashMap.put("nature", new FieldUDT_Mdl("nature", "Manufacturer", "Manufacturer"));

                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection())
                        .prepareCall("{call getManufacturerId(?, ?)}");

                if (csTemp instanceof SQLServerCallableStatement) {
                    ((SQLServerCallableStatement) csTemp).setString(1, DmShared.iNull(vType).toString());
                    ((SQLServerCallableStatement) csTemp).setStructured(2, "dbo.fieldUDT", (SQLServerDataTable) DmServer.getFieldUDT(fieldUDTHashMap));
                }

                rsTemp1 = csTemp.executeQuery();

                if (rsTemp1.next()) {
                    list.setManualCode(DmShared.xNull(rsTemp1.getString(1)));
                }

                rsTemp1.close();
                csTemp.close();
            }

            rsTemp.close();
            psTemp.close();

            /*=============== Process New Records ===============*/
            for (ItemMakeMast_Mdl listTemp : newItemMake) {

                itemMakeCode = DmShared.xNull(DmServer.executeScalar(
                        "SELECT subcode FROM subgroup WHERE D365_code = '"
                        + DmShared.xNull(listTemp.getCode()) + "'", cnTemp));

                if (itemMakeCode.equalsIgnoreCase("")) {
                    listTemp.setHisCode(DmServer.getSerialNoBigInt("1", "SubGroup", cnTemp));
                    sqlQuery = "INSERT INTO subgroup "
                            + "(subcode, name, displayName, groupCode,manualCode,nature,filterNature,"
                            + " isActive,site_Code,preparedBy, u_EntDt,d365_code) "
                            + "VALUES "
                            + "(:subcode, :name, :displayName, :groupCode,:manualCode,:nature,:filterNature,"
                            + " :isActive,:site_Code,:preparedBy, :uEntDt,:d365_code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("subcode", DmShared.xNull(listTemp.getHisCode()));
                    psTemp.setString("d365_code", DmShared.xNull(listTemp.getCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getCompanyName()));
                    psTemp.setString("displayName", DmShared.xNull(listTemp.getCompanyName()));
                    psTemp.setString("groupCode", ("1000100000000000010"));
                    psTemp.setString("manualCode", DmShared.xNull(listTemp.getManualCode()));
                    psTemp.setString("nature", ("25"));
                    psTemp.setString("filterNature", ("25"));
                    psTemp.setString("isActive", ("1"));
                    psTemp.setString("site_Code", ("1"));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "declare @subCode bigint\n"
                            + "\n"
                            + "set @subCode=" + listTemp.getHisCode() + "\n"
                            + " \n"
                            + "insert into subGroupSiteList(subCode,site_Code)\n"
                            + " \n"
                            + "select subCode,site_Code from (\n"
                            + "\n"
                            + "select @subCode subCode,1 site_Code   \n"
                            + "\n"
                            + "union all\n"
                            + "\n"
                            + "select @subCode subCode,2 site_Code  \n"
                            + "\n"
                            + ") as tmp\n";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.close();

                }

                sqlQuery = "UPDATE D365_itemMakeMast\n"
                        + "SET jp_TimeStamp = GETDATE(),\n"
                        + "His_Code = :His_Code,"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("His_Code", DmShared.iNull(listTemp.getHisCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*=============== Process Update Records ===============*/
            for (ItemMakeMast_Mdl listTemp : updateItemMake) {
                listTemp.setHisCode(DmServer.getSerialNoBigInt("1", "SubGroup", cnTemp));
                itemMakeCode = DmShared.xNull(DmServer.executeScalar(
                        "SELECT subcode FROM subgroup WHERE D365_code = '"
                        + DmShared.xNull(listTemp.getCode()) + "'", cnTemp));

                if (itemMakeCode.equalsIgnoreCase("")) {

                    sqlQuery = "INSERT INTO subgroup "
                            + "(subcode, name, displayName, groupCode,manualCode,nature,filterNature,"
                            + " isActive,site_Code,preparedBy, u_EntDt,d365_code) "
                            + "VALUES "
                            + "(:subcode, :name, :displayName, :groupCode,:manualCode,:nature,:filterNature,"
                            + " :isActive,:site_Code,:preparedBy, :uEntDt,:d365_code)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("subcode", DmShared.xNull(listTemp.getHisCode()));
                    psTemp.setString("d365_code", DmShared.xNull(listTemp.getCode()));
                    psTemp.setString("name", DmShared.xNull(listTemp.getCompanyName()));
                    psTemp.setString("displayName", DmShared.xNull(listTemp.getCompanyName()));
                    psTemp.setString("groupCode", ("1000100000000000010"));
                    psTemp.setString("manualCode", DmShared.xNull(listTemp.getManualCode()));
                    psTemp.setString("nature", ("25"));
                    psTemp.setString("filterNature", ("25"));
                    psTemp.setString("isActive", ("1"));
                    psTemp.setString("site_Code", ("1"));
                    psTemp.setString("preparedBy", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "declare @subCode bigint\n"
                            + "\n"
                            + "set @subCode=" + listTemp.getHisCode() + "\n"
                            + " \n"
                            + "insert into subGroupSiteList(subCode,site_Code)\n"
                            + " \n"
                            + "select subCode,site_Code from (\n"
                            + "\n"
                            + "select @subCode subCode,1 site_Code   \n"
                            + "\n"
                            + "union all\n"
                            + "\n"
                            + "select @subCode subCode,2 site_Code  \n"
                            + "\n"
                            + ") as tmp\n";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.close();

                } else {

                    /* Update existing record */
                    sqlQuery = "UPDATE subgroup SET "
                            + "name = :name, "
                            + "displayName = :displayName, "
                            + "u_Name = :u_Name, "
                            + "u_EntDt = :uEntDt "
                            + "WHERE subcode = :code";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(itemMakeCode).toString());
                    psTemp.setString("name", DmShared.xNull(listTemp.getCompanyName()));
                    psTemp.setString("displayName", DmShared.xNull(listTemp.getCompanyName()));
                    psTemp.setString("u_Name", "Scheduler");
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                sqlQuery = "UPDATE D365_itemMakeMast\n"
                        + "SET jp_TimeStamp = GETDATE(),\n"
                        + "His_Code = :His_Code,"
                        + "jp_status = 'Success'\n"
                        + "WHERE code = :code "
                        + "and jp_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("His_Code", DmShared.iNull(listTemp.getHisCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
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
    }

    private static String syncDivisionMast(Connection cnTemp, Connection stagingCnTemp, String name, String code) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        String divisionCode = "";
        try {

            /*=============== Process New Records ===============*/
            sqlQuery = "INSERT INTO subgroup "
                    + "(subcode, name, displayName, groupCode,manualCode,nature,filterNature,"
                    + " isActive,site_Code,preparedBy, u_EntDt,d365_code) "
                    + "VALUES "
                    + "(:subcode, :name, :displayName, :groupCode,:manualCode,:nature,:filterNature,"
                    + " :isActive,:site_Code,:preparedBy, :uEntDt,:d365_code)";
            divisionCode = DmShared.xNull(DmServer.getSerialNoBigInt("1", "SubGroup", cnTemp));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subcode", divisionCode);
            psTemp.setString("d365_code", DmShared.xNull(code));
            psTemp.setString("name", DmShared.xNull(name));
            psTemp.setString("displayName", DmShared.xNull(name));
            psTemp.setString("groupCode", ("1000100000000000008"));
            psTemp.setString("manualCode", DmShared.xNull(name + code));
            psTemp.setString("nature", ("26"));
            psTemp.setString("filterNature", ("26"));
            psTemp.setString("isActive", ("1"));
            psTemp.setString("site_Code", ("1"));
            psTemp.setString("preparedBy", "Scheduler");
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw e;
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

        return divisionCode;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="check item is used">
    private static String checkItem(String itemCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String isItemUsed = "";
        String sqlQuery = "";
        try {
            sqlQuery = " SELECT COUNT(s.itemCode) AS cnt FROM stock s "
                    + " WHERE s.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " SELECT COUNT(ib.itemCode) AS cnt FROM itemBatch ib "
                    + " WHERE ib.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " SELECT COUNT(req.itemCode) AS cnt FROM requisitionDetail req "
                    + " WHERE req.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " SELECT COUNT(ind.itemCode) AS cnt FROM indentDetail ind "
                    + " WHERE ind.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " SELECT COUNT(ed.itemCode) AS cnt FROM enquiryDetail ed "
                    + " WHERE ed.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " SELECT COUNT(qd.itemCode) AS cnt FROM quotationDetail qd "
                    + " WHERE qd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " SELECT COUNT(pcd.itemCode) AS cnt FROM pChallanDetail pcd "
                    + " WHERE pcd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " SELECT COUNT(pbd.itemCode) AS cnt FROM pBillDetail pbd "
                    + " WHERE pbd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw e;
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
        return isItemUsed;
    }
    //</editor-fold>
}
