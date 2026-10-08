package aarogya.pharmacy.stockBatch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class GrdStockDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manufacturingDate;
    private String expiryDate;
    private String mrp;
    private String mrpDisplay;
    private String saleRate;
    private String saleRateDisplay;
    private String openingQty;
    private String displayOpeningQty;
    private String openingValue;
    private String displayOpeningValue;
    private String purchaseQty;
    private String displayPurchaseQty;
    private String otherReceiveQty;
    private String displayOtherReceiveQty;
    private String saleQty;
    private String displaySaleQty;
    private String otherIssueQty;
    private String displayOtherIssueQty;
    private String closingQty;
    private String displayClosingQty;
    private String closingValue;
    private String displayClosingValue;

    public GrdStockDetail_Mdl() {
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getMrpDisplay() {
        return mrpDisplay;
    }

    public void setMrpDisplay(String mrpDisplay) {
        this.mrpDisplay = mrpDisplay;
    }

    public String getClosingQty() {
        return closingQty;
    }

    public void setClosingQty(String closingQty) {
        this.closingQty = closingQty;
    }

    public String getDisplayClosingQty() {
        return displayClosingQty;
    }

    public void setDisplayClosingQty(String displayClosingQty) {
        this.displayClosingQty = displayClosingQty;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public String getSaleRateDisplay() {
        return saleRateDisplay;
    }

    public void setSaleRateDisplay(String saleRateDisplay) {
        this.saleRateDisplay = saleRateDisplay;
    }

    public String getClosingValue() {
        return closingValue;
    }

    public void setClosingValue(String closingValue) {
        this.closingValue = closingValue;
    }

    public String getDisplayClosingValue() {
        return displayClosingValue;
    }

    public void setDisplayClosingValue(String displayClosingValue) {
        this.displayClosingValue = displayClosingValue;
    }

    public String getOpeningQty() {
        return openingQty;
    }

    public void setOpeningQty(String openingQty) {
        this.openingQty = openingQty;
    }

    public String getDisplayOpeningQty() {
        return displayOpeningQty;
    }

    public void setDisplayOpeningQty(String displayOpeningQty) {
        this.displayOpeningQty = displayOpeningQty;
    }

    public String getOpeningValue() {
        return openingValue;
    }

    public void setOpeningValue(String openingValue) {
        this.openingValue = openingValue;
    }

    public String getDisplayOpeningValue() {
        return displayOpeningValue;
    }

    public void setDisplayOpeningValue(String displayOpeningValue) {
        this.displayOpeningValue = displayOpeningValue;
    }

    public String getPurchaseQty() {
        return purchaseQty;
    }

    public void setPurchaseQty(String purchaseQty) {
        this.purchaseQty = purchaseQty;
    }

    public String getDisplayPurchaseQty() {
        return displayPurchaseQty;
    }

    public void setDisplayPurchaseQty(String displayPurchaseQty) {
        this.displayPurchaseQty = displayPurchaseQty;
    }

    public String getOtherReceiveQty() {
        return otherReceiveQty;
    }

    public void setOtherReceiveQty(String otherReceiveQty) {
        this.otherReceiveQty = otherReceiveQty;
    }

    public String getDisplayOtherReceiveQty() {
        return displayOtherReceiveQty;
    }

    public void setDisplayOtherReceiveQty(String displayOtherReceiveQty) {
        this.displayOtherReceiveQty = displayOtherReceiveQty;
    }

    public String getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(String saleQty) {
        this.saleQty = saleQty;
    }

    public String getDisplaySaleQty() {
        return displaySaleQty;
    }

    public void setDisplaySaleQty(String displaySaleQty) {
        this.displaySaleQty = displaySaleQty;
    }

    public String getOtherIssueQty() {
        return otherIssueQty;
    }

    public void setOtherIssueQty(String otherIssueQty) {
        this.otherIssueQty = otherIssueQty;
    }

    public String getDisplayOtherIssueQty() {
        return displayOtherIssueQty;
    }

    public void setDisplayOtherIssueQty(String displayOtherIssueQty) {
        this.displayOtherIssueQty = displayOtherIssueQty;
    }

}
