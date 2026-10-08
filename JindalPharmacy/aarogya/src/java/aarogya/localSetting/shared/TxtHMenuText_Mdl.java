package aarogya.localSetting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHMenuText_Mdl extends KeyField implements Serializable, IsSerializable {

    private String menuText;
    private String menuModuleName;

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    public String getMenuModuleName() {
        return menuModuleName;
    }

    public void setMenuModuleName(String menuModuleName) {
        this.menuModuleName = menuModuleName;
    }

}
