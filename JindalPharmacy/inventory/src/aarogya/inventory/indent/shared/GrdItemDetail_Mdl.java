package aarogya.inventory.indent.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String docId;
    private String vSno;
    private String qty;
    private String reqDate;
    private String remarks;
    private String approval;
    private String srNo;
    private String isStatus;
    private String approvedBy;
    private String unitName;
    private String decimalPlace;
    private String itemProperty;
    private String colorCode;
    private String color;
    private String sizeCode;
    private String size;
    private String requisitionDocId;
    private String requisitionNo;
    private String requisitionVsNo;
    private String requisitionFor;
    private String properties;
    private String requisitionDate;
    private String itemMakeCode;
    private String itemMakeName;

    public GrdItemDetail_Mdl() {
    }

    public String getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(String requisitionDate) {
        this.requisitionDate = requisitionDate;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getRequisitionDocId() {
        return requisitionDocId;
    }

    public void setRequisitionDocId(String requisitionDocId) {
        this.requisitionDocId = requisitionDocId;
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String Approval) {
        this.approval = Approval;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDecimalPlace() {
        return decimalPlace;
    }

    public void setDecimalPlace(String decimalPlace) {
        this.decimalPlace = decimalPlace;
    }

    public String getItemProperty() {
        return itemProperty;
    }

    public void setItemProperty(String itemProperty) {
        this.itemProperty = itemProperty;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getvSno() {
        return vSno;
    }

    public void setvSno(String vSno) {
        this.vSno = vSno;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getRequisitionVsNo() {
        return requisitionVsNo;
    }

    public void setRequisitionVsNo(String requisitionVsNo) {
        this.requisitionVsNo = requisitionVsNo;
    }

    public String getRequisitionFor() {
        return requisitionFor;
    }

    public void setRequisitionFor(String requisitionFor) {
        this.requisitionFor = requisitionFor;
    }

    public String getItemMakeCode() {
        return itemMakeCode;
    }

    public void setItemMakeCode(String itemMakeCode) {
        this.itemMakeCode = itemMakeCode;
    }

    public String getItemMakeName() {
        return itemMakeName;
    }

    public void setItemMakeName(String itemMakeName) {
        this.itemMakeName = itemMakeName;
    }

}
