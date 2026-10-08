package aarogya.selfToolkit.itemUpdatePopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ItemUpdateField_Mdl extends KeyField implements Serializable, IsSerializable {

    public enum Field implements Serializable, IsSerializable {
        MRP, SALE_RATE, PURCHASE_RATE, SCHEME, PURCHASE_DISCOUNT_PERCENTAGE;

        private Field() {
        }
    }

    //sharedKeyFieldCode = fieldText
    //sharedKeyFieldName = fieldText
    private Field field;
    private String masterValue;
    private String currentValue;
    private String finalValue;
    private String saveToItem;
    private String saveToCompany;

    public ItemUpdateField_Mdl() {
    }

    public ItemUpdateField_Mdl(String caption, Field field, String masterValue, String currentValue, String saveToItem, String saveToCompany) {
        super(caption, caption);
        this.field = field;
        this.masterValue = masterValue;
        this.currentValue = currentValue;
        this.finalValue = this.currentValue;
        this.saveToItem = saveToItem;
        this.saveToCompany = saveToCompany;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getMasterValue() {
        return masterValue;
    }

    public void setMasterValue(String masterValue) {
        this.masterValue = masterValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(String finalValue) {
        this.finalValue = finalValue;
    }

    public String getSaveToItem() {
        return saveToItem;
    }

    public void setSaveToItem(String saveToItem) {
        this.saveToItem = saveToItem;
    }

    public String getSaveToCompany() {
        return saveToCompany;
    }

    public void setSaveToCompany(String saveToCompany) {
        this.saveToCompany = saveToCompany;
    }

    public ItemUpdateField_Mdl clone() {
        ItemUpdateField_Mdl grdMdl = new ItemUpdateField_Mdl();

        copy(grdMdl);

        return grdMdl;
    }

    private void copy(ItemUpdateField_Mdl grn_Mdl) {
        grn_Mdl.setSharedKeyFieldCode(grn_Mdl.getSharedKeyFieldCode());
        grn_Mdl.setSharedKeyFieldName(grn_Mdl.getSharedKeyFieldName());
        grn_Mdl.setField(grn_Mdl.getField());
        grn_Mdl.setMasterValue(grn_Mdl.getMasterValue());
        grn_Mdl.setCurrentValue(grn_Mdl.getCurrentValue());
        grn_Mdl.setFinalValue(grn_Mdl.getFinalValue());
        grn_Mdl.setSaveToItem(grn_Mdl.getSaveToItem());
        grn_Mdl.setSaveToCompany(grn_Mdl.getSaveToCompany());
    }

}
