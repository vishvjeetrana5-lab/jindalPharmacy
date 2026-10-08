package aarogya.selfToolkit.stStatic.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ItemPriceList_Mdl extends KeyField implements Serializable, IsSerializable {

    private STEnum.PriceListEntryFrom entryFrom = STEnum.PriceListEntryFrom.NONE;
    private String entryFromNature;
    private STEnum.PriceListNature priceListNature = STEnum.PriceListNature.None;
    private String itemName;
    private String itemCode;
    private String unitName;
    private String unitCode;
    private String rate;
    private String mrp;
    private String printSNo;
    private String discountPercentage;
    private String discountAmount;
    private String priceListCode;
    private String date;
    private String partyCode;
    private String siteCode;

    public ItemPriceList_Mdl() {
    }

    public STEnum.PriceListEntryFrom getEntryFrom() {
        return entryFrom;
    }

    public void setEntryFrom(STEnum.PriceListEntryFrom entryFrom) {
        this.entryFrom = entryFrom;
    }

    public String getEntryFromNature() {
        return entryFromNature;
    }

    public void setEntryFromNature(String entryFromNature) {
        this.entryFromNature = entryFromNature;
    }

    public STEnum.PriceListNature getPriceListNature() {
        return priceListNature;
    }

    public void setPriceListNature(STEnum.PriceListNature priceListNature) {
        this.priceListNature = priceListNature;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    
    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPrintSNo() {
        return printSNo;
    }

    public void setPrintSNo(String printSNo) {
        this.printSNo = printSNo;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getPriceListCode() {
        return priceListCode;
    }

    public void setPriceListCode(String priceListCode) {
        this.priceListCode = priceListCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }


    public ItemPriceList_Mdl clone() {
        ItemPriceList_Mdl local = new ItemPriceList_Mdl();

        super.copy(local);

        local.setEntryFrom(this.getEntryFrom());
        local.setEntryFromNature(this.getEntryFromNature());
        local.setPriceListNature(this.getPriceListNature());
        local.setItemCode(this.getItemCode());
        local.setItemName(this.getItemName());
        local.setUnitCode(this.getUnitCode());
        local.setUnitName(this.getUnitName());
        local.setRate(this.getRate());
        local.setPrintSNo(this.getPrintSNo());
        local.setDiscountAmount(this.getDiscountAmount());
        local.setDiscountPercentage(this.getDiscountPercentage());
        local.setPriceListCode(this.getPriceListCode());
        local.setDate(this.getDate());
        local.setPartyCode(this.getPartyCode());
        local.setSiteCode(this.getSiteCode());
        return local;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

}
