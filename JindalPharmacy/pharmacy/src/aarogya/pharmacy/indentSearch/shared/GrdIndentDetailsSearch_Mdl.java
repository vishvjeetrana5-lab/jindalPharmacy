package aarogya.pharmacy.indentSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdIndentDetailsSearch_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherSNo;
    private String quantity;
    private String sku;
    private String itemName;
    private String itemCompany;
    private String remark;
    private String departmentText;
    private String requiredDate;
    private String indentDate;
    private String isChecked;
    private String lastPurchaseRate;
    private String lastPurchaseDiscount;
    

        public GrdIndentDetailsSearch_Mdl() {
    }

    public String getVoucherSNo() {
        return voucherSNo;
    }

    public void setVoucherSNo(String voucherSNo) {
        this.voucherSNo = voucherSNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getIndentDate() {
        return indentDate;
    }

    public void setIndentDate(String indentDate) {
        this.indentDate = indentDate;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getlastPurchaseRate(){
        return lastPurchaseRate;
    }
    
    public void setlastPurchaseRate(String lastPurchaseRate){
        this.lastPurchaseRate=lastPurchaseRate;
    }
    
    public String getlastPurchaseDiscount(){
        return lastPurchaseDiscount;
    }
    
    public void setlastPurchaseDiscount(String lastPurchaseDiscount){
        this.lastPurchaseDiscount=lastPurchaseDiscount;
    }

    public String getItemCompany() {
        return itemCompany;
    }

    public void setItemCompany(String itemCompany) {
        this.itemCompany = itemCompany;
    }
    
}
