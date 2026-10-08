package aarogya.dashBoard.favorite.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModuleDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String moduleName;
    private List<MenuDetail_Mdl> menuDetailMdl = new ArrayList<MenuDetail_Mdl>();

    public ModuleDetail_Mdl() {
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<MenuDetail_Mdl> getMenuDetailMdl() {
        return menuDetailMdl;
    }

}
