package dataman.utility.voucherType.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class TxtHMenuText_Mdl extends KeyField implements Serializable, IsSerializable {

    private String menuText;
    private String menuName;
    private String menuModuleName;

    public TxtHMenuText_Mdl() {
    }

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuModuleName() {
        return menuModuleName;
    }

    public void setMenuModuleName(String menuModuleName) {
        this.menuModuleName = menuModuleName;
    }

}
