package aarogya.pharmacy.medicineSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class MedicineSearchFilter_Mdl implements Serializable, IsSerializable {

    //------------------------------------< Prerequisites >--------------------------------//
    private String v_Date;
    private String departmentCode;
    private String departmentName;
    //------------------------------------< ************* >--------------------------------//

    private String medicineMakeCode;
    private String medicineMakeName;
    private String medicineCategoryCode;
    private String medicineCategoryName;
    private String medicineGroupCode;
    private String medicineGroupName;
    private String divisionCode;
    private String divisionName;
    private String compositionCode;
    private String compositionName;
    private String hsnCode;
    private String hsnName;
    private String compositionCodeCsv;
    private String compositionCodeWithPowerCsv;
    private String medicineCode;
    private boolean flagInStock;
    private String itemCodeCsv;

    public MedicineSearchFilter_Mdl() {
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getMedicineMakeCode() {
        return medicineMakeCode;
    }

    public void setMedicineMakeCode(String medicineMakeCode) {
        this.medicineMakeCode = medicineMakeCode;
    }

    public String getMedicineMakeName() {
        return medicineMakeName;
    }

    public void setMedicineMakeName(String medicineMakeName) {
        this.medicineMakeName = medicineMakeName;
    }

    public String getMedicineCategoryCode() {
        return medicineCategoryCode;
    }

    public void setMedicineCategoryCode(String medicineCategoryCode) {
        this.medicineCategoryCode = medicineCategoryCode;
    }

    public String getMedicineCategoryName() {
        return medicineCategoryName;
    }

    public void setMedicineCategoryName(String medicineCategoryName) {
        this.medicineCategoryName = medicineCategoryName;
    }

    public String getMedicineGroupCode() {
        return medicineGroupCode;
    }

    public void setMedicineGroupCode(String medicineGroupCode) {
        this.medicineGroupCode = medicineGroupCode;
    }

    public String getMedicineGroupName() {
        return medicineGroupName;
    }

    public void setMedicineGroupName(String medicineGroupName) {
        this.medicineGroupName = medicineGroupName;
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

    public String getCompositionCodeCsv() {
        return compositionCodeCsv;
    }

    public void setCompositionCodeCsv(String compositionCodeCsv) {
        this.compositionCodeCsv = compositionCodeCsv;
    }

    public String getCompositionCodeWithPowerCsv() {
        return compositionCodeWithPowerCsv;
    }

    public void setCompositionCodeWithPowerCsv(String compositionCodeWithPowerCsv) {
        this.compositionCodeWithPowerCsv = compositionCodeWithPowerCsv;
    }

    public String getCompositionCode() {
        return compositionCode;
    }

    public void setCompositionCode(String compositionCode) {
        this.compositionCode = compositionCode;
    }

    public String getCompositionName() {
        return compositionName;
    }

    public void setCompositionName(String compositionName) {
        this.compositionName = compositionName;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getHsnName() {
        return hsnName;
    }

    public void setHsnName(String hsnName) {
        this.hsnName = hsnName;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }

    public boolean getFlagInStock() {
        return flagInStock;
    }

    public void setFlagInStock(boolean flagInStock) {
        this.flagInStock = flagInStock;
    }

    public String getItemCodeCsv() {
        return itemCodeCsv;
    }

    public void setItemCodeCsv(String itemCodeCsv) {
        this.itemCodeCsv = itemCodeCsv;
    }

}
