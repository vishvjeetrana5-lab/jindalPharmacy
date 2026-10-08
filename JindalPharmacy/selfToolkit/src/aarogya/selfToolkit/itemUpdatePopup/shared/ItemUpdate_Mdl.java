package aarogya.selfToolkit.itemUpdatePopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemUpdate_Mdl implements Serializable, IsSerializable {

    private String id;
    private String docId;
    private String v_SNo;
    private String v_Type;
    private String v_No;
    private String v_Date;
    private String recId;
    private String subcode;
    private String itemCode;
    private String unitCode;
    private List<ItemUpdateField_Mdl> lstItemUpdateFields = new ArrayList<>();

    public ItemUpdate_Mdl() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
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

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public List<ItemUpdateField_Mdl> getLstItemUpdateFields() {
        return lstItemUpdateFields;
    }

    public void setLstItemUpdateFields(List<ItemUpdateField_Mdl> lstItemUpdateFields) {
        this.lstItemUpdateFields = lstItemUpdateFields;
    }

}
