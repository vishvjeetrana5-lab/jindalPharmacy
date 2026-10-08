package aarogya.master.logConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogConfiguration_Mdl extends KeyField implements Serializable, IsSerializable {

    private String menuName;
    private String email;
    private String moduleName;
    
    

    private List<GridLogConfiguration_Mdl> grdLogDetailMdl = new ArrayList<GridLogConfiguration_Mdl>();

    public LogConfiguration_Mdl() {
    }
    
       

    public List<GridLogConfiguration_Mdl> getGrdLogDetailMdl() {
        return grdLogDetailMdl;
    }

    public void setGrdLogDetailMdl(List<GridLogConfiguration_Mdl> grdLogDetailMdl) {
        this.grdLogDetailMdl = grdLogDetailMdl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

}
