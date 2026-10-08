package aarogya.pharmacy.quotation.shared;

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
    private String remark;
    private String image;
    private String indentMrp;

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

    public String getIndentMrp() {
        return indentMrp;
    }

    public void setIndentMrp(String indentMrp) {
        this.indentMrp = indentMrp;
    }

}
