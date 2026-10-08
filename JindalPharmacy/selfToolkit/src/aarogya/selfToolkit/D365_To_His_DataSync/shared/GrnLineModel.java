package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GrnLineModel implements Serializable, IsSerializable {

    private String d365DocId;
    private String vSNo;
    private String indentDocId;
    private String indentVSNo;

    private String d365ItemCode;
    private String dmItemCode;
    private String itemName;

    private String d365DepartmentCode;
    private String dmDepartmentCode;
    private String departmentName;

    private String d365UnitCode;
    private String dmUnitCode;

    private String d365HsnCode;
    private String dmHsnCode;

    private String indentSKUQty;
    private String recSKUQty;
    private String rate;

    private String ohAmtGross;
    private String ohAmtDiscount;
    private String ohAtDiscount;
    private String ohAmtTaxable;

    private String ohAtCGST;
    private String ohAmtCGST;
    private String ohAtSGST;
    private String ohAmtSGST;
    private String ohAtIGST;
    private String ohAmtIGST;
    private String ohAtUGST;
    private String ohAmtUGST;

    private String ohAtFinalDiscount;
    private String ohAmtFinalDiscount;
    private String ohAmtNet;

    private String landedSkuValue;
    private String landedSkuRate;

    private String remark;

    private String mrp;
    private String skuMrp;
    private String saleRate;
    private String skuSaleRate;
    private String staffSaleRate;
    private String skuStaffSaleRate;

    private String barcode;
    private String batchNo;
    private String ibCode;
    private String manufacturingDate;
    private String expiryDate;

    private String itemMakeCode;
    private String gstTypeCode;
    private String itemGSTNature;

    private String dmStatus;
    private String dmTimeStamp;
    private String dmDocId;

    public String getD365DocId() {
        return d365DocId;
    }

    public void setD365DocId(String d365DocId) {
        this.d365DocId = d365DocId;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getIndentDocId() {
        return indentDocId;
    }

    public void setIndentDocId(String indentDocId) {
        this.indentDocId = indentDocId;
    }

    public String getIndentVSNo() {
        return indentVSNo;
    }

    public void setIndentVSNo(String indentVSNo) {
        this.indentVSNo = indentVSNo;
    }

    public String getD365ItemCode() {
        return d365ItemCode;
    }

    public void setD365ItemCode(String d365ItemCode) {
        this.d365ItemCode = d365ItemCode;
    }

    public String getDmItemCode() {
        return dmItemCode;
    }

    public void setDmItemCode(String dmItemCode) {
        this.dmItemCode = dmItemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getD365DepartmentCode() {
        return d365DepartmentCode;
    }

    public void setD365DepartmentCode(String d365DepartmentCode) {
        this.d365DepartmentCode = d365DepartmentCode;
    }

    public String getDmDepartmentCode() {
        return dmDepartmentCode;
    }

    public void setDmDepartmentCode(String dmDepartmentCode) {
        this.dmDepartmentCode = dmDepartmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getD365UnitCode() {
        return d365UnitCode;
    }

    public void setD365UnitCode(String d365UnitCode) {
        this.d365UnitCode = d365UnitCode;
    }

    public String getDmUnitCode() {
        return dmUnitCode;
    }

    public void setDmUnitCode(String dmUnitCode) {
        this.dmUnitCode = dmUnitCode;
    }

    public String getD365HsnCode() {
        return d365HsnCode;
    }

    public void setD365HsnCode(String d365HsnCode) {
        this.d365HsnCode = d365HsnCode;
    }

    public String getDmHsnCode() {
        return dmHsnCode;
    }

    public void setDmHsnCode(String dmHsnCode) {
        this.dmHsnCode = dmHsnCode;
    }

    public String getIndentSKUQty() {
        return indentSKUQty;
    }

    public void setIndentSKUQty(String indentSKUQty) {
        this.indentSKUQty = indentSKUQty;
    }

    public String getRecSKUQty() {
        return recSKUQty;
    }

    public void setRecSKUQty(String recSKUQty) {
        this.recSKUQty = recSKUQty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getOhAmtGross() {
        return ohAmtGross;
    }

    public void setOhAmtGross(String ohAmtGross) {
        this.ohAmtGross = ohAmtGross;
    }

    public String getOhAmtDiscount() {
        return ohAmtDiscount;
    }

    public void setOhAmtDiscount(String ohAmtDiscount) {
        this.ohAmtDiscount = ohAmtDiscount;
    }

    public String getOhAtDiscount() {
        return ohAtDiscount;
    }

    public void setOhAtDiscount(String ohAtDiscount) {
        this.ohAtDiscount = ohAtDiscount;
    }

    public String getOhAmtTaxable() {
        return ohAmtTaxable;
    }

    public void setOhAmtTaxable(String ohAmtTaxable) {
        this.ohAmtTaxable = ohAmtTaxable;
    }

    public String getOhAtCGST() {
        return ohAtCGST;
    }

    public void setOhAtCGST(String ohAtCGST) {
        this.ohAtCGST = ohAtCGST;
    }

    public String getOhAmtCGST() {
        return ohAmtCGST;
    }

    public void setOhAmtCGST(String ohAmtCGST) {
        this.ohAmtCGST = ohAmtCGST;
    }

    public String getOhAtSGST() {
        return ohAtSGST;
    }

    public void setOhAtSGST(String ohAtSGST) {
        this.ohAtSGST = ohAtSGST;
    }

    public String getOhAmtSGST() {
        return ohAmtSGST;
    }

    public void setOhAmtSGST(String ohAmtSGST) {
        this.ohAmtSGST = ohAmtSGST;
    }

    public String getOhAtIGST() {
        return ohAtIGST;
    }

    public void setOhAtIGST(String ohAtIGST) {
        this.ohAtIGST = ohAtIGST;
    }

    public String getOhAmtIGST() {
        return ohAmtIGST;
    }

    public void setOhAmtIGST(String ohAmtIGST) {
        this.ohAmtIGST = ohAmtIGST;
    }

    public String getOhAtUGST() {
        return ohAtUGST;
    }

    public void setOhAtUGST(String ohAtUGST) {
        this.ohAtUGST = ohAtUGST;
    }

    public String getOhAmtUGST() {
        return ohAmtUGST;
    }

    public void setOhAmtUGST(String ohAmtUGST) {
        this.ohAmtUGST = ohAmtUGST;
    }

    public String getOhAtFinalDiscount() {
        return ohAtFinalDiscount;
    }

    public void setOhAtFinalDiscount(String ohAtFinalDiscount) {
        this.ohAtFinalDiscount = ohAtFinalDiscount;
    }

    public String getOhAmtFinalDiscount() {
        return ohAmtFinalDiscount;
    }

    public void setOhAmtFinalDiscount(String ohAmtFinalDiscount) {
        this.ohAmtFinalDiscount = ohAmtFinalDiscount;
    }

    public String getOhAmtNet() {
        return ohAmtNet;
    }

    public void setOhAmtNet(String ohAmtNet) {
        this.ohAmtNet = ohAmtNet;
    }

    public String getLandedSkuValue() {
        return landedSkuValue;
    }

    public void setLandedSkuValue(String landedSkuValue) {
        this.landedSkuValue = landedSkuValue;
    }

    public String getLandedSkuRate() {
        return landedSkuRate;
    }

    public void setLandedSkuRate(String landedSkuRate) {
        this.landedSkuRate = landedSkuRate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getStaffSaleRate() {
        return staffSaleRate;
    }

    public void setStaffSaleRate(String staffSaleRate) {
        this.staffSaleRate = staffSaleRate;
    }

    public String getSkuStaffSaleRate() {
        return skuStaffSaleRate;
    }

    public void setSkuStaffSaleRate(String skuStaffSaleRate) {
        this.skuStaffSaleRate = skuStaffSaleRate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
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

    public String getItemMakeCode() {
        return itemMakeCode;
    }

    public void setItemMakeCode(String itemMakeCode) {
        this.itemMakeCode = itemMakeCode;
    }

    public String getGstTypeCode() {
        return gstTypeCode;
    }

    public void setGstTypeCode(String gstTypeCode) {
        this.gstTypeCode = gstTypeCode;
    }

    public String getItemGSTNature() {
        return itemGSTNature;
    }

    public void setItemGSTNature(String itemGSTNature) {
        this.itemGSTNature = itemGSTNature;
    }

    public String getDmStatus() {
        return dmStatus;
    }

    public void setDmStatus(String dmStatus) {
        this.dmStatus = dmStatus;
    }

    public String getDmTimeStamp() {
        return dmTimeStamp;
    }

    public void setDmTimeStamp(String dmTimeStamp) {
        this.dmTimeStamp = dmTimeStamp;
    }

    public String getDmDocId() {
        return dmDocId;
    }

    public void setDmDocId(String dmDocId) {
        this.dmDocId = dmDocId;
    }

}
