package aarogya.inventory.enquiry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String remark;
    private String vSno;
    private String qty;
    private String scheduleDt;
    private String colourCode;
    private String colourName;
    private String sizeCode;
    private String sizeName;
    private String itemMakeCode;
    private String itemMakeName;
    private String propertyList;
    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String displayUnit;
    private String skuQuantity;
    private String conversionRate;
    private String decimalPlaces;
    private String decimalPlacesPurchaseUnit;
    private String properties;

    private List<GrdIndentDetails_Mdl> grdIndentDetail = new ArrayList<GrdIndentDetails_Mdl>();

    public String getDisplayUnit() {
        return displayUnit;
    }

    public void setDisplayUnit(String displayUnit) {
        this.displayUnit = displayUnit;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public GrdItemDetail_Mdl() {
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public List<GrdIndentDetails_Mdl> getGrdIndentDetail() {
        return grdIndentDetail;
    }

    public void setGrdIndentDetail(List<GrdIndentDetails_Mdl> grdIndentDetail) {
        this.grdIndentDetail = grdIndentDetail;
    }

    public String getColourName() {
        return colourName;
    }

    public void setColourName(String colourName) {
        this.colourName = colourName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getScheduleDt() {
        return scheduleDt;
    }

    public void setScheduleDt(String scheduleDt) {
        this.scheduleDt = scheduleDt;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(String propertyList) {
        this.propertyList = propertyList;
    }

    public String getColourCode() {
        return colourCode;
    }

    public void setColourCode(String colourCode) {
        this.colourCode = colourCode;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getvSno() {
        return vSno;
    }

    public void setvSno(String vSno) {
        this.vSno = vSno;
    }

    public String getPurchaseUnitCode() {
        return purchaseUnitCode;
    }

    public void setPurchaseUnitCode(String purchaseUnitCode) {
        this.purchaseUnitCode = purchaseUnitCode;
    }

    public String getPurchaseUnitName() {
        return purchaseUnitName;
    }

    public void setPurchaseUnitName(String purchaseUnitName) {
        this.purchaseUnitName = purchaseUnitName;
    }

    public String getSkuQuantity() {
        return skuQuantity;
    }

    public void setSkuQuantity(String skuQuantity) {
        this.skuQuantity = skuQuantity;
    }

    public String getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
    }

    public String getDecimalPlacesPurchaseUnit() {
        return decimalPlacesPurchaseUnit;
    }

    public void setDecimalPlacesPurchaseUnit(String decimalPlacesPurchaseUnit) {
        this.decimalPlacesPurchaseUnit = decimalPlacesPurchaseUnit;
    }
    
    public String getItemMakeCode() {
        return itemMakeCode;
    }

    public void setItemMakeCode(String itemMakeCode) {
        this.itemMakeCode = itemMakeCode;
    }

    public String getItemMakeName() {
        return itemMakeName;
    }

    public void setItemMakeName(String itemMakeName) {
        this.itemMakeName = itemMakeName;
    }

}
