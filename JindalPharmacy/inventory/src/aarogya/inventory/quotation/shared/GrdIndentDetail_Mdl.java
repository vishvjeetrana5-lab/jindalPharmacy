package aarogya.inventory.quotation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class GrdIndentDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String indentVSNo;
    private String indentQty;
    private String indentQtyInPurchaseUnit;
    private String indentSKU;
    private String indentDate;
    private String requiredDate;
    private String colourCode;
    private String colourName;
    private String sizeCode;
    private String sizeName;
    private String remark;
    private String image;

    public GrdIndentDetail_Mdl() {
    }

    public String getIndentQtyInPurchaseUnit() {
        return indentQtyInPurchaseUnit;
    }

    public void setIndentQtyInPurchaseUnit(String indentQtyInPurchaseUnit) {
        this.indentQtyInPurchaseUnit = indentQtyInPurchaseUnit;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

}
