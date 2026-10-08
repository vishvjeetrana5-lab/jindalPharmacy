package aarogya.inventory.pendingPODisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdMedicineDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String isView;
    private String ApprovedBy;
    private String ApprovedDateTime;
    private String orderNo;
    private String orderDate;
    private String isStatus;
    private String isChecked;
    private String vSNo;
    private String orderQty;
    private String unit;
    private String medName;
    private String reqDate;
    private String rate;
    private String amount;
    private String Remark;
    private String image;
    private String challanQty;
   
     private List<GrdPendingPODetail_Display_Mdl> grdPurchaseDetailList = new ArrayList<GrdPendingPODetail_Display_Mdl>();


    public GrdMedicineDetail_Mdl() {
    }

    public String getIsView() {
        return isView;
    }

    public void setIsView(String isView) {
        this.isView = isView;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
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

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
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

    public String getChallanQty() {
        return challanQty;
    }

    public void setChallanQty(String challanQty) {
        this.challanQty = challanQty;
    }

    public List<GrdPendingPODetail_Display_Mdl> getGrdPurchaseDetailList() {
        return grdPurchaseDetailList;
    }

  



  

   

}
