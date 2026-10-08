package dataman.dmToolkit.dmGUI.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DmGUIRow_Mdl implements Serializable, IsSerializable {

    String configCode;
    List<DmGUIControl_Mdl> guiControlList = new ArrayList<>();

    public DmGUIRow_Mdl() {
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public List<DmGUIControl_Mdl> getGUIControlList() {
        return guiControlList;
    }

    public void setGUIControlList(List<DmGUIControl_Mdl> guiControlList) {
        this.guiControlList = guiControlList;
    }
}
