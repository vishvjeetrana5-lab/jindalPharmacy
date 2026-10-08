package aarogya.pharmacy.medicineModification.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String category;
    private String company;
    private String composition;
    private String medGroup;
    private String restrictGroup;
    private String sku;
    private String active;
    private String medicinePacking;
    private String ManualCode;
    private String modifiedOn;

    public Find_Mdl() {
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getManualCode() {
        return ManualCode;
    }

    public void setManualCode(String ManualCode) {
        this.ManualCode = ManualCode;
    }

    public String getMedicinePacking() {
        return medicinePacking;
    }

    public void setMedicinePacking(String medicinePacking) {
        this.medicinePacking = medicinePacking;
    }

    public String getMedicineTypeTag() {
        return medicineTypeTag;
    }

    public void setMedicineTypeTag(String medicineTypeTag) {
        this.medicineTypeTag = medicineTypeTag;
    }
    private String medicineTypeTag;
    private String medicineTypeText;

    public String getMedicineTypeText() {
        return medicineTypeText;
    }

    public void setMedicineTypeText(String medicineTypeText) {
        this.medicineTypeText = medicineTypeText;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getMedGroup() {
        return medGroup;
    }

    public void setMedGroup(String medGroup) {
        this.medGroup = medGroup;
    }

    public String getRestrictGroup() {
        return restrictGroup;
    }

    public void setRestrictGroup(String restrictGroup) {
        this.restrictGroup = restrictGroup;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
