package aarogya.pharmacy.enquiry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdIndentDetails_Mdl extends KeyField implements Serializable, IsSerializable {

    private String indentQty;
    private String indentSkuTag;
    private String indentSkuText;
    private String indentDate;
    private String reqDate;
    private String indentVSNo;
    private String image;
    private String indentQtyInPurchaseUnit;

    public GrdIndentDetails_Mdl() {
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

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getIndentQty() {
        return indentQty;
    }

    public void setIndentQty(String indentQty) {
        this.indentQty = indentQty;
    }

    public String getIndentSkuTag() {
        return indentSkuTag;
    }

    public void setIndentSkuTag(String indentSkuTag) {
        this.indentSkuTag = indentSkuTag;
    }

    public String getIndentSkuText() {
        return indentSkuText;
    }

    public void setIndentSkuText(String indentSkuText) {
        this.indentSkuText = indentSkuText;
    }

    public String getIndentDate() {
        return indentDate;
    }

    public void setIndentDate(String indentDate) {
        this.indentDate = indentDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
