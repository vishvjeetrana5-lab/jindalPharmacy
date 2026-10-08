package aarogya.pharmacy.requisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHMedicine_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String skuName;
    private String medicineGroup;
    private String medicineCategory;
    private String allowDecimalPlaces;
    private String curStkOfToDepartment;
    private String purchaseDecimalPlaces;
    private String purchaseUnit;

    public TxtHMedicine_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getMedicineGroup() {
        return medicineGroup;
    }

    public void setMedicineGroup(String medicineGroup) {
        this.medicineGroup = medicineGroup;
    }

    public String getMedicineCategory() {
        return medicineCategory;
    }

    public void setMedicineCategory(String medicineCategory) {
        this.medicineCategory = medicineCategory;
    }

    public String getAllowDecimalPlaces() {
        return allowDecimalPlaces;
    }

    public void setAllowDecimalPlaces(String allowDecimalPlaces) {
        this.allowDecimalPlaces = allowDecimalPlaces;
    }

    public String getCurStkOfToDepartment() {
        return curStkOfToDepartment;
    }

    public void setCurStkOfToDepartment(String curStkOfToDepartment) {
        this.curStkOfToDepartment = curStkOfToDepartment;
    }

    public String getPurchaseDecimalPlaces() {
        return purchaseDecimalPlaces;
    }

    public void setPurchaseDecimalPlaces(String purchaseDecimalPlaces) {
        this.purchaseDecimalPlaces = purchaseDecimalPlaces;
    }

    public String getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(String purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
    }
}
