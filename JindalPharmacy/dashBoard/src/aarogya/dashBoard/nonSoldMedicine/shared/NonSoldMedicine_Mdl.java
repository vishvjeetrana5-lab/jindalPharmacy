package aarogya.dashBoard.nonSoldMedicine.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class NonSoldMedicine_Mdl extends KeyField implements Serializable, IsSerializable {

    private String medicineName, medicineCategory;
    private String departmentName, batchNo;
    private String expiry, balanceQty;
    private String skuMRP;

    public NonSoldMedicine_Mdl() {
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getBalanceQty() {
        return balanceQty;
    }

    public void setBalanceQty(String balanceQty) {
        this.balanceQty = balanceQty;
    }

    public String getMedicineCategory() {
        return medicineCategory;
    }

    public void setMedicineCategory(String medicineCategory) {
        this.medicineCategory = medicineCategory;
    }

    public String getSkuMRP() {
        return skuMRP;
    }

    public void setSkuMRP(String skuMRP) {
        this.skuMRP = skuMRP;
    }

}
