package aarogya.pharmacy.medicineMaster.server;

import aarogya.pharmacy.medicineMaster.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import static dataman.dmToolkit.stock.server.Stock_Server.deleteUnusedItemBatches;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STShared;
import com.google.gson.Gson;
import dataman.dmBase.self.shared.DmEnum.*;
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
import dataman.dmBase.self.server.ZipUtils;
import java.io.File;
import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicineMaster_SrvImpl extends DmRSSPrivate implements MedicineMaster_Srv {

    private final String ALTERNATIVE_ITEM_NATURE = "AI";
    private final String SOUND_LOOK_ALIKE_NATURE = "SL";
    private String vCat = new ProjectVoucherCategory().PHARMACY_MEDICINE_MAST;

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
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT CODE FROM itemMast WHERE filterItemType = :filterItemType ORDER BY NAME");
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
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
    public DmResultPageing<List<String>> getSuggestionMedicine(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResult<MedicineMaster_Mdl> getMoveRec(String strKey) {
        DmResult<MedicineMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            dmrTemp.setData(moveRec(strKey, cnTemp, false));
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
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

    private MedicineMaster_Mdl moveRec(String strKey, Connection cnTemp, boolean isExport) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        MedicineMaster_Mdl mrTemp = new MedicineMaster_Mdl();
        List<GrdMedicineDepartmentInfo_Mdl> listMedicineDepartmentInfo = new ArrayList<>();
        GrdMedicineDepartmentInfo_Mdl nodeMedicineDepartmentInfo;

        List<GrdAlternativeItems_Mdl> listAlternativeItems = new ArrayList<>();
        GrdAlternativeItems_Mdl alternativeItem;

        List<GrdComposition_Mdl> listCompositionDetails = new ArrayList<>();
        GrdComposition_Mdl compositionDetails;

        List<GrdSoundLookAlike_Mdl> listSoundLookAlike = new ArrayList<>();
        GrdSoundLookAlike_Mdl SoundLookAlike;

        List<GrdRestrictedDept_Mdl> listRestrictedDept = new ArrayList<>();
        GrdRestrictedDept_Mdl RestrictedDept;

        List<GrdApprovedVendors_Mdl> listApprovedVendors = new ArrayList<>();
        GrdApprovedVendors_Mdl ApprovedVendors;

        List<GrdSchemeDetails_Mdl> listSchemeDetails = new ArrayList<>();
        GrdSchemeDetails_Mdl schemeDetails;

        try {
            sqlQuery = " SELECT itsp.totalTaxPercent as purchaseTax, itss.totalTaxPercent as salesTax, "
                    + "          im.code, im.name, im.itemGroup AS groupCode, ig.name AS groupName \n"
                    + "        , im.itemType AS typeCode, it.name AS typeName, im.sku AS skuCode \n"
                    + "        , um.unit AS sku, im.itemMakeCode AS itemCompany, comp.companyName \n"
                    + "        , comp.shortName as companyShortName, im.isActive, im.isBestBefore \n"
                    + "        , im.isLifeSaving, im.isHighValue, im.isFlowThrough, im.isNarcotics \n"
                    + "        , im.isPOMandatory , im.isIndentMandatory , im.isQuotationMandatory, im.allowMRPDiscPattern  \n"
                    + "        , im.isBarcodeActive , im.isConsignment, im.isReturnableItem \n"
                    + "        , im.isBilledItem, im.itemCategoryCode, ic.name AS categoryName \n"
                    + "        , im.restrictGroup, rgm.name AS resName, im.preparedBY, im.u_name  \n"
                    + "        , im.ManualCode, hs.name as hsnName, im.hsnCode, im.itemGSTNature \n"
                    + "        , im.purchaseUnit as purchaseUnitCode, im.saleUnit as saleUnitCode \n"
                    + "        , ump.unit as purchaseUnitName, ump.decimalPlaces as purchaseUnitDecimalPlaces \n"
                    + "        , ums.unit as saleUnitName, im.purchaseUnitConversionRate, im.saleUnitConversionRate \n"
                    + "        , um.decimalPlaces, im.allowNegativeStock, im.isSaleRateEditable, im.isIncludeFreeQtyInSaleRate \n"
                    + "        , im.displayName, im.manufacturerCode, sgm.name as manufacturerName, im.isDiscountAllow \n"
                    + "        , im.isCommon, instruction, regionalInstruction, sgd.name AS divisionName \n"
                    + "        , im.divisionCode, im.itempacking, im.mrp , im.saleRate , im.purchaseRate  \n"
                    + "        , im.schemeOnQty , im.schemeFreeQty , im.purchaseDiscountPer , im.saleDiscountPer  , im.priority \n"
                    + "        , iid.its_Code_Purchase,iid.its_Code_PurchaseRet,iid.its_Code_Sales,iid.its_Code_SalesRet \n"
                    + "        , itsp.name as itsPurchaseName,itspr.name as itsPurchaseReturnName,itss.name as itsSaleName,itssr.name as itsSaleRetName \n"
                    + "        , iid.purchaseAc,iid.purchaseReturnAc,iid.SalesAc,iid.SalesReturnAc \n"
                    + "        , sgp.name as purAcName,sgpr.name as purRetAcName,sgs.name as SalAcName,sgsr.name as salRetAcName \n"
                    + "        , im.materialCategoryCode,im.model,md.name as modelName,im.strength,stm.name as strengthName,mcm.name as materialCategoryName, "
                    + "        im.purchaseAc AS purchaseAcCode ,im.purchaseReturnAc AS purchaseReturnAcCode ,im.salesAc as salesAcCode, im.salesReturnAc as salesReturnAcCode,\n"
                    + "        im.subCode as inventoryAc,sgpAc.name as purchaseAcName, sgprAc.name as purchaseReturnAcName,sgsAc.name as salesAcName, sgsrAc.name as salesReturnAcName,sgi.name as inventoryAcName, \n"
                    + "        im.grIrAc AS grIrAcCode ,im.consumptionAc AS consumptionAcCode ,im.cogsAc as cogsAcCode, im.pidAc as pidAcCode,im.inTransitAc as inTransitAcCode,im.marginRateFix, \n"
                    + "        sgGrIr.name as grIrAcName,sgCon.name as consumptionAcName, sgCog.name as cogsAcName,sgPid.name as pidAcName, sgIntr.name as inTransitAcName,im.taxPerc, msm.name as subCategoryName,msm.code  subCategoryCode \n"
                    + "  FROM itemMast AS im   \n"
                    + "  LEFT JOIN itemCat AS ic ON im.itemCategoryCode = ic.code   \n"
                    + "  LEFT JOIN materialCatMast AS mcm ON im.materialCategoryCode = mcm.code   \n"
                    + "  LEFT JOIN itemgroup AS ig ON im.itemGroup = ig.code   \n"
                    + "  LEFT JOIN itemmakemast AS comp ON im.itemMakeCode = comp.code   \n"
                    + "  LEFT JOIN itemtype AS it ON im.itemType = it.code   \n"
                    + "  LEFT JOIN restrictedgrpmast AS rgm ON im.restrictGroup = rgm.code   \n"
                    + "  LEFT JOIN unitmast AS um ON im.sku = um.code   \n"
                    + "	 LEFT JOIN unitmast AS ump ON im.purchaseUnit = ump.code  \n"
                    + "	 LEFT JOIN unitmast AS ums ON im.saleUnit = ums.code  \n"
                    + "  LEFT JOIN hsnMast AS hs ON im.hsnCode = hs.code   \n"
                    + "  LEFT JOIN subgroup AS sgm ON im.manufacturerCode = sgm.subCode  \n"
                    + "  LEFT JOIN subgroup AS sgd ON im.divisionCode = sgd.subCode  \n"
                    + "  LEFT JOIN itemItsDetail AS iid ON im.code = iid.Code  \n"
                    + "  LEFT JOIN its AS itsp ON iid.its_Code_Purchase = itsp.Code  \n"
                    + "  LEFT JOIN its AS itspr ON iid.its_Code_PurchaseRet = itspr.Code  \n"
                    + "  LEFT JOIN its AS itss ON iid.its_Code_Sales = itss.Code  \n"
                    + "  LEFT JOIN its AS itssr ON iid.its_Code_SalesRet = itssr.Code  \n"
                    + "  LEFT JOIN subGroup AS sgp ON iid.purchaseAc = sgp.subCode  \n"
                    + "  LEFT JOIN subGroup AS sgpr ON iid.purchaseReturnAc = sgpr.subCode  \n"
                    + "  LEFT JOIN subGroup AS sgs ON iid.SalesAc = sgs.subCode \n"
                    + "  LEFT JOIN subGroup AS sgsr ON iid.SalesReturnAc = sgsr.subCode \n"
                    + "        left join subgroup AS sgpac ON im.purchaseAc = sgpAc.subCode"
                    + "        left join subgroup AS sgprAc ON im.purchaseReturnAc = sgprAc.subCode"
                    + "        left join subgroup AS sgsAc ON im.salesAc = sgsAc.subCode"
                    + "        left join subgroup AS sgsrAc ON im.salesReturnAc = sgsrAc.subCode"
                    + "        left join subgroup AS sgi ON im.subCode = sgi.subCode"
                    + "        left join subgroup AS sgGrIr ON im.grIrAc = sgGrIr.subCode"
                    + "        left join subgroup AS sgCon ON im.consumptionAc = sgCon.subCode"
                    + "        left join subgroup AS sgCog ON im.cogsAc = sgCog.subCode"
                    + "        left join subgroup AS sgPid ON im.pidAc = sgPid.subCode"
                    + "        left join subgroup AS sgIntr ON im.inTransitAc = sgIntr.subCode"
                    + "  LEFT JOIN itemModelMast AS md ON im.model = md.code \n"
                    + "  LEFT JOIN itemStrengthMast AS stm ON im.strength = stm.code \n"
                    + "  Left join medicineSubCategoryMast msm on msm.code=im.subCategoryCode  "
                    + "  WHERE im.code = :code ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setStatusTag(DmShared.xNull(rsTemp.getString("isActive")));
                mrTemp.setStatusText(DmShared.vNull(rsTemp.getString("isActive")).doubleValue() == 0 ? "InActive"
                        : DmShared.vNull(rsTemp.getString("isActive")).doubleValue() == 1 ? "Active"
                        : DmShared.vNull(rsTemp.getString("isActive")).doubleValue() == 2 ? "Hold" : "");

                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                mrTemp.setCategoryTag(DmShared.xNull(rsTemp.getString("itemCategoryCode")));
                mrTemp.setCategoryText(DmShared.xNull(rsTemp.getString("categoryName")));
                mrTemp.setCompanyTag(DmShared.xNull(rsTemp.getString("itemCompany")));
                mrTemp.setCompanyText(DmShared.xNull(rsTemp.getString("companyName")));

                mrTemp.setMaterialCategoryTag(DmShared.xNull(rsTemp.getString("materialCategoryCode")));
                mrTemp.setMaterialCategoryText(DmShared.xNull(rsTemp.getString("materialCategoryName")));

                mrTemp.setModel(DmShared.xNull(rsTemp.getString("model")));
                mrTemp.setModelName(DmShared.xNull(rsTemp.getString("modelName")));
                mrTemp.setStrength(DmShared.xNull(rsTemp.getString("strength")));
                mrTemp.setStrengthName(DmShared.xNull(rsTemp.getString("strengthName")));

                mrTemp.setPriority(DmShared.iNull(rsTemp.getString("priority")).toString());

                mrTemp.setMedGroupTag(DmShared.xNull(rsTemp.getString("groupCode")));
                mrTemp.setMedGroupText(DmShared.xNull(rsTemp.getString("groupName")));
                mrTemp.setPackSize(DmShared.xNull(rsTemp.getString("itempacking")));
                mrTemp.setCompanyShortName(DmShared.xNull(rsTemp.getString("companyShortName")));
                mrTemp.setManufacturerCode(DmShared.xNull(rsTemp.getString("manufacturerCode")));
                mrTemp.setManufacturerName(DmShared.xNull(rsTemp.getString("manufacturerName")));
                mrTemp.setPurchaseDecimalPlaces(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).toString());
                mrTemp.setRestrictGroupTag(DmShared.xNull(rsTemp.getString("restrictGroup")));
                mrTemp.setRestrictGroupText(DmShared.xNull(rsTemp.getString("resName")));
                mrTemp.setPrvSkuTag(DmShared.xNull(rsTemp.getString("skuCode")));
                mrTemp.setSkuTag(DmShared.xNull(rsTemp.getString("skuCode")));
                mrTemp.setSkuText(DmShared.xNull(rsTemp.getString("sku")));
                //=============================<HSN ITS Detail>============================================//
                mrTemp.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                mrTemp.setHsnName(DmShared.xNull(rsTemp.getString("hsnName")));
                mrTemp.setIts_Code_Purchase(DmShared.xNull(rsTemp.getString("its_Code_Purchase")));
                mrTemp.setIts_Tax_Purchase(DmShared.xNull(rsTemp.getString("purchaseTax")));
                mrTemp.setIts_Code_PurchaseRet(DmShared.xNull(rsTemp.getString("its_Code_PurchaseRet")));
                mrTemp.setIts_Code_Sales(DmShared.xNull(rsTemp.getString("its_Code_Sales")));
                mrTemp.setIts_Tax_Sales(DmShared.xNull(rsTemp.getString("salesTax")));
                mrTemp.setIts_Code_SalesRet(DmShared.xNull(rsTemp.getString("its_Code_SalesRet")));
                mrTemp.setItsPurName(DmShared.xNull(rsTemp.getString("itsPurchaseName")));
                mrTemp.setItsPurRetName(DmShared.xNull(rsTemp.getString("itsPurchaseReturnName")));
                mrTemp.setItsSaleName(DmShared.xNull(rsTemp.getString("itsSaleName")));
                mrTemp.setItsSaleReturnName(DmShared.xNull(rsTemp.getString("itsSaleRetName")));
                mrTemp.setItsPurAcCode(DmShared.xNull(rsTemp.getString("purchaseAc")));
                mrTemp.setItsPurRetAcCode(DmShared.xNull(rsTemp.getString("purchaseReturnAc")));
                mrTemp.setItsSaleAcCode(DmShared.xNull(rsTemp.getString("SalesAc")));
                mrTemp.setItsSaleReturnAcCode(DmShared.xNull(rsTemp.getString("SalesReturnAc")));
                mrTemp.setItsPurAcName(DmShared.xNull(rsTemp.getString("purAcName")));
                mrTemp.setItsPurRetAcName(DmShared.xNull(rsTemp.getString("purRetAcName")));
                mrTemp.setItsSaleAcName(DmShared.xNull(rsTemp.getString("SalAcName")));
                mrTemp.setItsSaleReturnAcName(DmShared.xNull(rsTemp.getString("salRetAcName")));
                mrTemp.setMarginRateText(DmShared.xNull(rsTemp.getString("marginRateFix")));
                mrTemp.setBestBeforeTag(DmShared.xNull(rsTemp.getString("isBestBefore")));
                mrTemp.setBestBeforeText(DmShared.vNull(rsTemp.getString("isBestBefore")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setLifeSavingTag(DmShared.xNull(rsTemp.getString("isLifeSaving")));
                mrTemp.setLifeSavingText(DmShared.vNull(rsTemp.getString("isLifeSaving")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setHighValueTag(DmShared.xNull(rsTemp.getString("isHighValue")));
                mrTemp.setHighValueText(DmShared.vNull(rsTemp.getString("isHighValue")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setFlowThroughTag(DmShared.xNull(rsTemp.getString("isFlowThrough")));
                mrTemp.setFlowThroughText(DmShared.vNull(rsTemp.getString("isFlowThrough")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setNarcoticsTag(DmShared.xNull(rsTemp.getString("isNarcotics")));
                mrTemp.setNarcoticsText(DmShared.vNull(rsTemp.getString("isNarcotics")).doubleValue() == 1 ? "Yes" : "No");

                mrTemp.setPoReqTag(DmShared.xNull(rsTemp.getString("isPOMandatory")));
                mrTemp.setPoReqText(DmShared.vNull(rsTemp.getString("isPOMandatory")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setIndentReqTag(DmShared.xNull(rsTemp.getString("isIndentMandatory")));
                mrTemp.setIndentReqText(DmShared.vNull(rsTemp.getString("isIndentMandatory")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setAllowMRPDiscTag(DmShared.xNull(rsTemp.getString("allowMRPDiscPattern")));
                mrTemp.setAllowMRPDiscText(DmShared.vNull(rsTemp.getString("allowMRPDiscPattern")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setQuotationReqTag(DmShared.xNull(rsTemp.getString("isQuotationMandatory")));
                mrTemp.setQuotationReqText(DmShared.vNull(rsTemp.getString("isQuotationMandatory")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setConsignmentTag(DmShared.xNull(rsTemp.getString("isConsignment")));
                mrTemp.setConsignmentText(DmShared.vNull(rsTemp.getString("isConsignment")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setReturnableItemTag(DmShared.xNull(rsTemp.getString("isReturnableItem")));
                mrTemp.setReturnableItemText(DmShared.vNull(rsTemp.getString("isReturnableItem")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setBilledItemTag(DmShared.xNull(rsTemp.getString("isBilledItem")));
                mrTemp.setBilledItemText(DmShared.vNull(rsTemp.getString("isBilledItem")).doubleValue() == 1 ? "Yes" : "No");
                mrTemp.setIsIncludeFreeQtyInSaleRateCode(DmShared.xNull(rsTemp.getString("isIncludeFreeQtyInSaleRate")));
                if (DmShared.xNull(rsTemp.getString("isIncludeFreeQtyInSaleRate")).equalsIgnoreCase("1")) {
                    mrTemp.setIsIncludeFreeQtyInSaleRateName("Yes");
                } else {
                    mrTemp.setIsIncludeFreeQtyInSaleRateName("No");
                }
                mrTemp.setIsDiscountAllowTag(DmShared.xNull(rsTemp.getString("isDiscountAllow")));
                if (DmShared.xNull(rsTemp.getString("isDiscountAllow")).equalsIgnoreCase("1")) {
                    mrTemp.setIsDiscountAllowText("Yes");
                } else {
                    mrTemp.setIsDiscountAllowText("No");
                }
                mrTemp.setIsCommonItemTag(DmShared.xNull(rsTemp.getString("isCommon")));
                if (DmShared.xNull(rsTemp.getString("isCommon")).equalsIgnoreCase("1")) {
                    mrTemp.setIsCommonItemText("Yes");
                } else {
                    mrTemp.setIsCommonItemText("No");
                }

                mrTemp.setGstNatureTag(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                if (GSTNature.getValueOf(DmShared.xNull(rsTemp.getString("itemGSTNature"))) != null) {
                    mrTemp.setGstNatureText(GSTNature.getValueOf(DmShared.xNull(rsTemp.getString("itemGSTNature"))).getValue());
                }

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

                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
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
                mrTemp.setInstruction(DmShared.xNull(rsTemp.getString("instruction")));
                mrTemp.setRegionalInstruction(DmShared.xNull(rsTemp.getString("regionalInstruction")));
                mrTemp.setTaxPerc(DmShared.xNull(rsTemp.getString("taxPerc")));

                if (!isExport) {
                    mrTemp.setDivisionTag(DmShared.xNull(rsTemp.getString("divisionCode")));
                    mrTemp.setDivisionText(DmShared.xNull(rsTemp.getString("divisionName")));
                    mrTemp.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                    mrTemp.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                    mrTemp.setPurchaseRate(DmServer.vNull(rsTemp.getString("purchaseRate"), "0.00"));
                    mrTemp.setPurchaseDisPer(DmServer.vNull(rsTemp.getString("purchaseDiscountPer"), "0.00"));
                    mrTemp.setSaleDisPer(DmServer.vNull(rsTemp.getString("saleDiscountPer"), "0.00"));
                    mrTemp.setSchemeOnQty(DmServer.vNull(rsTemp.getString("schemeOnQty"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).intValue())));
                    mrTemp.setSchemeFreeQty(DmServer.vNull(rsTemp.getString("schemeFreeQty"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).intValue())));

                    mrTemp.setAllowNegetiveStockCode(DmShared.xNull(rsTemp.getString("allowNegativeStock")));
                    if (DmShared.xNull(rsTemp.getString("allowNegativeStock")).equalsIgnoreCase("1")) {
                        mrTemp.setAllowNegetiveStockName("Yes");
                    } else {
                        mrTemp.setAllowNegetiveStockName("No");
                    }
                    mrTemp.setIsSaleRateEditableCode(DmShared.xNull(rsTemp.getString("isSaleRateEditable")));
                    if (DmShared.xNull(rsTemp.getString("isSaleRateEditable")).equalsIgnoreCase("1")) {
                        mrTemp.setIsSaleRateEditableName("Yes");
                    } else {
                        mrTemp.setIsSaleRateEditableName("No");
                    }

                    mrTemp.setBarcodeActiveTag(DmShared.xNull(rsTemp.getString("isBarcodeActive")));
                    mrTemp.setBarcodeActiveText(DmShared.vNull(rsTemp.getString("isBarcodeActive")).doubleValue() == 1 ? "Yes" : "No");

                    mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBY")));
                    mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
                } else {
                    mrTemp.setAllowNegetiveStockCode("0");
                    mrTemp.setAllowNegetiveStockName("No");

                    mrTemp.setIsSaleRateEditableCode("0");
                    mrTemp.setIsSaleRateEditableName("No");

                    mrTemp.setBarcodeActiveTag("0");
                    mrTemp.setBarcodeActiveText("No");

                }

                mrTemp.setSubCategoryCode(DmShared.xNull(rsTemp.getString("subCategoryCode")));
                mrTemp.setSubCategoryName(DmShared.xNull(rsTemp.getString("subCategoryName")));

            }
            rsTemp.close();
            psTemp.close();

            //==========================Composition grid=======================
            sqlQuery = "  select "
                    + "       mc.sNo "
                    + "     , mc.compositionCode "
                    + "     , mcm.name AS compositionName"
                    + "     , mc.power "
                    + "     , mc.isBase "
                    + "     , mc.unitCode "
                    + "     , um.unit as unitText "
                    + "     , im.name as itemName "
                    + " from medicineComposition as mc"
                    + " LEFT JOIN itemMast as im on mc.code = im.code  "
                    + " LEFT JOIN unitmast AS um ON mc.unitCode = um.code  "
                    + " LEFT JOIN medicinecompomast AS mcm ON mc.compositionCode = mcm.code  "
                    + " where mc.code = :code order by sNo ; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                compositionDetails = new GrdComposition_Mdl();
                compositionDetails.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("compositionCode")).toString());
                compositionDetails.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("compositionName")));
                compositionDetails.setPower(DmServer.vNull(rsTemp.getString("power"), "0.00"));
                compositionDetails.setBaseTag(DmShared.iNull(rsTemp.getString("isBase")).toString());
                compositionDetails.setBaseText(DmShared.iNull(rsTemp.getString("isBase")).intValue() == 1 ? "Yes" : "No");
                compositionDetails.setUnitTag(DmShared.iNull(rsTemp.getString("unitCode")).toString());
                compositionDetails.setUnitText(DmShared.xNull(rsTemp.getString("unitText")));
                compositionDetails.setV_SNo(DmShared.iNull(rsTemp.getString("sNo")).toString());
                listCompositionDetails.add(compositionDetails);

            }
            mrTemp.getGrdComposition_Mdl().addAll(listCompositionDetails);
            rsTemp.close();
            psTemp.close();

            if (!isExport) {
                //==================== Medicine Details Grid =====================================
                sqlQuery = "SELECT  mdi.itemCode, im.name AS itemName,mdi.minStock,mdi.maxStock,mdi.reOrder,mdi.binName, "
                        + "   mdi.departmentCode,sg.name AS departmentName ,mdi.site_Code,sm.name AS siteName "
                        + "   FROM itemDepartmentInfo AS mdi "
                        + "   LEFT JOIN subGroup AS sg ON mdi.departmentCode=sg.subCode "
                        + "   LEFT JOIN itemMast AS im ON mdi.itemCode=im.code "
                        + "   LEFT JOIN siteMast AS sm ON mdi.site_Code=sm.code "
                        + "   where mdi.itemCode =:itemCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("itemCode", strKey);
                rsTemp = psTemp.executeQuery();

                while (rsTemp.next()) {
                    nodeMedicineDepartmentInfo = new GrdMedicineDepartmentInfo_Mdl();
                    nodeMedicineDepartmentInfo.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                    nodeMedicineDepartmentInfo.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("departmentName")));
                    nodeMedicineDepartmentInfo.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                    nodeMedicineDepartmentInfo.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                    nodeMedicineDepartmentInfo.setMinStock(DmServer.vNull(rsTemp.getString("minStock"), "0"));
                    nodeMedicineDepartmentInfo.setMaxStock(DmServer.vNull(rsTemp.getString("maxStock"), "0"));
                    nodeMedicineDepartmentInfo.setReOrder(DmServer.vNull(rsTemp.getString("reOrder"), "0"));
                    nodeMedicineDepartmentInfo.setBinName(DmShared.xNull(rsTemp.getString("binName")));
                    listMedicineDepartmentInfo.add(nodeMedicineDepartmentInfo);
                }
                mrTemp.getGrdMed_Mdl().addAll(listMedicineDepartmentInfo);
                rsTemp.close();
                psTemp.close();

            } else {
                mrTemp.getGrdMed_Mdl().addAll(listMedicineDepartmentInfo);
            }

            if (!isExport) {
                //==========================Alternative Items grid=======================
                sqlQuery = "  select sNo,itemCode,im.name as itemName "
                        + " from alternativeItem as ai"
                        + " left join itemMast as im on ai.itemCode = im.code  "
                        + " where ai.code = :code and nature = :nature order by sNo ; ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(strKey).toString());
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
            } else {
                mrTemp.getGrdAlternativeItems_Mdls().addAll(listAlternativeItems);
            }

            if (!isExport) {
                //==========================Sound Look Alike Items grid=======================
                sqlQuery = "  select sNo,itemCode,im.name as itemName "
                        + " from alternativeItem as ai"
                        + " left join itemMast as im on ai.itemCode = im.code  "
                        + " where ai.code = :code and nature = :nature order by sNo ";
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

            } else {
                mrTemp.getGrdSoundLookAlike_Mdls().addAll(listSoundLookAlike);
            }

            if (!isExport) {
                //========================== Restricted Department grid=======================
                sqlQuery = "  select sNo,departmentCode,sg.name as departmentName "
                        + " from itemRestrictedToDepartment as rd "
                        + "     LEFT JOIN  subGroup AS sg ON rd.departmentCode = sg.subCode "
                        + "     where code = :code order by sNo";
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

            } else {
                mrTemp.getGrdRestrictedDept_Mdls().addAll(listRestrictedDept);
            }

            if (!isExport) {
                //========================== Approved vendor grid=======================
                sqlQuery = " select av.sNo,av.approvedVendorCode,sg.name as approvedVendorsName "
                        + " from approvedVendors as av "
                        + "  LEFT JOIN  subGroup AS sg ON av.approvedVendorCode = sg.subCode  "
                        + "  where  av.code = :code order by sNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());

                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    ApprovedVendors = new GrdApprovedVendors_Mdl();
                    ApprovedVendors.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("approvedVendorCode")).toString());
                    ApprovedVendors.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("approvedVendorsName")).toString());
                    ApprovedVendors.setV_SNo(DmShared.iNull(rsTemp.getString("sNo")).toString());
                    listApprovedVendors.add(ApprovedVendors);

                }
                mrTemp.getGrdApprovedVendors_Mdls().addAll(listApprovedVendors);
                rsTemp.close();
                psTemp.close();
            } else {
                mrTemp.getGrdApprovedVendors_Mdls().addAll(listApprovedVendors);
            }

            if (!isExport) {
                sqlQuery = " SELECT "
                        + "      isd.code as itemCode "
                        + "    , im.name as itemName "
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
                        + " FROM  "
                        + " itemSaleSchemeDetail isd "
                        + " LEFT JOIN itemMast AS im ON im.code = isd.code "
                        + " LEFT JOIN itemMast AS imf ON imf.code = isd.freeItemCode "
                        + " LEFT JOIN unitmast AS um ON isd.itemUnitCode = um.code "
                        + " LEFT JOIN unitmast AS umf ON isd.freeItemUnitCode = umf.code "
                        + " WHERE isd.code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", strKey);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {

                    schemeDetails = new GrdSchemeDetails_Mdl();

                    schemeDetails.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("freeItemCode")));
                    schemeDetails.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("freeItemName")));
                    schemeDetails.setDescription(DmShared.xNull(rsTemp.getString("schemeDesc")));
                    schemeDetails.setSchemeOnQty(DmServer.vNull(rsTemp.getString("schemeOnQty")));
                    schemeDetails.setSchemeFreeQty(DmServer.vNull(rsTemp.getString("schemeFreeQty")));
                    schemeDetails.setFreeItemUnitCode(DmShared.xNull(rsTemp.getString("freeItemUnitCode")));
                    schemeDetails.setFreeItemUnitName(DmShared.xNull(rsTemp.getString("freeitemUnitName")));

                    listSchemeDetails.add(schemeDetails);
                }
                mrTemp.getLstSchemeDetails().addAll(listSchemeDetails);
                rsTemp.close();
                psTemp.close();
            } else {
                mrTemp.getLstSchemeDetails().addAll(listSchemeDetails);
            }
            //====================== END ==============================================

            if (!isExport) {
                mrTemp.setIsItemUsed(checkItem(mrTemp, cnTemp));
                //====================== ItemHaveNegativeStock ==============================================
                mrTemp.setIsNegativeStock(isItemHaveNegativeStock(DmShared.xNull(mrTemp.getSharedKeyFieldCode()), cnTemp));

            } else {
                mrTemp.setIsItemUsed("0");
                mrTemp.setIsNegativeStock(false);
            }
            ////////////////////////////////////////////////////////
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

        return mrTemp;
    }

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From itemDepartmentInfo Where itemCode=:itemCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From itemRestrictedToDepartment Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From approvedVendors Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From alternativeItem Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From medicineComposition Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From itemITSDetail Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From itemSiteList Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From itemSaleSchemeDetail Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //Delete Unused Item Batches
            deleteUnusedItemBatches("", strKey, cnTemp);

            sqlQuery = "Delete From itemmast Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
    public DmResult<String> saveRecord(MedicineMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false, isSiteListExists = false;
        CallableStatement csTemp = null;
        String sqlQuery, vType = "", sqlQuery1 = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            MedicineMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "itemMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (DmShared.xNull(checkItem(mrTemp, cnTemp)).equalsIgnoreCase("1")) {
                if (DmShared.iNull(mrTemp.getSkuTag()).intValue() != DmShared.iNull(mrTemp.getPrvSkuTag()).intValue()) {
                    dmrTemp.setErrorNumber(6000);
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
            psTemp.setString("Name", mrTemp.getSharedKeyFieldName());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("Medicine Name already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT vt.v_Type  FROM voucher_type vt WHERE  vt.category = '" + vCat + " ' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(mrTemp.getAllowNegetiveStockCode()).trim().equals("0")) {
                if (isItemHaveNegativeStock(DmShared.xNull(mrTemp.getSharedKeyFieldCode()), cnTemp)) {
                    throw new DmException(0, "Allow negative stock cannot be updated with </b>No</b> due to current stock is neagative.");
                }
            }

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
                    mrTemp.setManualCode(DmShared.xNull(rsTemp.getString(1)).toString().toUpperCase());
                }
                rsTemp.close();
                csTemp.close();

            }

            if (DmShared.xNull(mrTemp.getManualCode()).trim().equals("")) {
                throw new UnsupportedOperationException("Invalid Manual Code.");
            }

            sqlQuery = " Select Count(ManualCode) As Cnt From itemMast mm Where mm.ManualCode = :ManualCode And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("manualCode", mrTemp.getManualCode());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
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
//<editor-fold defaultstate="collapsed" desc="comment">
//            if (mrTemp.getGrdComposition_Mdl() != null) {
//                String compCsv = "";
//                for (GrdComposition_Mdl grdComposition : mrTemp.getGrdComposition_Mdl()) {
//                    if (DmShared.iNull(grdComposition.getSharedKeyFieldCode()).longValue() > 0) {
//                        if (DmShared.iNull(grdComposition.getRestrictGroupCode()).longValue() > 0) {
//                            sqlQuery = " SELECT "
//                                    + " count(*) as cnt "
//                                    + " FROM medicineCompoMast mcm "
//                                    + " WHERE mcm.code = :Code "
//                                    + " AND mcm.restrictGroupCode=:restrictGroupCode ";
//                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                            psTemp.setString("Code", grdComposition.getSharedKeyFieldCode());
//                            psTemp.setString("restrictGroupCode", grdComposition.getRestrictGroupCode());
//                            rsTemp = psTemp.executeQuery();
//                            if (rsTemp.next()) {
//                                if (rsTemp.getLong("cnt") > 0) {
//                                    compCsv += grdComposition.getSharedKeyFieldName() + " , ";
//                                }
//                            }
//                            rsTemp.close();
//                            psTemp.close();
//                        }
//                    }
//                }
//                if (!DmShared.xNull(compCsv).trim().equalsIgnoreCase("")) {
//                    throw new UnsupportedOperationException("Restricted Group changed for following compositions : " + compCsv);
//                }
//            }
//</editor-fold>
            if (blnAdd) {
                sqlQuery = "INSERT INTO itemmast (CODE, NAME, itemGroup, itemType,filterItemType, sku, "
                        + " preparedBY, u_EntDt, transferDt, itemMakeCode, isActive, itemCategoryCode,"
                        + " manualCode, hsnCode,isBestBefore,isLifeSaving,isHighValue,isFlowThrough,  "
                        + " isNarcotics,isConsignment,isReturnableItem,isBilledItem,propertyList,itemGSTNature,purchaseUnit,saleUnit,"
                        + " purchaseUnitConversionRate,saleUnitConversionRate,allowNegativeStock,displayName,isSaleRateEditable,isIncludeFreeQtyInSaleRate,"
                        + " isDiscountAllow,isCommon,instruction,regionalInstruction,itempacking,manufacturerCode,divisionCode"
                        + " , mrp , saleRate , purchaseRate , schemeOnQty , schemeFreeQty , isPOMandatory , isIndentMandatory "
                        + " , isQuotationMandatory , isBarcodeActive , purchaseDiscountPer , saleDiscountPer , priority , preparedDate "
                        + " ,materialCategoryCode,model,strength, allowMRPDiscPattern, "
                        + " purchaseAc,salesAc,subCode, purchaseReturnAc , salesReturnAc,\n"
                        + " grIrAc,ConsumptionAc,cogsAc, pidAc , inTransitAc , taxPerc,subCategoryCode, marginRateFix) "
                        + " VALUES (:code, :name, :itemGroup, :itemType,:filterItemType,:sku, "
                        + " :preparedBY, :uEntDt, NULL, :itemMakeCode, :isActive,  :itemCategoryCode,"
                        + " :manualCode,:hsnCode,:isBestBefore,:isLifeSaving,:isHighValue,:isFlowThrough, "
                        + " :isNarcotics,:isConsignment,:isReturnableItem,:isBilledItem,:propertyList,:itemGSTNature,:purchaseUnit,:saleUnit,"
                        + " :purchaseUnitConversionRate,:saleUnitConversionRate,:allowNegativeStock,:displayName,:isSaleRateEditable,"
                        + " :isIncludeFreeQtyInSaleRate,:isDiscountAllow,:isCommon,"
                        + " :instruction,:regionalInstruction,:itempacking,:manufacturerCode,:divisionCode"
                        + " , :mrp , :saleRate , :purchaseRate , :schemeOnQty , :schemeFreeQty , :isPOMandatory , :isIndentMandatory "
                        + " , :isQuotationMandatory , :isBarcodeActive , :purchaseDiscountPer , :saleDiscountPer , :priority , :uEntDt "
                        + " ,:materialCategoryCode,:model,:strength, :allowMRPDiscPattern, "
                        + " :purchaseAc,:salesAc,:subCode, :purchaseReturnAc,:salesReturnAc"
                        + " ,:grIrAc,:ConsumptionAc,:cogsAc, :pidAc , :inTransitAc , :taxPerc ,:subCategoryCode, :marginRateFix)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("itemGroup", DmShared.foriegnKey(mrTemp.getMedGroupTag()));
                psTemp.setString("manualCode", mrTemp.getManualCode());
                psTemp.setString("itemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
                psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp));
                psTemp.setString("sku", DmShared.foriegnKey(mrTemp.getSkuTag()));
                //psTemp.setString("isPOMandatory", "0");
                psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getCompanyTag()));
                psTemp.setString("isActive", DmShared.xNull(mrTemp.getStatusTag()));
                psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryTag()));
                psTemp.setString("hsnCode", DmShared.foriegnKey(mrTemp.getHsnCode()));
                psTemp.setString("isBestBefore", DmShared.xNull(mrTemp.getBestBeforeTag()));
                psTemp.setString("isLifeSaving", DmShared.xNull(mrTemp.getLifeSavingTag()));
                psTemp.setString("isHighValue", DmShared.xNull(mrTemp.getHighValueTag()));
                psTemp.setString("isFlowThrough", DmShared.xNull(mrTemp.getFlowThroughTag()));
                psTemp.setString("isNarcotics", DmShared.xNull(mrTemp.getNarcoticsTag()));
                psTemp.setString("isPOMandatory", DmShared.xNull(mrTemp.getPoReqTag()));
                psTemp.setString("isIndentMandatory", DmShared.xNull(mrTemp.getIndentReqTag()));
                psTemp.setString("allowMRPDiscPattern", DmShared.xNull(mrTemp.getAllowMRPDiscTag()));
                psTemp.setString("isQuotationMandatory", DmShared.xNull(mrTemp.getQuotationReqTag()));
                psTemp.setString("isBarcodeActive", DmShared.xNull(mrTemp.getBarcodeActiveTag()));
                psTemp.setString("isConsignment", DmShared.xNull(mrTemp.getConsignmentTag()));
                psTemp.setString("isReturnableItem", DmShared.xNull(mrTemp.getReturnableItemTag()));
                psTemp.setString("isBilledItem", DmShared.xNull(mrTemp.getBilledItemTag()));
                psTemp.setString("propertyList", MedicineMaster_Mdl.MedicineProperties.PROPERTIES.getProperties());
                psTemp.setString("preparedBY", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNatureTag()));
                psTemp.setString("isDiscountAllow", DmShared.xNull(mrTemp.getIsDiscountAllowTag()));
                psTemp.setString("isCommon", DmShared.xNull(mrTemp.getIsCommonItemTag()));
                psTemp.setString("purchaseUnit", DmShared.xNull(mrTemp.getPurchaseUnitSkuCode()));
                psTemp.setString("saleUnit", DmShared.xNull(mrTemp.getSaleUnitSkuCode()));
                psTemp.setString("purchaseUnitConversionRate", DmServer.vNull(mrTemp.getConversionRatePurchase()));
                psTemp.setString("saleUnitConversionRate", DmServer.vNull(mrTemp.getConversionRateSale()));
                psTemp.setString("allowNegativeStock", DmShared.xNull(mrTemp.getAllowNegetiveStockCode()));
                psTemp.setString("isSaleRateEditable", DmShared.xNull(mrTemp.getIsSaleRateEditableCode()));
                psTemp.setString("isIncludeFreeQtyInSaleRate", DmShared.xNull(mrTemp.getIsIncludeFreeQtyInSaleRateCode()));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("instruction", DmShared.xNull(mrTemp.getInstruction()));
                psTemp.setString("regionalInstruction", DmShared.xNull(mrTemp.getRegionalInstruction()));
                psTemp.setString("itempacking", DmShared.xNull(mrTemp.getPackSize()));
                psTemp.setString("manufacturerCode", DmShared.foriegnKey(mrTemp.getManufacturerCode()));
                psTemp.setString("divisionCode", DmShared.foriegnKey(mrTemp.getDivisionTag()));
                psTemp.setString("mrp", DmServer.vNull(mrTemp.getMrp(), "0.00"));
                psTemp.setString("saleRate", DmServer.vNull(mrTemp.getSaleRate(), "0.00"));
                psTemp.setString("purchaseRate", DmServer.vNull(mrTemp.getPurchaseRate(), "0.00"));
                psTemp.setString("schemeFreeQty", DmServer.vNull(mrTemp.getSchemeFreeQty()));
                psTemp.setString("schemeOnQty", DmServer.vNull(mrTemp.getSchemeOnQty()));
                psTemp.setString("purchaseDiscountPer", DmServer.vNull(mrTemp.getPurchaseDisPer(), "0.00"));
                psTemp.setString("saleDiscountPer", DmServer.vNull(mrTemp.getSaleDisPer(), "0.00"));
                psTemp.setString("taxPerc", DmServer.vNull(mrTemp.getTaxPerc(), "0.00"));
                psTemp.setString("priority", DmShared.iNull(mrTemp.getPriority()).toString());
                psTemp.setString("materialCategoryCode", DmShared.foriegnKey(mrTemp.getMaterialCategoryTag()));
                psTemp.setString("model", DmShared.foriegnKey(mrTemp.getModel()));
                psTemp.setString("strength", DmShared.foriegnKey(mrTemp.getStrength()));
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
                psTemp.setString("subCategoryCode", DmShared.foriegnKey(mrTemp.getSubCategoryCode()));
                psTemp.setString("marginRateFix", DmShared.xNull(mrTemp.getMarginRateTag()));
            } else {
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
                        + "  u_Name = :userName, "
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
                        + "  isPOMandatory = :isPOMandatory, "
                        + "  isIndentMandatory = :isIndentMandatory, "
                        + "  isQuotationMandatory = :isQuotationMandatory, "
                        + "  isBarcodeActive = :isBarcodeActive, "
                        + "  isConsignment = :isConsignment, "
                        + "  isReturnableItem = :isReturnableItem, "
                        + "  isBilledItem = :isBilledItem, "
                        + "  propertyList = :propertyList,"
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
                        + "  priority = :priority, "
                        + "  itempacking=:itempacking, "
                        + "  materialCategoryCode = :materialCategoryCode, "
                        + "  model = :model, "
                        + "  strength=:strength, "
                        + "  purchaseAc = :purchaseAc, "
                        + "  salesAc = :salesAc, "
                        + "  subCode = :subCode, "
                        + "  purchaseReturnAc= :purchaseReturnAc, "
                        + "  salesReturnAc = :salesReturnAc, "
                        + "  grIrAc = :grIrAc, "
                        + "  ConsumptionAc = :ConsumptionAc, "
                        + "  cogsAc = :cogsAc, "
                        + "  pidAc = :pidAc, "
                        + "  inTransitAc = :inTransitAc ,"
                        + "  taxPerc = :taxPerc,"
                        + "  subCategoryCode = :subCategoryCode,"
                        + "  allowMRPDiscPattern = :allowMRPDiscPattern,"
                        + "  marginRateFix = :marginRateFix  "
                        + " WHERE CODE = :CODE ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("itemGroup", DmShared.foriegnKey(mrTemp.getMedGroupTag()));
                psTemp.setString("manualCode", mrTemp.getManualCode());
                psTemp.setString("itemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
                psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp));
                psTemp.setString("sku", DmShared.foriegnKey(mrTemp.getSkuTag()));
                psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getCompanyTag()));
                psTemp.setString("isActive", DmShared.xNull(mrTemp.getStatusTag()));
                //psTemp.setString("isPOMandatory", "0");
                psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryTag()));
                psTemp.setString("hsnCode", DmShared.foriegnKey(mrTemp.getHsnCode()));
                psTemp.setString("isBestBefore", DmShared.xNull(mrTemp.getBestBeforeTag()));
                psTemp.setString("isLifeSaving", DmShared.xNull(mrTemp.getLifeSavingTag()));
                psTemp.setString("isHighValue", DmShared.xNull(mrTemp.getHighValueTag()));
                psTemp.setString("isFlowThrough", DmShared.xNull(mrTemp.getFlowThroughTag()));
                psTemp.setString("isNarcotics", DmShared.xNull(mrTemp.getNarcoticsTag()));
                psTemp.setString("isPOMandatory", DmShared.xNull(mrTemp.getPoReqTag()));
                psTemp.setString("isIndentMandatory", DmShared.xNull(mrTemp.getIndentReqTag()));
                psTemp.setString("isQuotationMandatory", DmShared.xNull(mrTemp.getQuotationReqTag()));
                psTemp.setString("isBarcodeActive", DmShared.xNull(mrTemp.getBarcodeActiveTag()));
                psTemp.setString("isConsignment", DmShared.xNull(mrTemp.getConsignmentTag()));
                psTemp.setString("isReturnableItem", DmShared.xNull(mrTemp.getReturnableItemTag()));
                psTemp.setString("isBilledItem", DmShared.xNull(mrTemp.getBilledItemTag()));
                psTemp.setString("propertyList", MedicineMaster_Mdl.MedicineProperties.PROPERTIES.getProperties());
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNatureTag()));
                psTemp.setString("isDiscountAllow", DmShared.xNull(mrTemp.getIsDiscountAllowTag()));
                psTemp.setString("isCommon", DmShared.xNull(mrTemp.getIsCommonItemTag()));
                psTemp.setString("purchaseUnit", DmShared.xNull(mrTemp.getPurchaseUnitSkuCode()));
                psTemp.setString("saleUnit", DmShared.xNull(mrTemp.getSaleUnitSkuCode()));
                psTemp.setString("purchaseUnitConversionRate", DmServer.vNull(mrTemp.getConversionRatePurchase()));
                psTemp.setString("saleUnitConversionRate", DmServer.vNull(mrTemp.getConversionRateSale()));
                psTemp.setString("allowNegativeStock", DmShared.xNull(mrTemp.getAllowNegetiveStockCode()));
                psTemp.setString("isSaleRateEditable", DmShared.xNull(mrTemp.getIsSaleRateEditableCode()));
                psTemp.setString("isIncludeFreeQtyInSaleRate", DmShared.xNull(mrTemp.getIsIncludeFreeQtyInSaleRateCode()));
                psTemp.setString("instruction", DmShared.xNull(mrTemp.getInstruction()));
                psTemp.setString("regionalInstruction", DmShared.xNull(mrTemp.getRegionalInstruction()));
                psTemp.setString("itempacking", DmShared.xNull(mrTemp.getPackSize()));
                psTemp.setString("divisionCode", DmShared.foriegnKey(mrTemp.getDivisionTag()));
                psTemp.setString("manufacturerCode", DmShared.foriegnKey(mrTemp.getManufacturerCode()));
                psTemp.setString("mrp", DmServer.vNull(mrTemp.getMrp(), "0.00"));
                psTemp.setString("saleRate", DmServer.vNull(mrTemp.getSaleRate(), "0.00"));
                psTemp.setString("purchaseRate", DmServer.vNull(mrTemp.getPurchaseRate(), "0.00"));
                psTemp.setString("schemeFreeQty", DmServer.vNull(mrTemp.getSchemeFreeQty()));
                psTemp.setString("schemeOnQty", DmServer.vNull(mrTemp.getSchemeOnQty()));
                psTemp.setString("purchaseDiscountPer", DmServer.vNull(mrTemp.getPurchaseDisPer(), "0.00"));
                psTemp.setString("saleDiscountPer", DmServer.vNull(mrTemp.getSaleDisPer(), "0.00"));
                psTemp.setString("taxPerc", DmServer.vNull(mrTemp.getTaxPerc(), "0.00"));
                psTemp.setString("priority", DmShared.iNull(mrTemp.getPriority()).toString());
                psTemp.setString("materialCategoryCode", DmShared.foriegnKey(mrTemp.getMaterialCategoryTag()));
                psTemp.setString("model", DmShared.foriegnKey(mrTemp.getModel()));
                psTemp.setString("strength", DmShared.foriegnKey(mrTemp.getStrength()));

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
                psTemp.setString("subCategoryCode", DmShared.foriegnKey(mrTemp.getSubCategoryCode()));
                psTemp.setString("allowMRPDiscPattern", DmShared.xNull(mrTemp.getAllowMRPDiscTag()));
                psTemp.setString("marginRateFix", DmShared.xNull(mrTemp.getMarginRateTag()));
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

            /*===========================< Update record of itemMast >===============================*/
            sqlQuery = "UPDATE itemMast "
                    + " SET itemMast.name = vItem.itemName "
                    + " FROM ( "
                    + " 	SELECT im.code as itemCode "
                    + " 	, upper(mcm.name+ '-'+im.displayName +case when isNUll(ism.name,'')<>''then '-' else '' end+isNull(ism.name,'') "
                    + " 	+ case when isNUll(imm.name,'')<>''then '-' else '' end  + isNUll(imm.name,'')) AS itemName "
                    + " 	FROM itemMast im  "
                    + " 	INNER JOIN materialCatMast mcm ON im.materialCategoryCode = mcm.code "
                    + " 	INNER JOIN itemModelMast imm ON imm.code = im.model "
                    + " 	INNER JOIN itemStrengthMast ism ON ism.code=im.strength "
                    + " 	WHERE im.code = :code "
                    + " 	) vItem "
                    + " WHERE itemMast.code = vItem.itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Update  im set "
                    + "         im.itemGSTNature = hm.itemGSTNature,"
                    + "         im.itsBasedOn = hm.itsBasedOn "
                    + "         from itemmast as im "
                    + "         left join hsnMast as hm on im.hsnCode = hm.code "
                    + "         where im.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*===========================< Update record of itemITS >===============================*/
            sqlQuery = " delete from itemITSDetail where code = :itemCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getHsnCode()).intValue() > 0) {
//                sqlQuery = "insert into itemITSDetail (code ,v_SNo,rateFrom,rateUpto,its_Code_Purchase,its_Code_PurchaseRet,"
//                        + "                  its_Code_Sales,its_Code_SalesRet,purchaseAc,purchaseReturnAc,salesAc,salesReturnAc) "
//                        + "         		(select :code ,hid.v_SNo,hid.rateFrom,hid.rateUpto,hid.its_Code_Purchase,hid.its_Code_PurchaseRet,"
//                        + "                 hid.its_Code_Sales,hid.its_Code_SalesRet,hid.purchaseAc,hid.purchaseReturnAc,hid.salesAc,hid.salesReturnAc "
//                        + "         		from hsnITSDetail as hid where hid.code = :hsnCode) ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
//                psTemp.setString("hsnCode", mrTemp.getHsnCode());

                sqlQuery = "insert into itemITSDetail (code ,v_SNo,its_Code_Purchase,its_Code_PurchaseRet,"
                        + "                  its_Code_Sales,its_Code_SalesRet,purchaseAc,purchaseReturnAc,salesAc,salesReturnAc) "
                        + "         		values(:code ,:v_SNo,:its_Code_Purchase,:its_Code_PurchaseRet,"
                        + "                 :its_Code_Sales,:its_Code_SalesRet,:purchaseAc,:purchaseReturnAc,:salesAc,:salesReturnAc) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("v_SNo", "1");
                psTemp.setString("its_Code_Purchase", DmShared.foriegnKey(mrTemp.getIts_Code_Purchase()));
                psTemp.setString("its_Code_PurchaseRet", DmShared.foriegnKey(mrTemp.getIts_Code_PurchaseRet()));
                psTemp.setString("its_Code_Sales", DmShared.foriegnKey(mrTemp.getIts_Code_Sales()));
                psTemp.setString("its_Code_SalesRet", DmShared.foriegnKey(mrTemp.getIts_Code_SalesRet()));
                psTemp.setString("purchaseAc", DmShared.foriegnKey(mrTemp.getItsPurAcCode()));
                psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(mrTemp.getItsPurRetAcCode()));
                psTemp.setString("salesAc", DmShared.foriegnKey(mrTemp.getItsSaleAcCode()));
                psTemp.setString("salesReturnAc", DmShared.foriegnKey(mrTemp.getItsSaleReturnAcCode()));
                psTemp.executeUpdate();
                psTemp.close();

            }

            //============================= Item Details Grid Entry =================================
            sqlQuery = " delete from itemDepartmentInfo where itemCode = :itemCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            List<GrdMedicineDepartmentInfo_Mdl> listMed = mrTemp.getGrdMed_Mdl();
            for (GrdMedicineDepartmentInfo_Mdl med_Mdl : listMed) {
                if ((!DmShared.xNull(med_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase(""))) {
                    sqlQuery = "insert into itemDepartmentInfo(departmentCode,itemCode, minStock, maxStock, reOrder, binName,site_Code)  "
                            + " VALUES (:departmentCode,:itemCode, :minStock, :maxStock, :reOrder,:binName, :siteCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("departmentCode", DmShared.foriegnKey(med_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("itemCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("minStock", DmServer.vNull(med_Mdl.getMinStock(), "0"));
                    psTemp.setString("maxStock", DmServer.vNull(med_Mdl.getMaxStock(), "0"));
                    psTemp.setString("reOrder", DmServer.vNull(med_Mdl.getReOrder(), "0"));
                    psTemp.setString("binName", DmShared.xNull(med_Mdl.getBinName()));
                    psTemp.setString("siteCode", DmShared.foriegnKey(med_Mdl.getSiteCode()));

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

            //=============================Composition Grid Entry =================================
            sqlQuery = " delete from medicineComposition where code = :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            int sNo2 = 0;
            if (mrTemp.getGrdComposition_Mdl() != null) {
                for (GrdComposition_Mdl grdComposition : mrTemp.getGrdComposition_Mdl()) {

                    if (DmShared.iNull(grdComposition.getSharedKeyFieldCode()).longValue() > 0) {
                        sqlQuery = "INSERT INTO medicineComposition (code, sNo, compositionCode, power, isBase, unitCode) "
                                + "VALUES (:code, :sNo, :compositionCode, :power,:isBase,:unitCode);";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setInt("sNo", ++sNo2);
                        psTemp.setString("compositionCode", DmShared.foriegnKey(grdComposition.getSharedKeyFieldCode()));
                        psTemp.setString("power", DmServer.vNull(grdComposition.getPower()));
                        psTemp.setString("isBase", grdComposition.getBaseTag());
                        psTemp.setString("unitCode", DmShared.foriegnKey(grdComposition.getUnitTag()));
                        psTemp.executeUpdate();
                        psTemp.close();

                        if (DmShared.iNull(grdComposition.getBaseTag()).longValue() == 1) {
                            sqlQuery = "UPDATE itemMast "
                                    + " set itemMast.compositionCode=:compositionCode "
                                    + " where itemMast.code=:code";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                            psTemp.setString("compositionCode", DmShared.foriegnKey(grdComposition.getSharedKeyFieldCode()));
                            psTemp.executeUpdate();
                            psTemp.close();

                        }
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
                    sNo++;
                    if (DmShared.vNull(grdRestrictedDept.getSharedKeyFieldCode()).doubleValue() != 0) {
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
                    sNo++;
                    if (DmShared.vNull(grdApprovedVendors.getSharedKeyFieldCode()).doubleValue() != 0) {
                        sqlQuery = "INSERT INTO approvedVendors (code, sNo, approvedVendorCode) "
                                + "VALUES (:code, :sNo, :approvedVendorCode);";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setInt("sNo", sNo);
                        psTemp.setString("approvedVendorCode", DmShared.foriegnKey(grdApprovedVendors.getSharedKeyFieldCode()));

                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }

            //<editor-fold defaultstate="collapsed" desc="SAVE IN ITEM SALE SCHEME DETAILS">
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
                    psTemp.setString("itemUnitCode", DmShared.foriegnKey(mrTemp.getSaleUnitSkuCode()).toString());
                    psTemp.setString("freeItemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("freeItemUnitCode", DmShared.foriegnKey(detail_Mdl.getFreeItemUnitCode()));
                    psTemp.setString("schemeOnQty", DmServer.vNull(detail_Mdl.getSchemeOnQty()));
                    psTemp.setString("schemeFreeQty", DmServer.vNull(detail_Mdl.getSchemeFreeQty()));
                    psTemp.setString("schemeDesc", DmShared.xNull(detail_Mdl.getDescription()));

                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            //<editor-fold defaultstate="collapsed" desc="Update Item Mast">
            sqlQuery = " UPDATE "
                    + " itemMast "
                    + " SET "
                    + " itemMast.restrictGroup = vMcm.restrictGroupCode "
                    + " FROM ( "
                    + "     SELECT "
                    + "       im.code AS itemCode "
                    + "     , MAX(mcm.restrictGroupCode) AS restrictGroupCode "
                    + "     FROM itemMast im "
                    + "     LEFT JOIN medicineComposition mc ON mc.code=im.code "
                    + "     LEFT JOIN medicineCompoMast mcm ON mcm.code=mc.compositionCode "
                    + "     LEFT JOIN restrictedGrpMast rgm ON rgm.code=mcm.restrictGroupCode "
                    + "     WHERE im.code=:code "
                    + "     GROUP BY im.code "
                    //                    + "     HAVING SUM(CASE WHEN ISNULL(mcm.restrictGroupCode,ISNULL(im.restrictGroup,0)) = ISNULL(im.restrictGroup,0) THEN 1 ELSE 0 END)  = COUNT(im.code) "
                    + "	) vMcm "
                    + " WHERE itemMast.code = vMcm.itemCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();
//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Chk of wrong restrict group update">
            sqlQuery = " SELECT "
                    + " COUNT(*) cnt "
                    + " , im.code "
                    + " , Max(im.name) "
                    + " FROM itemMast im "
                    + " LEFT JOIN medicineComposition mc ON mc.code=im.code "
                    + " LEFT JOIN medicineCompoMast mcm ON mcm.code=mc.compositionCode "
                    + " WHERE im.code = :code "
                    + " GROUP BY im.code "
                    + " HAVING SUM(CASE WHEN ISNULL(mcm.restrictGroupCode,ISNULL(im.restrictGroup,0)) = ISNULL(im.restrictGroup,0) THEN 1 ELSE 0 END)  <> COUNT(im.code)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Restricted Group changed for compositions in composition Master");
                }
            }
            rsTemp.close();
            psTemp.close();
//</editor-fold>
            //========================== END====================================================

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
            sqlWithParam.setSQL(" SELECT im.code, im.name , mg.name AS groupName, um.unit AS sku, comp.companyName, im.isActive, mc.name AS categoryName, "
                    + "		 rgm.name AS resName,it.code as typeCode,it.name as typename,im.itemPacking ,im.manualCode, "
                    + "		 case im.isActive when 0  then 'InActive' when 1 then 'Active' when 2 then 'Hold' end  as activeText,   "
                    + "          CAST(im.u_EntDt as date) as modifiedDate ,"
                    + "          compositionName = STUFF(("
                    + "			SELECT  concat('+ ', mcm.name,' ', mc.power, ' ', um.unit) "
                    + "				from medicineComposition mc"
                    + "				left join medicineCompoMast mcm on mc.compositionCode = mcm.code "
                    + "                         left join unitMast um on um.code = mc.unitCode "
                    + "				where mc.code = im.code "
                    + "				ORDER BY mcm.name FOR XML PATH(''), TYPE ).value('.[1]','nvarchar(max)'), 1,1,'')  "
                    + " , msm.name subCategoryName "
                    + "	       FROM itemmast AS im  "
                    + "        LEFT JOIN itemCat AS mc ON im.itemCategoryCode = mc.code  "
                    + "        LEFT JOIN itemGroup AS mg ON im.itemGroup = mg.code  "
                    + "        LEFT JOIN itemMakemast comp ON im.itemMakeCode = comp.code  "
                    + "        LEFT JOIN restrictedgrpmast rgm ON im.restrictGroup = rgm.code  "
                    + "        LEFT JOIN itemType AS it ON im.itemType = it.code   "
                    + "        LEFT JOIN unitmast AS um ON im.sku = um.code  "
                    + "        LEFT JOIN medicineSubCategoryMast AS msm ON msm.code = im.subCategoryCode  "
                    + "         WHERE  im.filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setActive(DmShared.xNull(rsTemp.getString("activeText")));
                listNode.setModifiedOn(DmShared.dNull(rsTemp.getDate("modifiedDate")));

                listNode.setCategory(DmShared.xNull(rsTemp.getString("categoryName")));
                listNode.setCompany(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setComposition(DmShared.xNull(rsTemp.getString("compositionName")));
                listNode.setMedGroup(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setRestrictGroup(DmShared.xNull(rsTemp.getString("resName")));
                listNode.setSku(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setMedicinePacking(DmShared.xNull(rsTemp.getString("itemPacking")));
                listNode.setMedicineTypeTag(DmShared.xNull(rsTemp.getString("typeCode")));
                listNode.setMedicineTypeText(DmShared.xNull(rsTemp.getString("typename")));
                listNode.setMedicineSubCategory(DmShared.xNull(rsTemp.getString("subCategoryName")));

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
    public DmResultPageing<List<Sku_Mdl>> getSKU(String sku, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Sku_Mdl>> rstTemp = new DmResultPageing<>();
        List<Sku_Mdl> lstTemp = new ArrayList<>();
        Sku_Mdl listNode;
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
            sqlWithParam.setSQL("select code, unit, decimalPlaces from unitmast " + condition);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Sku_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setAllowedDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).toString());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSubCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select code ,name from medicineSubCategoryMast ";
            sqlWithParam.setSQL(sqlQuery);

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
    public DmResultPageing<List<TxtHCategory_Mdl>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCategory_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCategory_Mdl> lstTemp = new ArrayList<>();
        TxtHCategory_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select imc.code as categoryCode, imc.name as categoryName "
                    + " from itemcat imc"
                    + " where (filterItemType = :filterItemType OR imc.isCommon = 1) ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
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
    public DmResultPageing<List<TxtHComposition_Mdl>> getTxtHComposition(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHComposition_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHComposition_Mdl> lstTemp = new ArrayList<>();
        TxtHComposition_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT "
                    + "     mcm.CODE "
                    + "   , mcm.NAME "
                    + "   , rgm.code as restrictGroupCode  "
                    + "   , rgm.name as restrictGroupName  "
                    + "     FROM medicinecompomast mcm "
                    + "     LEFT JOIN restrictedgrpmast rgm ON rgm.code=mcm.restrictGroupCode "
                    + "     WHERE mcm.isActive = 1 "
                    + "     AND ISNULL(mcm.nature,0) = 1");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHComposition_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setRestrictGroupCode(DmShared.xNull(rsTemp.getString("restrictGroupCode")));
                listNode.setRestrictGroupName(DmShared.xNull(rsTemp.getString("restrictGroupName")));
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
    public DmResultPageing<List<TxtHMedGrp_Mdl>> getTxtHMedGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedGrp_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedGrp_Mdl> lstTemp = new ArrayList<>();
        TxtHMedGrp_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT ig.code, ig.name, ig.saleDiscount, "
                    + " ig.purchaseAc AS purchaseAcCode ,ig.purchaseReturnAc AS purchaseReturnAcCode ,ig.salesAc as salesAcCode, ig.salesReturnAc as salesReturnAcCode,\n"
                    + " ig.subCode as inventoryAc,sgp.name as purchaseAcName, sgpr.name as purchaseReturnAcName,sgs.name as salesAcName, sgsr.name as salesReturnAcName,sgi.name as inventoryAcName, \n"
                    + " ig.grIrAc AS grIrAcCode ,ig.consumptionAc AS consumptionAcCode ,ig.cogsAc as cogsAcCode, ig.pidAc as pidAcCode,ig.inTransitAc as inTransitAcCode, \n"
                    + " sgGrIr.name as grIrAcName,sgCon.name as consumptionAcName, sgCog.name as cogsAcName,sgPid.name as pidAcName, sgIntr.name as inTransitAcName \n"
                    + " FROM itemGroup ig "
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
                    + " WHERE (ig.filterItemType = :filterItemType OR ig.isCommon = 1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedGrp_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setSaleDisPer(DmShared.iNull(rsTemp.getString("saleDiscount")).toString());

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
    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCompany_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCompany_Mdl> lstTemp = new ArrayList<>();
        TxtHCompany_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT CODE, companyName, shortName, purchaseDiscountPer FROM itemmakemast"
                    + "   WHERE filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCompany_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setPurchaseDisPer(DmServer.vNull(rsTemp.getString("purchaseDiscountPer"), "0.00"));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("TAB");
            listNode.setSharedKeyFieldName("TAB");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("INJ");
            listNode.setSharedKeyFieldName("INJ");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("UNG");
            listNode.setSharedKeyFieldName("UNG");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("GP");
            listNode.setSharedKeyFieldName("GP");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("SYP");
            listNode.setSharedKeyFieldName("SYP");
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
        String condition = "", sqlQuery = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " and dm.subCode in (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }

            sqlQuery = "select	dm.subCode as code, sg.name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sg.nature"
                    + "         where sgn.manualCode IN ('CM','PH') and sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
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
    public DmResultPageing<List<TxtHItemsName_Mdl>> getTxtHItemName(String itemCode, boolean isFromScheme, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemsName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemsName_Mdl> lstTemp = new ArrayList<>();
        TxtHItemsName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "", sqlQuery = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (isFromScheme) {
                condition = " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1) ";
            } else {
                condition = " WHERE im.filterItemType = :filterItemType ";
            }
            condition += " AND im.code <> :itemCode "
                    + "  AND im.isActive = 1  ";

            sqlQuery = " SELECT "
                    + "   im.code "
                    + " , im.name "
                    + " , im.allowNegativeStock "
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
//            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("itemCode", DmShared.iNull(itemCode).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemsName_Mdl();
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
                listNode.setCompositionCode(DmShared.xNull(rsTemp.getString("compositionCode")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCompositionName(DmShared.xNull(rsTemp.getString("compositionName")));
                listNode.setItemPacking(DmShared.xNull(rsTemp.getString("itemPacking")));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRestrictedDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "", sqlQuery = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " and dm.subCode in (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }

            sqlQuery = "select	dm.subCode as code, sg.name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sg.nature"
                    + "         where sgn.manualCode IN ('CM','PH') and sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
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

            sqlWithParam.setSQL(" select sg.subCode,sg.name,ct.cityName,sg.manualCode,concat(add1,add2) as address,sg.filterNature  "
                    + " from subGroup as sg "
                    + " left join city as ct on sg.cityCode = ct.cityCode "
                    + " where filterNature = :filterNature and sg.isActive=1 ");
            sqlWithParam.setString("filterNature", DmShared.xNull(filterNature));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHApprovedVendors_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setAddress(DmShared.xNull(rsTemp.getString("address")));
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

    private String checkItem(MedicineMaster_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String isItemUsed = "";
        String sqlQuery = "";
        try {
            sqlQuery = " SELECT COUNT(s.itemCode) AS cnt FROM stock s "
                    + " WHERE s.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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

            sqlQuery = " SELECT COUNT(ib.itemCode) AS cnt FROM itemBatch ib "
                    + " WHERE ib.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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

            sqlQuery = " SELECT COUNT(req.itemCode) AS cnt FROM requisitionDetail req "
                    + " WHERE req.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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

            sqlQuery = " SELECT COUNT(ind.itemCode) AS cnt FROM indentDetail ind "
                    + " WHERE ind.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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

            sqlQuery = " SELECT COUNT(ed.itemCode) AS cnt FROM enquiryDetail ed "
                    + " WHERE ed.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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

            sqlQuery = " SELECT COUNT(qd.itemCode) AS cnt FROM quotationDetail qd "
                    + " WHERE qd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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

            sqlQuery = " SELECT COUNT(pcd.itemCode) AS cnt FROM pChallanDetail pcd "
                    + " WHERE pcd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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

            sqlQuery = " SELECT COUNT(pbd.itemCode) AS cnt FROM pBillDetail pbd "
                    + " WHERE pbd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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

            sqlWithParam.setSQL("select code, unit, decimalPlaces from unitmast ");
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

            sqlQuery = "Select hm.code, hm.name, hm.hsn ,hm.itemGSTNature  "
                    + "      from hsnMast hm ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHHSN_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                listNode.setItemGSTNatureTag(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                if (!DmShared.xNull(rsTemp.getString("itemGSTNature")).equalsIgnoreCase("")) {
                    listNode.setItemGSTNatureText(GSTNature.getValueOf(DmShared.xNull(rsTemp.getString("itemGSTNature"))).getValue());
                }
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
    public DmResult<DefaultValue_Mdl> getDefaultVal() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT  "
                    + "    env.alphaCase_Medicine,env.name_Editable_Inventory_ItemMAst "
                    + "  FROM enviro as env "
                    + "  WHERE env.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setMedicineNameAlphabetCase(DmShared.xNull(rsTemp.getString("alphaCase_Medicine")));
                mrTemp.setEdit_NameIn_Inventory_ItemMAst(DmShared.iNull(rsTemp.getString("name_Editable_Inventory_ItemMAst")).intValue() == 1);
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " SELECT  "
                    + "    env.isInventoryAccountingActive "
                    + "  FROM enviroCompany as env ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setInventoryAccountingActive(DmShared.iNull(rsTemp.getString("isInventoryAccountingActive")).intValue() == 1);
            }
            psTemp.close();
            rsTemp.close();

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
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
    public DmResult<KeyFieldExtended> exportJson(String session) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sessionId = "", fileName = "", directoryPath, fullFilePath = "", serverFileName = "";
        Connection cnTemp = null;
        MedicineMaster_Mdl mrTemp = null;
        KeyFieldExtended field = null;
        File sFile;
        FileWriter writer;
        try {
            sessionId = session;
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }
            directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "MedicineMaster";
            sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT im.CODE FROM itemMast im WHERE im.filterItemType = :filterItemType AND isActive = 1 ORDER BY im.NAME");
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                mrTemp = moveRec(rsTemp.getString("code"), cnTemp, true);
                fileName = STShared.getValidFileName(mrTemp.getManualCode());
                if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                    throw new DmException(0, "File Name not given.");
                }
                serverFileName = fileName + ".json";
                fullFilePath = directoryPath + File.separatorChar + serverFileName;
                Gson gson = new Gson();
                String json = gson.toJson(mrTemp);
                writer = new FileWriter(fullFilePath);
                writer.write(json);
                writer.close();
            }
            rsTemp.close();
            psTemp.close();
            ZipUtils zipUtils = new ZipUtils(directoryPath, directoryPath + File.separatorChar + "MedicineMaster.zip");
            zipUtils.zipIt();
            field = new KeyFieldExtended();
            field.setSharedKeyFieldCode(directoryPath);
            field.setSharedKeyFieldName("MedicineMaster.zip");
            rstTemp.setStatus(true);
            rstTemp.setData(field);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;

    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHITS(String nature, DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select Code,Name,totalTaxPercent,nature  from its where nature = '" + nature + "'");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSharedPreparedBy(DmShared.vNull(rsTemp.getString("totalTaxPercent")).toString());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT dm.subcode AS code, dm.name FROM subgroup AS dm  ");
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

    @Override
    public DmResult<HSNDetail_Mdl> getHSNDetail(String hsnCode) {
        DmResult<HSNDetail_Mdl> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        HSNDetail_Mdl mrTemp = new HSNDetail_Mdl();
        HSNDetail_Mdl listNode;
        String sqlQuery;
        rstTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select itsp.totalTaxPercent purchaseTax,itssl.totalTaxPercent saleTax,hid.code, hid.v_SNo, hid.rateFrom, hid.rateUpto, hid.its_Code_Purchase, hid.its_Code_PurchaseRet \n"
                    + "      , hid.its_Code_Sales,hid.its_Code_SalesRet, hid.purchaseAc,hid.purchaseReturnAc, hid.salesAc \n"
                    + "      , hid.salesReturnAc, itsp.name as itsPurName,itspr.name as itsPurRetName, itssl.name as itsSalename \n"
                    + "      , itsslr.name as itsSaleRetName,sgp.name as purAcName \n"
                    + "      ,sgpr.name as purRetAcName, sgsl.name as saleAcName, sgslr.name as saleRetAcName \n"
                    + "      from hsnITSDetail hid  \n"
                    + "          Left Join its itsp on hid.its_Code_Purchase = itsp.Code \n"
                    + "          Left Join its itspr on hid.its_Code_PurchaseRet = itspr.Code \n"
                    + "          Left Join its itssl on hid.its_Code_Sales = itssl.Code \n"
                    + "          Left Join its itsslr on hid.its_Code_SalesRet = itsslr.Code \n"
                    + "          left join subgroup sgp on hid.purchaseAc=sgp.subCode \n"
                    + "          left join subgroup sgpr on hid.purchaseReturnAc=sgpr.subCode \n"
                    + "          left join subgroup sgsl on hid.salesAc=sgsl.subCode \n"
                    + "          left join subgroup sgslr on hid.salesReturnAc=sgslr.subCode \n"
                    + "          where hid.code=:code ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(hsnCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
//                listNode = new HSNDetail_Mdl();
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setIts_Code_Purchase(DmShared.xNull(rsTemp.getString("its_Code_Purchase")));
                mrTemp.setIts_Tax_Purchase(DmShared.vNull(rsTemp.getString("purchaseTax")).toString());
                mrTemp.setIts_Code_PurchaseRet(DmShared.xNull(rsTemp.getString("its_Code_PurchaseRet")));
                mrTemp.setIts_Code_Sales(DmShared.xNull(rsTemp.getString("its_Code_Sales")));
                mrTemp.setIts_Tax_Sales(DmShared.vNull(rsTemp.getString("saleTax")).toString());
                mrTemp.setIts_Code_SalesRet(DmShared.xNull(rsTemp.getString("its_Code_SalesRet")));
                mrTemp.setItsPurName(DmShared.xNull(rsTemp.getString("itsPurName")));
                mrTemp.setItsPurRetName(DmShared.xNull(rsTemp.getString("itsPurRetName")));
                mrTemp.setItsSaleName(DmShared.xNull(rsTemp.getString("itsSalename")));
                mrTemp.setItsSaleReturnName(DmShared.xNull(rsTemp.getString("itsSaleRetName")));
                mrTemp.setItsPurAcCode(DmShared.xNull(rsTemp.getString("purchaseAc")));
                mrTemp.setItsPurAcName(DmShared.xNull(rsTemp.getString("purAcName")));
                mrTemp.setItsPurRetAcCode(DmShared.xNull(rsTemp.getString("purchaseReturnAc")));
                mrTemp.setItsPurRetAcName(DmShared.xNull(rsTemp.getString("purRetAcName")));
                mrTemp.setItsSaleAcCode(DmShared.xNull(rsTemp.getString("salesAc")));
                mrTemp.setItsSaleAcName(DmShared.xNull(rsTemp.getString("saleAcName")));
                mrTemp.setItsSaleReturnAcCode(DmShared.xNull(rsTemp.getString("salesReturnAc")));
                mrTemp.setItsSaleReturnAcName(DmShared.xNull(rsTemp.getString("saleRetAcName")));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

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
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMarginRateFix(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Rate Fix");
            listNode.setSharedKeyFieldName("Rate Fix");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Margin Fix");
            listNode.setSharedKeyFieldName("Margin Fix");
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

}
