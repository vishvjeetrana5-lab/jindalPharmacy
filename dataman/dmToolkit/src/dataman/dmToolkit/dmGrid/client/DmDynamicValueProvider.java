package dataman.dmToolkit.dmGrid.client;

import dataman.dmBase.self.shared.DmDynamicGridRow;
import com.sencha.gxt.core.client.ValueProvider;

public class DmDynamicValueProvider implements ValueProvider<DmDynamicGridRow, Object> {

    private String path;

    public DmDynamicValueProvider(String path) {
        this.path = path;
    }

    @Override
    public Object getValue(DmDynamicGridRow object) {
        return object.getCells().get(this.path);
    }

    @Override
    public void setValue(DmDynamicGridRow object, Object value) {
        if (object.getCells().containsKey(this.path)) {
            object.getCells().remove(this.path);
        }
        object.getCells().put(path, value);
    }

    @Override
    public String getPath() {
        return this.path;
    }
}
