package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class MenuLink implements Serializable, IsSerializable {

    public final String moduleName;
    public final String menuName;
    public final String caption;
        
    public MenuLink(String moduleName, String caption) {
        this.moduleName = moduleName;
        this.menuName = caption;
        this.caption = caption;
    }

    public MenuLink(String moduleName, String caption, String menuName) {
        this.moduleName = moduleName;
        this.menuName = menuName ;
        this.caption = caption;
    }
}
