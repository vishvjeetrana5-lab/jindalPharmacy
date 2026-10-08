package aarogya.inventory.grnApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String ApprovedBy;
    private String ApprovedDateTime;
    private String indNo;
    private String indDocId;
    private String isStatus;
    private String isChecked;
    private String isCancel;
    private String vSNo;
    private String indQty;
    private String unit;
    private String itemName;
    private String itemGroup;
    private String reqDate;
//    private String aprQty;
    private String ApprovedQuantity;
    private String ApprovalRemarks;
    private String remark;
    private String image;

    public GrdItemDetail_Mdl() {
    }

    public String getIndNo() {
        return indNo;
    }

    public void setIndNo(String indNo) {
        this.indNo = indNo;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getIndQty() {
        return indQty;
    }

    public void setIndQty(String indQty) {
        this.indQty = indQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String Remark) {
        this.remark = Remark;
    }

    public String getApprovedBy() {
        return ApprovedBy;
    }

    public void setApprovedBy(String ApprovedBy) {
        this.ApprovedBy = ApprovedBy;
    }

    public String getApprovedDateTime() {
        return ApprovedDateTime;
    }

    public void setApprovedDateTime(String ApprovedDateTime) {
        this.ApprovedDateTime = ApprovedDateTime;
    }
//
//    public String getAprQty() {
//        return aprQty;
//    }
//
//    public void setAprQty(String aprQty) {
//        this.aprQty = aprQty;
//    }

    public String getApprovedQuantity() {
        return ApprovedQuantity;
    }

    public void setApprovedQuantity(String ApprovedQuantity) {
        this.ApprovedQuantity = ApprovedQuantity;
    }

    public String getApprovalRemarks() {
        return ApprovalRemarks;
    }

    public void setApprovalRemarks(String ApprovalRemarks) {
        this.ApprovalRemarks = ApprovalRemarks;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getIndDocId() {
        return indDocId;
    }

    public void setIndDocId(String indDocId) {
        this.indDocId = indDocId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }

}
