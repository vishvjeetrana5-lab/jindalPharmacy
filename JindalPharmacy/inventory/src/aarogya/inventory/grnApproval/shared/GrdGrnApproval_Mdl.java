package aarogya.inventory.grnApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdGrnApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String wtaCode;
    private String itemCode;
    private String ItemName;
    private String orderNo;
    private String batchNo;
    private String colourName;
    private String sizeName;
    private String mrp;
    private String expiryDate;
    private String manufacturingDate;
    private String propertiesCsv;
    private String FreeQty;
    private String batchCode;
    private String unitName;
    private String unitCode;
    private String challanQty;
    private String acptQty;
    private String SKUQty;
    private String SKUUnit;
    private String SKUUnitCode;
    private String rate;
    private String approvedBy;
    private String amount;
    private String voucherType;
    private String voucherNo;
    private String supplierCode;
    private String supplierName;
    private String DepartmentCode;
    private String DepartmentName;
    private String challanNo;
    private String challanDate;
    private String remark;
    private String vSNo;
    private String isChecked;
    private String currentUser;
    private String siteCode;
    private String siteName;
    private String approval;
    private String isStatus;
    private String VDate;
    private String orderCode;
    private String isView;

    public GrdGrnApproval_Mdl() {
    }

    public String getIsView() {
        return isView;
    }

    public void setIsView(String isView) {
        this.isView = isView;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getVDate() {
        return VDate;
    }

    public void setVDate(String VDate) {
        this.VDate = VDate;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getPropertiesCsv() {
        return propertiesCsv;
    }

    public void setPropertiesCsv(String propertiesCsv) {
        this.propertiesCsv = propertiesCsv;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getChallanQty() {
        return challanQty;
    }

    public void setChallanQty(String challanQty) {
        this.challanQty = challanQty;
    }

    public String getAcptQty() {
        return acptQty;
    }

    public void setAcptQty(String acptQty) {
        this.acptQty = acptQty;
    }

    public String getSKUQty() {
        return SKUQty;
    }

    public void setSKUQty(String SKUQty) {
        this.SKUQty = SKUQty;
    }

    public String getSKUUnit() {
        return SKUUnit;
    }

    public void setSKUUnit(String SKUUnit) {
        this.SKUUnit = SKUUnit;
    }

    public String getSKUUnitCode() {
        return SKUUnitCode;
    }

    public void setSKUUnitCode(String SKUUnitCode) {
        this.SKUUnitCode = SKUUnitCode;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getDepartmentCode() {
        return DepartmentCode;
    }

    public void setDepartmentCode(String DepartmentCode) {
        this.DepartmentCode = DepartmentCode;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getChallanDate() {
        return challanDate;
    }

    public void setChallanDate(String challanDate) {
        this.challanDate = challanDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public List<aarogya.inventory.grnApproval.shared.GrdItemDetail_Mdl> getGrdItemDetails() {
        return grdItemDetails;
    }

    public void setGrdItemDetails(List<aarogya.inventory.grnApproval.shared.GrdItemDetail_Mdl> grdItemDetails) {
        this.grdItemDetails = grdItemDetails;
    }

    private List<GrdItemDetail_Mdl> grdItemDetails = new ArrayList<GrdItemDetail_Mdl>();

    public String getColourName() {
        return colourName;
    }

    public void setColourName(String colourName) {
        this.colourName = colourName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getFreeQty() {
        return FreeQty;
    }

    public void setFreeQty(String FreeQty) {
        this.FreeQty = FreeQty;
    }

}
