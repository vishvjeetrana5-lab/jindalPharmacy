package aarogya.master.tabIndexSettings.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TabIndexSettings_Mdl extends KeyField implements Serializable, IsSerializable {

    private String menuName;
    private String menuText;

    private List<GridTabIndexSettings_Mdl> grdTabIndexMdl = new ArrayList<GridTabIndexSettings_Mdl>();

    public TabIndexSettings_Mdl() {
    }

    public List<GridTabIndexSettings_Mdl> getGrdTabIndexMdl() {
        return grdTabIndexMdl;
    }

    public void setGrdTabIndexMdl(List<GridTabIndexSettings_Mdl> grdTabIndexMdl) {
        this.grdTabIndexMdl = grdTabIndexMdl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

}
