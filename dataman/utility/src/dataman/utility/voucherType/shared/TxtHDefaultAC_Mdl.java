package dataman.utility.voucherType.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHDefaultAC_Mdl extends KeyField implements Serializable, IsSerializable {

    private String menuModule;

    public TxtHDefaultAC_Mdl() {
    }

    public String getMenuModule() {
        return menuModule;
    }

    public void setMenuModule(String menuModule) {
        this.menuModule = menuModule;
    }
}
