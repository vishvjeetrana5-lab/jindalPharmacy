package aarogya.pharmacy.medicineIssue.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class GrdRequisitiontDetails_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherSNo;
    private String displayQty;
    private String quantity;
    private String unitName;
    private String medicineName;
    private String remark;
    private String departmentText;
    private String requiredDate;
    private String requisitiontBy;
    private String requisitiontDate;
    private String isChecked;

    public GrdRequisitiontDetails_Mdl() {
    }

    public String getVoucherSNo() {
        return voucherSNo;
    }

    public void setVoucherSNo(String voucherSNo) {
        this.voucherSNo = voucherSNo;
    }

    public String getDisplayQty() {
        return displayQty;
    }

    public void setDisplayQty(String displayQty) {
        this.displayQty = displayQty;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getRequisitiontBy() {
        return requisitiontBy;
    }

    public void setRequisitiontBy(String requisitiontBy) {
        this.requisitiontBy = requisitiontBy;
    }

    public String getRequisitiontDate() {
        return requisitiontDate;
    }

    public void setRequisitiontDate(String requisitiontDate) {
        this.requisitiontDate = requisitiontDate;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

}
