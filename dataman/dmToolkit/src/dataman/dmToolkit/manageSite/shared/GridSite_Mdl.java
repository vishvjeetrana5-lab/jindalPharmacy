package dataman.dmToolkit.manageSite.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GridSite_Mdl extends KeyField implements Serializable, IsSerializable {

    private String delete;

    public GridSite_Mdl() {
    }

    
    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

}
