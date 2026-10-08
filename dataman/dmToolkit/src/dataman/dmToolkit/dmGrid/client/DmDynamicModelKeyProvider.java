package dataman.dmToolkit.dmGrid.client;

import dataman.dmBase.self.shared.DmDynamicGridRow;
import com.sencha.gxt.data.shared.ModelKeyProvider;

public class DmDynamicModelKeyProvider implements ModelKeyProvider<DmDynamicGridRow> {

    private String keyname;

    public DmDynamicModelKeyProvider(String keyName) {
        this.keyname = keyName;
    }

    @Override
    public String getKey(DmDynamicGridRow item) {
        return String.valueOf(item.getCells().get(this.keyname));
    }
}
