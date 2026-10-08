package aarogya.inventory.gateOut.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GateOutGridEdit_Mdl extends KeyField implements Serializable, IsSerializable {

    private String itemDescription;
    private String Quantity;
    private String remark;
    private String unitCode;
    private String unitName;
    private String specification;
    private String estdValue;
    private String assetCode;
    private String assetName;
    private String refDocID;
    private String refv_Sno;
    private String destination;
    private String gateInV_SNo;
    private String gateInDate;
    private String gateInTime;

    public GateOutGridEdit_Mdl() {
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String Quantity) {
        this.Quantity = Quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
    
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    
    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getEstdValue() {
        return estdValue;
    }

    public void setEstdValue(String estdValue) {
        this.estdValue = estdValue;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getRefDocID() {
        return refDocID;
    }

    public void setRefDocID(String refDocID) {
        this.refDocID = refDocID;
    }

    public String getRefv_Sno() {
        return refv_Sno;
    }

    public void setRefv_Sno(String refv_Sno) {
        this.refv_Sno = refv_Sno;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getGateInV_SNo() {
        return gateInV_SNo;
    }

    public void setGateInV_SNo(String gateInV_SNo) {
        this.gateInV_SNo = gateInV_SNo;
    }

    public String getGateInDate() {
        return gateInDate;
    }

    public void setGateInDate(String gateInDate) {
        this.gateInDate = gateInDate;
    }

    public String getGateInTime() {
        return gateInTime;
    }

    public void setGateInTime(String gateInTime) {
        this.gateInTime = gateInTime;
    }

    

    

}
