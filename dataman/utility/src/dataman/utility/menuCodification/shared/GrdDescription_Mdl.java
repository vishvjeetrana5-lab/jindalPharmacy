package dataman.utility.menuCodification.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdDescription_Mdl extends KeyField implements Serializable,IsSerializable{

    private String menuName;
    private String menuCode;
    private String moduleName;

    public GrdDescription_Mdl() {
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

//    public String getMenuText() {
//        return menuText;
//    }
//
//    public void setMenuText(String menuText) {
//        this.menuText = menuText;
//    }
}
