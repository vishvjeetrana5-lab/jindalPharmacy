package aarogya.pharmacy.enquiry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<GrdIndentDetails_Mdl> grdIndentDetail = new ArrayList<GrdIndentDetails_Mdl>();

    public GrdItemDetail_Mdl() {
    }

    private String remark;
    private String qty;
    private String scheduleDt;
    private String vSno;
    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String skuQuantity;
    private String conversionRate;
    private String decimalPlacesSku;
    private String decimalPlacesPurchaseUnit;
    private String displayUnit;

    public String getDisplayUnit() {
        return displayUnit;
    }

    public void setDisplayUnit(String displayUnit) {
        this.displayUnit = displayUnit;
    }

    public List<GrdIndentDetails_Mdl> getGrdIndentDetail() {
        return grdIndentDetail;
    }

    public void setGrdIndentDetail(List<GrdIndentDetails_Mdl> grdIndentDetail) {
        this.grdIndentDetail = grdIndentDetail;
    }

    public String getDecimalPlacesSku() {
        return decimalPlacesSku;
    }

    public void setDecimalPlacesSku(String decimalPlacesSku) {
        this.decimalPlacesSku = decimalPlacesSku;
    }

    public String getDecimalPlacesPurchaseUnit() {
        return decimalPlacesPurchaseUnit;
    }

    public void setDecimalPlacesPurchaseUnit(String decimalPlacesPurchaseUnit) {
        this.decimalPlacesPurchaseUnit = decimalPlacesPurchaseUnit;
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

}
