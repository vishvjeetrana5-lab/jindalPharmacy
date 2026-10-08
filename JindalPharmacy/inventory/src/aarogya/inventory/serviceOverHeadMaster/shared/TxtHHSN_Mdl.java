package aarogya.inventory.serviceOverHeadMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHHSN_Mdl extends KeyField implements Serializable, IsSerializable {

    //sharedKeyFieldCode: code
    //sharedKeyFieldName: name
    private String hsn;
    private String itemGSTNatureTag;
    private String itemGSTNatureText;

    public TxtHHSN_Mdl() {
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getItemGSTNatureTag() {
        return itemGSTNatureTag;
    }

    public void setItemGSTNatureTag(String itemGSTNatureTag) {
        this.itemGSTNatureTag = itemGSTNatureTag;
    }

    public String getItemGSTNatureText() {
        return itemGSTNatureText;
    }

    public void setItemGSTNatureText(String itemGSTNatureText) {
        this.itemGSTNatureText = itemGSTNatureText;
    }

}
