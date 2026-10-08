package aarogya.pharmacy.purchaseBillDirect.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherType;
    private String voucherDate;
    private String supplierText;
    private String departmentText;
    private String supplierBillNo;
    private String supplierBillDate;

    public Find_Mdl() {
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getSupplierText() {
        return supplierText;
    }

    public void setSupplierText(String supplierText) {
        this.supplierText = supplierText;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    public String getSupplierBillNo() {
        return supplierBillNo;
    }

    public void setSupplierBillNo(String supplierBillNo) {
        this.supplierBillNo = supplierBillNo;
    }

    public String getSupplierBillDate() {
        return supplierBillDate;
    }

    public void setSupplierBillDate(String supplierBillDate) {
        this.supplierBillDate = supplierBillDate;
    }

}
