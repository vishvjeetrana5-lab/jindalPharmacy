package aarogya.dashBoard.inventoryIndentPendingForApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class InventoryIndentPendingForApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String indNo, indDate, depName, indentorName, itemName, reqQty, aprQty, unit, reqDate, pendingQuantity,
            requisitionNo, statusRemark;

    public InventoryIndentPendingForApproval_Mdl() {
    }

    public String getIndNo() {
        return indNo;
    }

    public void setIndNo(String indNo) {
        this.indNo = indNo;
    }

    public String getIndDate() {
        return indDate;
    }

    public void setIndDate(String indDate) {
        this.indDate = indDate;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getIndentorName() {
        return indentorName;
    }

    public void setIndentorName(String indentorName) {
        this.indentorName = indentorName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getReqQty() {
        return reqQty;
    }

    public void setReqQty(String reqQty) {
        this.reqQty = reqQty;
    }

    public String getAprQty() {
        return aprQty;
    }

    public void setAprQty(String aprQty) {
        this.aprQty = aprQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getPendingQuantity() {
        return pendingQuantity;
    }

    public void setPendingQuantity(String pendingQuantity) {
        this.pendingQuantity = pendingQuantity;
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public String getStatusRemark() {
        return statusRemark;
    }

    public void setStatusRemark(String statusRemark) {
        this.statusRemark = statusRemark;
    }

    
}
