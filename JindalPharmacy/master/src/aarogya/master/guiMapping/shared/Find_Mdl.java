package aarogya.master.guiMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String entrypoint;
    private String gui;

    public Find_Mdl() {

    }

    public String getEntrypoint() {
        return entrypoint;
    }

    public void setEntrypoint(String entrypoint) {
        this.entrypoint = entrypoint;
    }

    public String getGui() {
        return gui;
    }

    public void setGui(String gui) {
        this.gui = gui;
    }
}
