package aarogya.pharmacy.barcodeGeneration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdBarcodeGeneration_Mdl extends KeyField implements Serializable, IsSerializable {

    private String itemName;
    private String itemCode;
    private String department;
    private String batchNo;
    private String color;
    private String size;
    private String expiryDate;
    private String barcode;
    private String isBarcode;
    private String itemMRP;
    private String noOFStricker;
    private String skuQty;

    public GrdBarcodeGeneration_Mdl() {
    }

    public String getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(String skuQty) {
        this.skuQty = skuQty;
    }

    public String getNoOFStricker() {
        return noOFStricker;
    }

    public void setNoOFStricker(String noOFStricker) {
        this.noOFStricker = noOFStricker;
    }

    public String getItemMRP() {
        return itemMRP;
    }

    public void setItemMRP(String itemMRP) {
        this.itemMRP = itemMRP;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getIsBarcode() {
        return isBarcode;
    }

    public void setIsBarcode(String isBarcode) {
        this.isBarcode = isBarcode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

}
