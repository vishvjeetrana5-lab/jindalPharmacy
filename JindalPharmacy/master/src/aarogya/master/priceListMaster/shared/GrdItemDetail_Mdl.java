package aarogya.master.priceListMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.Objects;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String itemName;
    private String itemCode;
    private String unitName;  //<Ag> Manage in flow
    private String unitCode;  //<Ag> Manage in flow
    private String itemCatName;
    private String itemCatCode;
    private String rate;
    private String printSNo;
    private String discountPercentage;
    private String discountAmount;
    private String applicableOn;

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }
    private String recordType;

    public String getApplicableOn() {
        return applicableOn;
    }

    public void setApplicableOn(String applicableOn) {
        this.applicableOn = applicableOn;
    }

    private boolean colorChange = false;

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
    
    public String getItemCatName() {
        return itemCatName;
    }

    public void setItemCatName(String itemCatName) {
        this.itemCatName = itemCatName;
    }

    public String getItemCatCode() {
        return itemCatCode;
    }

    public void setItemCatCode(String itemCatCode) {
        this.itemCatCode = itemCatCode;
    }

    public GrdItemDetail_Mdl() {
    }

    public boolean isColorChange() {
        return colorChange;
    }

    public void setColorChange(boolean colorChange) {
        this.colorChange = colorChange;
    }

    public String getPrintSNo() {
        return printSNo;
    }

    public void setPrintSNo(String printSNo) {
        this.printSNo = printSNo;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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

    public GrdItemDetail_Mdl clone() {
        GrdItemDetail_Mdl local = new GrdItemDetail_Mdl();

        super.copy(local);

        local.setItemCode(this.getItemCode());
        local.setItemName(this.getItemName());
        local.setUnitCode(this.getUnitCode());
        local.setUnitName(this.getUnitName());
        local.setItemCatCode(this.getItemCatCode());
        local.setItemCatName(this.getItemCatName());
        local.setRate(this.getRate());
        local.setPrintSNo(this.getPrintSNo());
        local.setDiscountAmount(this.getDiscountAmount());
        local.setDiscountPercentage(this.getDiscountPercentage());

        return local;
    }

//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 97 * hash + Objects.hashCode(this.itemCode);
//        hash = 97 * hash + Objects.hashCode(this.unitCode);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final GrdItemDetail_Mdl other = (GrdItemDetail_Mdl) obj;
//        if (!Objects.equals(this.itemCode, other.itemCode)) {
//            return false;
//        }
//        if (!Objects.equals(this.unitCode, other.unitCode)) {
//            return false;
//        }
//        return true;
//    }
}
