package aarogya.inventory.workOrder.shared;

import dataman.dmToolkit.overHeadLine.shared.OHLExtended;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.List;

public class GrdIndentDetail_Mdl extends KeyField implements Serializable, OHLExtended {

    private String indentVSNo;
    private String indentQty;
    private String orderQty;
    private String adjustedQty;
    private String indentSKU;
    private String decimalPlaces;
    private String indentDate;
    private String requiredDate;
    private String remark;
    private String properties;
    private List<OverHeadLine_Mdl> lstOHL;

    public GrdIndentDetail_Mdl() {
    }

    @Override
    public List<OverHeadLine_Mdl> getOHLList() {
        return lstOHL;
    }

    @Override
    public void setOHLList(List<OverHeadLine_Mdl> ohlList) {
        this.lstOHL = ohlList;
    }

    public String getIndentVSNo() {
        return indentVSNo;
    }

    public void setIndentVSNo(String indentVSNo) {
        this.indentVSNo = indentVSNo;
    }

    public String getIndentQty() {
        return indentQty;
    }

    public void setIndentQty(String indentQty) {
        this.indentQty = indentQty;
    }

    public String getIndentSKU() {
        return indentSKU;
    }

    public void setIndentSKU(String indentSKU) {
        this.indentSKU = indentSKU;
    }

    public String getIndentDate() {
        return indentDate;
    }

    public void setIndentDate(String indentDate) {
        this.indentDate = indentDate;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getAdjustedQty() {
        return adjustedQty;
    }

    public void setAdjustedQty(String adjustedQty) {
        this.adjustedQty = adjustedQty;
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
