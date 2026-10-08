package aarogya.master.environmentService.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdEnviroSerive_Mdl extends KeyField implements Serializable, IsSerializable {

    private String defConsultantTag, defConsultantText;

    public GrdEnviroSerive_Mdl() {
    }

    public String getDefConsultantTag() {
        return defConsultantTag;
    }

    public void setDefConsultantTag(String defConsultantTag) {
        this.defConsultantTag = defConsultantTag;
    }

    public String getDefConsultantText() {
        return defConsultantText;
    }

    public void setDefConsultantText(String defConsultantText) {
        this.defConsultantText = defConsultantText;
    }

}
