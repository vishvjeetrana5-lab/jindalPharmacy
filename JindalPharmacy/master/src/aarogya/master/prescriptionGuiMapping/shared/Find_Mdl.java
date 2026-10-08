package aarogya.master.prescriptionGuiMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String guiTemplateName;

    public Find_Mdl() {
    }

    public String getGuiTemplateName() {
        return guiTemplateName;
    }

    public void setGuiTemplateName(String guiTemplateName) {
        this.guiTemplateName = guiTemplateName;
    }
}
