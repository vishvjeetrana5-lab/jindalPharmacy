package aarogya.inventory.workOrder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHIndentNo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String date;
    private String department;
    private String departmentCode;
    private String indentor;
    private String indentorCode;
    private String itemName;
    private String itemNameCode;
    private String sku;
    private String skuCode;
    private String indQty;
    private String adjQty;
    private String pendingQty;
    private String vType;

    public TxtHIndentNo_Mdl() {
    }

    public String getvType() {
        return vType;
    }

    public void setvType(String vType) {
        this.vType = vType;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getIndentorCode() {
        return indentorCode;
    }

    public void setIndentorCode(String indentorCode) {
        this.indentorCode = indentorCode;
    }

    public String getItemNameCode() {
        return itemNameCode;
    }

    public void setItemNameCode(String itemNameCode) {
        this.itemNameCode = itemNameCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIndentor() {
        return indentor;
    }

    public void setIndentor(String indentor) {
        this.indentor = indentor;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getIndQty() {
        return indQty;
    }

    public void setIndQty(String indQty) {
        this.indQty = indQty;
    }

    public String getAdjQty() {
        return adjQty;
    }

    public void setAdjQty(String adjQty) {
        this.adjQty = adjQty;
    }

    public String getPendingQty() {
        return pendingQty;
    }

    public void setPendingQty(String pendingQty) {
        this.pendingQty = pendingQty;
    }

}
