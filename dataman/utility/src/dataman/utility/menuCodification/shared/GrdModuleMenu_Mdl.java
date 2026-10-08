package dataman.utility.menuCodification.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdModuleMenu_Mdl extends KeyField implements Serializable,IsSerializable{

    private String menuName;
    private String menuShortHand;

    public GrdModuleMenu_Mdl() {
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuShortHand() {
        return menuShortHand;
    }

    public void setMenuShortHand(String menuShortHand) {
        this.menuShortHand = menuShortHand;
    }

}
