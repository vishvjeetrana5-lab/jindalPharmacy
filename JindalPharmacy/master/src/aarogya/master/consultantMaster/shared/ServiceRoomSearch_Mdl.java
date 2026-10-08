package aarogya.master.consultantMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ServiceRoomSearch_Mdl extends KeyField implements Serializable, IsSerializable {

    // Service Code & name in KeyFieldExtended
    private String categoryTag, categoryText, amount;

    public ServiceRoomSearch_Mdl() {
    }

    public String getCategoryTag() {
        return categoryTag;
    }

    public void setCategoryTag(String categoryTag) {
        this.categoryTag = categoryTag;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
