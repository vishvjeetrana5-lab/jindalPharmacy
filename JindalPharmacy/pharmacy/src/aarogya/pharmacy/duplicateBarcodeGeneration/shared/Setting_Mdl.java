package aarogya.pharmacy.duplicateBarcodeGeneration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Setting_Mdl implements Serializable, IsSerializable {

    private String fromDate;
    private String toDate;
    private String vPrefix;
    private String department;
    private String departmentCode;
    private String pBillNoCode;
    private String pBillNo;
    private String barCodeDocid;
    private String barCodeRecid;
    private String item;
    private String itemCode;
    private String batch;
    private String color;
    private String colorCode;
    private String size;
    private String sizeCode;
    private String manufacturingDate;
    private String expiryDate;
    private String saleRate;
    private String mrp;

    public Setting_Mdl() {
    }

    public String getpBillNoCode() {
        return pBillNoCode;
    }

    public void setpBillNoCode(String pBillNoCode) {
        this.pBillNoCode = pBillNoCode;
    }

    public String getpBillNo() {
        return pBillNo;
    }

    public void setpBillNo(String pBillNo) {
        this.pBillNo = pBillNo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getBarCodeDocid() {
        return barCodeDocid;
    }

    public void setBarCodeDocid(String barCodeDocid) {
        this.barCodeDocid = barCodeDocid;
    }

    public String getBarCodeRecid() {
        return barCodeRecid;
    }

    public void setBarCodeRecid(String barCodeRecid) {
        this.barCodeRecid = barCodeRecid;
    }

    public String getvPrefix() {
        return vPrefix;
    }

    public void setvPrefix(String vPrefix) {
        this.vPrefix = vPrefix;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

}
