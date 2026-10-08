package aarogya.pharmacy.employeeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHAccountGroup_Mdl extends KeyField implements Serializable, IsSerializable {

    private String nature;
    private String groupNature;

    public TxtHAccountGroup_Mdl() {
    }

    public String getGroupNature() {
        return groupNature;
    }

    public void setGroupNature(String groupNature) {
        this.groupNature = groupNature;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }
}
