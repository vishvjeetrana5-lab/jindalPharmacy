package aarogya.dashBoard.billPendingForGRN.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class BillPendingForGRN_Mdl extends KeyField implements Serializable, IsSerializable {

    private String chalanType, supplierName;
    private String grnNo, grnDate;
    private String itemName, unitName;
    private String grnQty, billedQty,pendingQty;

    public BillPendingForGRN_Mdl() {
    }

    public String getChalanType() {
        return chalanType;
    }

    public void setChalanType(String chalanType) {
        this.chalanType = chalanType;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getGrnNo() {
        return grnNo;
    }

    public void setGrnNo(String grnNo) {
        this.grnNo = grnNo;
    }

    public String getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(String grnDate) {
        this.grnDate = grnDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getGrnQty() {
        return grnQty;
    }

    public void setGrnQty(String grnQty) {
        this.grnQty = grnQty;
    }

    public String getBilledQty() {
        return billedQty;
    }

    public void setBilledQty(String billedQty) {
        this.billedQty = billedQty;
    }

    public String getPendingQty() {
        return pendingQty;
    }

    public void setPendingQty(String pendingQty) {
        this.pendingQty = pendingQty;
    }

     

}
