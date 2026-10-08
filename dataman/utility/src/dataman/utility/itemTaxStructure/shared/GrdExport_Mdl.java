package dataman.utility.itemTaxStructure.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdExport_Mdl extends KeyField implements Serializable, IsSerializable {

    private boolean checked = false;

    public GrdExport_Mdl() {
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
