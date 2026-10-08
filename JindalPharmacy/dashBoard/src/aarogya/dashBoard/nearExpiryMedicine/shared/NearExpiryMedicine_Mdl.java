package aarogya.dashBoard.nearExpiryMedicine.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class NearExpiryMedicine_Mdl extends KeyField implements Serializable, IsSerializable {

    private String medicineName, medicineGroup;
    private String departmentName, batchNo;
    private String expiry, balanceQty;
    private String mrp, value;

    public NearExpiryMedicine_Mdl() {
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineGroup() {
        return medicineGroup;
    }

    public void setMedicineGroup(String medicineGroup) {
        this.medicineGroup = medicineGroup;
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

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
