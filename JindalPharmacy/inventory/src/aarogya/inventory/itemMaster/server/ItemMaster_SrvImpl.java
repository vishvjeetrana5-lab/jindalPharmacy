package aarogya.inventory.itemMaster.server;

import aarogya.inventory.itemMaster.shared.DefaultValue_Mdl;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmEnum.GSTNature;
import static dataman.dmToolkit.stock.server.Stock_Server.deleteUnusedItemBatches;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import aarogya.inventory.itemMaster.shared.Find_Mdl;
import aarogya.inventory.itemMaster.shared.GrdAlternativeItems_Mdl;
import aarogya.inventory.itemMaster.shared.GrdApprovedVendors_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHMaterialCategory_Mdl;
import aarogya.inventory.itemMaster.shared.GrdItemDepartmentInfo_Mdl;
import aarogya.inventory.itemMaster.shared.GrdQcParameter_Mdl;
import aarogya.inventory.itemMaster.shared.GrdItsRateSlab_Mdl;
import aarogya.inventory.itemMaster.shared.GrdRestrictedDept_Mdl;
import aarogya.inventory.itemMaster.shared.GrdSoundLookAlike_Mdl;
import aarogya.inventory.itemMaster.shared.GrdUnit_Mdl;
import aarogya.inventory.itemMaster.shared.ItemMaster_Mdl;
import aarogya.inventory.itemMaster.shared.ItemMaster_Srv;
import aarogya.inventory.itemMaster.shared.ItemMaster_Vld;
import aarogya.inventory.itemMaster.shared.Sku_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHApprovedVendors_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHCategory_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHDivision_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHHSN_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHItemMake_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHItemPrperty_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHItemType_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHItemsName_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHManufacturer_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHMarketingCo_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHGroup_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl.Properties;
import aarogya.selfToolkit.stStatic.server.STApprovals;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.ItemStatus;
import aarogya.selfToolkit.stStatic.shared.STEnum.LabRequired;

public class ItemMaster_SrvImpl extends DmRSSPrivate implements ItemMaster_Srv {

    private final String ALTERNATIVE_ITEM_NATURE = "AI";
    private final String SOUND_LOOK_ALIKE_NATURE = "SL";
    private String vCat = new LibraryVoucherCategory().INVENTORY_ITEM_MAST;

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT im.CODE FROM itemMast as im WHERE im.filterItemType = :filterItemType ORDER BY NAME");
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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

    @Override
    public DmResultPageing<List<String>> getSuggestionItem(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT NAME FROM itemMast ");
            helper.addSort("NAME", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("Name")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResult<ItemMaster_Mdl> getMoveRec(String strKey) {
        DmResult<ItemMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        String filterNature = "";
        ItemMaster_Mdl mrTemp = new ItemMaster_Mdl();
        List<GrdItemDepartmentInfo_Mdl> listNode = new ArrayList<>();
        GrdItemDepartmentInfo_Mdl node;

        List<GrdAlternativeItems_Mdl> listAlternativeItems = new ArrayList<>();
        GrdAlternativeItems_Mdl alternativeItem;

        List<GrdSoundLookAlike_Mdl> listSoundLookAlike = new ArrayList<>();
        GrdSoundLookAlike_Mdl SoundLookAlike;

        List<GrdRestrictedDept_Mdl> listRestrictedDept = new ArrayList<>();
        GrdRestrictedDept_Mdl RestrictedDept;

        List<GrdApprovedVendors_Mdl> listApprovedVendors = new ArrayList<>();
        GrdApprovedVendors_Mdl ApprovedVendors;

        List<GrdQcParameter_Mdl> listQcParameters = new ArrayList<>();
        GrdQcParameter_Mdl QcParameters;

        List<GrdItsRateSlab_Mdl> listGrdRateMdl = new ArrayList<>();
        GrdItsRateSlab_Mdl grdRateMdl;

        List<GrdUnit_Mdl> listUnit_Mdl = new ArrayList<>();
        GrdUnit_Mdl Unit_Mdl;
        String propertyCodes = "", propertyNames = "", condition = "", propertyList = "";

        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT im.code, im.name, im.itemGroup AS groupCode, ig.name AS groupName, im.itemType AS typeCode, it.name AS typeName,im.sku AS skuCode, "
                    + "        um.unit AS sku,um.decimalPlaces,im.itemCategoryCode, ic.name AS categoryName, "
                    + "        im.itemPacking, hs.name as hsName ,im.hsnCode as hsnCode,im.preparedBY,im.preparedDate,im.u_name ,im.u_EntDt,im.statusChangedBy, im.statusChangedDate,  "
                    + "        im.ManualCode,im.isBestBefore,im.isLifeSaving,im.isHighValue,im.isFlowThrough,im.isNarcotics,im.isConsignment,  "
                    + "        im.isReturnableItem,im.isBilledItem,im.manufacturerCode,sgm.name as manufacturerName, "
                    + "        im.divisionCode,im.propertyList,sgd.name AS divisionName,vedCode, "
                    + "        im.itemGSTNature, im.tl_ExcessPer, im.tl_ShortagePer, im.wtaCode,im.isLabRequired , "
                    + "        im.isPOMandatory , im.isActive, im.isIndentMandatory, im.isQuotationMandatory, "
                    + "        im.purchaseUnit as purchaseUnitCode,"
                    + "        im.saleUnit as saleUnitCode,ump.unit as purchaseUnitName,"
                    + "        ums.unit as saleUnitName,im.purchaseUnitConversionRate,im.saleUnitConversionRate,"
                    + "        im.allowNegativeStock, im.itsBasedOn,\n"
                    + "        im.purchaseAc AS purchaseAcCode ,im.purchaseReturnAc AS purchaseReturnAcCode ,im.salesAc as salesAcCode, im.salesReturnAc as salesReturnAcCode,\n"
                    + "        im.subCode as inventoryAc,sgp.name as purchaseAcName, sgpr.name as purchaseReturnAcName,sgs.name as salesAcName, sgsr.name as salesReturnAcName,sgi.name as inventoryAcName, \n"
                    + "        im.grIrAc AS grIrAcCode ,im.consumptionAc AS consumptionAcCode ,im.cogsAc as cogsAcCode, im.pidAc as pidAcCode,im.inTransitAc as inTransitAcCode, \n"
                    + "        sgGrIr.name as grIrAcName,sgCon.name as consumptionAcName, sgCog.name as cogsAcName,sgPid.name as pidAcName, sgIntr.name as inTransitAcName, \n"
                    + "        imam.companyName as marketingCompanyName,im.marketingCompany as marketingCompanyCode, \n"
                    + "        im.model,md.name as modelName,im.strength,stm.name as strengthName,im.itemMakeCode,ima.companyName,sgd.name AS divisionName, \n"
                    + "        im.divisionCode,im.itemSpecialityCode,ism.name AS itemSpeciality,im.manufacturerCode,sgm.name as manufacturerName, \n "
                    + "        im.materialCategoryCode,im.materialTypeCode,mcm.name as materialCategoryName,mtm.name as materialTypeName, "
                    + "        im.costCenter,im.budgetHead,ccm.name as costCenterName,bhm.headName as budgetHeadName\n"
                    + "        FROM itemMast AS im "
                    + "        LEFT JOIN itemCat AS ic ON im.itemCategoryCode = ic.code "
                    + "        LEFT JOIN materialCatMast AS mcm ON im.materialCategoryCode = mcm.code "
                    + "        LEFT JOIN materialTypeMast AS mtm ON im.materialTypeCode = mtm.code "
                    + "        LEFT JOIN itemgroup AS ig ON im.itemGroup = ig.code "
                    + "        LEFT JOIN itemtype AS it ON im.itemType = it.code   "
                    + "        LEFT JOIN unitmast AS um ON im.sku = um.code "
                    + "        LEFT JOIN unitmast AS ump ON im.purchaseUnit = ump.code "
                    + "        LEFT JOIN unitmast AS ums ON im.saleUnit = ums.code "
                    + "        LEFT JOIN hsnMast AS hs ON im.hsnCode = hs.code "
                    + "        LEFT JOIN itemSpecialityMast AS ism ON im.itemSpecialityCode = ism.code  \n"
                    + "        left join itemMakeMast As ima ON im.itemMakeCode = ima.code  \n"
                    + "        left join itemMakeMast As imam ON im.marketingCompany = imam.code  \n"
                    + "        left join subgroup AS sgm ON im.manufacturerCode = sgm.subCode "
                    + "        left join subgroup AS sgd ON im.divisionCode = sgd.subCode "
                    + "        left join subgroup AS sgp ON im.purchaseAc = sgp.subCode"
                    + "        left join subgroup AS sgpr ON im.purchaseReturnAc = sgpr.subCode"
                    + "        left join subgroup AS sgs ON im.salesAc = sgs.subCode"
                    + "        left join subgroup AS sgsr ON im.salesReturnAc = sgsr.subCode"
                    + "        left join subgroup AS sgi ON im.subCode = sgi.subCode"
                    + "        left join subgroup AS sgGrIr ON im.grIrAc = sgGrIr.subCode"
                    + "        left join subgroup AS sgCon ON im.consumptionAc = sgCon.subCode"
                    + "        left join subgroup AS sgCog ON im.cogsAc = sgCog.subCode"
                    + "        left join subgroup AS sgPid ON im.pidAc = sgPid.subCode"
                    + "        left join subgroup AS sgIntr ON im.inTransitAc = sgIntr.subCode"
                    + "        LEFT JOIN itemModelMast AS md ON im.model = md.code \n"
                    + "        LEFT JOIN itemStrengthMast AS stm ON im.strength = stm.code \n"
                    + "        Left join costCenterMast AS ccm ON im.costCenter = ccm.code"
                    + "        Left join budgetHeadMast AS bhm ON im.budgetHead = bhm.headCode"
                    + "        WHERE im.code = :code and im.filterItemType=:filterItemType ; ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                mrTemp.setItemTypeTag(DmShared.xNull(rsTemp.getString("typeCode")));
                mrTemp.setItemTypeText(DmShared.xNull(rsTemp.getString("typeName")));
                mrTemp.setCategoryTag(DmShared.xNull(rsTemp.getString("itemCategoryCode")));
                mrTemp.setCategoryText(DmShared.xNull(rsTemp.getString("categoryName")));

                mrTemp.setMaterialCategoryTag(DmShared.xNull(rsTemp.getString("materialCategoryCode")));
                mrTemp.setMaterialCategoryText(DmShared.xNull(rsTemp.getString("materialCategoryName")));
                mrTemp.setMaterialTypeTag(DmShared.xNull(rsTemp.getString("materialTypeCode")));
                mrTemp.setMaterialTypeText(DmShared.xNull(rsTemp.getString("materialTypeName")));

                mrTemp.setMedGroupTag(DmShared.xNull(rsTemp.getString("groupCode")));
                mrTemp.setMedGroupText(DmShared.xNull(rsTemp.getString("groupName")));
                mrTemp.setPrvSkuTag(DmShared.xNull(rsTemp.getString("skuCode")));
                mrTemp.setSkuTag(DmShared.xNull(rsTemp.getString("skuCode")));
                mrTemp.setSkuText(DmShared.xNull(rsTemp.getString("sku")));
                mrTemp.setPropertyCode(DmShared.xNull(rsTemp.getString("propertyList")));
                propertyList = DmShared.xNull(rsTemp.getString("propertyList"));

                mrTemp.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                mrTemp.setHsnName(DmShared.xNull(rsTemp.getString("hsName")));
                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setStatusChangedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("statusChangedBy")), rsTemp.getTimestamp("statusChangedDate")));
//                mrTemp.setItemStatusCode(DmShared.xNull(rsTemp.getString("itemStatusCode")));
//                if (!DmShared.xNull(rsTemp.getString("itemStatusCode")).trim().equalsIgnoreCase("")) {
//                    mrTemp.setItemStatusName(DmShared.xNull(ItemStatus.getViaKey(DmShared.iNull(rsTemp.getString("itemStatusCode")).intValue()).getValue()));
//                }
                mrTemp.setBestBeforeTag(DmShared.xNull(rsTemp.getString("isBestBefore")));
                mrTemp.setBestBeforeText(DmShared.vNull(rsTemp.getString("isBestBefore")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setHighValueTag(DmShared.xNull(rsTemp.getString("isHighValue")));
                mrTemp.setHighValueText(DmShared.vNull(rsTemp.getString("isHighValue")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setFlowThroughTag(DmShared.xNull(rsTemp.getString("isFlowThrough")));
                mrTemp.setFlowThroughText(DmShared.vNull(rsTemp.getString("isFlowThrough")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setPoReqTag(DmShared.xNull(rsTemp.getString("isPOMandatory")));
                mrTemp.setPoReqText(DmShared.vNull(rsTemp.getString("isPOMandatory")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setIndentReqTag(DmShared.xNull(rsTemp.getString("isIndentMandatory")));
                mrTemp.setIndentReqText(DmShared.vNull(rsTemp.getString("isIndentMandatory")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setQuotationReqTag(DmShared.xNull(rsTemp.getString("isQuotationMandatory")));
                mrTemp.setQuotationReqText(DmShared.vNull(rsTemp.getString("isQuotationMandatory")).doubleValue() == 1 ? "Yes" : "No");

                mrTemp.setConsignmentTag(DmShared.xNull(rsTemp.getString("isConsignment")));
                mrTemp.setConsignmentText(DmShared.vNull(rsTemp.getString("isConsignment")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setReturnableItemTag(DmShared.xNull(rsTemp.getString("isReturnableItem")));
                mrTemp.setReturnableItemText(DmShared.vNull(rsTemp.getString("isReturnableItem")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setBilledItemTag(DmShared.xNull(rsTemp.getString("isBilledItem")));
                mrTemp.setBilledItemText(DmShared.vNull(rsTemp.getString("isBilledItem")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setGstNatureTag(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                mrTemp.setPackSize(DmShared.xNull(rsTemp.getString("itemPacking")));
                if (!DmShared.xNull(rsTemp.getString("itemGSTNature")).equalsIgnoreCase("")) {
                    mrTemp.setGstNatureText(GSTNature.getValueOf(DmShared.xNull(rsTemp.getString("itemGSTNature"))).getValue());
                }

                mrTemp.setPrvPurchaseUnitSkuCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                mrTemp.setPurchaseUnitSkuCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                mrTemp.setPurchaseUnitSkuName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                mrTemp.setPrvSaleUnitSkuCode(DmShared.xNull(rsTemp.getString("saleUnitCode")));
                mrTemp.setSaleUnitSkuCode(DmShared.xNull(rsTemp.getString("saleUnitCode")));
                mrTemp.setSaleUnitSkuName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                mrTemp.setPrvConversionRateSale(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                mrTemp.setConversionRateSale(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                mrTemp.setPrvConversionRatePurchase(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                mrTemp.setConversionRatePurchase(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                mrTemp.setAllowDecimalPlaces(DmServer.vNull(rsTemp.getString("decimalPlaces")));
                mrTemp.setTolerancePlus(DmServer.vNull(rsTemp.getString("tl_ExcessPer"), "0.00"));
                mrTemp.setToleranceMinus(DmServer.vNull(rsTemp.getString("tl_ShortagePer"), "0.00"));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                if (DmShared.iNull(rsTemp.getString("isLabRequired")).intValue() > 0) {
                    mrTemp.setLabRequiredTag(DmShared.xNull(rsTemp.getString("isLabRequired")).toString());
                    mrTemp.setLabRequiredText(DmShared.xNull(STEnum.LabRequired.getViaKey(DmShared.iNull(rsTemp.getString("isLabRequired")).intValue()).getValue()));
                }
                mrTemp.setAllowNegetiveStockCode(DmShared.xNull(rsTemp.getString("allowNegativeStock")));
                if (DmShared.xNull(rsTemp.getString("allowNegativeStock")).equalsIgnoreCase("1")) {
                    mrTemp.setAllowNegetiveStockName("Yes");
                } else {
                    mrTemp.setAllowNegetiveStockName("No");
                }
                mrTemp.setItsBasedOnTag(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                if (DmShared.xNull(rsTemp.getString("itsBasedOn")).equalsIgnoreCase("0")) {
                    mrTemp.setItsBasedOn("Fixed");//fixed
                } else if (DmShared.xNull(rsTemp.getString("itsBasedOn")).equalsIgnoreCase("1")) {
                    mrTemp.setItsBasedOn("Rate");//rate
                }

                //====================== ItemHaveNegativeStock ==============================================
                mrTemp.setIsNegativeStock(isItemHaveNegativeStock(DmShared.xNull(mrTemp.getSharedKeyFieldCode()), cnTemp));

                mrTemp.setInventoryAcTag(DmShared.xNull(rsTemp.getString("inventoryAc")));
                mrTemp.setInventoryAcText(DmShared.xNull(rsTemp.getString("inventoryAcName")));
                mrTemp.setPurchaseAcTag(DmShared.xNull(rsTemp.getString("purchaseAcCode")));
                mrTemp.setPurchaseAcText(DmShared.xNull(rsTemp.getString("purchaseAcName")));
                mrTemp.setSalesAcTag(DmShared.xNull(rsTemp.getString("salesAcCode")));
                mrTemp.setSalesAcText(DmShared.xNull(rsTemp.getString("salesAcName")));

                mrTemp.setPurchaseReturnAcTag(DmShared.xNull(rsTemp.getString("purchaseReturnAcCode")));
                mrTemp.setPurchaseReturnAcText(DmShared.xNull(rsTemp.getString("purchaseReturnAcName")));
                mrTemp.setSalesReturnAcTag(DmShared.xNull(rsTemp.getString("salesReturnAcCode")));
                mrTemp.setSalesReturnAcText(DmShared.xNull(rsTemp.getString("salesReturnAcName")));

                mrTemp.setGrIrAcTag(DmShared.xNull(rsTemp.getString("grIrAcCode")));
                mrTemp.setGrIrAcText(DmShared.xNull(rsTemp.getString("grIrAcName")));
                mrTemp.setConsumptionAcTag(DmShared.xNull(rsTemp.getString("consumptionAcCode")));
                mrTemp.setConsumptionAcText(DmShared.xNull(rsTemp.getString("consumptionAcName")));
                mrTemp.setCogsAcTag(DmShared.xNull(rsTemp.getString("cogsAcCode")));
                mrTemp.setCogsAcText(DmShared.xNull(rsTemp.getString("cogsAcName")));
                mrTemp.setPidAcTag(DmShared.xNull(rsTemp.getString("pidAcCode")));
                mrTemp.setPidAcText(DmShared.xNull(rsTemp.getString("pidAcName")));
                mrTemp.setInTransitAcTag(DmShared.xNull(rsTemp.getString("inTransitAcCode")));
                mrTemp.setInTransitAcText(DmShared.xNull(rsTemp.getString("inTransitAcName")));

                mrTemp.setNarcoticsTag(DmShared.xNull(rsTemp.getString("isNarcotics")));
                mrTemp.setNarcoticsText(DmShared.vNull(rsTemp.getString("isNarcotics")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setItemSpecialityTag(DmShared.xNull(rsTemp.getString("itemSpecialityCode")));
                mrTemp.setItemSpecialityText(DmShared.xNull(rsTemp.getString("itemSpeciality")));
                mrTemp.setManufacturerTag(DmShared.xNull(rsTemp.getString("manufacturerCode")));
                mrTemp.setManufacturerText(DmShared.xNull(rsTemp.getString("manufacturerName")));
                mrTemp.setDivisionTag(DmShared.xNull(rsTemp.getString("divisionCode")));
                mrTemp.setDivisionText(DmShared.xNull(rsTemp.getString("divisionName")));
                mrTemp.setItemMakeTag(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                mrTemp.setItemMakeText(DmShared.xNull(rsTemp.getString("companyName")));

                mrTemp.setModel(DmShared.xNull(rsTemp.getString("model")));
                mrTemp.setModelName(DmShared.xNull(rsTemp.getString("modelName")));
                mrTemp.setStrength(DmShared.xNull(rsTemp.getString("strength")));
                mrTemp.setStrengthName(DmShared.xNull(rsTemp.getString("strengthName")));

                mrTemp.setMarketingCompany(DmShared.xNull(rsTemp.getString("marketingCompanyName")));
                mrTemp.setMarketingCompanyCode(DmShared.xNull(rsTemp.getString("marketingCompanyCode")));

                mrTemp.setActiveTag(DmShared.xNull(rsTemp.getString("isActive")));
                mrTemp.setActiveText(DmShared.vNull(rsTemp.getString("isActive")).doubleValue() == 0 ? "InActive"
                        : DmShared.vNull(rsTemp.getString("isActive")).doubleValue() == 1 ? "Active"
                        : DmShared.vNull(rsTemp.getString("isActive")).doubleValue() == 2 ? "Hold" : "");
                
                mrTemp.setCostCenterTag(DmShared.xNull(rsTemp.getString("costCenter")));
                mrTemp.setCostCenterText(DmShared.xNull(rsTemp.getString("costCenterName")));
                mrTemp.setBudgetHeadTag(DmShared.xNull(rsTemp.getString("budgetHead")));
                mrTemp.setBudgetHeadText(DmShared.xNull(rsTemp.getString("budgetHeadName")));
            }
            rsTemp.close();
            psTemp.close();

            String[] propertyArra = DmShared.xNull(propertyList).split(",");

            if (!DmShared.xNull(propertyList).equalsIgnoreCase("") && propertyArra.length > 0) {
                for (String itemProperty : propertyArra) {
                    if (!DmShared.xNull(itemProperty).trim().equals("")) {
                        for (Properties prp : Properties.values()) {
                            if (DmShared.xNull(prp.getPropertyManualCode()).equalsIgnoreCase(DmShared.xNull(itemProperty))) {
                                propertyCodes += DmShared.xNull(prp.getPropertyManualCode()) + ",";
                                propertyNames += DmShared.xNull(prp.getPropertyName()).trim() + ",";
                                break;
                            }
                        }
                    }
                }

                if (propertyCodes.trim().contains(",")) {
                    propertyCodes = propertyCodes.substring(0, (propertyCodes.length() - 1));
                }
                if (propertyNames.trim().contains(",")) {
                    propertyNames = propertyNames.substring(0, (propertyNames.length() - 1));
                }
                mrTemp.setPropertyCode(propertyCodes);
                mrTemp.setPropertyName(propertyNames);
            }

            //==================== GST Rate Slab Grid =====================================
            sqlQuery = "select iid.rateFrom, iid.rateUpto, \n"
                    + " iid.gstTypeCode, it.name as gstTypeName \n"
                    + " from itemItsDetail iid\n"
                    + " left join gstType it on it.code = iid.gstTypeCode\n"
                    + " where iid.code = :itemCode \n"
                    + " ORDER BY iid.rateFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", strKey);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                grdRateMdl = new GrdItsRateSlab_Mdl();
                grdRateMdl.setRateFrom(DmServer.vNull(rsTemp.getString("rateFrom")));
                grdRateMdl.setRateUpto(DmServer.vNull(rsTemp.getString("rateUpto")));
                grdRateMdl.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                grdRateMdl.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                listGrdRateMdl.add(grdRateMdl);
            }
            mrTemp.getGrdRateIts_Mdls().addAll(listGrdRateMdl);
            rsTemp.close();
            psTemp.close();

            //==================== Item Details Grid =====================================
            sqlQuery = "SELECT  idi.itemCode, im.name AS itemName,idi.minStock,idi.maxStock,idi.reOrder,idi.binName, "
                    + " idi.departmentCode,sg.name AS departmentName ,idi.site_Code,sm.name AS siteName,"
                    + " idi.allowedConsumption "
                    + " FROM itemDepartmentInfo AS idi "
                    + " LEFT JOIN subGroup AS sg ON idi.departmentCode=sg.subCode "
                    + " LEFT JOIN itemMast AS im ON idi.itemCode=im.code "
                    + " LEFT JOIN siteMast AS sm ON idi.site_Code=sm.code "
                    + " where idi.itemCode =:itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", strKey);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                node = new GrdItemDepartmentInfo_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("departmentName")));
                node.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                node.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                node.setMinStock(DmServer.vNull(rsTemp.getString("minStock")));
                node.setMaxStock(DmServer.vNull(rsTemp.getString("maxStock")));
                node.setReOrder(DmServer.vNull(rsTemp.getString("reOrder")));
                node.setBinName(DmShared.xNull(rsTemp.getString("binName")));
                node.setAMConsumptionValue(DmServer.vNull(rsTemp.getString("allowedConsumption")));
                listNode.add(node);
            }
            mrTemp.getGrdItem_Mdl().addAll(listNode);
            rsTemp.close();
            psTemp.close();

            //==========================Alternative Items grid=======================
            sqlQuery = "  select sNo,itemCode,im.name as itemName "
                    + " from alternativeItem as ai"
                    + " left join itemMast as im on ai.itemCode = im.code  "
                    + " where ai.code = :code "
                    + " and nature = :nature ; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("nature", DmShared.xNull(ALTERNATIVE_ITEM_NATURE));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                alternativeItem = new GrdAlternativeItems_Mdl();
                alternativeItem.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("itemCode")).toString());
                alternativeItem.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")).toString());
                alternativeItem.setV_SNo(DmShared.iNull(rsTemp.getString("sNo")).toString());
                listAlternativeItems.add(alternativeItem);

            }
            mrTemp.getGrdAlternativeItems_Mdls().addAll(listAlternativeItems);
            rsTemp.close();
            psTemp.close();

            //==========================Sound Look Alike Items grid=======================
            sqlQuery = "  select sNo,itemCode,im.name as itemName "
                    + " from alternativeItem as ai"
                    + " left join itemMast as im on ai.itemCode = im.code  "
                    + " where ai.code = :code "
                    + " and nature = :nature ; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("nature", DmShared.xNull(SOUND_LOOK_ALIKE_NATURE));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                SoundLookAlike = new GrdSoundLookAlike_Mdl();
                SoundLookAlike.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("itemCode")).toString());
                SoundLookAlike.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")).toString());
                SoundLookAlike.setV_SNo(DmShared.iNull(rsTemp.getString("sNo")).toString());
                listSoundLookAlike.add(SoundLookAlike);

            }
            mrTemp.getGrdSoundLookAlike_Mdls().addAll(listSoundLookAlike);
            rsTemp.close();
            psTemp.close();

            //========================== Restricted Department grid=======================
            sqlQuery = "  select sNo,departmentCode,sg.name as departmentName "
                    + "     from itemRestrictedToDepartment as rd "
                    + "     LEFT JOIN  subGroup AS sg ON rd.departmentCode = sg.subCode "
                    + "     where code = :code ; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                RestrictedDept = new GrdRestrictedDept_Mdl();
                RestrictedDept.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("departmentCode")).toString());
                RestrictedDept.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("departmentName")).toString());
                RestrictedDept.setV_SNo(DmShared.iNull(rsTemp.getString("sNo")).toString());
                listRestrictedDept.add(RestrictedDept);

            }
            mrTemp.getGrdRestrictedDept_Mdls().addAll(listRestrictedDept);
            rsTemp.close();
            psTemp.close();

            //========================== Approved vendor grid=======================
            sqlQuery = " select av.sNo,av.approvedVendorCode,sg.name as approvedVendorsName"
                    + ", av.rate, av.unitCode, um.unit as UnitName  "
                    + "  from approvedVendors as av "
                    + "  LEFT JOIN  subGroup AS sg ON av.approvedVendorCode = sg.subCode  "
                    + "  LEFT JOIN  unitMast AS um ON um.code = av.unitCode  "
                    + "  where  av.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                ApprovedVendors = new GrdApprovedVendors_Mdl();
                ApprovedVendors.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("approvedVendorCode")).toString());
                ApprovedVendors.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("approvedVendorsName")).toString());
                ApprovedVendors.setV_SNo(DmShared.iNull(rsTemp.getString("sNo")).toString());
                ApprovedVendors.setUnitCode(DmShared.iNull(rsTemp.getString("unitCode")).toString());
                ApprovedVendors.setUnitName(DmShared.xNull(rsTemp.getString("UnitName")).toString());
                ApprovedVendors.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                listApprovedVendors.add(ApprovedVendors);

            }
            mrTemp.getGrdApprovedVendors_Mdls().addAll(listApprovedVendors);
            rsTemp.close();
            psTemp.close();

            //========================== QC Parameter grid=======================
            sqlQuery = " select iid.code, iid.impurityCode, im.name as impurityName, iid.sNo "
                    + " from itemImpurityDetail iid "
                    + " LEFT JOIN itemImpurityMast iim ON iim.code = iid.code "
                    + " LEFT JOIN impurityMast im ON im.code = iid.impurityCode "
                    + " where iim.itemCode = :code"
                    + " AND iim.testTemplateCode IS NULL "
                    + " ORDER BY iid.sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                QcParameters = new GrdQcParameter_Mdl();
                QcParameters.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("impurityCode")));
                QcParameters.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("impurityName")));
                QcParameters.setV_SNo(DmShared.xNull(rsTemp.getString("sNo")));
                listQcParameters.add(QcParameters);
            }
            mrTemp.getGrdQcParameter_Mdls().addAll(listQcParameters);
            rsTemp.close();
            psTemp.close();

            //====================== END ==============================================
            mrTemp.setIsItemUsed(checkItem(mrTemp, cnTemp));

            //====================== END ==============================================
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

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "Delete From itemDepartmentInfo Where itemCode=:itemCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From itemRestrictedToDepartment Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From approvedVendors Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From alternativeItem Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From itemITSDetail Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From itemSiteList Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //Delete Item Impurity Info
            deleteItemImpurityInfo(strKey, cnTemp);

            //Delete Unused Item Batches
            deleteUnusedItemBatches("", strKey, cnTemp);

            strSQL = "Delete From itemMast Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
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
    public DmResult<String> saveRecord(ItemMaster_Mdl mrTemp, boolean isActive) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false, isSiteListExists = false;
        String sqlQuery, sqlQuery1 = "";
        CallableStatement csTemp = null;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        String vType = "";
        try {

            ItemMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                vType = getItemVType(cnTemp);
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "itemmast", cnTemp));
                mrTemp.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(vType, cnTemp), DmServer.getSsnUserName(this), vType, mrTemp.getSharedKeyFieldCode(), cnTemp, this));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (DmShared.xNull(checkItem(mrTemp, cnTemp)).equalsIgnoreCase("1")) {
                if (DmShared.iNull(mrTemp.getSkuTag()).intValue() != DmShared.iNull(mrTemp.getPrvSkuTag()).intValue()) {
                    dmrTemp.setErrorNumber(500);
                    throw new UnsupportedOperationException("Cannot change sku as item is used.");
                }
                if (DmShared.iNull(mrTemp.getPurchaseUnitSkuCode()).intValue() != DmShared.iNull(mrTemp.getPrvPurchaseUnitSkuCode()).intValue()) {
                    dmrTemp.setErrorNumber(7000);
                    throw new UnsupportedOperationException("Cannot change purchase unit as item is used.");
                }
                if (DmShared.iNull(mrTemp.getSaleUnitSkuCode()).intValue() != DmShared.iNull(mrTemp.getPrvSaleUnitSkuCode()).intValue()) {
                    dmrTemp.setErrorNumber(8000);
                    throw new UnsupportedOperationException("Cannot change sale unit as item is used.");
                }
                if (DmShared.vNull(mrTemp.getConversionRateSale()).doubleValue() != DmShared.vNull(mrTemp.getPrvConversionRateSale()).doubleValue()) {
                    dmrTemp.setErrorNumber(9500);
                    throw new UnsupportedOperationException("Cannot change sale conversion rate as item is used.");
                }
                if (DmShared.vNull(mrTemp.getConversionRatePurchase()).doubleValue() != DmShared.vNull(mrTemp.getPrvConversionRatePurchase()).doubleValue()) {
                    dmrTemp.setErrorNumber(8500);
                    throw new UnsupportedOperationException("Cannot change purchase conversion rate as item is used.");
                }
            }

            sqlQuery = " Select Count(Name) As Cnt From itemMast mm Where mm.Name = :Name And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("Item Name already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            // ************************************** AllowNegetiveStockCode****************************      
            if (DmShared.xNull(mrTemp.getAllowNegetiveStockCode()).trim().equals("0")) {
                if (isItemHaveNegativeStock(DmShared.xNull(mrTemp.getSharedKeyFieldCode()), cnTemp)) {
                    throw new DmException(0, "Allow negative stock cannot be updated with </b>No</b> due to current stock is neagative.");
                }
            }
            //************************************************************
            if (blnAdd) {

                // ************************************** GET MANUAL CODE *********************************************
                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
                fieldUDTHashMap.put("itemName", new FieldUDT_Mdl("itemName", DmShared.xNull(mrTemp.getSharedKeyFieldName()), DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim()));
                fieldUDTHashMap.put("itemGroup", new FieldUDT_Mdl("itemGroup", DmShared.xNull(mrTemp.getMedGroupText()).toString(), DmShared.xNull(mrTemp.getMedGroupText()).trim()));
                fieldUDTHashMap.put("itemCat", new FieldUDT_Mdl("itemCat", DmShared.xNull(mrTemp.getCategoryText()).toString(), DmShared.xNull(mrTemp.getCategoryText()).trim()));
                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getItemManualCode(?, ?)}");
                if (csTemp instanceof SQLServerCallableStatement) {
                    ((SQLServerCallableStatement) csTemp).setString(1, DmShared.foriegnKey(vType));
                    ((SQLServerCallableStatement) csTemp).setStructured(2, "dbo.fieldUDT", (SQLServerDataTable) DmServer.getFieldUDT(fieldUDTHashMap));
                }
                rsTemp = csTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setManualCode(DmShared.xNull(rsTemp.getString(1)).toString());
                }
                rsTemp.close();
                csTemp.close();
                // ******************************************* END ***********************************************
            }

            if (DmShared.xNull(mrTemp.getManualCode()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Manual Code is mandatory.");
            }

            sqlQuery = " Select Count(ManualCode) As Cnt From itemmast mm Where mm.ManualCode = :ManualCode And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
            psTemp.setString("Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("Manual Code already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="CHECK OF SITE LIST">
            sqlQuery = " Select Count(code) As Cnt From itemSiteList isl Where isl.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("Cnt") > 0) {
                    isSiteListExists = true;
                }
            }
            rsTemp.close();
            psTemp.close();

//</editor-fold>
            if (blnAdd) {
                sqlQuery = "INSERT INTO itemmast (CODE, NAME,displayName, itemGroup,itemType, filterItemType, sku,\n "
                        + " preparedBY, preparedDate, transferDt,itemCategoryCode,\n"
                        + " manualCode,isBestBefore,isHighValue,isFlowThrough,\n  "
                        + " isConsignment,isReturnableItem,isBilledItem,\n"
                        + " propertyList,itemGSTNature,purchaseUnit,saleUnit,\n"
                        + " isPOMandatory, tl_ExcessPer, tl_ShortagePer, wtaCode,isActive,\n"
                        + " isIndentMandatory, isQuotationMandatory,isLabRequired ,\n"
                        + " purchaseUnitConversionRate,saleUnitConversionRate,itemPacking,allowNegativeStock,hsnCode,itsBasedOn , \n "
                        + " purchaseAc,salesAc,subCode, purchaseReturnAc , salesReturnAc,\n"
                        + " grIrAc,ConsumptionAc,cogsAc, pidAc , inTransitAc,\n"
                        + " model,strength,marketingCompany,itemSpecialityCode,manufacturerCode,itemMakeCode,divisionCode,isNarcotics , "
                        + " materialCategoryCode,materialTypeCode,costCenter,budgetHead)\n  "
                        + " VALUES (:code, :name, :displayName,:itemGroup, :itemType, :filterItemType, :sku,\n "
                        + "    :preparedBY, :preparedDate, NULL, :itemCategoryCode,\n"
                        + "    :manualCode,:isBestBefore,:isHighValue,:isFlowThrough,\n "
                        + "    :isConsignment,:isReturnableItem,:isBilledItem\n "
                        + " ,  :propertyList,:itemGSTNature,:purchaseUnit,:saleUnit\n"
                        + " ,  :isPOMandatory, :tl_ExcessPer, :tl_ShortagePer, :wtaCode, :isActive\n "
                        + " ,  :isIndentMandatory,  :isQuotationMandatory ,:isLabRequired\n "
                        + " ,  :purchaseUnitConversionRate,:saleUnitConversionRate,:itemPacking,:allowNegativeStock,:hsnCode,:itsBasedOn"
                        + " ,  :purchaseAc,:salesAc,:subCode, :purchaseReturnAc,:salesReturnAc"
                        + " ,  :grIrAc,:ConsumptionAc,:cogsAc, :pidAc , :inTransitAc\n"
                        + " ,  :model,:strength,:marketingCompany,:itemSpecialityCode,:manufacturerCode,:itemMakeCode,:divisionCode,:isNarcotics "
                        + " ,  :materialCategoryCode,:materialTypeCode,:costCenter,:budgetHead)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("itemGroup", DmShared.foriegnKey(mrTemp.getMedGroupTag()));
                psTemp.setString("hsnCode", DmShared.foriegnKey(mrTemp.getHsnCode()));
                psTemp.setString("manualCode", mrTemp.getManualCode());
                psTemp.setString("itemType", DmShared.foriegnKey(mrTemp.getItemTypeTag()));
                psTemp.setString("filterItemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
                psTemp.setString("sku", DmShared.foriegnKey(mrTemp.getSkuTag()));
                psTemp.setString("isActive", DmShared.foriegnKey(mrTemp.getActiveTag()));
                psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryTag()));
                psTemp.setString("isBestBefore", DmShared.xNull(mrTemp.getBestBeforeTag()));
                psTemp.setString("isHighValue", DmShared.xNull(mrTemp.getHighValueTag()));

                psTemp.setString("isFlowThrough", DmShared.xNull(mrTemp.getFlowThroughTag()));
                psTemp.setString("isConsignment", DmShared.xNull(mrTemp.getConsignmentTag()));
                psTemp.setString("isReturnableItem", DmShared.xNull(mrTemp.getReturnableItemTag()));
                psTemp.setString("isBilledItem", DmShared.xNull(mrTemp.getBilledItemTag()));

                psTemp.setString("propertyList", DmShared.xNull(mrTemp.getPropertyCode()));
                psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNatureTag()));
                psTemp.setString("purchaseUnit", DmShared.xNull(mrTemp.getPurchaseUnitSkuCode()));
                psTemp.setString("saleUnit", DmShared.xNull(mrTemp.getSaleUnitSkuCode()));
                psTemp.setString("tl_ExcessPer", DmShared.xNull(mrTemp.getTolerancePlus()));
                psTemp.setString("tl_ShortagePer", DmShared.xNull(mrTemp.getToleranceMinus()));
                psTemp.setString("isPOMandatory", DmShared.xNull(mrTemp.getPoReqTag()));
                psTemp.setString("isLabRequired", DmShared.iNull(mrTemp.getLabRequiredTag()).toString());
                psTemp.setString("isIndentMandatory", DmShared.xNull(mrTemp.getIndentReqTag()));
                psTemp.setString("isQuotationMandatory", DmShared.xNull(mrTemp.getQuotationReqTag()));
                psTemp.setString("purchaseUnitConversionRate", DmServer.vNull(mrTemp.getConversionRatePurchase()));
                psTemp.setString("saleUnitConversionRate", DmServer.vNull(mrTemp.getConversionRateSale()));
                psTemp.setString("itemPacking", DmShared.xNull(mrTemp.getPackSize()));
                psTemp.setString("allowNegativeStock", DmShared.xNull(mrTemp.getAllowNegetiveStockCode()));
                psTemp.setString("preparedBY", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTemp.getWtaCode()));
                psTemp.setString("itsBasedOn", DmShared.iNull(mrTemp.getItsBasedOnTag()).toString());
                psTemp.setString("purchaseAc", DmShared.foriegnKey(mrTemp.getPurchaseAcTag()));
                psTemp.setString("salesAc", DmShared.foriegnKey(mrTemp.getSalesAcTag()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getInventoryAcTag()));
                psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(mrTemp.getPurchaseReturnAcTag()));
                psTemp.setString("salesReturnAc", DmShared.foriegnKey(mrTemp.getSalesReturnAcTag()));
                psTemp.setString("grIrAc", DmShared.foriegnKey(mrTemp.getGrIrAcTag()));
                psTemp.setString("ConsumptionAc", DmShared.foriegnKey(mrTemp.getConsumptionAcTag()));
                psTemp.setString("cogsAc", DmShared.foriegnKey(mrTemp.getCogsAcTag()));
                psTemp.setString("pidAc", DmShared.foriegnKey(mrTemp.getPidAcTag()));
                psTemp.setString("inTransitAc", DmShared.foriegnKey(mrTemp.getInTransitAcTag()));

                psTemp.setString("itemSpecialityCode", DmShared.foriegnKey(mrTemp.getItemSpecialityTag()));
                psTemp.setString("manufacturerCode", DmShared.foriegnKey(mrTemp.getManufacturerTag()));
                psTemp.setString("divisionCode", DmShared.foriegnKey(mrTemp.getDivisionTag()));
                psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getItemMakeTag()));
                psTemp.setString("model", DmShared.foriegnKey(mrTemp.getModel()));
                psTemp.setString("strength", DmShared.foriegnKey(mrTemp.getStrength()));
                psTemp.setString("marketingCompany", DmShared.foriegnKey(mrTemp.getMarketingCompanyCode()));
                psTemp.setString("isNarcotics", DmShared.xNull(mrTemp.getNarcoticsTag()));
                psTemp.setString("materialCategoryCode", DmShared.foriegnKey(mrTemp.getMaterialCategoryTag()));
                psTemp.setString("materialTypeCode", DmShared.foriegnKey(mrTemp.getMaterialTypeTag()));
                psTemp.setString("costCenter", DmShared.foriegnKey(mrTemp.getCostCenterTag()));
                psTemp.setString("budgetHead", DmShared.foriegnKey(mrTemp.getBudgetHeadTag()));
            } else {
                sqlQuery = "UPDATE itemmast SET "
                        + "  CODE = :code, "
                        + "  NAME = :name, "
                        + "  displayName= :displayName,"
                        + "  itemGroup = :itemGroup, "
                        + "  itemType = :itemType, "
                        + "  filterItemType = :filterItemType, "
                        + "  sku = :sku, "
                        + "  u_Name = :userName, "
                        + "  hsnCode = :hsnCode, "
                        + "  u_EntDt = :u_EntDt, "
                        + "  transferDt = NULL, "
                        + "  manualCode = :manualCode, "
                        + "  isBestBefore = :isBestBefore, "
                        + "  isHighValue = :isHighValue, "
                        + "  isFlowThrough = :isFlowThrough, "
                        + "  isConsignment = :isConsignment, "
                        + "  isReturnableItem = :isReturnableItem, "
                        + "  isBilledItem = :isBilledItem, "
                        + "  isLabRequired = :isLabRequired, "
                        + "  itemCategoryCode = :itemCategoryCode,  "
                        + "  propertyList = :propertyList,"
                        + "  itemGSTNature=:itemGSTNature,"
                        + "  purchaseUnit=:purchaseUnit,"
                        + "  saleUnit=:saleUnit,"
                        + "  isPOMandatory = :isPOMandatory, "
                        + "  isIndentMandatory = :isIndentMandatory, "
                        + "  isQuotationMandatory = :isQuotationMandatory, "
                        + "  purchaseUnitConversionRate=:purchaseUnitConversionRate,"
                        + "  allowNegativeStock=:allowNegativeStock,"
                        + "  itemPacking=:itemPacking, "
                        + "  saleUnitConversionRate=:saleUnitConversionRate, "
                        + "  tl_ExcessPer = :tl_ExcessPer,"
                        + "  tl_ShortagePer = :tl_ShortagePer,"
                        + "  itsBasedOn = :itsBasedOn, "
                        + "  purchaseAc = :purchaseAc, "
                        + "  salesAc = :salesAc, "
                        + "  subCode = :subCode, "
                        + "  purchaseReturnAc= :purchaseReturnAc, "
                        + "  salesReturnAc = :salesReturnAc, "
                        + "  grIrAc = :grIrAc, "
                        + "  ConsumptionAc = :ConsumptionAc, "
                        + "  cogsAc = :cogsAc, "
                        + "  pidAc = :pidAc, "
                        + "  inTransitAc = :inTransitAc, "
                        + "  isNarcotics = :isNarcotics, "
                        + "  itemSpecialityCode = :itemSpecialityCode, "
                        + "  manufacturerCode = :manufacturerCode, "
                        + "  divisionCode = :divisionCode, "
                        + "  itemMakeCode = :itemMakeCode, "
                        + "  model=:model, "
                        + "  strength=:strength, "
                        + "  isActive=:isActive, "
                        + "  marketingCompany=:marketingCompany, "
                        + "  materialCategoryCode=:materialCategoryCode, "
                        + "  materialTypeCode=:materialTypeCode, "
                        + " costCenter = :costCenter, "
                        + " budgetHead = :budgetHead "
                        + " WHERE CODE = :CODE ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("isActive", DmShared.foriegnKey(mrTemp.getActiveTag()));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("itemGroup", DmShared.foriegnKey(mrTemp.getMedGroupTag()));
                psTemp.setString("hsnCode", DmShared.foriegnKey(mrTemp.getHsnCode()));
                psTemp.setString("manualCode", mrTemp.getManualCode());
                psTemp.setString("itemType", DmShared.foriegnKey(mrTemp.getItemTypeTag()));
                psTemp.setString("filterItemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
                psTemp.setString("sku", DmShared.foriegnKey(mrTemp.getSkuTag()));
                psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryTag()));
                psTemp.setString("isBestBefore", DmShared.xNull(mrTemp.getBestBeforeTag()));
                psTemp.setString("isHighValue", DmShared.xNull(mrTemp.getHighValueTag()));
                psTemp.setString("isFlowThrough", DmShared.xNull(mrTemp.getFlowThroughTag()));
                psTemp.setString("isConsignment", DmShared.xNull(mrTemp.getConsignmentTag()));
                psTemp.setString("isReturnableItem", DmShared.xNull(mrTemp.getReturnableItemTag()));
                psTemp.setString("isBilledItem", DmShared.xNull(mrTemp.getBilledItemTag()));
                psTemp.setString("isLabRequired", DmShared.iNull(mrTemp.getLabRequiredTag()).toString());
                psTemp.setString("propertyList", DmShared.xNull(mrTemp.getPropertyCode()));
                psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNatureTag()));
                psTemp.setString("purchaseUnit", DmShared.xNull(mrTemp.getPurchaseUnitSkuCode()));
                psTemp.setString("saleUnit", DmShared.xNull(mrTemp.getSaleUnitSkuCode()));
                psTemp.setString("isPOMandatory", DmShared.xNull(mrTemp.getPoReqTag()));
                psTemp.setString("isIndentMandatory", DmShared.xNull(mrTemp.getIndentReqTag()));
                psTemp.setString("isQuotationMandatory", DmShared.xNull(mrTemp.getQuotationReqTag()));
                psTemp.setString("purchaseUnitConversionRate", DmServer.vNull(mrTemp.getConversionRatePurchase()));
                psTemp.setString("saleUnitConversionRate", DmServer.vNull(mrTemp.getConversionRateSale()));
                psTemp.setString("itemPacking", DmShared.xNull(mrTemp.getPackSize()));
                psTemp.setString("allowNegativeStock", DmShared.xNull(mrTemp.getAllowNegetiveStockCode()));
                psTemp.setString("tl_ExcessPer", DmShared.xNull(mrTemp.getTolerancePlus()));
                psTemp.setString("tl_ShortagePer", DmShared.xNull(mrTemp.getToleranceMinus()));
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("itsBasedOn", DmShared.iNull(mrTemp.getItsBasedOnTag()).toString());
                psTemp.setString("purchaseAc", DmShared.foriegnKey(mrTemp.getPurchaseAcTag()));
                psTemp.setString("salesAc", DmShared.foriegnKey(mrTemp.getSalesAcTag()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getInventoryAcTag()));
                psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(mrTemp.getPurchaseReturnAcTag()));
                psTemp.setString("salesReturnAc", DmShared.foriegnKey(mrTemp.getSalesReturnAcTag()));
                psTemp.setString("grIrAc", DmShared.foriegnKey(mrTemp.getGrIrAcTag()));
                psTemp.setString("ConsumptionAc", DmShared.foriegnKey(mrTemp.getConsumptionAcTag()));
                psTemp.setString("cogsAc", DmShared.foriegnKey(mrTemp.getCogsAcTag()));
                psTemp.setString("pidAc", DmShared.foriegnKey(mrTemp.getPidAcTag()));
                psTemp.setString("inTransitAc", DmShared.foriegnKey(mrTemp.getInTransitAcTag()));
                psTemp.setString("itemSpecialityCode", DmShared.foriegnKey(mrTemp.getItemSpecialityTag()));
                psTemp.setString("manufacturerCode", DmShared.foriegnKey(mrTemp.getManufacturerTag()));
                psTemp.setString("divisionCode", DmShared.foriegnKey(mrTemp.getDivisionTag()));
                psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getItemMakeTag()));
                psTemp.setString("model", DmShared.foriegnKey(mrTemp.getModel()));
                psTemp.setString("strength", DmShared.foriegnKey(mrTemp.getStrength()));
                psTemp.setString("marketingCompany", DmShared.foriegnKey(mrTemp.getMarketingCompanyCode()));
                psTemp.setString("isNarcotics", DmShared.xNull(mrTemp.getNarcoticsTag()));
                psTemp.setString("materialCategoryCode", DmShared.foriegnKey(mrTemp.getMaterialCategoryTag()));
                psTemp.setString("materialTypeCode", DmShared.foriegnKey(mrTemp.getMaterialTypeTag()));
                psTemp.setString("costCenter", DmShared.foriegnKey(mrTemp.getCostCenterTag()));
                psTemp.setString("budgetHead", DmShared.foriegnKey(mrTemp.getBudgetHeadTag()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="INSERT IN ITEM_SITE_LIST">
            if (!isSiteListExists) {
                sqlQuery1 = "INSERT INTO itemSiteList (CODE, site_Code)  "
                        + " VALUES (:code, :site_Code)";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery1);
                psTemp1.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp1.setString("site_Code", DmShared.foriegnKey(DmServer.getSsnSiteCode(this)));

                psTemp1.executeUpdate();
                psTemp1.close();
            }
            //</editor-fold>


            /*===========================< itemITSDetail >===============================*/
            sqlQuery = " delete from itemITSDetail where code = :itemCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            int sNo2 = 0;
            for (int i = 0; i < mrTemp.getGrdRateIts_Mdls().size(); i++) {
                GrdItsRateSlab_Mdl detail = mrTemp.getGrdRateIts_Mdls().get(i);
                sNo2++;
                if (!DmShared.xNull(detail.getGstTypeCode()).equalsIgnoreCase("")) {
                    sqlQuery = "insert into itemITSDetail (code ,v_SNo,rateFrom,rateUpto,gstTypeCode)"
                            + " values(:code,:v_SNo,:rateFrom,:rateUpto,:gstTypeCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", DmShared.iNull(sNo2).toString());
                    psTemp.setString("rateFrom", DmServer.vNull(detail.getRateFrom(), "0.00"));
                    psTemp.setString("rateUpto", DmServer.vNull(detail.getRateUpto(), "0.00"));
                    psTemp.setString("gstTypeCode", DmShared.foriegnKey(detail.getGstTypeCode()));

                    psTemp.executeUpdate();
                    psTemp.close();
                }

            }

            //============================= Item Details Grid Entry =================================
            sqlQuery = " delete from itemDepartmentInfo where itemCode = :itemCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            List<GrdItemDepartmentInfo_Mdl> listItem = mrTemp.getGrdItem_Mdl();
            for (GrdItemDepartmentInfo_Mdl item_Mdl : listItem) {
                if ((!DmShared.xNull(item_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase(""))) {
                    sqlQuery = "insert into itemDepartmentInfo(departmentCode,itemCode, minStock, maxStock, reOrder, binName,site_Code,"
                            + "                                allowedConsumption)  "
                            + " VALUES (:departmentCode,:itemCode, :minStock, :maxStock, :reOrder,:binName, :siteCode,"
                            + "         :allowedConsumption) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("departmentCode", DmShared.foriegnKey(item_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("itemCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("minStock", DmServer.vNull(item_Mdl.getMinStock()));
                    psTemp.setString("maxStock", DmServer.vNull(item_Mdl.getMaxStock()));
                    psTemp.setString("reOrder", DmServer.vNull(item_Mdl.getReOrder()));
                    psTemp.setString("binName", DmShared.xNull(item_Mdl.getBinName()));
                    psTemp.setString("siteCode", DmShared.foriegnKey(item_Mdl.getSiteCode()));
                    psTemp.setString("allowedConsumption", DmServer.vNull(item_Mdl.getAMConsumptionValue()));

                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            //=============================Alternative Items Grid Entry =================================
            sqlQuery = " delete from alternativeItem where code = :code  and nature = :nature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("nature", DmShared.xNull(ALTERNATIVE_ITEM_NATURE));
            psTemp.executeUpdate();
            psTemp.close();

            int sNo1 = 0;
            if (mrTemp.getGrdAlternativeItems_Mdls() != null) {

                for (GrdAlternativeItems_Mdl grdAlternativeItem : mrTemp.getGrdAlternativeItems_Mdls()) {

                    if (DmShared.vNull(grdAlternativeItem.getSharedKeyFieldCode()).doubleValue() != 0) {
                        sqlQuery = "INSERT INTO alternativeItem (code, sNo, itemCode, nature) "
                                + "VALUES (:code, :sNo, :itemCode, :nature);";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setInt("sNo", ++sNo1);
                        psTemp.setString("itemCode", DmShared.foriegnKey(grdAlternativeItem.getSharedKeyFieldCode()));
                        psTemp.setString("nature", DmShared.xNull(ALTERNATIVE_ITEM_NATURE));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }
            //============================= sound and look alike Item  Grid Entry =================================
            sqlQuery = " delete from alternativeItem where code = :code  and nature = :nature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("nature", DmShared.xNull(SOUND_LOOK_ALIKE_NATURE));
            psTemp.executeUpdate();
            psTemp.close();

            if (mrTemp.getGrdSoundLookAlike_Mdls() != null) {
                for (GrdSoundLookAlike_Mdl grdSoundLookAlike : mrTemp.getGrdSoundLookAlike_Mdls()) {
                    if (DmShared.vNull(grdSoundLookAlike.getSharedKeyFieldCode()).doubleValue() != 0) {
                        sqlQuery = "INSERT INTO alternativeItem (code, sNo, itemCode, nature) "
                                + "VALUES (:code, :sNo, :itemCode, :nature);";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setInt("sNo", ++sNo1);
                        psTemp.setString("itemCode", DmShared.foriegnKey(grdSoundLookAlike.getSharedKeyFieldCode()));
                        psTemp.setString("nature", DmShared.xNull(SOUND_LOOK_ALIKE_NATURE));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }

            //============================= Restricted department Entry =================================
            sqlQuery = " delete from itemRestrictedToDepartment where code = :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (mrTemp.getGrdRestrictedDept_Mdls() != null) {
                int sNo = 0;
                for (GrdRestrictedDept_Mdl grdRestrictedDept : mrTemp.getGrdRestrictedDept_Mdls()) {
                    if (DmShared.vNull(grdRestrictedDept.getSharedKeyFieldCode()).doubleValue() != 0) {
                        sNo++;
                        sqlQuery = "INSERT INTO itemRestrictedToDepartment (code,sNo,departmentCode) "
                                + "VALUES (:code,:sNo,:departmentCode); ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setInt("sNo", sNo);
                        psTemp.setString("departmentCode", DmShared.foriegnKey(grdRestrictedDept.getSharedKeyFieldCode()));

                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }
            //============================= Approved vendors Grid Entry =================================
            sqlQuery = " delete from approvedVendors where code = :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (mrTemp.getGrdApprovedVendors_Mdls() != null) {
                int sNo = 0;
                for (GrdApprovedVendors_Mdl grdApprovedVendors : mrTemp.getGrdApprovedVendors_Mdls()) {
                    if (DmShared.vNull(grdApprovedVendors.getSharedKeyFieldCode()).doubleValue() > 0) {
                        sNo++;
                        sqlQuery = "INSERT INTO approvedVendors (code, sNo, approvedVendorCode, rate, unitCode) "
                                + "VALUES (:code, :sNo, :approvedVendorCode, :rate, :unitCode);";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setInt("sNo", sNo);
                        psTemp.setString("approvedVendorCode", DmShared.foriegnKey(grdApprovedVendors.getSharedKeyFieldCode()));
                        psTemp.setString("unitCode", DmShared.foriegnKey(grdApprovedVendors.getUnitCode()));
                        psTemp.setString("rate", DmServer.vNull(grdApprovedVendors.getRate(), "0.00"));

                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }

            //===========================<< save Qc parameter >>==================================
            saveQCParameter(mrTemp, cnTemp);

            //====================<< Item Active >>=================//
            if (DmShared.iNull(mrTemp.getWtaCode()).longValue() == 0 && isActive && blnAdd) {
                STApprovals.itemActivation(WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT im.code, im.name , ig.name AS groupName, um.unit AS sku,  "
                    + " im.isActive, it.name as itemTypeName,it.code as itemTypeCode,"
                    + " ic.name AS categoryName ,im.manualCode,hs.name as hsName ,"
                    + " im.isActive as itemStatusCode , "
                    + " im.purchaseUnit as purchaseUnitCode,"
                    + " ump.unit as purchaseUnitName,"
                    + " ums.unit as saleUnitName "
                    + " FROM itemmast AS im "
                    + " LEFT JOIN itemcat AS ic ON im.itemCategoryCode = ic.code "
                    + " LEFT JOIN itemgroup AS ig ON im.itemGroup = ig.code "
                    + " LEFT JOIN unitmast AS um ON im.sku = um.code "
                    + " LEFT JOIN unitmast AS ump ON im.purchaseUnit = ump.code "
                    + " LEFT JOIN unitmast AS ums ON im.saleUnit = ums.code "
                    + " left join itemType as it on im.ItemType=it.code"
                    + " LEFT JOIN hsnMast AS hs ON im.hsnCode = hs.code "
                    + " where im.filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setItemType(DmShared.xNull(rsTemp.getString("itemTypeName")));
                listNode.setItemTypeTag(DmShared.xNull(rsTemp.getString("itemTypeCode")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("categoryName")));
                listNode.setMedGroup(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setSku(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setSaleUnit(DmShared.xNull(rsTemp.getString("saleUnitName")));
                listNode.setHsn(DmShared.xNull(rsTemp.getString("hsName")));
                if (!DmShared.xNull(rsTemp.getString("itemStatusCode")).trim().equalsIgnoreCase("")) {
                    listNode.setActive(DmShared.xNull(ItemStatus.getViaKey(DmShared.iNull(rsTemp.getString("itemStatusCode")).intValue()).getValue()));
                }

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile, boolean isActiveCall) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            if (isActiveCall) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("0");
                listNode.setSharedKeyFieldName("Inactive");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("1");
                listNode.setSharedKeyFieldName("Active");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("2");
                listNode.setSharedKeyFieldName("Hold");
                lstTemp.add(listNode);
            } else {

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("0");
                listNode.setSharedKeyFieldName("No");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("1");
                listNode.setSharedKeyFieldName("Yes");
                lstTemp.add(listNode);
            }

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getSKU(String sku, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(sku).equalsIgnoreCase("")) {
                condition += " where code not in(" + sku + ")";
            }
            sqlWithParam.setSQL("select code, unit from unitmast " + condition);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("unit")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<TxtHCategory_Mdl>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCategory_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCategory_Mdl> lstTemp = new ArrayList<>();
        TxtHCategory_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select "
                    + "   imc.code as categoryCode "
                    + " , imc.name as categoryName "
                    + "   from itemcat imc "
                    + "   where (imc.filterItemType = :filterItemType OR imc.isCommon = 1) ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCategory_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("categoryName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<TxtHGroup_Mdl>> getTxtHItemGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHGroup_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHGroup_Mdl> lstTemp = new ArrayList<>();
        TxtHGroup_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT ig.code, ig.name ,ig.itemcatgory ,ic.name as itemcatgoryName,"
                    + " ig.purchaseAc AS purchaseAcCode ,ig.purchaseReturnAc AS purchaseReturnAcCode ,ig.salesAc as salesAcCode, ig.salesReturnAc as salesReturnAcCode,\n"
                    + " ig.subCode as inventoryAc,sgp.name as purchaseAcName, sgpr.name as purchaseReturnAcName,sgs.name as salesAcName, sgsr.name as salesReturnAcName,sgi.name as inventoryAcName, \n"
                    + " ig.grIrAc AS grIrAcCode ,ig.consumptionAc AS consumptionAcCode ,ig.cogsAc as cogsAcCode, ig.pidAc as pidAcCode,ig.inTransitAc as inTransitAcCode, \n"
                    + " sgGrIr.name as grIrAcName,sgCon.name as consumptionAcName, sgCog.name as cogsAcName,sgPid.name as pidAcName, sgIntr.name as inTransitAcName, \n"
                    + " ig.costCenter,ig.budgetHead,ccm.name as costCenterName,bhm.headName as budgetHeadName\n"
                    + " FROM itemgroup ig "
                    + " left join itemcat ic on ic.code=ig.itemcatgory "
                    + " left join subgroup AS sgp ON ig.purchaseAc = sgp.subCode"
                    + " left join subgroup AS sgpr ON ig.purchaseReturnAc = sgpr.subCode"
                    + " left join subgroup AS sgs ON ig.salesAc = sgs.subCode"
                    + " left join subgroup AS sgsr ON ig.salesReturnAc = sgsr.subCode"
                    + " left join subgroup AS sgi ON ig.subCode = sgi.subCode"
                    + " left join subgroup AS sgGrIr ON ig.grIrAc = sgGrIr.subCode"
                    + " left join subgroup AS sgCon ON ig.consumptionAc = sgCon.subCode"
                    + " left join subgroup AS sgCog ON ig.cogsAc = sgCog.subCode"
                    + " left join subgroup AS sgPid ON ig.pidAc = sgPid.subCode"
                    + " left join subgroup AS sgIntr ON ig.inTransitAc = sgIntr.subCode"
                    + " left join costCenterMast AS ccm ON ig.costCenter = ccm.code"
                    + " left join budgetHeadMast AS bhm ON ig.budgetHead = bhm.headCode"
                    + " WHERE (ig.filterItemType = :filterItemType OR ig.isCommon = 1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHGroup_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setCategoryCode(DmShared.xNull(rsTemp.getString("itemcatgory")));
                listNode.setCategoryName(DmShared.xNull(rsTemp.getString("itemcatgoryName")));
                listNode.setInventoryAcTag(DmShared.xNull(rsTemp.getString("inventoryAc")));
                listNode.setInventoryAcText(DmShared.xNull(rsTemp.getString("inventoryAcName")));
                listNode.setPurchaseAcTag(DmShared.xNull(rsTemp.getString("purchaseAcCode")));
                listNode.setPurchaseAcText(DmShared.xNull(rsTemp.getString("purchaseAcName")));
                listNode.setSalesAcTag(DmShared.xNull(rsTemp.getString("salesAcCode")));
                listNode.setSalesAcText(DmShared.xNull(rsTemp.getString("salesAcName")));
                listNode.setPurchaseReturnAcTag(DmShared.xNull(rsTemp.getString("purchaseReturnAcCode")));
                listNode.setPurchaseReturnAcText(DmShared.xNull(rsTemp.getString("purchaseReturnAcName")));
                listNode.setSalesReturnAcTag(DmShared.xNull(rsTemp.getString("salesReturnAcCode")));
                listNode.setSalesReturnAcText(DmShared.xNull(rsTemp.getString("salesReturnAcName")));
                listNode.setGrIrAcTag(DmShared.xNull(rsTemp.getString("grIrAcCode")));
                listNode.setGrIrAcText(DmShared.xNull(rsTemp.getString("grIrAcName")));
                listNode.setConsumptionAcTag(DmShared.xNull(rsTemp.getString("consumptionAcCode")));
                listNode.setConsumptionAcText(DmShared.xNull(rsTemp.getString("consumptionAcName")));
                listNode.setCogsAcTag(DmShared.xNull(rsTemp.getString("cogsAcCode")));
                listNode.setCogsAcText(DmShared.xNull(rsTemp.getString("cogsAcName")));
                listNode.setPidAcTag(DmShared.xNull(rsTemp.getString("pidAcCode")));
                listNode.setPidAcText(DmShared.xNull(rsTemp.getString("pidAcName")));
                listNode.setInTransitAcTag(DmShared.xNull(rsTemp.getString("inTransitAcCode")));
                listNode.setInTransitAcText(DmShared.xNull(rsTemp.getString("inTransitAcName")));
                listNode.setCostCenterTag(DmShared.xNull(rsTemp.getString("costCenter")));
                listNode.setCostCenterText(DmShared.xNull(rsTemp.getString("costCenterName")));
                listNode.setBudgetHeadTag(DmShared.xNull(rsTemp.getString("budgetHead")));
                listNode.setBudgetHeadText(DmShared.xNull(rsTemp.getString("budgetHeadName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<TxtHItemType_Mdl>> getTxtHItemType(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<TxtHItemType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemType_Mdl> lstTemp = new ArrayList<>();
        TxtHItemType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select code,name,manualcode "
                    + " from itemType "
                    + " WHERE manualcode NOT IN ('FM','OI') ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortname(DmShared.xNull(rsTemp.getString("manualCode")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHApplicableBatch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("batchNo");
            listNode.setSharedKeyFieldName("batchNo");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("expiryDate");
            listNode.setSharedKeyFieldName("expiryDate");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("mrp");
            listNode.setSharedKeyFieldName("mrp");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + " LEFT JOIN subGroupSiteList as sgsl on sg.subcode = sgsl.subcode "
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PR') "
                    + " AND sgsl.site_Code = :siteCode "
                    + " AND sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT code, name FROM siteMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<TxtHItemsName_Mdl>> getTxtHItemName(String itemCode, DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<TxtHItemsName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemsName_Mdl> lstTemp = new ArrayList<>();
        TxtHItemsName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" select code,name,manualCode from itemMast "
                    + " where isActive = 1 and code<>:itemCode and filterItemType = :filterItemType");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("itemCode", itemCode);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemsName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRestrictedDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode, sg.name"
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','DP') AND sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<TxtHApprovedVendors_Mdl>> getTxtHApprovedVendors(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHApprovedVendors_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHApprovedVendors_Mdl> lstTemp = new ArrayList<>();
        TxtHApprovedVendors_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String filterNature = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            filterNature = DmServer.getCodeViaManualCode("SU", "manualCode", "subgroupNature", "code", cnTemp);

            sqlWithParam.setSQL(" select sg.subCode,sg.name,ct.cityName,sg.manualCode,sg.filterNature"
                    + " from subGroup as sg "
                    + " left join city as ct on sg.cityCode = ct.cityCode "
                    + " where filterNature = :filterNature ");
            sqlWithParam.setString("filterNature", DmShared.xNull(filterNature));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHApprovedVendors_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVED(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Vital");
            listNode.setSharedKeyFieldName("Vital");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Essential");
            listNode.setSharedKeyFieldName("Essential");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Desire");
            listNode.setSharedKeyFieldName("Desire");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    private String checkItem(ItemMaster_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String isItemUsed = "";
        String strSQL = "";
        try {
            strSQL = " SELECT COUNT(s.itemCode) AS cnt FROM stock s "
                    + " WHERE s.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "select count(*) AS cnt from requisitionDetail rd where rd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "select count(*) AS cnt from indentDetail id where id.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();
            strSQL = "select count(*) AS cnt from enquiryDetail ed where ed.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();
            strSQL = "select count(*) AS cnt from quotationDetail qd where qd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = " SELECT COUNT(ib.itemCode) AS cnt FROM itemBatch ib "
                    + " WHERE ib.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = " SELECT COUNT(pcd.itemCode) AS cnt FROM pChallanDetail pcd "
                    + " WHERE pcd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = " SELECT COUNT(pbd.itemCode) AS cnt FROM pBillDetail pbd "
                    + " WHERE pbd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
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
        return isItemUsed;
    }

    @Override
    public DmResultPageing<List<Sku_Mdl>> getSKUDetails(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Sku_Mdl>> rstTemp = new DmResultPageing<>();
        List<Sku_Mdl> lstTemp = new ArrayList<>();
        Sku_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select code, unit,decimalPlaces from unitmast ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Sku_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setAllowedDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<TxtHItemPrperty_Mdl>> getTxtHProperty(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemPrperty_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemPrperty_Mdl> lstTemp = new ArrayList<>();
        TxtHItemPrperty_Mdl listNode;
        rstTemp.setData(lstTemp);
        try {
            for (Properties prp : Properties.values()) {
                if ((!DmShared.xNull(prp.getPropertyManualCode()).equalsIgnoreCase(Properties.MRP.getPropertyManualCode()))) {
                    listNode = new TxtHItemPrperty_Mdl();
                    listNode.setSharedKeyFieldCode(DmShared.xNull(prp.getPropertyManualCode()));
                    listNode.setSharedKeyFieldName(DmShared.xNull(prp.getPropertyName()));
                    listNode.setManualCode(DmShared.xNull(prp.getPropertyManualCode()));
                    lstTemp.add(listNode);
                }
            }

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHHSN_Mdl>> getTxtHHSN(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHHSN_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHHSN_Mdl> lstTemp = new ArrayList<>();
        TxtHHSN_Mdl listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select hm.code, hm.name, hm.hsn from hsnMast hm";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHHSN_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
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

    private boolean isItemHaveNegativeStock(String itemCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        boolean returnValue = false;
        try {
            sqlQuery = "SELECT COUNT(*) AS cnt "
                    + " FROM itemBatchStock ibs"
                    + " WHERE ibs.itemCode = :itemCode"
                    + " AND ISNULL(ibs.balanceQty,0) < 0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    returnValue = true;
                }
            }
            rsTemp.close();
            psTemp.close();

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
        return returnValue;

    }

    @Override
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = "";// ImportData_Server.getCSVFormats(new ItemMaster().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "Item Master";
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT  im.name, it.name AS itemType, ig.name AS itemGroup, ic.name AS itemCategory, hs.name as hsn ,  "
                    + "         sgm.name as manufacturer, ima.companyName as itemMake,"
                    + "		vedCode, sgd.name AS Division, im.propertyList as applicableProperty,"
                    + "		0 AS allowNegativeStock, im.isConsignment as consignmentItem, im.isBestBefore,im.isLifeSaving,"
                    + "	        im.isNarcotics, im.isFlowThrough, im.isHighValue, im.isReturnableItem,  im.isBilledItem,"
                    + "	        um.unit AS sku, ump.unit as purchaseUnit, im.purchaseUnitConversionRate,  ums.unit as saleUnit,"
                    + "         im.saleUnitConversionRate,"
                    + "	        im.isPOMandatory   , im.isIndentMandatory   , im.isQuotationMandatory "
                    + "                            FROM itemMast AS im "
                    + "                            LEFT JOIN itemCat AS ic ON im.itemCategoryCode = ic.code "
                    + "                            LEFT JOIN itemgroup AS ig ON im.itemGroup = ig.code "
                    + "                            LEFT JOIN itemtype AS it ON im.itemType = it.code   "
                    + "                            LEFT JOIN unitmast AS um ON im.sku = um.code "
                    + "                            LEFT JOIN unitmast AS ump ON im.purchaseUnit = ump.code "
                    + "                            LEFT JOIN unitmast AS ums ON im.saleUnit = ums.code "
                    + "                            LEFT JOIN hsnMast AS hs ON im.hsnCode = hs.code "
                    + "                            left join subgroup AS sgm ON im.manufacturerCode = sgm.subCode "
                    + "                            left join subgroup AS sgd ON im.divisionCode = sgd.subCode "
                    + "	 WHERE im.filterItemType = :filterItemType "
                    + "  AND im.isActive = 1 "
                    + "	 order by im.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                lineData = "\"" + DmShared.xNull(rsTemp.getString("name")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemType")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemGroup")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemCategory")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("hsn")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("manufacturer")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("vedCode")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("Division")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("applicableProperty")) + "\"";
                lineData += commaDelimiter;
                if (DmShared.xNull(rsTemp.getString("allowNegativeStock")).equalsIgnoreCase("1")) {

                    lineData += "\"" + "Yes" + "\"";
                    lineData += commaDelimiter;

                } else {
                    lineData += "\"" + "No" + "\"";
                    lineData += commaDelimiter;
                }
                if (DmShared.xNull(rsTemp.getString("consignmentItem")).equalsIgnoreCase("1")) {

                    lineData += "\"" + "Yes" + "\"";
                    lineData += commaDelimiter;

                } else {
                    lineData += "\"" + "No" + "\"";
                    lineData += commaDelimiter;
                }
                if (DmShared.xNull(rsTemp.getString("isBestBefore")).equalsIgnoreCase("1")) {

                    lineData += "\"" + "Yes" + "\"";
                    lineData += commaDelimiter;

                } else {
                    lineData += "\"" + "No" + "\"";
                    lineData += commaDelimiter;
                }
                if (DmShared.xNull(rsTemp.getString("isLifeSaving")).equalsIgnoreCase("1")) {

                    lineData += "\"" + "Yes" + "\"";
                    lineData += commaDelimiter;

                } else {
                    lineData += "\"" + "No" + "\"";
                    lineData += commaDelimiter;
                }
                if (DmShared.xNull(rsTemp.getString("isNarcotics")).equalsIgnoreCase("1")) {

                    lineData += "\"" + "Yes" + "\"";
                    lineData += commaDelimiter;

                } else {
                    lineData += "\"" + "No" + "\"";
                    lineData += commaDelimiter;
                }
                if (DmShared.xNull(rsTemp.getString("isFlowThrough")).equalsIgnoreCase("1")) {

                    lineData += "\"" + "Yes" + "\"";
                    lineData += commaDelimiter;

                } else {
                    lineData += "\"" + "No" + "\"";
                    lineData += commaDelimiter;
                }
                if (DmShared.xNull(rsTemp.getString("isHighValue")).equalsIgnoreCase("1")) {

                    lineData += "\"" + "Yes" + "\"";
                    lineData += commaDelimiter;

                } else {
                    lineData += "\"" + "No" + "\"";
                    lineData += commaDelimiter;
                }
                if (DmShared.xNull(rsTemp.getString("isReturnableItem")).equalsIgnoreCase("1")) {

                    lineData += "\"" + "Yes" + "\"";
                    lineData += commaDelimiter;

                } else {
                    lineData += "\"" + "No" + "\"";
                    lineData += commaDelimiter;
                }
                if (DmShared.xNull(rsTemp.getString("isBilledItem")).equalsIgnoreCase("1")) {

                    lineData += "\"" + "Yes" + "\"";
                    lineData += commaDelimiter;

                } else {
                    lineData += "\"" + "No" + "\"";
                    lineData += commaDelimiter;
                }

                lineData += "\"" + DmShared.xNull(rsTemp.getString("sku")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("purchaseUnit")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("saleUnit")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmServer.vNull(rsTemp.getString("saleUnitConversionRate")) + "\"";
                lineData += commaDelimiter;

                if (DmShared.xNull(rsTemp.getString("isPOMandatory")).equalsIgnoreCase("1")) {

                    lineData += "\"" + "Yes" + "\"";
                    lineData += commaDelimiter;

                } else {
                    lineData += "\"" + "No" + "\"";
                    lineData += commaDelimiter;
                }
                if (DmShared.xNull(rsTemp.getString("isIndentMandatory")).equalsIgnoreCase("1")) {

                    lineData += "\"" + "Yes" + "\"";
                    lineData += commaDelimiter;

                } else {
                    lineData += "\"" + "No" + "\"";
                    lineData += commaDelimiter;
                }
                if (DmShared.xNull(rsTemp.getString("isQuotationMandatory")).equalsIgnoreCase("1")) {

                    lineData += "\"" + "Yes" + "\"";
                    lineData += commaDelimiter;

                } else {
                    lineData += "\"" + "No" + "\"";
                    lineData += commaDelimiter;
                }

                handleFile.writeLine(lineData);
            }
            psTemp.close();
            rsTemp.close();
            handleFile.close();
            dmrTemp.setData(keyField);
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
                if (handleFile != null) {
                    handleFile.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private String getItemVType(Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, vType = "";
        try {
            sqlQuery = "SELECT vt.v_Type  FROM voucher_type vt WHERE  vt.category = '" + vCat + " ' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
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
        return vType;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLabRequired(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (LabRequired labRequired : LabRequired.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(String.valueOf(labRequired.getId())));
                listNode.setSharedKeyFieldName(DmShared.xNull(labRequired.getValue()));
                lstTemp.add(listNode);
            }
            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHImpurity(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSQL = " Select im.code, im.name "
                    + " from impurityMast im "
                    + " where im.nature = :nature "
                    + " And im.isActive = 1 ";
            sqlWithParam.setSQL(strSQL);
            sqlWithParam.setString("nature", DmShared.iNull(STEnum.ImpurityNature.QC.getId()).toString());
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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

    private void saveQCParameter(ItemMaster_Mdl itemMasterMdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        boolean blnAdd = false;
        String sqlQuery, itemImpurityCode = "";
        ResultSet rsTemp = null;

        try {

            //================<< check itemImpurityCode >>==================
            sqlQuery = "select code "
                    + " From itemImpurityMast "
                    + " Where itemCode = :itemCode "
                    + " AND testTemplateCode IS NULL ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemMasterMdl.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                itemImpurityCode = (DmShared.xNull(rsTemp.getString("code")));
            }
            rsTemp.close();
            psTemp.close();

            //=================<< Create code >>===================
            if (DmShared.xNull(itemImpurityCode).equalsIgnoreCase("")) {
                blnAdd = true;
                itemImpurityCode = (DmServer.getSerialNoInteger(this, "itemImpurityMast", cnTemp));
            }

            if (DmShared.xNull(itemImpurityCode).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid QC parameter key.");
            }

            if (blnAdd) {
                sqlQuery = "Insert Into itemImpurityMast(code , itemCode, preparedBY, preparedDate ) "
                        + " Values (:code, :itemCode, :preparedBY, :preparedDate) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(itemImpurityCode));
                psTemp.setString("itemCode", DmShared.foriegnKey(itemMasterMdl.getSharedKeyFieldCode()));
                psTemp.setString("preparedBY", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
            } else {
                sqlQuery = " Update itemImpurityMast Set "
                        + " u_Name = :userName, "
                        + " u_EntDt = :u_EntDt "
                        + " Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(itemImpurityCode));
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from itemImpurityDetail >>====================================*/
            sqlQuery = "Delete From itemImpurityDetail Where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(itemImpurityCode));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into itemImpurityDetail >>====================================*/
            List<GrdQcParameter_Mdl> listDetail = itemMasterMdl.getGrdQcParameter_Mdls();
            int vSNo = 0;
            for (GrdQcParameter_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    ++vSNo;
                    detail_Mdl.setV_SNo(String.valueOf(vSNo));
                    sqlQuery = "INSERT INTO itemImpurityDetail ( code, sNo, impurityCode ) "
                            + " Values ( :code, :sNo, :impurityCode ) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.foriegnKey(itemImpurityCode));
                    psTemp.setString("sNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                    psTemp.setString("impurityCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void deleteItemImpurityInfo(String strKey, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", itemImpurityCode = "";
        ResultSet rsTemp = null;

        try {
            //================<< check itemImpurityCode >>==================
            sqlQuery = "select code "
                    + " From itemImpurityMast "
                    + " Where itemCode = :itemCode "
                    + " AND testTemplateCode IS NULL ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                itemImpurityCode = (DmShared.xNull(rsTemp.getString("code")));
            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(itemImpurityCode).trim().equalsIgnoreCase("")) {
                //===============<< Delete from itemImpurityDetail >>=============
                sqlQuery = "Delete From itemImpurityDetail Where code = :code";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(itemImpurityCode).toString());
                psTemp.executeUpdate();
                psTemp.close();

                //===============<< Delete from itemImpurityMast >>=============
                sqlQuery = "Delete From itemImpurityMast Where itemCode = :itemCode AND code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("itemCode", DmShared.iNull(strKey).toString());
                psTemp.setString("code", DmShared.iNull(itemImpurityCode).toString());
                psTemp.executeUpdate();
                psTemp.close();

            }

        } catch (Exception e) {
            throw e;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Active">
    @Override
    public DmResult<String> setActive(String activeBy, boolean isActive, String code) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");
        String[] userName;
        try {

            userName = activeBy.split("-");
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (isActive) {
                STApprovals.itemActivation(WalkThroughEnum.ACCEPT, code, userName[0], cnTemp);
            } else {
                STApprovals.itemActivation(WalkThroughEnum.DECLINE, code, userName[0], cnTemp);
            }
            dmrTemp.setData(code);

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

    private WalkThroughDefault_Mdl getWalkThroughFor(String vType, Connection cnTemp) throws Exception {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode("");
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(vType);

        return walkThroughDefault;
    }

    @Override
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    STApprovals.itemActivation(walkThroughEnum, keyCode, userName, cnTemp);
                }
            }, this);

            dmrTemp.setData("");
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
            dmrTemp.setStatus(false);
        }
        return dmrTemp;
    }

    private String getUserWithDateTime(String userName, Timestamp entryDateTime) {
        String rtnValue = "";
        Calendar cal;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy - HH:mm");
        try {
            if (!DmShared.xNull(userName).equalsIgnoreCase("") && entryDateTime != null) {
                cal = Calendar.getInstance();
                cal.setTime(new Date(entryDateTime.getTime()));
                rtnValue = userName + " - " + sdf.format(cal.getTime());
            }
        } catch (Exception e) {
        }
        return rtnValue;
    }
//</editor-fold>

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGSTNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (GSTNature nature : GSTNature.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(nature.getId());
                listNode.setSharedKeyFieldName(nature.getValue());
                lstTemp.add(listNode);
            }
            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHITSBasedOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Fixed");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Rate");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT code, name, printSrNo FROM gstType\n"
                    + "Where isActive = '1' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSubCode(String subgroupNature, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String Condition = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(subgroupNature).trim().equalsIgnoreCase("")) {
                Condition = " and sg.nature = :nature ";
            }

            sqlWithParam.setSQL(" SELECT sg.subCode, sg.name, sg.manualCode "
                    + " FROM subGroup sg where sg.isActive = 1 "
                    + Condition);
            if (!DmShared.xNull(subgroupNature).trim().equalsIgnoreCase("")) {
                sqlWithParam.setString("nature", DmShared.xNull(DmServer.getCodeViaManualCode(subgroupNature, "manualCode", "subGroupNature", "code", cnTemp)));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualCode")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<TxtHManufacturer_Mdl>> getTxtHManufacturer(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHManufacturer_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHManufacturer_Mdl> lstTemp = new ArrayList<>();
        TxtHManufacturer_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String filterNature = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            filterNature = DmServer.getCodeViaManualCode("MF", "manualCode", "subgroupNature", "code", cnTemp);

            sqlWithParam.setSQL(" select sg.subCode,sg.name,ct.cityName,sg.manualCode,concat(add1,add2) as address,sg.filterNature"
                    + "  from subGroup as sg "
                    + " left join city as ct on sg.cityCode = ct.cityCode where filterNature = :filterNature  ");
            sqlWithParam.setString("filterNature", filterNature);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHManufacturer_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setAddress(DmShared.xNull(rsTemp.getString("address")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<TxtHDivision_Mdl>> getTxtHDivision(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHDivision_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHDivision_Mdl> lstTemp = new ArrayList<>();
        TxtHDivision_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String filterNature = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            filterNature = DmServer.getCodeViaManualCode("DV", "manualCode", "subgroupNature", "code", cnTemp);

            sqlWithParam.setSQL(" select sg.subCode,sg.name,ct.cityName,sg.manualCode,concat(add1,add2) as address,sg.filterNature  from subGroup as sg "
                    + " left join city as ct on sg.cityCode = ct.cityCode where filterNature = :filterNature ");
            sqlWithParam.setString("filterNature", filterNature);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHDivision_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setAddress(DmShared.xNull(rsTemp.getString("address")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<TxtHItemMake_Mdl>> getTxtHItemMake(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemMake_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemMake_Mdl> lstTemp = new ArrayList<>();
        TxtHItemMake_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT imm.code, imm.companyName, imm.shortName "
                    + " FROM itemMakeMast imm"
                    + " WHERE filterItemType = :filterItemType and entryType=:entryType");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("entryType", DmShared.iNull(STEnum.ITEM_MAKE_ENTRY_TYPE.MAKE.getId()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemMake_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<TxtHMarketingCo_Mdl>> getTxtHMarketingCo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMarketingCo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMarketingCo_Mdl> lstTemp = new ArrayList<>();
        TxtHMarketingCo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT imm.code, imm.companyName, imm.shortName "
                    + " FROM itemMakeMast imm"
                    + " WHERE filterItemType = :filterItemType and entryType=:entryType");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("entryType", DmShared.iNull(STEnum.ITEM_MAKE_ENTRY_TYPE.MARKETING.getId()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMarketingCo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemSpeciality(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" select code,name from itemSpecialityMast ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResult<List<GrdRestrictedDept_Mdl>> getGroupRestrictedDepartment(String strKey) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        DmResult<List<GrdRestrictedDept_Mdl>> rstResult = new DmResult<>();

        List<GrdRestrictedDept_Mdl> listRestrictedDept = new ArrayList<>();
        GrdRestrictedDept_Mdl RestrictedDept;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "  select sNo,departmentCode,sg.name as departmentName "
                    + "     from itemGroupRestrictedtoDepartment as rd "
                    + "     LEFT JOIN  subGroup AS sg ON rd.departmentCode = sg.subCode "
                    + "     where code = :code ; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                RestrictedDept = new GrdRestrictedDept_Mdl();
                RestrictedDept.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("departmentCode")).toString());
                RestrictedDept.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("departmentName")).toString());
                RestrictedDept.setV_SNo(DmShared.iNull(rsTemp.getString("sNo")).toString());
                listRestrictedDept.add(RestrictedDept);
            }
            rstResult.setData(listRestrictedDept);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstResult.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstResult;
    }

    @Override
    public DmResultPageing<List<TxtHMaterialCategory_Mdl>> getTxtHMaterialCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMaterialCategory_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMaterialCategory_Mdl> lstTemp = new ArrayList<>();
        TxtHMaterialCategory_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select imc.code as categoryCode, imc.name as categoryName "
                    + " from materialCatMast imc"
                    + " where (filterItemType = :filterItemType OR imc.isCommon = 2) ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMaterialCategory_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("categoryName")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMaterialType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT code, name FROM MaterialTypeMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemModel(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT code, name FROM itemModelMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemStrength(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT code, name FROM itemStrengthMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl lstTemp = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get Enviro Data >>====================================*/
            sqlQuery = "SELECT e.accountsEditableInItemMaster,e.alphaCase_Medicine,e.name_Editable_Inventory_ItemMAst \n"
                    + "FROM enviro e\n"
                    + "WHERE e.site_Code = :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                lstTemp.setAccountsEditable(DmShared.iNull(rsTemp.getString("accountsEditableInItemMaster")).intValue() == 1);
                lstTemp.setMedicineNameAlphabetCase(DmShared.xNull(rsTemp.getString("alphaCase_Medicine")));
                lstTemp.setEdit_NameIn_Inventory_ItemMAst(DmShared.iNull(rsTemp.getString("name_Editable_Inventory_ItemMAst")).intValue() == 1);
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " SELECT  "
                    + "    env.isInventoryAccountingActive "
                    + "  FROM enviroCompany as env ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                lstTemp.setInventoryAccountingActive(DmShared.iNull(rsTemp.getString("isInventoryAccountingActive")).intValue() == 1);
            }
            psTemp.close();
            rsTemp.close();

            dmrTemp.setData(lstTemp);

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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" select code,name from costCenterMast where underCostCenter is null and isActive=1 ");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBudgerHead(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" select headCode as code,headName as name from budgetHeadMast");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
}
