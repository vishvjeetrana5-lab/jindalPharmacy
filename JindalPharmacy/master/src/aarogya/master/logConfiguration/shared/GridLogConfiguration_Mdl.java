package aarogya.master.logConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GridLogConfiguration_Mdl extends KeyField implements Serializable, IsSerializable {

    private String isChecked;
    private String module;

    public GridLogConfiguration_Mdl() {
    }
    
     public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

}
