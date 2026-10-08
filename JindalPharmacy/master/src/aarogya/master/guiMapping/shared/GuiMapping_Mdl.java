package aarogya.master.guiMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GuiMapping_Mdl extends KeyField implements Serializable, IsSerializable {

    private String entrypoint;
    private String guiTempalte;
    private String guiTempalteCode;

    public GuiMapping_Mdl() {
    }

    public String getEntrypoint() {
        return entrypoint;
    }

    public void setEntrypoint(String entrypoint) {
        this.entrypoint = entrypoint;
    }

    public String getGuiTempalte() {
        return guiTempalte;
    }

    public void setGuiTempalte(String guiTempalte) {
        this.guiTempalte = guiTempalte;
    }

    public String getGuiTempalteCode() {
        return guiTempalteCode;
    }

    public void setGuiTempalteCode(String guiTempalteCode) {
        this.guiTempalteCode = guiTempalteCode;
    }

}
