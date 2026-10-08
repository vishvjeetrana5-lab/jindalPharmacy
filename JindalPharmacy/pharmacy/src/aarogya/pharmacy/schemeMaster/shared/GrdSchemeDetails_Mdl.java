package aarogya.pharmacy.schemeMaster.shared;

import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdSchemeDetails_Mdl extends KeyField implements Serializable, IsSerializable {

    public GrdSchemeDetails_Mdl() {
    }

    //sharedkeyFieldCode=freeItemCode
    //sharedkeyFieldName=freeitemName
    private String v_SNo;
    private String freeWithItemCode;
    private String schemeOnQty;
    private String schemeFreeQty;
    private String freeQty;
    private String totalFreeQty;
    private String description;
    private String freeItemUnitCode;
    private String freeItemUnitName;
    private String isChecked;
    private String allowNegativeStockFreeItem;
    private boolean barcodeActiveFreeItem;
    private String itsBasedOnFreeItem;
    private String saleAcCode;
    private String saleUnitConversionRate;
    private String decimalPlacesSaleUnit;
    private String decimalPlacesPurchaseUnit;
    private String purchaseUnitConversionRate;
    BatchNo_Mdl batchNo_Mdl = new BatchNo_Mdl();
    private List<BatchNo_Mdl> lstBatchDetails = new ArrayList<>();
    private List<OverHeadITS_Mdl> lstITS = new ArrayList<OverHeadITS_Mdl>();
    private TxtHMedicine_Mdl medicineMdl = new TxtHMedicine_Mdl();

    public String getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty = freeQty;
    }

    public TxtHMedicine_Mdl getMedicineMdl() {
        return medicineMdl;
    }

    public void setMedicineMdl(TxtHMedicine_Mdl medicineMdl) {
        this.medicineMdl = medicineMdl;
    }

    public String getDecimalPlacesSaleUnit() {
        return decimalPlacesSaleUnit;
    }

    public void setDecimalPlacesSaleUnit(String decimalPlacesSaleUnit) {
        this.decimalPlacesSaleUnit = decimalPlacesSaleUnit;
    }

    public String getDecimalPlacesPurchaseUnit() {
        return decimalPlacesPurchaseUnit;
    }

    public void setDecimalPlacesPurchaseUnit(String decimalPlacesPurchaseUnit) {
        this.decimalPlacesPurchaseUnit = decimalPlacesPurchaseUnit;
    }

    public String getSaleUnitConversionRate() {
        return saleUnitConversionRate;
    }

    public void setSaleUnitConversionRate(String saleUnitConversionRate) {
        this.saleUnitConversionRate = saleUnitConversionRate;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public BatchNo_Mdl getBatchNo_Mdl() {
        return batchNo_Mdl;
    }

    public void setBatchNo_Mdl(BatchNo_Mdl batchNo_Mdl) {
        this.batchNo_Mdl = batchNo_Mdl;
    }

    public String getSaleAcCode() {
        return saleAcCode;
    }

    public void setSaleAcCode(String saleAcCode) {
        this.saleAcCode = saleAcCode;
    }

    public List<OverHeadITS_Mdl> getLstITS() {
        return lstITS;
    }

    public void setLstITS(List<OverHeadITS_Mdl> lstITS) {
        this.lstITS = lstITS;
    }

    public String getItsBasedOnFreeItem() {
        return itsBasedOnFreeItem;
    }

    public void setItsBasedOnFreeItem(String itsBasedOnFreeItem) {
        this.itsBasedOnFreeItem = itsBasedOnFreeItem;
    }

    public String getTotalFreeQty() {
        return totalFreeQty;
    }

    public void setTotalFreeQty(String totalFreeQty) {
        this.totalFreeQty = totalFreeQty;
    }

    public String getAllowNegativeStockFreeItem() {
        return allowNegativeStockFreeItem;
    }

    public void setAllowNegativeStockFreeItem(String allowNegativeStockFreeItem) {
        this.allowNegativeStockFreeItem = allowNegativeStockFreeItem;
    }

    public String getFreeWithItemCode() {
        return freeWithItemCode;
    }

    public void setFreeWithItemCode(String freeWithItemCode) {
        this.freeWithItemCode = freeWithItemCode;
    }

    public List<BatchNo_Mdl> getLstBatchDetails() {
        return lstBatchDetails;
    }

    public void setLstBatchDetails(List<BatchNo_Mdl> lstBatchDetails) {
        this.lstBatchDetails = lstBatchDetails;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getFreeItemUnitCode() {
        return freeItemUnitCode;
    }

    public void setFreeItemUnitCode(String freeItemUnitCode) {
        this.freeItemUnitCode = freeItemUnitCode;
    }

    public String getFreeItemUnitName() {
        return freeItemUnitName;
    }

    public void setFreeItemUnitName(String freeItemUnitName) {
        this.freeItemUnitName = freeItemUnitName;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBarcodeActiveFreeItem() {
        return barcodeActiveFreeItem;
    }

    public void setBarcodeActiveFreeItem(boolean barcodeActiveFreeItem) {
        this.barcodeActiveFreeItem = barcodeActiveFreeItem;
    }

}
