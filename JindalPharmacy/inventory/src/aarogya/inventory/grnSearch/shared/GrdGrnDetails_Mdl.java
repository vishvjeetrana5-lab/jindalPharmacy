package aarogya.inventory.grnSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdGrnDetails_Mdl extends KeyField implements Serializable, IsSerializable {

    //sharedKeyFieldCode = pcDocId;
    //sharedKeyFieldName = pcRecId;
    
    private String pcV_SNo;
    private String pcV_Date;
    private String pcQty;
    private String pendingQty;

    private String itemName;
    private String unitName;
    private String remark;
    private String isChecked;

    private String poV_Date;
    private String poNo;
    private String poDocID;
    
    public GrdGrnDetails_Mdl() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getPcQty() {
        return pcQty;
    }

    public void setPcQty(String pcQty) {
        this.pcQty = pcQty;
    }

    public String getPendingQty() {
        return pendingQty;
    }

    public void setPendingQty(String pendingQty) {
        this.pendingQty = pendingQty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getPcV_SNo() {
        return pcV_SNo;
    }

    public void setPcV_SNo(String pcV_SNo) {
        this.pcV_SNo = pcV_SNo;
    }

    public String getPcV_Date() {
        return pcV_Date;
    }

    public void setPcV_Date(String pcV_Date) {
        this.pcV_Date = pcV_Date;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getPoV_Date() {
        return poV_Date;
    }

    public void setPoV_Date(String poV_Date) {
        this.poV_Date = poV_Date;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getPoDocID() {
        return poDocID;
    }

    public void setPoDocID(String poDocID) {
        this.poDocID = poDocID;
    }

}
