package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.LinkedHashMap;

public class DmDynamicGridRow implements Serializable, IsSerializable {

    private LinkedHashMap<String, Object> cells = new LinkedHashMap<>();

    public DmDynamicGridRow() {
    }

    public LinkedHashMap<String, Object> getCells() {
        return this.cells;
    }
}
