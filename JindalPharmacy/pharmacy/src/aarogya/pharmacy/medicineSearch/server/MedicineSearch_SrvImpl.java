package aarogya.pharmacy.medicineSearch.server;

import aarogya.pharmacy.medicineSearch.shared.DefaultValue_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearchFilter_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Srv;
import aarogya.pharmacy.medicineSearch.shared.TxtHHsn_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.self.shared.DmEnum.GSTNature;
import dataman.dmBase.self.shared.DmResult;

public class MedicineSearch_SrvImpl extends DmRSSPrivate implements MedicineSearch_Srv {

    @Override
    public DmResultPageing<List<MedicineSearch_Mdl>> getMedicineSearchDetail(DmHelper helper, boolean blnToEndOfFile, MedicineSearchFilter_Mdl filter, String v_Type) {
        DmResultPageing<List<MedicineSearch_Mdl>> dmrTemp = new DmResultPageing<>();
        List<MedicineSearch_Mdl> lstTemp = new ArrayList<>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        MedicineSearch_Mdl lstNode = null;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", matchStatus = "", siteCode = "", siteName = "", deparmentQuery = "", istkDepartment = "", idkDepartment = "", binName = "";
        long count =0;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            siteCode = DmServer.getSsnSiteCode(this);
            siteName = DmServer.executeScalar("SELECT sm.name FROM siteMast sm WHERE sm.code = " + DmServer.getSsnSiteCode(this), cnTemp);

            if (DmShared.iNull(filter.getDepartmentCode()).longValue() != 0) {
                condition += " AND virtd.code IS NULL ";
                deparmentQuery += " LEFT JOIN ( 	"
                        + "                     SELECT irtd.code "
                        + "                     FROM itemRestrictedToDepartment irtd"
                        + "                     WHERE irtd.departmentCode = :departmentCode"
                        + "                   ) AS virtd ON virtd.code = im.code";
                istkDepartment += " AND iStk.departmentCode = :departmentCode";
                idkDepartment += " AND idi.departmentCode = :departmentCode ";
                binName += ", MAX(idi.binName)  AS binName";
            } else {
                deparmentQuery += " ";
                istkDepartment += " ";
                idkDepartment += " ";
                binName += ", '' AS binName";
            }

            if (!DmShared.xNull(filter.getMedicineMakeCode()).equalsIgnoreCase("")) {
                condition += " AND im.itemMakeCode IN (" + DmServer.getParamList(":itemMakeCode", filter.getMedicineMakeCode()) + " )";
            }
            if (!DmShared.xNull(filter.getMedicineCategoryCode()).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + DmServer.getParamList(":itemCategoryCode", filter.getMedicineCategoryCode()) + " )";
            }
            if (!DmShared.xNull(filter.getMedicineGroupCode()).equalsIgnoreCase("")) {
                condition += " AND im.itemGroup IN (" + DmServer.getParamList(":itemGroup", filter.getMedicineGroupCode()) + " )";
            }
            if (!DmShared.xNull(filter.getDivisionCode()).equalsIgnoreCase("")) {
                condition += " AND im.divisionCode IN (" + DmServer.getParamList(":divisionCode", filter.getDivisionCode()) + " )";
            }
            if (!DmShared.xNull(filter.getHsnCode()).equalsIgnoreCase("")) {
                condition += " AND im.hsnCode IN (" + DmServer.getParamList(":hsnCode", filter.getHsnCode()) + " )";
            }

            matchStatus = " , 0 AS matchStatus";

            if (!DmShared.xNull(filter.getCompositionCodeWithPowerCsv()).equalsIgnoreCase("")) {
                condition += " AND (ccp.compositionCodeWithPowerCsv = :compositionCodeWithPowerCsv OR vmc.code IS NOT NULL)";
                condition += " AND im.code != :medicineCode";
                matchStatus = " , CASE WHEN ccp.compositionCodeWithPowerCsv = :compositionCodeWithPowerCsv THEN 2 ELSE 1 END AS matchStatus";
            } else if (!DmShared.xNull(filter.getCompositionCode()).equalsIgnoreCase("")) {
                condition += " AND (STUFF (ccl.compositionCodeCsv, 1, 1, '') = :compositionCodeString OR vmcl.code IS NOT NULL)";
                matchStatus = " , CASE WHEN STUFF (ccl.compositionCodeCsv, 1, 1, '') = :compositionCodeString THEN 2 ELSE 1 END AS matchStatus";
            } else if (DmShared.iNull(filter.getMedicineCode()).longValue() != 0) {
                condition += " AND im.code = :medicineCode";
            }

            if (!DmShared.xNull(filter.getItemCodeCsv()).equalsIgnoreCase("")) {
                condition += " AND im.code IN ( " + DmShared.xNull(filter.getItemCodeCsv()) + " )";
            }

            if (filter.getFlagInStock()) {
                condition += " AND ISNULL(vis.currentStock, 0) > 0";
            }
            
            sqlQuery = "Select Count(*) As Cnt From itemCatFilteration where v_Type= :v_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("v_Type", DmShared.iNull(v_Type).toString());

            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            count = rsTemp.getLong("cnt");
            if (count > 0) {
                condition = " and ic.code in (SELECT icf.itemCategoryCode FROM itemCatFilteration icf WHERE icf.v_Type = :v_Type)";
            } 
            psTemp.close();

            sqlQuery = "SELECT im.remark, im.code, im.name, im.displayName, im.manualCode, im.itemPacking AS pack, im.itemMakeCode AS makeCode \n"
                    + " , mm.companyName AS makeName, vis.currentStock, im.sku AS skuCode, im.priority , ums.unit AS skuName \n"
                    + " , ums.decimalPlaces AS skuDecimalPlaces, im.purchaseUnit AS purchaseUnitCode, ump.unit AS purchaseUnitName \n"
                    + " , ump.decimalPlaces AS purchaseUnitDecimalPlaces, ump.gstUnitCode AS purchaseUnitGstUnitCode \n"
                    + " , im.purchaseUnitConversionRate, im.saleUnit AS saleUnitCode, uml.unit AS saleUnitName \n"
                    + " , uml.decimalPlaces AS saleUnitDecimalPlaces, im.saleUnitConversionRate, im.hsnCode \n"
                    + " , hm.hsn AS hsnName, im.itemGSTNature, im.itsBasedOn, im.isBestBefore, im.isBarcodeActive \n"
                    + " , im.isIncludeFreeQtyInSaleRate, im.mrp, im.saleRate, im.purchaseRate, im.schemeOnQty \n"
                    + " , im.schemeFreeQty, im.saleDiscountPer, im.purchaseDiscountPer, im.propertyList, im.itemCategoryCode AS categoryCode \n"
                    + " , ic.name AS categoryName, im.itemGroup AS groupCode, ig.name AS groupName \n"
                    + " , STUFF (cnp.compositionNameWithPowerCsv, 2, 1, '') AS compositionNameWithPowerCsv \n"
                    + " , ccp.compositionCodeWithPowerCsv, STUFF (ccl.compositionCodeCsv, 1, 1, '') AS compositionCodeCsv \n"
                    + " , vidi.minStock, vidi.maxStock, vidi.reOrder, vidi.binName, im.manufacturerCode, sm.name AS manufacturerName \n"
                    + " , im.instruction, im.regionalInstruction, im.restrictGroup AS restrictedGroupCode, rg.name AS restrictedGroupName \n"
                    + " , im.divisionCode, sd.name AS divisionName, im.compositionCode, mcm.name AS compositionName \n"
                    + " , im.isSaleRateEditable, im.isReturnableItem, im.allowNegativeStock, im.isDiscountAllow \n"
                    + " , isd.code AS saleSchemeCode, imTts.itsPurchase, uml.gstUnitCode as saleUnitGstUnitCode , ic.saleRateProffitMargine \n"
                    + " , im.subcode as inventoryAc,im.grIrAc,im.consumptionAc,im.cogsAc,im.pidAc,im.inTransitAc,pl.rate as approvedPurchaseRate,pl.mrp as approvedMrp,ic.isMarkupApplicable \n"
                    + " " + matchStatus
                    + " FROM itemMast im \n"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code \n"
                    + " LEFT JOIN priceList pl ON pl.itemCode = im.code and pl.site_Code=:site_Code and :vDate between pl.applicableFrom and pl.applicableTo \n"
                    + " LEFT JOIN itemMakeMast mm ON mm.code = im.itemMakeCode \n"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code \n"
                    + " LEFT JOIN unitmast ump ON im.purchaseUnit = ump.code \n"
                    + " LEFT JOIN unitmast uml ON im.saleUnit = uml.code \n"
                    + " LEFT JOIN hsnMast hm ON hm.code = im.hsnCode \n"
                    + " LEFT JOIN itemCat ic ON ic.code = im.itemCategoryCode \n"
                    + " LEFT JOIN itemGroup ig ON ig.code = im.itemGroup \n"
                    + " LEFT JOIN subGroup sm ON sm.subCode = im.manufacturerCode \n"
                    + " LEFT JOIN restrictedGrpMast rg ON rg.code = im.restrictGroup \n"
                    + " LEFT JOIN subGroup sd ON sd.subCode = im.divisionCode \n"
                    + " LEFT JOIN medicineCompoMast mcm ON mcm.code = im.compositionCode \n"
                    + " LEFT JOIN ( \n"
                    + "         SELECT im.code, it.name AS itsPurchase \n"
                    + "         FROM itemMast im \n"
                    + "         LEFT JOIN itemItsDetail iid ON iid.code = im.code       \n"
                    + "         LEFT JOIN itsType it ON it.code = iid.itsTypeCode       \n"
                    + "         WHERE CASE WHEN ISNULL(im.itsBasedOn,0) = 1 THEN \n"
                    + "                         CASE WHEN ISNULL(im.purchaseRate,0) BETWEEN iid.rateFrom AND iid.rateUpto THEN 1 ELSE 0 END \n"
                    + "                     ELSE 1 \n"
                    + "               END = 1 \n"
                    + " ) AS imTts ON imTts.code = im.code \n"
                    + " LEFT JOIN ( \n"
                    + "         SELECT issd.code \n"
                    + "         FROM itemSaleSchemeDetail issd \n"
                    + "         GROUP BY issd.code \n"
                    + " ) AS isd ON isd.code = im.code \n" + deparmentQuery
                    + " LEFT JOIN ( \n"
                    + " 	SELECT iStk.itemCode, SUM(iStk.balanceQty) AS currentStock \n"
                    + "         FROM itemStock iStk \n"
                    + "         WHERE iStk.site_Code = :site_Code  \n" + istkDepartment
                    + "         GROUP BY iStk.itemCode \n"
                    + " ) AS vis ON im.code = vis.itemCode \n"
                    + " LEFT JOIN ( \n"
                    + " 	SELECT idi.itemCode, SUM(idi.minStock) as minStock, SUM(idi.maxStock) as maxStock, SUM(idi.reOrder) as reOrder \n" + binName
                    + " 	FROM itemDepartmentInfo idi \n"
                    + " 	WHERE idi.site_Code = :site_Code \n" + idkDepartment
                    + "         GROUP BY idi.itemCode \n"
                    + " ) AS vidi ON vidi.itemCode = im.code \n"
                    + " CROSS APPLY ( \n"
                    + "		SELECT (' + ' + ISNULL(cm.name, '') + ' ' + CAST(ISNULL(mc.power, 0) AS VARCHAR) + ' ' + ISNULL(um.unit, '')) AS [text()] \n"
                    + "		FROM medicineComposition mc \n"
                    + "         LEFT JOIN unitMast um ON um.code = mc.unitCode \n"
                    + "		LEFT JOIN medicineCompoMast cm ON cm.code = mc.compositionCode \n"
                    + "		WHERE mc.code = im.Code \n"
                    + "         ORDER BY mc.sNo  \n"
                    + "		FOR XML PATH('') \n"
                    + " ) cnp (compositionNameWithPowerCsv) \n"
                    + " CROSS APPLY ( \n"
                    + "		SELECT (',' + CAST(cm.code AS VARCHAR) + '|' + CAST(ISNULL(mc.power, 0) AS VARCHAR) + '|' + CAST(ISNULL(mc.unitCode, 0) AS VARCHAR)) AS [text()] \n"
                    + "		FROM medicineComposition mc \n"
                    + "		LEFT JOIN medicineCompoMast cm ON cm.code = mc.compositionCode \n"
                    + "		WHERE mc.code = im.Code \n"
                    + "         ORDER BY cm.code \n"
                    + "		FOR XML PATH('') \n"
                    + " ) ccp (compositionCodeWithPowerCsv) \n"
                    + " CROSS APPLY ( \n"
                    + "		SELECT ',' + CAST(cm.code AS VARCHAR) AS [text()] \n"
                    + "		FROM medicineComposition mc \n"
                    + "		LEFT JOIN medicineCompoMast cm ON cm.code = mc.compositionCode \n"
                    + "		WHERE mc.code = im.Code \n"
                    + "         ORDER BY cm.code \n"
                    + "		FOR XML PATH('') \n"
                    + " ) ccl (compositionCodeCsv) \n"
                    + " LEFT JOIN ( \n"
                    + "         SELECT mc.code \n"
                    + "         FROM medicineComposition mc \n"
                    + "         WHERE mc.compositionCode IN (" + DmServer.getParamList(":compositionCodeCsv", DmShared.xNull(filter.getCompositionCodeCsv())) + ") \n"
                    + "         GROUP BY mc.code \n"
                    + " ) AS vmc ON vmc.code = im.code \n"
                    + " LEFT JOIN ( \n"
                    + "         SELECT mc.code \n"
                    + "         FROM medicineComposition mc \n"
                    + "         WHERE mc.compositionCode IN (" + DmServer.getParamList(":compositionCode", DmShared.xNull(filter.getCompositionCode())) + ") \n"
                    + "         GROUP BY mc.code \n"
                    + " ) AS vmcl ON vmcl.code = im.code \n"
                    + " WHERE im.isActive = 1 \n"
                    + " AND sl.site_Code = :site_Code \n"
                    + " AND (im.filterItemType = :filterItemType OR im.isCommon = 1) \n"
                    //                    + " AND CASE WHEN ISNULL(im.allowNegativeStock,0) = 1 THEN 1 ELSE CASE WHEN ISNULL(vis.currentStock, 0) > 0 THEN 1 ELSE 0 END END = 1"
                    + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (count > 0) {
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            }
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setStringList("compositionCodeCsv", DmShared.xNull(filter.getCompositionCodeCsv()));
            sqlWithParam.setStringList("compositionCode", DmShared.xNull(filter.getCompositionCode()));
            sqlWithParam.setDate("vDate", DmServer.convertDate(filter.getV_Date()));

            if (DmShared.iNull(filter.getDepartmentCode()).longValue() != 0) {
                sqlWithParam.setString("departmentCode", DmShared.xNull(filter.getDepartmentCode()));
            }
            if (!DmShared.xNull(filter.getMedicineMakeCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("itemMakeCode", DmShared.xNull(filter.getMedicineMakeCode()));
            }
            if (!DmShared.xNull(filter.getMedicineCategoryCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("itemCategoryCode", DmShared.xNull(filter.getMedicineCategoryCode()));
            }
            if (!DmShared.xNull(filter.getMedicineGroupCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("itemGroup", DmShared.xNull(filter.getMedicineGroupCode()));
            }
            if (!DmShared.xNull(filter.getDivisionCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("divisionCode", DmShared.xNull(filter.getDivisionCode()));
            }
            if (!DmShared.xNull(filter.getHsnCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("hsnCode", DmShared.xNull(filter.getHsnCode()));
            }
            if (!DmShared.xNull(filter.getCompositionCodeWithPowerCsv()).equalsIgnoreCase("")) {
                sqlWithParam.setString("compositionCodeWithPowerCsv", DmShared.xNull(filter.getCompositionCodeWithPowerCsv()));
                sqlWithParam.setString("medicineCode", DmShared.xNull(filter.getMedicineCode()));
            } else if (!DmShared.xNull(filter.getCompositionCode()).equalsIgnoreCase("")) {
                sqlWithParam.setString("compositionCodeString", DmShared.xNull(filter.getCompositionCode()));
            } else if (DmShared.iNull(filter.getMedicineCode()).longValue() != 0) {
                sqlWithParam.setString("medicineCode", DmShared.xNull(filter.getMedicineCode()));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new MedicineSearch_Mdl();
                lstNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstNode.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                lstNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                lstNode.setPack(DmShared.xNull(rsTemp.getString("pack")));
                lstNode.setMakeCode(DmShared.xNull(rsTemp.getString("makeCode")));
                lstNode.setMakeName(DmShared.xNull(rsTemp.getString("makeName")));
                lstNode.setCurrentStock(DmServer.vNull(DmServer.vNull(rsTemp.getString("currentStock"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue()))));
                lstNode.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                lstNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                lstNode.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));
                lstNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                lstNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                lstNode.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                lstNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                lstNode.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnitCode")));
                lstNode.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                lstNode.setSaleUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("saleUnitDecimalPlaces")));
                lstNode.setSaleUnitConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                lstNode.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                lstNode.setHsnName(DmShared.xNull(rsTemp.getString("hsnName")));
                lstNode.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                GSTNature gstNature = GSTNature.getValueOf(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                lstNode.setItemGSTNatureName(gstNature != null ? gstNature.getValue() : "");
                lstNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                lstNode.setIsBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).toString());
                lstNode.setIsBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).toString());
                lstNode.setIsIncludeFreeQtyInSaleRate(DmServer.vNull(rsTemp.getString("isIncludeFreeQtyInSaleRate")));
                lstNode.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                lstNode.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                lstNode.setPurchaseRate(DmServer.vNull(rsTemp.getString("purchaseRate"), "0.00"));
                lstNode.setApprovedPurchaseRate(DmServer.vNull(rsTemp.getString("approvedPurchaseRate"), "0.00"));
                lstNode.setApprovedMRP(DmServer.vNull(rsTemp.getString("approvedMrp"), "0.00"));
                lstNode.setSchemeOnQty(DmServer.vNull(rsTemp.getString("schemeOnQty")));
                lstNode.setSchemeFreeQty(DmServer.vNull(rsTemp.getString("schemeFreeQty")));
                lstNode.setSaleDiscountPer(DmServer.vNull(rsTemp.getString("saleDiscountPer")));
                lstNode.setPurchaseDiscountPer(DmServer.vNull(rsTemp.getString("purchaseDiscountPer")));
                lstNode.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                lstNode.setCategoryCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                lstNode.setCategoryName(DmShared.xNull(rsTemp.getString("categoryName")));
                lstNode.setGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                lstNode.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                lstNode.setRestrictedGroupCode(DmShared.xNull(rsTemp.getString("restrictedGroupCode")));
                lstNode.setRestrictedGroupName(DmShared.xNull(rsTemp.getString("restrictedGroupName")));
                lstNode.setDivisionCode(DmShared.xNull(rsTemp.getString("divisionCode")));
                lstNode.setDivisionName(DmShared.xNull(rsTemp.getString("divisionName")));
                lstNode.setMinStock(DmShared.xNull(rsTemp.getString("minStock")));
                lstNode.setMaxStock(DmShared.xNull(rsTemp.getString("maxStock")));
                lstNode.setReOrder(DmShared.xNull(rsTemp.getString("reOrder")));
                lstNode.setBinName(DmShared.xNull(rsTemp.getString("binName")));
                lstNode.setCompositionCodeCsv(DmShared.xNull(rsTemp.getString("compositionCodeCsv")).trim());
                lstNode.setCompositionCodeWithPowerCsv(DmShared.xNull(rsTemp.getString("compositionCodeWithPowerCsv")).trim());
                lstNode.setCompositionNameWithPowerCsv(DmShared.xNull(rsTemp.getString("compositionNameWithPowerCsv")).trim());
                lstNode.setManufacturerCode(DmShared.xNull(rsTemp.getString("manufacturerCode")));
                lstNode.setManufacturerName(DmShared.xNull(rsTemp.getString("manufacturerName")));
                lstNode.setInstruction(DmShared.xNull(rsTemp.getString("instruction")));
                lstNode.setRegionalInstruction(DmShared.xNull(rsTemp.getString("regionalInstruction")));
                lstNode.setMatchtStatus(DmShared.iNull(rsTemp.getString("matchStatus")).toString());
                lstNode.setCompositionCode(DmShared.xNull(rsTemp.getString("compositionCode")));
                lstNode.setCompositionName(DmShared.xNull(rsTemp.getString("compositionName")));
                lstNode.setIsSaleRateEditable(DmShared.iNull(rsTemp.getString("isSaleRateEditable")).toString());
                lstNode.setIsReturnableItem(DmShared.iNull(rsTemp.getString("isReturnableItem")).toString());
                lstNode.setIsAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).toString());
                lstNode.setIsDiscountAllow(DmShared.iNull(rsTemp.getString("isDiscountAllow")).toString());
                lstNode.setSaleSchemeCode(DmShared.xNull(rsTemp.getString("saleSchemeCode")));
                lstNode.setPurchaseUnitGstUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitGstUnitCode")));
                lstNode.setSaleUnitGstUnitCode(DmShared.xNull(rsTemp.getString("saleUnitGstUnitCode")));
                lstNode.setItsPurchase(DmShared.xNull(rsTemp.getString("itsPurchase")));
                lstNode.setSaleRateProffitMargine(DmShared.xNull(rsTemp.getString("saleRateProffitMargine")));
                lstNode.setIsMarkupApplicable(DmShared.iNull(rsTemp.getString("isMarkupApplicable")).toString());
                
                lstNode.setInventoryAc(DmShared.xNull(rsTemp.getString("inventoryAc")));
                lstNode.setGrIrAc(DmShared.xNull(rsTemp.getString("grIrAc")));
                lstNode.setConsumptionAc(DmShared.xNull(rsTemp.getString("consumptionAc")));
                lstNode.setCogsAc(DmShared.xNull(rsTemp.getString("cogsAc")));
                lstNode.setPidAc(DmShared.xNull(rsTemp.getString("pidAc")));
                lstNode.setInTransitAc(DmShared.xNull(rsTemp.getString("inTransitAc")));
                lstNode.setRemark(DmShared.xNull(rsTemp.getString("remark")));

                double margin = 0, costRate = 0, marginPer = 0;
                if (DmShared.vNull(rsTemp.getString("schemeOnQty")).doubleValue() > 0 || DmShared.vNull(rsTemp.getString("schemeFreeQty")).doubleValue() > 0) {
                    costRate = (DmShared.vNull(rsTemp.getString("schemeOnQty")).doubleValue() * DmShared.vNull(rsTemp.getString("purchaseRate")).doubleValue())
                            / (DmShared.vNull(rsTemp.getString("schemeOnQty")).doubleValue() + DmShared.vNull(rsTemp.getString("schemeFreeQty")).doubleValue());
                    margin = DmShared.vNull(rsTemp.getString("saleRate")).doubleValue() - costRate;
                } else {
                    margin = DmShared.vNull(rsTemp.getString("saleRate")).doubleValue() - DmShared.vNull(rsTemp.getString("purchaseRate")).doubleValue();
                }
                if (DmShared.vNull(rsTemp.getString("saleRate")).doubleValue() > 0) {
                    marginPer = DmShared.vNull(DmShared.vNull(margin).doubleValue() * 100) / DmShared.vNull(rsTemp.getString("saleRate")).doubleValue();
                }
                lstNode.setMarginPercent(DmServer.vNull(marginPer, "0.00"));
                lstNode.setPriority(DmShared.iNull(rsTemp.getString("priority")).toString());

                lstTemp.add(lstNode);
            }

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            dmrTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(lstTemp);
            dmrTemp.setValue1(siteCode);
            dmrTemp.setValue2(siteName);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineMake(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT code, companyName AS name FROM itemMakeMast");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCategory(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ic.code, ic.name FROM itemCat ic");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineGroup(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ig.code, ig.name FROM itemGroup ig");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDivision(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT sg.subCode AS code, sg.name"
                    + " FROM subGroup sg "
                    + " WHERE filterNature = :filterNature ");
            sqlWithParam.setString("filterNature", DmServer.getCodeViaManualCode("DV", "manualCode", "subgroupNature", "code", cnTemp));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHComposition(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT cm.code, cm.name FROM medicineCompoMast cm WHERE cm.isActive = 1");
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
    public DmResultPageing<List<TxtHHsn_Mdl>> getTxtHHsn(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHHsn_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHHsn_Mdl> lstTemp = new ArrayList<>();
        TxtHHsn_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT hm.code, hm.name, hm.hsn FROM hsnMast hm");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHHsn_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);
            sqlQuery = "SELECT"
                    + "   dm.subCode AS code"
                    + " , sg.displayName AS name"
                    + " FROM departmentMast dm"
                    + " LEFT JOIN subGroup sg ON dm.subCode = sg.subCode"
                    + " WHERE sg.IsActive = 1"
                    + " AND dm.subCode = :depCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setDepartmentCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setDepartmentName(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(defaultValue);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PH') AND sg.isActive = 1 " + condition;
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

}
