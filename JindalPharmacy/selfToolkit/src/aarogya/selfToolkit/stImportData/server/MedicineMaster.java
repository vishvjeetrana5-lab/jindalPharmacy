package aarogya.selfToolkit.stImportData.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicineMaster implements DmImportData_Interface {

    private final String vCat = "'" + new ProjectVoucherCategory().PHARMACY_MEDICINE_MAST + "'";

    class Model {

        private String code, displayName, manualCode, categoryCode, categoryName, name, medGroupCode, medGroupName, HsnCode, hsnName, gstNature, medCompShortName, medCompanyCode, medcompanyName,
                restrictCode, restrictName, narcotics, narcoticsCode, returnItem, returnItemCode, freeQtyCode, freeQty,
                highValue, highValueCode, saleRateEditCode, saleRateEditName, consignmentItem, consignmentItemCode, bestBefore, bestBeforeCode, billedItem, billedItemCode,
                allowNegativeStock, allowNegativeStockCode, lifeSaving, lifeSavingCode, flowThrough, flowThroughCode, saleDiscountAllowCode, saleDiscountAllowName,
                SKU, SKUCode, purchaseUnit, purchaseUnitCode, purchaseUnitConversionRate,
                saleUnitConversionRate, saleUnit, saleUnitCode, active, commonTag, commonText, packing, manufacturerCode, manufacturerName, divisionCode, divisionName,
                instructions, regionalInstructions, mrp, saleRate, purchaseRate, schemeOnQty, schemeFreeQty, purchaseDiscountPer, saleDiscountPer,
                composition1Code, composition1Name, composition1Power, composition1UnitCode, composition1UnitName, composition2Code, composition2Name, composition2Power, composition2UnitCode, composition2UnitName,
                composition3Code, composition3Name, composition3Power, composition3UnitCode, composition3UnitName, composition4Code, composition4Name, composition4Power, composition4UnitCode, composition4UnitName,
                composition5Code, composition5Name, composition5Power, composition5UnitCode, composition5UnitName, composition6Code, composition6Name, composition6Power, composition6UnitCode, composition6UnitName,
                composition7Code, composition7Name, composition7Power, composition7UnitCode, composition7UnitName, composition8Code, composition8Name, composition8Power, composition8UnitCode, composition8UnitName,
                composition9Code, composition9Name, composition9Power, composition9UnitCode, composition9UnitName, composition10Code, composition10Name, composition10Power, composition10UnitCode, composition10UnitName,
                poReq, poReqCode, indentReq, indentReqCode, quotationReq, quotationReqCode, barcodeActiveTag, barcodeActiveText;

        //private String tempCode;
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getManualCode() {
            return manualCode;
        }

        public void setManualCode(String manualCode) {
            this.manualCode = manualCode;
        }

        public String getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getHsnCode() {
            return HsnCode;
        }

        public void setHsnCode(String HsnCode) {
            this.HsnCode = HsnCode;
        }

        public String getHsnName() {
            return hsnName;
        }

        public void setHsnName(String hsnName) {
            this.hsnName = hsnName;
        }

        public String getGstNature() {
            return gstNature;
        }

        public void setGstNature(String gstNature) {
            this.gstNature = gstNature;
        }

        public String getMedCompanyCode() {
            return medCompanyCode;
        }

        public void setMedCompanyCode(String medCompanyCode) {
            this.medCompanyCode = medCompanyCode;
        }

        public String getMedcompanyName() {
            return medcompanyName;
        }

        public void setMedcompanyName(String medcompanyName) {
            this.medcompanyName = medcompanyName;
        }

        public String getMedGroupCode() {
            return medGroupCode;
        }

        public void setMedGroupCode(String medGroupCode) {
            this.medGroupCode = medGroupCode;
        }

        public String getMedGroupName() {
            return medGroupName;
        }

        public void setMedGroupName(String medGroupName) {
            this.medGroupName = medGroupName;
        }

        public String getRestrictCode() {
            return restrictCode;
        }

        public void setRestrictCode(String restrictCode) {
            this.restrictCode = restrictCode;
        }

        public String getRestrictName() {
            return restrictName;
        }

        public void setRestrictName(String restrictName) {
            this.restrictName = restrictName;
        }

        public String getNarcotics() {
            return narcotics;
        }

        public void setNarcotics(String narcotics) {
            this.narcotics = narcotics;
        }

        public String getNarcoticsCode() {
            return narcoticsCode;
        }

        public void setNarcoticsCode(String narcoticsCode) {
            this.narcoticsCode = narcoticsCode;
        }

        public String getConsignmentItem() {
            return consignmentItem;
        }

        public void setConsignmentItem(String consignmentItem) {
            this.consignmentItem = consignmentItem;
        }

        public String getConsignmentItemCode() {
            return consignmentItemCode;
        }

        public void setConsignmentItemCode(String consignmentItemCode) {
            this.consignmentItemCode = consignmentItemCode;
        }

        public String getReturnItem() {
            return returnItem;
        }

        public void setReturnItem(String returnItem) {
            this.returnItem = returnItem;
        }

        public String getReturnItemCode() {
            return returnItemCode;
        }

        public void setReturnItemCode(String returnItemCode) {
            this.returnItemCode = returnItemCode;
        }

        public String getBestBefore() {
            return bestBefore;
        }

        public void setBestBefore(String bestBefore) {
            this.bestBefore = bestBefore;
        }

        public String getBestBeforeCode() {
            return bestBeforeCode;
        }

        public void setBestBeforeCode(String bestBeforeCode) {
            this.bestBeforeCode = bestBeforeCode;
        }

        public String getFreeQtyCode() {
            return freeQtyCode;
        }

        public void setFreeQtyCode(String freeQtyCode) {
            this.freeQtyCode = freeQtyCode;
        }

        public String getFreeQty() {
            return freeQty;
        }

        public void setFreeQty(String freeQty) {
            this.freeQty = freeQty;
        }

        public String getLifeSaving() {
            return lifeSaving;
        }

        public void setLifeSaving(String lifeSaving) {
            this.lifeSaving = lifeSaving;
        }

        public String getLifeSavingCode() {
            return lifeSavingCode;
        }

        public void setLifeSavingCode(String lifeSavingCode) {
            this.lifeSavingCode = lifeSavingCode;
        }

        public String getHighValue() {
            return highValue;
        }

        public void setHighValue(String highValue) {
            this.highValue = highValue;
        }

        public String getHighValueCode() {
            return highValueCode;
        }

        public void setHighValueCode(String highValueCode) {
            this.highValueCode = highValueCode;
        }

        public String getBilledItem() {
            return billedItem;
        }

        public void setBilledItem(String billedItem) {
            this.billedItem = billedItem;
        }

        public String getBilledItemCode() {
            return billedItemCode;
        }

        public void setBilledItemCode(String billedItemCode) {
            this.billedItemCode = billedItemCode;
        }

        public String getSaleRateEditCode() {
            return saleRateEditCode;
        }

        public void setSaleRateEditCode(String saleRateEditCode) {
            this.saleRateEditCode = saleRateEditCode;
        }

        public String getSaleRateEditName() {
            return saleRateEditName;
        }

        public void setSaleRateEditName(String saleRateEditName) {
            this.saleRateEditName = saleRateEditName;
        }

        public String getAllowNegativeStock() {
            return allowNegativeStock;
        }

        public void setAllowNegativeStock(String allowNegativeStock) {
            this.allowNegativeStock = allowNegativeStock;
        }

        public String getAllowNegativeStockCode() {
            return allowNegativeStockCode;
        }

        public void setAllowNegativeStockCode(String allowNegativeStockCode) {
            this.allowNegativeStockCode = allowNegativeStockCode;
        }

        public String getFlowThrough() {
            return flowThrough;
        }

        public void setFlowThrough(String flowThrough) {
            this.flowThrough = flowThrough;
        }

        public String getFlowThroughCode() {
            return flowThroughCode;
        }

        public void setFlowThroughCode(String flowThroughCode) {
            this.flowThroughCode = flowThroughCode;
        }

        public String getSKU() {
            return SKU;
        }

        public void setSKU(String SKU) {
            this.SKU = SKU;
        }

        public String getSKUCode() {
            return SKUCode;
        }

        public void setSKUCode(String SKUCode) {
            this.SKUCode = SKUCode;
        }

        public String getPurchaseUnit() {
            return purchaseUnit;
        }

        public void setPurchaseUnit(String purchaseUnit) {
            this.purchaseUnit = purchaseUnit;
        }

        public String getPurchaseUnitCode() {
            return purchaseUnitCode;
        }

        public void setPurchaseUnitCode(String purchaseUnitCode) {
            this.purchaseUnitCode = purchaseUnitCode;
        }

        public String getPurchaseUnitConversionRate() {
            return purchaseUnitConversionRate;
        }

        public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
            this.purchaseUnitConversionRate = purchaseUnitConversionRate;
        }

        public String getSaleUnitConversionRate() {
            return saleUnitConversionRate;
        }

        public void setSaleUnitConversionRate(String saleUnitConversionRate) {
            this.saleUnitConversionRate = saleUnitConversionRate;
        }

        public String getSaleUnit() {
            return saleUnit;
        }

        public void setSaleUnit(String saleUnit) {
            this.saleUnit = saleUnit;
        }

        public String getSaleUnitCode() {
            return saleUnitCode;
        }

        public void setSaleUnitCode(String saleUnitCode) {
            this.saleUnitCode = saleUnitCode;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getSaleDiscountAllowCode() {
            return saleDiscountAllowCode;
        }

        public void setSaleDiscountAllowCode(String saleDiscountAllowCode) {
            this.saleDiscountAllowCode = saleDiscountAllowCode;
        }

        public String getSaleDiscountAllowName() {
            return saleDiscountAllowName;
        }

        public void setSaleDiscountAllowName(String saleDiscountAllowName) {
            this.saleDiscountAllowName = saleDiscountAllowName;
        }

        public String getCommonTag() {
            return commonTag;
        }

        public void setCommonTag(String commonTag) {
            this.commonTag = commonTag;
        }

        public String getCommonText() {
            return commonText;
        }

        public void setCommonText(String commonText) {
            this.commonText = commonText;
        }

        public String getManufacturerCode() {
            return manufacturerCode;
        }

        public void setManufacturerCode(String manufacturerCode) {
            this.manufacturerCode = manufacturerCode;
        }

        public String getManufacturerName() {
            return manufacturerName;
        }

        public void setManufacturerName(String manufacturerName) {
            this.manufacturerName = manufacturerName;
        }

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        public String getRegionalInstructions() {
            return regionalInstructions;
        }

        public void setRegionalInstructions(String regionalInstructions) {
            this.regionalInstructions = regionalInstructions;
        }

        public String getMedCompShortName() {
            return medCompShortName;
        }

        public void setMedCompShortName(String medCompShortName) {
            this.medCompShortName = medCompShortName;
        }

        public String getPacking() {
            return packing;
        }

        public void setPacking(String packing) {
            this.packing = packing;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public String getSaleRate() {
            return saleRate;
        }

        public void setSaleRate(String saleRate) {
            this.saleRate = saleRate;
        }

        public String getPurchaseRate() {
            return purchaseRate;
        }

        public void setPurchaseRate(String purchaseRate) {
            this.purchaseRate = purchaseRate;
        }

        public String getSchemeOnQty() {
            return schemeOnQty;
        }

        public void setSchemeOnQty(String schemeOnQty) {
            this.schemeOnQty = schemeOnQty;
        }

        public String getSchemeFreeQty() {
            return schemeFreeQty;
        }

        public void setSchemeFreeQty(String schemeFreeQty) {
            this.schemeFreeQty = schemeFreeQty;
        }

        public String getPurchaseDiscountPer() {
            return purchaseDiscountPer;
        }

        public void setPurchaseDiscountPer(String purchaseDiscountPer) {
            this.purchaseDiscountPer = purchaseDiscountPer;
        }

        public String getSaleDiscountPer() {
            return saleDiscountPer;
        }

        public void setSaleDiscountPer(String saleDiscountPer) {
            this.saleDiscountPer = saleDiscountPer;
        }

        public String getDivisionCode() {
            return divisionCode;
        }

        public void setDivisionCode(String divisionCode) {
            this.divisionCode = divisionCode;
        }

        public String getDivisionName() {
            return divisionName;
        }

        public void setDivisionName(String divisionName) {
            this.divisionName = divisionName;
        }

        public String getComposition1Code() {
            return composition1Code;
        }

        public void setComposition1Code(String composition1Code) {
            this.composition1Code = composition1Code;
        }

        public String getComposition1Name() {
            return composition1Name;
        }

        public void setComposition1Name(String composition1Name) {
            this.composition1Name = composition1Name;
        }

        public String getComposition1Power() {
            return composition1Power;
        }

        public void setComposition1Power(String composition1Power) {
            this.composition1Power = composition1Power;
        }

        public String getComposition2Code() {
            return composition2Code;
        }

        public void setComposition2Code(String composition2Code) {
            this.composition2Code = composition2Code;
        }

        public String getComposition2Name() {
            return composition2Name;
        }

        public void setComposition2Name(String composition2Name) {
            this.composition2Name = composition2Name;
        }

        public String getComposition2Power() {
            return composition2Power;
        }

        public void setComposition2Power(String composition2Power) {
            this.composition2Power = composition2Power;
        }

        public String getComposition3Code() {
            return composition3Code;
        }

        public void setComposition3Code(String composition3Code) {
            this.composition3Code = composition3Code;
        }

        public String getComposition3Name() {
            return composition3Name;
        }

        public void setComposition3Name(String composition3Name) {
            this.composition3Name = composition3Name;
        }

        public String getComposition3Power() {
            return composition3Power;
        }

        public void setComposition3Power(String composition3Power) {
            this.composition3Power = composition3Power;
        }

        public String getComposition4Code() {
            return composition4Code;
        }

        public void setComposition4Code(String composition4Code) {
            this.composition4Code = composition4Code;
        }

        public String getComposition4Name() {
            return composition4Name;
        }

        public void setComposition4Name(String composition4Name) {
            this.composition4Name = composition4Name;
        }

        public String getComposition4Power() {
            return composition4Power;
        }

        public void setComposition4Power(String composition4Power) {
            this.composition4Power = composition4Power;
        }

        public String getComposition5Code() {
            return composition5Code;
        }

        public void setComposition5Code(String composition5Code) {
            this.composition5Code = composition5Code;
        }

        public String getComposition5Name() {
            return composition5Name;
        }

        public void setComposition5Name(String composition5Name) {
            this.composition5Name = composition5Name;
        }

        public String getComposition5Power() {
            return composition5Power;
        }

        public void setComposition5Power(String composition5Power) {
            this.composition5Power = composition5Power;
        }

        public String getComposition6Code() {
            return composition6Code;
        }

        public void setComposition6Code(String composition6Code) {
            this.composition6Code = composition6Code;
        }

        public String getComposition6Name() {
            return composition6Name;
        }

        public void setComposition6Name(String composition6Name) {
            this.composition6Name = composition6Name;
        }

        public String getComposition6Power() {
            return composition6Power;
        }

        public void setComposition6Power(String composition6Power) {
            this.composition6Power = composition6Power;
        }

        public String getComposition7Code() {
            return composition7Code;
        }

        public void setComposition7Code(String composition7Code) {
            this.composition7Code = composition7Code;
        }

        public String getComposition7Name() {
            return composition7Name;
        }

        public void setComposition7Name(String composition7Name) {
            this.composition7Name = composition7Name;
        }

        public String getComposition7Power() {
            return composition7Power;
        }

        public void setComposition7Power(String composition7Power) {
            this.composition7Power = composition7Power;
        }

        public String getComposition8Code() {
            return composition8Code;
        }

        public void setComposition8Code(String composition8Code) {
            this.composition8Code = composition8Code;
        }

        public String getComposition8Name() {
            return composition8Name;
        }

        public void setComposition8Name(String composition8Name) {
            this.composition8Name = composition8Name;
        }

        public String getComposition8Power() {
            return composition8Power;
        }

        public void setComposition8Power(String composition8Power) {
            this.composition8Power = composition8Power;
        }

        public String getComposition9Code() {
            return composition9Code;
        }

        public void setComposition9Code(String composition9Code) {
            this.composition9Code = composition9Code;
        }

        public String getComposition9Name() {
            return composition9Name;
        }

        public void setComposition9Name(String composition9Name) {
            this.composition9Name = composition9Name;
        }

        public String getComposition9Power() {
            return composition9Power;
        }

        public void setComposition9Power(String composition9Power) {
            this.composition9Power = composition9Power;
        }

        public String getComposition10Code() {
            return composition10Code;
        }

        public void setComposition10Code(String composition10Code) {
            this.composition10Code = composition10Code;
        }

        public String getComposition10Name() {
            return composition10Name;
        }

        public void setComposition10Name(String composition10Name) {
            this.composition10Name = composition10Name;
        }

        public String getComposition10Power() {
            return composition10Power;
        }

        public void setComposition10Power(String composition10Power) {
            this.composition10Power = composition10Power;
        }

//        public String getTempCode() {
//            return tempCode;
//        }
//
//        public void setTempCode(String tempCode) {
//            this.tempCode = tempCode;
//        }
        public String getComposition1UnitCode() {
            return composition1UnitCode;
        }

        public void setComposition1UnitCode(String composition1UnitCode) {
            this.composition1UnitCode = composition1UnitCode;
        }

        public String getComposition1UnitName() {
            return composition1UnitName;
        }

        public void setComposition1UnitName(String composition1UnitName) {
            this.composition1UnitName = composition1UnitName;
        }

        public String getComposition2UnitCode() {
            return composition2UnitCode;
        }

        public void setComposition2UnitCode(String composition2UnitCode) {
            this.composition2UnitCode = composition2UnitCode;
        }

        public String getComposition2UnitName() {
            return composition2UnitName;
        }

        public void setComposition2UnitName(String composition2UnitName) {
            this.composition2UnitName = composition2UnitName;
        }

        public String getComposition3UnitCode() {
            return composition3UnitCode;
        }

        public void setComposition3UnitCode(String composition3UnitCode) {
            this.composition3UnitCode = composition3UnitCode;
        }

        public String getComposition3UnitName() {
            return composition3UnitName;
        }

        public void setComposition3UnitName(String composition3UnitName) {
            this.composition3UnitName = composition3UnitName;
        }

        public String getComposition4UnitCode() {
            return composition4UnitCode;
        }

        public void setComposition4UnitCode(String composition4UnitCode) {
            this.composition4UnitCode = composition4UnitCode;
        }

        public String getComposition4UnitName() {
            return composition4UnitName;
        }

        public void setComposition4UnitName(String composition4UnitName) {
            this.composition4UnitName = composition4UnitName;
        }

        public String getComposition5UnitCode() {
            return composition5UnitCode;
        }

        public void setComposition5UnitCode(String composition5UnitCode) {
            this.composition5UnitCode = composition5UnitCode;
        }

        public String getComposition5UnitName() {
            return composition5UnitName;
        }

        public void setComposition5UnitName(String composition5UnitName) {
            this.composition5UnitName = composition5UnitName;
        }

        public String getComposition6UnitCode() {
            return composition6UnitCode;
        }

        public void setComposition6UnitCode(String composition6UnitCode) {
            this.composition6UnitCode = composition6UnitCode;
        }

        public String getComposition6UnitName() {
            return composition6UnitName;
        }

        public void setComposition6UnitName(String composition6UnitName) {
            this.composition6UnitName = composition6UnitName;
        }

        public String getComposition7UnitCode() {
            return composition7UnitCode;
        }

        public void setComposition7UnitCode(String composition7UnitCode) {
            this.composition7UnitCode = composition7UnitCode;
        }

        public String getComposition7UnitName() {
            return composition7UnitName;
        }

        public void setComposition7UnitName(String composition7UnitName) {
            this.composition7UnitName = composition7UnitName;
        }

        public String getComposition8UnitCode() {
            return composition8UnitCode;
        }

        public void setComposition8UnitCode(String composition8UnitCode) {
            this.composition8UnitCode = composition8UnitCode;
        }

        public String getComposition8UnitName() {
            return composition8UnitName;
        }

        public void setComposition8UnitName(String composition8UnitName) {
            this.composition8UnitName = composition8UnitName;
        }

        public String getComposition9UnitCode() {
            return composition9UnitCode;
        }

        public void setComposition9UnitCode(String composition9UnitCode) {
            this.composition9UnitCode = composition9UnitCode;
        }

        public String getComposition9UnitName() {
            return composition9UnitName;
        }

        public void setComposition9UnitName(String composition9UnitName) {
            this.composition9UnitName = composition9UnitName;
        }

        public String getComposition10UnitCode() {
            return composition10UnitCode;
        }

        public void setComposition10UnitCode(String composition10UnitCode) {
            this.composition10UnitCode = composition10UnitCode;
        }

        public String getComposition10UnitName() {
            return composition10UnitName;
        }

        public void setComposition10UnitName(String composition10UnitName) {
            this.composition10UnitName = composition10UnitName;
        }

        public String getPoReq() {
            return poReq;
        }

        public void setPoReq(String poReq) {
            this.poReq = poReq;
        }

        public String getPoReqCode() {
            return poReqCode;
        }

        public void setPoReqCode(String poReqCode) {
            this.poReqCode = poReqCode;
        }

        public String getIndentReq() {
            return indentReq;
        }

        public void setIndentReq(String indentReq) {
            this.indentReq = indentReq;
        }

        public String getIndentReqCode() {
            return indentReqCode;
        }

        public void setIndentReqCode(String indentReqCode) {
            this.indentReqCode = indentReqCode;
        }

        public String getQuotationReq() {
            return quotationReq;
        }

        public void setQuotationReq(String quotationReq) {
            this.quotationReq = quotationReq;
        }

        public String getQuotationReqCode() {
            return quotationReqCode;
        }

        public void setQuotationReqCode(String quotationReqCode) {
            this.quotationReqCode = quotationReqCode;
        }

        public String getBarcodeActiveTag() {
            return barcodeActiveTag;
        }

        public void setBarcodeActiveTag(String barcodeActiveTag) {
            this.barcodeActiveTag = barcodeActiveTag;
        }

        public String getBarcodeActiveText() {
            return barcodeActiveText;
        }

        public void setBarcodeActiveText(String barcodeActiveText) {
            this.barcodeActiveText = barcodeActiveText;
        }

    }
    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public MedicineMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

//        node = new CSVData();
//        node.setHeader("tempCode");
//        node.setSample("S-01");
//        node.setInstruction("{alphanumeric;20c}");
//        lstCSV.add(node);
        node = new CSVData();
        node.setHeader("displayName");
        node.setSample("Calcol Plus");
        node.setInstruction("{unique;mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("medicineCategory");
        node.setSample("solution");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("medicineGroup");
        node.setSample("PAEDIATRICS");
        node.setInstruction("{mandatory;alphanumeric;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("hsn");
        node.setSample("hsn");
        node.setInstruction("{alphanumeric;1500c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("medicineCompany");
        node.setSample("ALNICHE");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("instructions");
        node.setSample("no instruction");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("regionalInstructions");
        node.setSample("any lang. instructions");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("manufacturer");
        node.setSample("Amit");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("division");
        node.setSample("division");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("packing");
        node.setSample("10 Tab");
        node.setInstruction("{mandatory;alphanumeric;15c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("restrictGroup");
        node.setSample("H1");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("saleDiscountAllow");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");

        lstCSV.add(node);
        node = new CSVData();
        node.setHeader("narcotics&ControlSubstances");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("consignmentItem");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("returnableItem");
        node.setSample("Yes");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("bestBefore");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("includeFreeQtyInSaleRate");
        node.setSample("Yes");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("lifeSaving");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("highValue");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("billedItem");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("saleRateEditable");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("allowNegativeStock");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("flowThrough");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("sku");
        node.setSample("strip");
        node.setInstruction("{mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("purchaseUnit");
        node.setSample("Strip");
        node.setInstruction("{mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("purchaseUnitConversionRate");
        node.setSample("10");
        node.setInstruction("{mandatory;numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("saleUnit");
        node.setSample("PCS");
        node.setInstruction("{mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("saleUnitConversionRate");
        node.setSample("1");
        node.setInstruction("{mandatory;numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("mrp");
        node.setSample("100");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("saleRate");
        node.setSample("90");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("purchaseRate");
        node.setSample("70");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("schemeOnQty");
        node.setSample("10");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("schemeFreeQty");
        node.setSample("10");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("purchaseDiscountPer");
        node.setSample("5");
        node.setInstruction("{numeric;4c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("saleDiscountPer");
        node.setSample("5");
        node.setInstruction("{numeric;4c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition1");
        node.setSample("SALBUTAMOL");
        node.setInstruction("{alphanumeric;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition1Power");
        node.setSample("5");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition1Unit");
        node.setSample("mg");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition2");
        node.setSample("HYDROXYZINE");
        node.setInstruction("{alphanumeric;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition2Power");
        node.setSample("5");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition2Unit");
        node.setSample("mg");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition3");
        node.setSample("ATENOLOL");
        node.setInstruction("{alphanumeric;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition3Power");
        node.setSample("5");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition3Unit");
        node.setSample("mg");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition4");
        node.setSample("CALCITROL");
        node.setInstruction("{alphanumeric;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition4Power");
        node.setSample("5");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition4Unit");
        node.setSample("mg");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition5");
        node.setSample("ATORVA");
        node.setInstruction("{alphanumeric;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition5Power");
        node.setSample("5");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition5Unit");
        node.setSample("mg");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition6");
        node.setSample("ANTI SNAKE VACCINE");
        node.setInstruction("{alphanumeric;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition6Power");
        node.setSample("5");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition6Unit");
        node.setSample("mg");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition7");
        node.setSample("SODIUM BICARBONATE");
        node.setInstruction("{alphanumeric;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition7Power");
        node.setSample("5");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition7Unit");
        node.setSample("mg");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition8");
        node.setSample("HYDROXYZINE");
        node.setInstruction("{alphanumeric;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition8Power");
        node.setSample("5");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition8Unit");
        node.setSample("mg");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition9");
        node.setSample("BABY LOTION");
        node.setInstruction("{alphanumeric;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition9Power");
        node.setSample("5");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition9Unit");
        node.setSample("mg");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition10");
        node.setSample("SALBUTAMOL");
        node.setInstruction("{alphanumeric;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition10Power");
        node.setSample("5");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("composition10Unit");
        node.setSample("mg");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("common");
        node.setSample("No");
        node.setInstruction("{mandatory;Yes/No;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("poReq");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("indentReq");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("quotationReq");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("barcodeActive");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Medicine Master");
        fileDetail.setServerClassName(MedicineMaster.class.getName());

        return fileDetail;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue;
        Object objValue;
        String[] csvFileFormat;
        DmImportStatus importStatus = null;
        int status = 0, rtnStatus = 0;
        try {
            this.rss = rss;

            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);

            if (DmShared.xNull(importData.getDataFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Data file path can not be blank.");
            } else {
                csvFileFormat = ImportData_Server.getCSVFormats(importData.getHmCSV()).get("header").split(",");
                dataFile = new File(importData.getDataFilePath());
                if (dataFile.exists()) {
                    csvReader = new DmCsvReader(dataFile);
                    csvReader.readLines();
                    status = ImportData_Server.checkHeader(csvFileFormat, csvReader, handelFileLog);
                    if (status == DmImportStatus.ERROR.getId()) {
                        throw new DmException(0, "");
                    }
                } else {
                    status = DmImportStatus.ERROR.getId();
                    throw new Exception("Data file not found.");
                }

                for (int i = 0; i < csvReader.getRows().size(); i++) {
                    lineNoDisplay = "Line " + String.valueOf(i + 1) + ": ";
                    dynamicGridRow = csvReader.getRows().get(i);
                    mrTemp = new Model();
                    isFound = true;

                    for (String headerName : csvFileFormat) {
                        objValue = dynamicGridRow.getCells().get(headerName.toLowerCase());
                        if (objValue == null) {
                            fieldValue = "";
                        } else {
                            fieldValue = String.valueOf(objValue);
                        }
//                        if (headerName.trim().equalsIgnoreCase("tempCode")) {
//                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 15, lineNoDisplay, handelFileLog)) {
//                                isFound = false;
//                            } else {
//                                mrTemp.setTempCode(DmShared.removeSpace(fieldValue));
//                            }
                        if (headerName.trim().equalsIgnoreCase("displayName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDisplayName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("medicineCategory")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCategoryName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("medicineGroup")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMedGroupName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("hsn")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 1500, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setHsnName(DmShared.removeSpace(fieldValue));
                            }

                        } else if (headerName.trim().equalsIgnoreCase("medicineCompany")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMedcompanyName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition1")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition1Name(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition1Power")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition1Power(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition1Unit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {

                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition1UnitName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition2")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition2Name(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition2Power")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition2Power(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition2Unit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition2UnitName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition3")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition3Name(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition3Power")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition3Power(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition3Unit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition3UnitName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition4")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition4Name(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition4Power")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition4Power(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition4Unit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition4UnitName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition5")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition5Name(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition5Power")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition5Power(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition5Unit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition5UnitName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition6")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition6Name(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition6Power")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition6Power(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition6Unit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition6UnitName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition7")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {

                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition7Name(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition7Power")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition7Power(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition7Unit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition7UnitName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition8")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition8Name(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition8Power")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition8Power(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition8Unit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition8UnitName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition9")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition9Name(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition9Power")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition9Power(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition9Unit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition9UnitName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition10")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition10Name(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition10Power")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition10Power(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("composition10Unit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setComposition10UnitName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("instructions")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setInstructions(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("regionalInstructions")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRegionalInstructions(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("manufacturer")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setManufacturerName(DmShared.removeSpace(fieldValue));
                            }

                        } else if (headerName.trim().equalsIgnoreCase("division")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDivisionName(DmShared.removeSpace(fieldValue));
                            }

                        } else if (headerName.trim().equalsIgnoreCase("packing")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 15, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPacking(DmShared.removeSpace(fieldValue));
                            }

                        } else if (headerName.trim().equalsIgnoreCase("restrictGroup")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRestrictName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("saleDiscountAllow")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSaleDiscountAllowName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("narcotics&ControlSubstances")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNarcotics(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("consignmentItem")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setConsignmentItem(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("returnableItem")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setReturnItem(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("bestBefore")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setBestBefore(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("includeFreeQtyInSaleRate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setFreeQty(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("lifeSaving")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLifeSaving(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("highValue")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setHighValue(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("billedItem")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setBilledItem(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("saleRateEditable")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSaleRateEditName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("allowNegativeStock")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAllowNegativeStock(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("flowThrough")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setFlowThrough(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("sku")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSKU(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("purchaseUnit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPurchaseUnit(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("purchaseUnitConversionRate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPurchaseUnitConversionRate(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("saleUnit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSaleUnit(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("saleUnitConversionRate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSaleUnitConversionRate(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("common")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCommonText(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("mrp")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMrp(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("saleRate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSaleRate(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("purchaseRate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPurchaseRate(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("schemeOnQty")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSchemeOnQty(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("schemeFreeQty")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSchemeFreeQty(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("purchaseDiscountPer")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 4, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPurchaseDiscountPer(DmShared.removeSpace(fieldValue));
                            }

                        } else if (headerName.trim().equalsIgnoreCase("saleDiscountPer")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 4, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPurchaseDiscountPer(DmShared.removeSpace(fieldValue));
                            }

                        } else if (headerName.trim().equalsIgnoreCase("poReq")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPoReq(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("indentReq")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIndentReq(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("quotationReq")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setQuotationReq(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("barcodeActiveText")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setBarcodeActiveText(DmShared.removeSpace(fieldValue));
                            }
                        }
                    }
                    if (isFound) {
                        status = saveMedicineMaster(mrTemp);
                    }
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                }
            }
        } catch (Exception e) {

            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                handelFileLog.writeLine(e.getMessage());
            } catch (Exception ex) {

            }
        } finally {
            try {
                importStatus = DmImportStatus.getImportStatus(rtnStatus);
                if (csvReader != null) {
                    csvReader = null;
                }
                if (dataFile != null) {
                    dataFile = null;
                }
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception e) {
            }
        }
        return importStatus;
    }

    private int saveMedicineMaster(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, vType = "";
        CallableStatement csTemp = null;
        int status = 0, rtnStatus = 0;
        int snoMedicineComposition = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setCode(DmServer.getSerialNoBigInt(rss, "itemMast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            mrTemp.setSKUCode(DmServer.getCodeViaManualCode(mrTemp.getSKU(), "unit", "unitMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getSKUCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getSKU()) + " SKU does not exist");

            }
            if (!DmShared.xNull(mrTemp.getMedcompanyName()).equalsIgnoreCase("")) {
                sqlQuery = "select code,shortName  "
                        + " from itemMakeMast imm "
                        + " where companyName = :companyName ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("companyName", DmShared.xNull(mrTemp.getMedcompanyName()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setMedCompanyCode(DmShared.xNull(rsTemp.getString("code")));
                    mrTemp.setMedCompShortName(DmShared.xNull(rsTemp.getString("shortName")));
                }
                rsTemp.close();
                psTemp.close();

            }

            if (DmShared.iNull(mrTemp.getMedCompanyCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getMedcompanyName()) + " Medicine company does not exist");
            }

            mrTemp.setCategoryCode(DmServer.getCodeViaManualCode(mrTemp.getCategoryName(), "name", "itemCat", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getCategoryCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getCategoryName()) + " Medicine Category does not exist");
            }
            mrTemp.setComposition1Code(DmServer.getCodeViaManualCode(mrTemp.getComposition1Name(), "name", "medicineCompoMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getComposition1Code()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getComposition1Name()) + "  Medicine composition1 does not exist");
            }
            if (DmShared.vNull(mrTemp.getComposition1Power()).doubleValue() > 0) {
                mrTemp.setComposition1UnitCode(DmServer.getCodeViaManualCode(mrTemp.getComposition1UnitName(), "unit", "unitMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getComposition1UnitCode()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getComposition1UnitName()) + " Unit does not exist");

                }
            } else if (!DmShared.xNull(mrTemp.getComposition1UnitName()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "composition1Power cannot be blank as power unit '" + mrTemp.getComposition1UnitName() + "' exists.");
            }

            if (!DmShared.xNull(mrTemp.getComposition2Name()).equalsIgnoreCase("")) {
                mrTemp.setComposition2Code(DmServer.getCodeViaManualCode(mrTemp.getComposition2Name(), "name", "medicineCompoMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getComposition2Code()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getComposition2Name()) + "  Medicine composition2 does not exist");
                } else {
                    if (DmShared.iNull(mrTemp.getComposition1Code()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition2Name()) + " cannot be inserted. Medicine composition1 cannot be blank.");
                    }
                }
            }
            if (DmShared.vNull(mrTemp.getComposition2Power()).doubleValue() > 0) {
                if (!DmShared.xNull(mrTemp.getComposition2UnitName()).equalsIgnoreCase("")) {
                    mrTemp.setComposition2UnitCode(DmServer.getCodeViaManualCode(mrTemp.getComposition2UnitName(), "unit", "unitMast", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getComposition2UnitCode()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition2UnitName()) + " Unit does not exist");

                    } else {
                        if (DmShared.vNull(mrTemp.getComposition1Power()).doubleValue() > 0) {
                            if (DmShared.iNull(mrTemp.getComposition1UnitCode()).longValue() == 0) {
                                status = DmImportStatus.ERROR.getId();
                                throw new DmException(0, DmShared.xNull(mrTemp.getComposition2UnitName()) + " cannot be inserted. Medicine composition1 unit cannot be blank.");
                            }
                        }
                    }
                }
            } else if (!DmShared.xNull(mrTemp.getComposition2UnitName()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "composition2Power cannot be blank as power unit '" + mrTemp.getComposition2UnitName() + "' exists.");
            }

            if (!DmShared.xNull(mrTemp.getComposition3Name()).equalsIgnoreCase("")) {
                mrTemp.setComposition3Code(DmServer.getCodeViaManualCode(mrTemp.getComposition3Name(), "name", "medicineCompoMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getComposition3Code()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getComposition3Name()) + "  Medicine composition3 does not exist");
                } else {
                    if (DmShared.iNull(mrTemp.getComposition1Code()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition3Name()) + " cannot be inserted. Medicine composition1 cannot be blank.");
                    }
                }

            }
            if (DmShared.vNull(mrTemp.getComposition3Power()).doubleValue() > 0) {
                if (!DmShared.xNull(mrTemp.getComposition3UnitName()).equalsIgnoreCase("")) {
                    mrTemp.setComposition3UnitCode(DmServer.getCodeViaManualCode(mrTemp.getComposition3UnitName(), "unit", "unitMast", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getComposition3UnitCode()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition3UnitName()) + " Unit does not exist");

                    } else {
                        if (DmShared.vNull(mrTemp.getComposition1Power()).doubleValue() > 0) {
                            if (DmShared.iNull(mrTemp.getComposition1UnitCode()).longValue() == 0) {
                                status = DmImportStatus.ERROR.getId();
                                throw new DmException(0, DmShared.xNull(mrTemp.getComposition3UnitName()) + " cannot be inserted. Medicine composition1 unit cannot be blank.");
                            }
                        }
                    }
                }
            } else if (!DmShared.xNull(mrTemp.getComposition3UnitName()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "composition3Power cannot be blank as power unit '" + mrTemp.getComposition3UnitName() + "' exists.");
            }
            if (!DmShared.xNull(mrTemp.getComposition4Name()).equalsIgnoreCase("")) {
                mrTemp.setComposition4Code(DmServer.getCodeViaManualCode(mrTemp.getComposition4Name(), "name", "medicineCompoMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getComposition4Code()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getComposition4Name()) + "  Medicine composition4 does not exist");
                } else {
                    if (DmShared.iNull(mrTemp.getComposition1Code()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition4Name()) + " cannot be inserted. Medicine composition1 cannot be blank.");
                    }
                }
            }

            if (DmShared.vNull(mrTemp.getComposition4Power()).doubleValue() > 0) {
                if (!DmShared.xNull(mrTemp.getComposition4UnitName()).equalsIgnoreCase("")) {
                    mrTemp.setComposition4UnitCode(DmServer.getCodeViaManualCode(mrTemp.getComposition4UnitName(), "unit", "unitMast", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getComposition4UnitCode()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition4UnitName()) + " Unit does not exist");

                    } else {
                        if (DmShared.vNull(mrTemp.getComposition1Power()).doubleValue() > 0) {
                            if (DmShared.iNull(mrTemp.getComposition1UnitCode()).longValue() == 0) {
                                status = DmImportStatus.ERROR.getId();
                                throw new DmException(0, DmShared.xNull(mrTemp.getComposition4UnitName()) + " cannot be inserted. Medicine composition1 unit cannot be blank.");
                            }
                        }
                    }
                }
            } else if (!DmShared.xNull(mrTemp.getComposition4UnitName()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "composition4Power cannot be blank as power unit '" + mrTemp.getComposition4UnitName() + "' exists.");
            }

            if (!DmShared.xNull(mrTemp.getComposition5Name()).equalsIgnoreCase("")) {
                mrTemp.setComposition5Code(DmServer.getCodeViaManualCode(mrTemp.getComposition5Name(), "name", "medicineCompoMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getComposition5Code()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getComposition5Name()) + "  Medicine composition5 does not exist");
                } else {
                    if (DmShared.iNull(mrTemp.getComposition1Code()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition5Name()) + " cannot be inserted. Medicine composition1 cannot be blank.");
                    }
                }
            }

            if (DmShared.vNull(mrTemp.getComposition5Power()).doubleValue() > 0) {
                if (!DmShared.xNull(mrTemp.getComposition5UnitName()).equalsIgnoreCase("")) {
                    mrTemp.setComposition5UnitCode(DmServer.getCodeViaManualCode(mrTemp.getComposition5UnitName(), "unit", "unitMast", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getComposition5UnitCode()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition5UnitName()) + " Unit does not exist");

                    } else {
                        if (DmShared.vNull(mrTemp.getComposition1Power()).doubleValue() > 0) {
                            if (DmShared.iNull(mrTemp.getComposition1UnitCode()).longValue() == 0) {
                                status = DmImportStatus.ERROR.getId();
                                throw new DmException(0, DmShared.xNull(mrTemp.getComposition5UnitName()) + " cannot be inserted. Medicine composition1 unit cannot be blank.");
                            }
                        }
                    }
                }
            } else if (!DmShared.xNull(mrTemp.getComposition5UnitName()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "composition5Power cannot be blank as power unit '" + mrTemp.getComposition5UnitName() + "' exists.");
            }

            if (!DmShared.xNull(mrTemp.getComposition6Name()).equalsIgnoreCase("")) {
                mrTemp.setComposition6Code(DmServer.getCodeViaManualCode(mrTemp.getComposition6Name(), "name", "medicineCompoMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getComposition6Code()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getComposition6Name()) + "  Medicine composition6 does not exist");
                } else {
                    if (DmShared.iNull(mrTemp.getComposition1Code()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition6Name()) + " cannot be inserted. Medicine composition1 cannot be blank.");
                    }
                }
            }
            if (DmShared.vNull(mrTemp.getComposition6Power()).doubleValue() > 0) {
                if (!DmShared.xNull(mrTemp.getComposition6UnitName()).equalsIgnoreCase("")) {
                    mrTemp.setComposition6UnitCode(DmServer.getCodeViaManualCode(mrTemp.getComposition6UnitName(), "unit", "unitMast", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getComposition6UnitCode()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition6UnitName()) + " Unit does not exist");

                    } else {
                        if (DmShared.vNull(mrTemp.getComposition1Power()).doubleValue() > 0) {
                            if (DmShared.iNull(mrTemp.getComposition1UnitCode()).longValue() == 0) {
                                status = DmImportStatus.ERROR.getId();
                                throw new DmException(0, DmShared.xNull(mrTemp.getComposition6UnitName()) + " cannot be inserted. Medicine composition1 unit cannot be blank.");
                            }
                        }
                    }
                }
            } else if (!DmShared.xNull(mrTemp.getComposition6UnitName()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "composition6Power cannot be blank as power unit '" + mrTemp.getComposition6UnitName() + "' exists.");
            }

            if (!DmShared.xNull(mrTemp.getComposition7Name()).equalsIgnoreCase("")) {
                mrTemp.setComposition7Code(DmServer.getCodeViaManualCode(mrTemp.getComposition7Name(), "name", "medicineCompoMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getComposition7Code()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getComposition7Name()) + "  Medicine composition7 does not exist");
                } else {
                    if (DmShared.iNull(mrTemp.getComposition1Code()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition7Name()) + " cannot be inserted. Medicine composition1 cannot be blank.");
                    }
                }
            }
            if (DmShared.vNull(mrTemp.getComposition7Power()).doubleValue() > 0) {
                if (!DmShared.xNull(mrTemp.getComposition7UnitName()).equalsIgnoreCase("")) {
                    mrTemp.setComposition7UnitCode(DmServer.getCodeViaManualCode(mrTemp.getComposition7UnitName(), "unit", "unitMast", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getComposition7UnitCode()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition7UnitName()) + " Unit does not exist");

                    } else {
                        if (DmShared.vNull(mrTemp.getComposition1Power()).doubleValue() > 0) {
                            if (DmShared.iNull(mrTemp.getComposition1UnitCode()).longValue() == 0) {
                                status = DmImportStatus.ERROR.getId();
                                throw new DmException(0, DmShared.xNull(mrTemp.getComposition7UnitName()) + " cannot be inserted. Medicine composition1 unit cannot be blank.");
                            }
                        }
                    }
                }
            } else if (!DmShared.xNull(mrTemp.getComposition7UnitName()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "composition7Power cannot be blank as power unit '" + mrTemp.getComposition7UnitName() + "' exists.");
            }

            if (!DmShared.xNull(mrTemp.getComposition8Name()).equalsIgnoreCase("")) {
                mrTemp.setComposition8Code(DmServer.getCodeViaManualCode(mrTemp.getComposition8Name(), "name", "medicineCompoMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getComposition8Code()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getComposition8Name()) + "  Medicine composition8 does not exist");
                } else {
                    if (DmShared.iNull(mrTemp.getComposition1Code()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition8Name()) + " cannot be inserted. Medicine composition1 cannot be blank.");
                    }
                }
            }
            if (DmShared.vNull(mrTemp.getComposition8Power()).doubleValue() > 0) {
                if (!DmShared.xNull(mrTemp.getComposition8UnitName()).equalsIgnoreCase("")) {
                    mrTemp.setComposition8UnitCode(DmServer.getCodeViaManualCode(mrTemp.getComposition8UnitName(), "unit", "unitMast", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getComposition8UnitCode()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition8UnitName()) + " Unit does not exist");

                    } else {
                        if (DmShared.vNull(mrTemp.getComposition1Power()).doubleValue() > 0) {
                            if (DmShared.iNull(mrTemp.getComposition1UnitCode()).longValue() == 0) {
                                status = DmImportStatus.ERROR.getId();
                                throw new DmException(0, DmShared.xNull(mrTemp.getComposition8UnitName()) + " cannot be inserted. Medicine composition1 unit cannot be blank.");
                            }
                        }
                    }
                }
            } else if (!DmShared.xNull(mrTemp.getComposition8UnitName()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "composition8Power cannot be blank as power unit '" + mrTemp.getComposition8UnitName() + "' exists.");
            }

            if (!DmShared.xNull(mrTemp.getComposition9Name()).equalsIgnoreCase("")) {
                mrTemp.setComposition9Code(DmServer.getCodeViaManualCode(mrTemp.getComposition9Name(), "name", "medicineCompoMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getComposition9Code()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getComposition9Name()) + "  Medicine composition9 does not exist");
                } else {
                    if (DmShared.iNull(mrTemp.getComposition1Code()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition9Name()) + " cannot be inserted. Medicine composition1 cannot be blank.");
                    }
                }
            }

            if (DmShared.vNull(mrTemp.getComposition9Power()).doubleValue() > 0) {
                if (!DmShared.xNull(mrTemp.getComposition9UnitName()).equalsIgnoreCase("")) {
                    mrTemp.setComposition9UnitCode(DmServer.getCodeViaManualCode(mrTemp.getComposition9UnitName(), "unit", "unitMast", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getComposition9UnitCode()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition9UnitName()) + " Unit does not exist");

                    } else {
                        if (DmShared.vNull(mrTemp.getComposition1Power()).doubleValue() > 0) {
                            if (DmShared.iNull(mrTemp.getComposition1UnitCode()).longValue() == 0) {

                                status = DmImportStatus.ERROR.getId();
                                throw new DmException(0, DmShared.xNull(mrTemp.getComposition9UnitName()) + " cannot be inserted. Medicine composition1 unit cannot be blank.");
                            }
                        }
                    }
                }
            } else if (!DmShared.xNull(mrTemp.getComposition9UnitName()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "composition9Power cannot be blank as power unit '" + mrTemp.getComposition9UnitName() + "' exists.");
            }

            if (!DmShared.xNull(mrTemp.getComposition10Name()).equalsIgnoreCase("")) {
                mrTemp.setComposition10Code(DmServer.getCodeViaManualCode(mrTemp.getComposition10Name(), "name", "medicineCompoMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getComposition10Code()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getComposition10Name()) + "  Medicine composition10 does not exist");
                } else {
                    if (DmShared.iNull(mrTemp.getComposition1Code()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition10Name()) + " cannot be inserted. Medicine composition1 cannot be blank.");
                    }
                }
            }
            if (DmShared.vNull(mrTemp.getComposition10Power()).doubleValue() > 0) {
                if (!DmShared.xNull(mrTemp.getComposition10UnitName()).equalsIgnoreCase("")) {
                    mrTemp.setComposition10UnitCode(DmServer.getCodeViaManualCode(mrTemp.getComposition10UnitName(), "unit", "unitMast", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getComposition10UnitCode()).longValue() == 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, DmShared.xNull(mrTemp.getComposition10UnitName()) + " Unit does not exist");

                    } else {
                        if (DmShared.vNull(mrTemp.getComposition1Power()).doubleValue() > 0) {
                            if (DmShared.iNull(mrTemp.getComposition1UnitCode()).longValue() == 0) {
                                status = DmImportStatus.ERROR.getId();
                                throw new DmException(0, DmShared.xNull(mrTemp.getComposition10UnitName()) + " cannot be inserted. Medicine composition1 unit cannot be blank.");
                            }
                        }
                    }
                } else {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getComposition10UnitName()) + " cannot be blank.");
                }
            } else if (!DmShared.xNull(mrTemp.getComposition10UnitName()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "composition10Power cannot be blank as power unit '" + mrTemp.getComposition10UnitName() + "' exists.");
            }

            mrTemp.setMedGroupCode(DmServer.getCodeViaManualCode(mrTemp.getMedGroupName(), "name", "itemGroup", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getMedGroupCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getMedGroupName()) + "  Medicine Group does not exist");
            }
            mrTemp.setPurchaseUnitCode(DmServer.getCodeViaManualCode(mrTemp.getPurchaseUnit(), "unit", "unitMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getPurchaseUnitCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getPurchaseUnit()) + " Purchase Unit does not exist");
            }

            mrTemp.setSaleUnitCode(DmServer.getCodeViaManualCode(mrTemp.getSaleUnit(), "unit", "unitMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getSaleUnitCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getSaleUnit()) + " Sale Unit does not exist");
            }
            if (!DmShared.xNull(mrTemp.getRestrictName()).equalsIgnoreCase("")) {
                mrTemp.setRestrictCode(DmServer.getCodeViaManualCode(mrTemp.getRestrictName(), "name", "restrictedGrpMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getRestrictCode()).intValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getRestrictName() + " not inserted. Restrict Group not exists.");
                }
            }
            //Manufacturer Code
            if (!DmShared.xNull(mrTemp.getManufacturerName()).equalsIgnoreCase("")) {
                mrTemp.setManufacturerCode(DmServer.getCodeViaManualCode(mrTemp.getManufacturerName(), "name", "subGroup", "subCode", cnTemp));
                if (DmShared.iNull(mrTemp.getManufacturerCode()).longValue() == 0) {;
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getManufacturerName() + " not inserted. Manufacturer not exists.");
                }
            }
            //Division Code
            if (!DmShared.xNull(mrTemp.getDivisionName()).equalsIgnoreCase("")) {
                mrTemp.setDivisionCode(DmServer.getCodeViaManualCode(mrTemp.getDivisionName(), "name", "subGroup", "subCode", cnTemp));
                if (DmShared.iNull(mrTemp.getDivisionCode()).longValue() == 0) {;
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getDivisionName() + " not inserted. Division not exists.");
                }
            }

            //Sale Rate
            if (DmShared.vNull(mrTemp.getSaleRate()).doubleValue() > DmShared.vNull(mrTemp.getMrp()).doubleValue()) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Sale Rate cannot be greater than MRP.");
            }

            //Purchase Rate
            if (DmShared.vNull(mrTemp.getPurchaseRate()).doubleValue() > DmShared.vNull(mrTemp.getMrp()).doubleValue()) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Purchase Rate cannot be greater than MRP.");
            }

            //Scheme On Qty
            if (DmShared.vNull(mrTemp.getSchemeOnQty()).doubleValue() != 0 && DmShared.vNull(mrTemp.getSchemeFreeQty()).doubleValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "schemeOnQty not inserted. Scheme Free Qty. is required.");
            }

            //Scheme On Qty
            if (DmShared.vNull(mrTemp.getSchemeOnQty()).doubleValue() == 0 && DmShared.vNull(mrTemp.getSchemeFreeQty()).doubleValue() != 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "schemeFreeQty not inserted. Scheme On Qty. is required.");
            }

            mrTemp.setName(DmShared.xNull(mrTemp.getDisplayName()).trim() + "-" + DmShared.xNull(mrTemp.getCategoryName()).trim() + "-" + DmShared.xNull(mrTemp.getMedCompShortName()).trim() + " (Packing: " + DmShared.xNull(mrTemp.getPacking()).trim() + ")");
            sqlQuery = " Select Count(Name) As Cnt From itemMast mm Where mm.Name = :Name And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Name", mrTemp.getName());
            psTemp.setString("Code", mrTemp.getCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("Cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getName()) + "  Medicine Name already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " Select Count(ManualCode) As Cnt From itemMast mm Where mm.ManualCode = :ManualCode And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("manualCode", mrTemp.getManualCode());
            psTemp.setString("Code", mrTemp.getCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("Cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException("Manual Code already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT vt.v_Type  FROM voucher_type vt WHERE  vt.category = " + vCat + "  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();
            // ************************************** 
            if (!DmShared.xNull(mrTemp.getHsnName()).equalsIgnoreCase("")) {
                sqlQuery = "select code,itemGSTNature  "
                        + " from hsnMast hsn "
                        + " where name = :name ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", DmShared.xNull(mrTemp.getHsnName()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setHsnCode(DmShared.xNull(rsTemp.getString("code")));
                    mrTemp.setGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(mrTemp.getHsnCode()).intValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getHsnName() + " not inserted. HSN not exists.");
                }
            }

            //=======================================================
            if (DmShared.xNull(mrTemp.getConsignmentItem()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setConsignmentItemCode("1");
            } else if (DmShared.xNull(mrTemp.getConsignmentItem()).trim().equalsIgnoreCase("No")) {
                mrTemp.setConsignmentItemCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getBestBefore()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setBestBeforeCode("1");
            } else if (DmShared.xNull(mrTemp.getBestBefore()).trim().equalsIgnoreCase("No")) {
                mrTemp.setBestBeforeCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getLifeSaving()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setLifeSavingCode("1");
            } else if (DmShared.xNull(mrTemp.getLifeSaving()).trim().equalsIgnoreCase("No")) {
                mrTemp.setLifeSavingCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getNarcotics()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setNarcoticsCode("1");
            } else if (DmShared.xNull(mrTemp.getNarcotics()).trim().equalsIgnoreCase("No")) {
                mrTemp.setNarcoticsCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getFlowThrough()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setFlowThroughCode("1");
            } else if (DmShared.xNull(mrTemp.getFlowThrough()).trim().equalsIgnoreCase("No")) {
                mrTemp.setFlowThroughCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getHighValue()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setHighValueCode("1");
            } else if (DmShared.xNull(mrTemp.getHighValue()).trim().equalsIgnoreCase("No")) {
                mrTemp.setHighValueCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getReturnItem()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setReturnItemCode("1");
            } else if (DmShared.xNull(mrTemp.getReturnItem()).trim().equalsIgnoreCase("No")) {
                mrTemp.setReturnItemCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getBilledItem()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setBilledItemCode("1");
            } else if (DmShared.xNull(mrTemp.getBilledItem()).trim().equalsIgnoreCase("No")) {
                mrTemp.setBilledItemCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getAllowNegativeStock()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setAllowNegativeStockCode("1");
            } else if (DmShared.xNull(mrTemp.getAllowNegativeStock()).trim().equalsIgnoreCase("No")) {
                mrTemp.setAllowNegativeStockCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getFreeQty()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setFreeQtyCode("1");
            } else if (DmShared.xNull(mrTemp.getFreeQty()).trim().equalsIgnoreCase("No")) {
                mrTemp.setFreeQtyCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getSaleRateEditName()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setSaleRateEditCode("1");
            } else if (DmShared.xNull(mrTemp.getSaleRateEditName()).trim().equalsIgnoreCase("No")) {
                mrTemp.setSaleRateEditCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getSaleDiscountAllowName()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setSaleDiscountAllowCode("1");
            } else if (DmShared.xNull(mrTemp.getSaleDiscountAllowName()).trim().equalsIgnoreCase("No")) {
                mrTemp.setSaleDiscountAllowCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getCommonText()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setCommonTag("1");
            } else if (DmShared.xNull(mrTemp.getCommonText()).trim().equalsIgnoreCase("No")) {
                mrTemp.setCommonTag("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getPoReq()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setPoReqCode("1");
            } else if (DmShared.xNull(mrTemp.getPoReq()).trim().equalsIgnoreCase("No")) {
                mrTemp.setPoReqCode("0");
            }
            if (DmShared.xNull(mrTemp.getIndentReq()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setIndentReqCode("1");
            } else if (DmShared.xNull(mrTemp.getIndentReq()).trim().equalsIgnoreCase("No")) {
                mrTemp.setIndentReqCode("0");
            }
            if (DmShared.xNull(mrTemp.getQuotationReq()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setQuotationReqCode("1");
            } else if (DmShared.xNull(mrTemp.getQuotationReq()).trim().equalsIgnoreCase("No")) {
                mrTemp.setQuotationReqCode("0");
            }
            if (DmShared.xNull(mrTemp.getBarcodeActiveText()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setBarcodeActiveTag("1");
            } else if (DmShared.xNull(mrTemp.getBarcodeActiveText()).trim().equalsIgnoreCase("No")) {
                mrTemp.setBarcodeActiveTag("0");
            }
            //---------------------------------------------------------------

            // ************************************** GET MANUAL CODE *********************************************
            HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
            fieldUDTHashMap.put("itemName", new FieldUDT_Mdl("itemName", DmShared.xNull(mrTemp.getName()), DmShared.xNull(mrTemp.getName()).trim()));
            fieldUDTHashMap.put("itemGroup", new FieldUDT_Mdl("itemGroup", DmShared.xNull(mrTemp.getMedGroupName()).toString(), DmShared.xNull(mrTemp.getMedGroupName()).trim()));
            fieldUDTHashMap.put("itemCat", new FieldUDT_Mdl("itemCat", DmShared.xNull(mrTemp.getCategoryName()).toString(), DmShared.xNull(mrTemp.getCategoryName()).trim()));
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

            sqlQuery = "INSERT INTO itemmast (CODE, NAME, itemGroup, itemType,filterItemType, sku, "
                    + " preparedBY, u_EntDt, transferDt, itemMakeCode, isActive, compositionCode, itemCategoryCode, restrictGroup,"
                    + " manualCode, hsnCode,isBestBefore,isLifeSaving,isHighValue,isFlowThrough,  "
                    + " isNarcotics,isConsignment,isReturnableItem,isBilledItem,propertyList,itemGSTNature,purchaseUnit,saleUnit,"
                    + " purchaseUnitConversionRate,saleUnitConversionRate,allowNegativeStock,displayName,isSaleRateEditable,isIncludeFreeQtyInSaleRate,"
                    + " isDiscountAllow,isCommon,manufacturerCode,instruction,regionalInstruction,itemPacking, divisionCode, "
                    + " mrp, saleRate, purchaseRate, schemeOnQty, schemeFreeQty, purchaseDiscountPer,saleDiscountPer, "
                    + " isPOMandatory, isIndentMandatory, isQuotationMandatory, isBarcodeActive)  "
                    + " VALUES (:code, :name, :itemGroup, :itemType,:filterItemType,:sku, "
                    + " :preparedBY,:u_EntDt, NULL, :itemMakeCode, :isActive, :compositionCode, :itemCategoryCode, :restrictGroup,"
                    + " :manualCode,:hsnCode,:isBestBefore,:isLifeSaving,:isHighValue,:isFlowThrough, "
                    + " :isNarcotics,:isConsignment,:isReturnableItem,:isBilledItem,:propertyList,:itemGSTNature,:purchaseUnit,:saleUnit,"
                    + " :purchaseUnitConversionRate,:saleUnitConversionRate,:allowNegativeStock,:displayName,:isSaleRateEditable,:isIncludeFreeQtyInSaleRate,"
                    + " :isDiscountAllow,:isCommon,:manufacturerCode,:instruction,:regionalInstruction,:itemPacking, :divisionCode, "
                    + " :mrp, :saleRate, :purchaseRate, :schemeOnQty, :schemeFreeQty, :purchaseDiscountPer,:saleDiscountPer, "
                    + " :isPOMandatory, :isIndentMandatory, :isQuotationMandatory, :isBarcodeActive) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()).trim());
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()).trim());
            psTemp.setString("itemGroup", DmShared.foriegnKey(mrTemp.getMedGroupCode()));
            psTemp.setString("manualCode", mrTemp.getManualCode());
            psTemp.setString("itemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp));
            psTemp.setString("sku", DmShared.foriegnKey(mrTemp.getSKUCode()));
            psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getMedCompanyCode()));
            psTemp.setString("isActive", DmShared.xNull("1"));
            psTemp.setString("compositionCode", DmShared.foriegnKey(mrTemp.getComposition1Code()));
            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryCode()));
            psTemp.setString("restrictGroup", DmShared.foriegnKey(mrTemp.getRestrictCode()));
            psTemp.setString("hsnCode", DmShared.foriegnKey(mrTemp.getHsnCode()));
            psTemp.setString("isBestBefore", DmServer.vNull(mrTemp.getBestBeforeCode(), "0"));
            psTemp.setString("isLifeSaving", DmServer.vNull(mrTemp.getLifeSavingCode(), "0"));
            psTemp.setString("isHighValue", DmServer.vNull(mrTemp.getHighValueCode(), "0"));
            psTemp.setString("isFlowThrough", DmServer.vNull(mrTemp.getFlowThroughCode(), "0"));
            psTemp.setString("isNarcotics", DmServer.vNull(mrTemp.getNarcoticsCode(), "0"));
            psTemp.setString("isConsignment", DmServer.vNull(mrTemp.getConsignmentItemCode(), "0"));
            psTemp.setString("isReturnableItem", DmServer.vNull(mrTemp.getReturnItemCode(), "0"));
            psTemp.setString("isBilledItem", DmServer.vNull(mrTemp.getBilledItemCode(), "0"));
            psTemp.setString("allowNegativeStock", DmServer.vNull(mrTemp.getAllowNegativeStockCode(), "0"));
            psTemp.setString("isSaleRateEditable", DmServer.vNull(mrTemp.getSaleRateEditCode(), "0"));
            psTemp.setString("isIncludeFreeQtyInSaleRate", DmServer.vNull(mrTemp.getFreeQtyCode(), "0"));
            psTemp.setString("propertyList", DmShared.xNull("ED,MRP,BN"));
            psTemp.setString("preparedBY", DmServer.getSsnUserName(rss));
            psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNature()));
            psTemp.setString("purchaseUnit", DmShared.foriegnKey(mrTemp.getPurchaseUnitCode()));
            psTemp.setString("saleUnit", DmShared.foriegnKey(mrTemp.getSaleUnitCode()));
            psTemp.setString("isPOMandatory", DmServer.vNull(mrTemp.getPoReqCode(), "0"));
            psTemp.setString("isIndentMandatory", DmServer.vNull(mrTemp.getIndentReqCode(), "0"));
            psTemp.setString("isQuotationMandatory", DmServer.vNull(mrTemp.getQuotationReqCode(), "0"));
            psTemp.setString("isBarcodeActive", DmServer.vNull(mrTemp.getBarcodeActiveTag(), "0"));
            if ((DmShared.iNull(mrTemp.getSKUCode()).intValue() == DmShared.iNull(mrTemp.getPurchaseUnitCode()).intValue())
                    && (DmShared.iNull(mrTemp.getSKUCode()).intValue() == DmShared.iNull(mrTemp.getSaleUnitCode()).intValue())) {
                mrTemp.setPurchaseUnitConversionRate("1");
                mrTemp.setSaleUnitConversionRate("1");
            } else if (DmShared.iNull(mrTemp.getSKUCode()).intValue() == DmShared.iNull(mrTemp.getPurchaseUnitCode()).intValue()) {
                mrTemp.setPurchaseUnitConversionRate("1");
            } else if (DmShared.iNull(mrTemp.getSKUCode()).intValue() == DmShared.iNull(mrTemp.getSaleUnitCode()).intValue()) {
                mrTemp.setSaleUnitConversionRate("1");
            } else if (DmShared.iNull(mrTemp.getPurchaseUnitCode()).intValue() == DmShared.iNull(mrTemp.getSaleUnitCode()).intValue()) {
                mrTemp.setPurchaseUnitConversionRate("1");
                mrTemp.setSaleUnitConversionRate("1");
            }
            psTemp.setString("purchaseUnitConversionRate", DmServer.vNull(mrTemp.getPurchaseUnitConversionRate()));
            psTemp.setString("saleUnitConversionRate", DmServer.vNull(mrTemp.getSaleUnitConversionRate()));
            psTemp.setString("isDiscountAllow", DmServer.vNull(mrTemp.getSaleDiscountAllowCode(), "0"));
            psTemp.setString("isCommon", DmServer.vNull(mrTemp.getCommonTag(), "0"));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.setString("manufacturerCode", DmShared.foriegnKey(mrTemp.getManufacturerCode()));
            psTemp.setString("divisionCode", DmShared.foriegnKey(mrTemp.getDivisionCode()));
            psTemp.setString("instruction", DmShared.xNull(mrTemp.getInstructions()).trim());
            psTemp.setString("regionalInstruction", DmShared.xNull(mrTemp.getRegionalInstructions()).trim());
            psTemp.setString("itemPacking", DmShared.xNull(mrTemp.getPacking()).trim());
            psTemp.setString("mrp", DmServer.vNull(mrTemp.getMrp(), "0.00"));
            psTemp.setString("saleRate", DmServer.vNull(mrTemp.getSaleRate(), "0.00"));
            psTemp.setString("purchaseRate", DmServer.vNull(mrTemp.getPurchaseRate(), "0.00"));
            psTemp.setString("schemeOnQty", DmServer.vNull(mrTemp.getSchemeOnQty()));
            psTemp.setString("schemeFreeQty", DmServer.vNull(mrTemp.getSchemeFreeQty()));
            psTemp.setString("purchaseDiscountPer", DmServer.vNull(mrTemp.getPurchaseDiscountPer(), "0.00"));
            psTemp.setString("saleDiscountPer", DmServer.vNull(mrTemp.getSaleDiscountPer(), "0.00"));
//            psTemp.setString("tempCode", DmShared.xNull(mrTemp.getTempCode()).trim());
            psTemp.executeUpdate();
            psTemp.close();

            /*===========================< Insert record in itemSiteList >===============================*/
            sqlQuery = "INSERT INTO itemSiteList (CODE, site_Code)  "
                    + " VALUES (:code, :site_Code)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
            psTemp.setString("site_Code", DmShared.foriegnKey(DmServer.getSsnSiteCode(rss)));
            psTemp.executeUpdate();
            psTemp.close();


            /*===========================< Update record of itemMast >===============================*/
            if (DmShared.iNull(mrTemp.getHsnCode()).intValue() > 0) {
                sqlQuery = "Update  im set "
                        + "         im.itemGSTNature = hm.itemGSTNature,"
                        + "         im.itsBasedOn = hm.itsBasedOn "
                        + "         from itemmast as im "
                        + "         left join hsnMast as hm on im.hsnCode = hm.code "
                        + "         where im.code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getCode());
                psTemp.executeUpdate();
                psTemp.close();
            }
            /*===========================< Update record of itemITS >===============================*/
            sqlQuery = " delete from itemITSDetail where code = :itemCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(mrTemp.getCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getHsnCode()).intValue() > 0) {
                sqlQuery = "insert into itemITSDetail (code ,v_SNo,rateFrom,rateUpto,its_Code_Purchase,its_Code_PurchaseRet,"
                        + "                  its_Code_Sales,its_Code_SalesRet,purchaseAc,purchaseReturnAc,salesAc,salesReturnAc) "
                        + "         		(select :code ,hid.v_SNo,hid.rateFrom,hid.rateUpto,hid.its_Code_Purchase,hid.its_Code_PurchaseRet,"
                        + "                 hid.its_Code_Sales,hid.its_Code_SalesRet,hid.purchaseAc,hid.purchaseReturnAc,hid.salesAc,hid.salesReturnAc "
                        + "         		from hsnITSDetail as hid where hid.code = :hsnCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getCode());
                psTemp.setString("hsnCode", mrTemp.getHsnCode());
                psTemp.executeUpdate();
                psTemp.close();

            }

            //<editor-fold defaultstate="collapsed" desc="medicineComposition">
            if (DmShared.iNull(mrTemp.getComposition1Code()).longValue() > 0) {
                sqlQuery = "insert into medicineComposition (code, sno, compositionCode, power, isBase,unitCode) "
                        + " values(:code, :sno, :compositionCode, :power, :isBase,:unitCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
                psTemp.setString("sno", DmShared.iNull(++snoMedicineComposition).toString());
                psTemp.setString("compositionCode", DmShared.foriegnKey(mrTemp.getComposition1Code()));
                psTemp.setString("unitCode", DmShared.foriegnKey(mrTemp.getComposition1UnitCode()));
                psTemp.setString("power", DmServer.vNull(mrTemp.getComposition1Power(), "0.00"));
                psTemp.setString("isBase", "1");
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.iNull(mrTemp.getComposition2Code()).longValue() > 0) {
                sqlQuery = "insert into medicineComposition (code, sno, compositionCode, power,unitCode) "
                        + " values(:code, :sno, :compositionCode, :power,:unitCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
                psTemp.setString("sno", DmShared.iNull(++snoMedicineComposition).toString());
                psTemp.setString("compositionCode", DmShared.foriegnKey(mrTemp.getComposition2Code()));
                psTemp.setString("unitCode", DmShared.foriegnKey(mrTemp.getComposition2UnitCode()));
                psTemp.setString("power", DmServer.vNull(mrTemp.getComposition1Power(), "0.00"));
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.iNull(mrTemp.getComposition3Code()).longValue() > 0) {
                sqlQuery = "insert into medicineComposition (code, sno, compositionCode, power,unitCode) "
                        + " values(:code, :sno, :compositionCode, :power,:unitCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
                psTemp.setString("sno", DmShared.iNull(++snoMedicineComposition).toString());
                psTemp.setString("compositionCode", DmShared.foriegnKey(mrTemp.getComposition3Code()));
                psTemp.setString("unitCode", DmShared.foriegnKey(mrTemp.getComposition3UnitCode()));
                psTemp.setString("power", DmServer.vNull(mrTemp.getComposition1Power(), "0.00"));
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.iNull(mrTemp.getComposition4Code()).longValue() > 0) {
                sqlQuery = "insert into medicineComposition (code, sno, compositionCode, power,unitCode) "
                        + " values(:code, :sno, :compositionCode, :power,:unitCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
                psTemp.setString("sno", DmShared.iNull(++snoMedicineComposition).toString());
                psTemp.setString("compositionCode", DmShared.foriegnKey(mrTemp.getComposition4Code()));
                psTemp.setString("unitCode", DmShared.foriegnKey(mrTemp.getComposition4UnitCode()));
                psTemp.setString("power", DmServer.vNull(mrTemp.getComposition1Power(), "0.00"));
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.iNull(mrTemp.getComposition5Code()).longValue() > 0) {
                sqlQuery = "insert into medicineComposition (code, sno, compositionCode, power,unitCode) "
                        + " values(:code, :sno, :compositionCode, :power,:unitCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
                psTemp.setString("sno", DmShared.iNull(++snoMedicineComposition).toString());
                psTemp.setString("compositionCode", DmShared.foriegnKey(mrTemp.getComposition5Code()));
                psTemp.setString("unitCode", DmShared.foriegnKey(mrTemp.getComposition5UnitCode()));
                psTemp.setString("power", DmServer.vNull(mrTemp.getComposition1Power(), "0.00"));
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.iNull(mrTemp.getComposition6Code()).longValue() > 0) {
                sqlQuery = "insert into medicineComposition (code, sno, compositionCode, power,unitCode) "
                        + " values(:code, :sno, :compositionCode, :power,:unitCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
                psTemp.setString("sno", DmShared.iNull(++snoMedicineComposition).toString());
                psTemp.setString("compositionCode", DmShared.foriegnKey(mrTemp.getComposition6Code()));
                psTemp.setString("unitCode", DmShared.foriegnKey(mrTemp.getComposition6UnitCode()));
                psTemp.setString("power", DmServer.vNull(mrTemp.getComposition1Power(), "0.00"));
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.iNull(mrTemp.getComposition7Code()).longValue() > 0) {
                sqlQuery = "insert into medicineComposition (code, sno, compositionCode, power, unitCode) "
                        + " values(:code, :sno, :compositionCode, :power, :unitCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
                psTemp.setString("sno", DmShared.iNull(++snoMedicineComposition).toString());
                psTemp.setString("compositionCode", DmShared.foriegnKey(mrTemp.getComposition7Code()));
                psTemp.setString("unitCode", DmShared.foriegnKey(mrTemp.getComposition7UnitCode()));
                psTemp.setString("power", DmServer.vNull(mrTemp.getComposition1Power(), "0.00"));
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.iNull(mrTemp.getComposition8Code()).longValue() > 0) {
                sqlQuery = "insert into medicineComposition (code, sno, compositionCode, power, unitCode) "
                        + " values(:code, :sno, :compositionCode, :power, :unitCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
                psTemp.setString("sno", DmShared.iNull(++snoMedicineComposition).toString());
                psTemp.setString("compositionCode", DmShared.foriegnKey(mrTemp.getComposition8Code()));
                psTemp.setString("unitCode", DmShared.foriegnKey(mrTemp.getComposition8UnitCode()));
                psTemp.setString("power", DmServer.vNull(mrTemp.getComposition1Power(), "0.00"));
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.iNull(mrTemp.getComposition9Code()).longValue() > 0) {
                sqlQuery = "insert into medicineComposition (code, sno, compositionCode, power, unitCode) "
                        + " values(:code, :sno, :compositionCode, :power, :unitCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
                psTemp.setString("sno", DmShared.iNull(++snoMedicineComposition).toString());
                psTemp.setString("compositionCode", DmShared.foriegnKey(mrTemp.getComposition9Code()));
                psTemp.setString("unitCode", DmShared.foriegnKey(mrTemp.getComposition9UnitCode()));
                psTemp.setString("power", DmServer.vNull(mrTemp.getComposition1Power(), "0.00"));
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.iNull(mrTemp.getComposition10Code()).longValue() > 0) {
                sqlQuery = "insert into medicineComposition (code, sno, compositionCode, power, unitCode) "
                        + " values(:code, :sno, :compositionCode, :power, :unitCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
                psTemp.setString("sno", DmShared.iNull(++snoMedicineComposition).toString());
                psTemp.setString("compositionCode", DmShared.foriegnKey(mrTemp.getComposition10Code()));
                psTemp.setString("unitCode", DmShared.foriegnKey(mrTemp.getComposition10UnitCode()));
                psTemp.setString("power", DmServer.vNull(mrTemp.getComposition1Power(), "0.00"));
                psTemp.executeUpdate();
                psTemp.close();
            }
            //</editor-fold>

            spBegin.commit();
            cnTemp.close();
            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                handelFileLog.writeLine(lineNoDisplay + e.getMessage());
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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
        return rtnStatus;
    }
}
