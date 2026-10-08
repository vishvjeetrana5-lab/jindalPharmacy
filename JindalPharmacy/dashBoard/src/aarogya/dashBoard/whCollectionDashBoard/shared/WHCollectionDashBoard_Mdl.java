package aarogya.dashBoard.whCollectionDashBoard.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class WHCollectionDashBoard_Mdl extends KeyField implements Serializable, IsSerializable {

    private String supplierTag;
    private String supplierText;
    private String departmentTag;
    private String departmentText;
    private String totalBill;
    private String saleAmount;
    private String totalSettlementAmount;
    private String pendingForSettlementAmt;

    public WHCollectionDashBoard_Mdl() {

    }

    public String getSupplierTag() {
        return supplierTag;
    }

    public void setSupplierTag(String supplierTag) {
        this.supplierTag = supplierTag;
    }

    public String getSupplierText() {
        return supplierText;
    }

    public void setSupplierText(String supplierText) {
        this.supplierText = supplierText;
    }

    public String getDepartmentTag() {
        return departmentTag;
    }

    public void setDepartmentTag(String departmentTag) {
        this.departmentTag = departmentTag;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getTotalSettlementAmount() {
        return totalSettlementAmount;
    }

    public void setTotalSettlementAmount(String totalSettlementAmount) {
        this.totalSettlementAmount = totalSettlementAmount;
    }

    public String getPendingForSettlementAmt() {
        return pendingForSettlementAmt;
    }

    public void setPendingForSettlementAmt(String pendingForSettlementAmt) {
        this.pendingForSettlementAmt = pendingForSettlementAmt;
    }

}
