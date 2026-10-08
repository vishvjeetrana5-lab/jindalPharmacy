package aarogya.dashBoard.favorite.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class MenuDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String menuName;

    public MenuDetail_Mdl() {
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

}
