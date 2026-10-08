package dataman.utility.numberSystem.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String category;
    private String Description;
    private String nsGroup;

    public Find_Mdl() {
    }

    public String getNsGroup() {
        return nsGroup;
    }

    public void setNsGroup(String nsGroup) {
        this.nsGroup = nsGroup;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

}
