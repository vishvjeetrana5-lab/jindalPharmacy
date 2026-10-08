package aarogya.selfToolkit.toolTip.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ToolTip_Mdl implements Serializable, IsSerializable {

    private String caption;
    private String value;

    public ToolTip_Mdl() {
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
