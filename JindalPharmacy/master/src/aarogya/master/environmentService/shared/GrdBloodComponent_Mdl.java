package aarogya.master.environmentService.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdBloodComponent_Mdl extends KeyField implements Serializable, IsSerializable {

    private String linkedServiceTag, linkedServiceText;

    public GrdBloodComponent_Mdl() {
    }

    public String getLinkedServiceTag() {
        return linkedServiceTag;
    }

    public void setLinkedServiceTag(String linkedServiceTag) {
        this.linkedServiceTag = linkedServiceTag;
    }

    public String getLinkedServiceText() {
        return linkedServiceText;
    }

    public void setLinkedServiceText(String linkedServiceText) {
        this.linkedServiceText = linkedServiceText;
    }
}
