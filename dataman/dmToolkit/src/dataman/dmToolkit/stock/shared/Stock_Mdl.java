package dataman.dmToolkit.stock.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class Stock_Mdl extends KeyField implements IsSerializable, Serializable {

    private String site_Code;
    private String v_Type;
    private String v_No;
    private String recId;
    private String recIdPrefix;
    private String v_Date;
    private String departmentCode;
    private String stockContraSubCode;
    private String acContraSubCode;
    private String refDocId;
    private String refV_Type;
    private String refV_SNo;
    private String refChallanNo;
    private String refChallanDate;
    private String itemCode;
    private String ibCode;
    private String recQty;
    private String isuQty;
    private String skuValue;    
    private String landedSkuValue;
    private String staffSkuValue;
    private String otherAdjustment;
    private String skuSaleValue;    
    private String landedSkuSaleValue;
    private String isSystemGenerated;
    private String remark;
    private String grossPurchaseRate;
    private StockFifo_Mdl stockFifo_Mdl = new StockFifo_Mdl();

    public Stock_Mdl() {
    }

    public String getSite_Code() {
        return site_Code;
    }

    public void setSite_Code(String site_Code) {
        this.site_Code = site_Code;
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

    public String getV_No() {
        return v_No;
    }

    public void setV_No(String v_No) {
        this.v_No = v_No;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getRecIdPrefix() {
        return recIdPrefix;
    }

    public void setRecIdPrefix(String recIdPrefix) {
        this.recIdPrefix = recIdPrefix;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getStockContraSubCode() {
        return stockContraSubCode;
    }

    public void setStockContraSubCode(String stockContraSubCode) {
        this.stockContraSubCode = stockContraSubCode;
    }

    public String getAcContraSubCode() {
        return acContraSubCode;
    }

    public void setAcContraSubCode(String acContraSubCode) {
        this.acContraSubCode = acContraSubCode;
    }

    public String getRefDocId() {
        return refDocId;
    }

    public void setRefDocId(String refDocId) {
        this.refDocId = refDocId;
    }

    public String getRefV_Type() {
        return refV_Type;
    }

    public void setRefV_Type(String refV_Type) {
        this.refV_Type = refV_Type;
    }

    public String getRefChallanNo() {
        return refChallanNo;
    }

    public void setRefChallanNo(String refChallanNo) {
        this.refChallanNo = refChallanNo;
    }

    public String getRefChallanDate() {
        return refChallanDate;
    }

    public void setRefChallanDate(String refChallanDate) {
        this.refChallanDate = refChallanDate;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
    }
    
    public String getRecQty() {
        return recQty;
    }

    public void setRecQty(String recQty) {
        this.recQty = recQty;
    }

    public String getIsuQty() {
        return isuQty;
    }

    public void setIsuQty(String isuQty) {
        this.isuQty = isuQty;
    }

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue;
    }

    public String getLandedSkuValue() {
        return landedSkuValue;
    }

    public void setLandedSkuValue(String landedSkuValue) {
        this.landedSkuValue = landedSkuValue;
    }

    public String getOtherAdjustment() {
        return otherAdjustment;
    }

    public void setOtherAdjustment(String otherAdjustment) {
        this.otherAdjustment = otherAdjustment;
    }

        public String getSkuSaleValue() {
        return skuSaleValue;
    }

    public void setSkuSaleValue(String skuSaleValue) {
        this.skuSaleValue = skuSaleValue;
    }

    public String getLandedSkuSaleValue() {
        return landedSkuSaleValue;
    }

    public void setLandedSkuSaleValue(String landedSkuSaleValue) {
        this.landedSkuSaleValue = landedSkuSaleValue;
    }

    public String getIsSystemGenerated() {
        return isSystemGenerated;
    }

    public void setIsSystemGenerated(String isSystemGenerated) {
        this.isSystemGenerated = isSystemGenerated;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public StockFifo_Mdl getStockFifo_Mdl() {
        return stockFifo_Mdl;
    }

    public void setStockFifo_Mdl(StockFifo_Mdl stockFifo_Mdl) {
        this.stockFifo_Mdl = stockFifo_Mdl;
    }

    public String getRefV_SNo() {
        return refV_SNo;
    }

    public void setRefV_SNo(String refV_Sno) {
        this.refV_SNo = refV_Sno;
    }


    
    public void copy(Stock_Mdl stock_Mdl) {
        stock_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        stock_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        stock_Mdl.setSharedPreparedBy(this.getSharedPreparedBy());
        stock_Mdl.setSharedModifiedBy(this.getSharedModifiedBy());
        stock_Mdl.setSite_Code(this.getSite_Code());
        stock_Mdl.setV_Type(this.getV_Type());
        stock_Mdl.setV_No(this.getV_No());
        stock_Mdl.setRecId(this.getRecId());
        stock_Mdl.setRecIdPrefix(this.getRecIdPrefix());
        stock_Mdl.setV_Date(this.getV_Date());
        stock_Mdl.setDepartmentCode(this.getDepartmentCode());
        stock_Mdl.setStockContraSubCode(this.getStockContraSubCode());
        stock_Mdl.setAcContraSubCode(this.getAcContraSubCode());
        stock_Mdl.setRefDocId(this.getRefDocId());
        stock_Mdl.setRefV_Type(this.getRefV_Type());
        stock_Mdl.setRefChallanNo(this.getRefChallanNo());
        stock_Mdl.setRefChallanDate(this.getRefChallanDate());
        stock_Mdl.setItemCode(this.getItemCode());
        stock_Mdl.setIbCode(this.getIbCode());
        stock_Mdl.setRecQty(this.getRecQty());
        stock_Mdl.setIsuQty(this.getIsuQty());
        stock_Mdl.setSkuValue(this.getSkuValue());
        stock_Mdl.setLandedSkuValue(this.getLandedSkuValue());
        stock_Mdl.setStaffSkuValue(this.getStaffSkuValue());
        stock_Mdl.setOtherAdjustment(this.getOtherAdjustment());
        stock_Mdl.setSkuSaleValue(this.getSkuSaleValue());
        stock_Mdl.setLandedSkuSaleValue(this.getLandedSkuSaleValue());
        stock_Mdl.setIsSystemGenerated(this.getIsSystemGenerated());
        stock_Mdl.setRemark(this.getRemark());
        stock_Mdl.setRefV_SNo(this.getRefV_SNo());
        stock_Mdl.setGrossPurchaseRate(this.getGrossPurchaseRate());

        if (this.getStockFifo_Mdl() == null) {
            stock_Mdl.setStockFifo_Mdl(null);
        } else {
//            stock_Mdl.getStockFifo_Mdl().copy(this.getStockFifo_Mdl());
            StockFifo_Mdl fifo_Mdl = new StockFifo_Mdl();
            this.getStockFifo_Mdl().copy(fifo_Mdl);
            stock_Mdl.setStockFifo_Mdl(fifo_Mdl);
        }
    }

    public String getGrossPurchaseRate() {
        return grossPurchaseRate;
    }

    public void setGrossPurchaseRate(String grossPurchaseRate) {
        this.grossPurchaseRate = grossPurchaseRate;
    }

    public String getStaffSkuValue() {
        return staffSkuValue;
    }

    public void setStaffSkuValue(String staffSkuValue) {
        this.staffSkuValue = staffSkuValue;
    }

}
