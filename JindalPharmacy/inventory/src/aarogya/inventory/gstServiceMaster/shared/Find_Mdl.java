package aarogya.inventory.gstServiceMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    public Find_Mdl() {
    }
    //sharedkeyFieldCode=code
    //sharedkeyFieldName=name

    private String category;
    private String group;
    private String skuName;
    private String active;
    private String ManualCode;
    private String itemTypeTag;
    private String itemTypeText;
    private String hsnName;

    public String getHsnName() {
        return hsnName;
    }

    public void setHsnName(String hsnName) {
        this.hsnName = hsnName;
    }

    public String getItemTypeTag() {
        return itemTypeTag;
    }

    public void setItemTypeTag(String itemTypeTag) {
        this.itemTypeTag = itemTypeTag;
    }

    public String getItemTypeText() {
        return itemTypeText;
    }

    public void setItemTypeText(String itemTypeText) {
        this.itemTypeText = itemTypeText;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getManualCode() {
        return ManualCode;
    }

    public void setManualCode(String ManualCode) {
        this.ManualCode = ManualCode;
    }

}
