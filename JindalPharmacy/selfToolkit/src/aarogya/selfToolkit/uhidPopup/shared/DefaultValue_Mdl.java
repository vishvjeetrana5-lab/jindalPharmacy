package aarogya.selfToolkit.uhidPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String IdTypeTag, IdTypeText;

    public DefaultValue_Mdl() {

    }

    public String getIdTypeTag() {
        return IdTypeTag;
    }

    public void setIdTypeTag(String IdTypeTag) {
        this.IdTypeTag = IdTypeTag;
    }

    public String getIdTypeText() {
        return IdTypeText;
    }

    public void setIdTypeText(String IdTypeText) {
        this.IdTypeText = IdTypeText;
    }

}
