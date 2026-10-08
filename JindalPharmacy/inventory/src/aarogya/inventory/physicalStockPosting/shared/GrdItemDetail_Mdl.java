package aarogya.inventory.physicalStockPosting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    //sharedKeyFieldCode = itemCode
    //sharedKeyFieldName = itemName
    //
    //Item Details
    private String propertyList;
    private String skuRate;
    private String skuValue;

    //IAP
    private String isubCode;
    
    //Properties
    private String ibCode;
    private String batchNo;
    private String manufacturingDate;
    private String expiryDate;
    private String colourCode;
    private String colourName;
    private String sizeCode;
    private String sizeName;
    private String mrp;
    private String skuMrp;

    //Quantity
    private String physicalQty;
    private String computerQty;
    private String adjQty;

    //Other
    private String EType_IR;
    private String v_SNo;

    public GrdItemDetail_Mdl() {
    }

    public String getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(String propertyList) {
        this.propertyList = propertyList;
    }

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getColourCode() {
        return colourCode;
    }

    public void setColourCode(String colourCode) {
        this.colourCode = colourCode;
    }

    public String getColourName() {
        return colourName;
    }

    public void setColourName(String colourName) {
        this.colourName = colourName;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSkuMrp() {
        return skuMrp;
    }

    public void setSkuMrp(String skuMrp) {
        this.skuMrp = skuMrp;
    }

    public String getPhysicalQty() {
        return physicalQty;
    }

    public void setPhysicalQty(String physicalQty) {
        this.physicalQty = physicalQty;
    }

    public String getComputerQty() {
        return computerQty;
    }

    public void setComputerQty(String computerQty) {
        this.computerQty = computerQty;
    }

    public String getAdjQty() {
        return adjQty;
    }

    public void setAdjQty(String adjQty) {
        this.adjQty = adjQty;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getEType_IR() {
        return EType_IR;
    }

    public void setEType_IR(String EType_IR) {
        this.EType_IR = EType_IR;
    }

    public String getSkuRate() {
        return skuRate;
    }

    public void setSkuRate(String skuRate) {
        this.skuRate = skuRate;
    }

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue;
    }

    public String getIsubCode() {
        return isubCode;
    }

    public void setIsubCode(String isubCode) {
        this.isubCode = isubCode;
    }
}
