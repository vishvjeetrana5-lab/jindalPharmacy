package aarogya.pharmacy.physicalStockPosting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdMedicineDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    //sharedKeyFieldCode = medicineCode
    //sharedKeyFieldName = medicineName
    //
    //Item Details
    private String propertyList;
    private String skuRate;
    private String skuValue;

    //Properties
    private String ibCode;
    private String batchNo;
    private String manufacturingDate;
    private String expiryDate;
    private String mrp;
    private String skuMrp;
    private String saleRate;
    private String skuSaleRate;

    //Quantity
    private String physicalQty;
    private String computerQty;
    private String adjQty;

    //Other
    private String EType_IR;
    private String v_SNo;

    public GrdMedicineDetail_Mdl() {
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

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public String getSkuSaleRate() {
        return skuSaleRate;
    }

    public void setSkuSaleRate(String skuSaleRate) {
        this.skuSaleRate = skuSaleRate;
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
}
