package aarogya.dashBoard.billPendingForApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class BillPendingForApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String description, supplierName;
    private String billNo, billDate;
    private String billValue;

    public BillPendingForApproval_Mdl() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillValue() {
        return billValue;
    }

    public void setBillValue(String billValue) {
        this.billValue = billValue;
    }

   

}
