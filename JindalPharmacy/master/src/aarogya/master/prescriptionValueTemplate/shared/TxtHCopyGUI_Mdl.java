package aarogya.master.prescriptionValueTemplate.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHCopyGUI_Mdl extends KeyField implements Serializable, IsSerializable {

    private String consultantCode;
    private String consultantName;
    private String guiConfigCode;
    private String guiConfigName;

    public TxtHCopyGUI_Mdl() {
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }

    public String getGuiConfigName() {
        return guiConfigName;
    }

    public void setGuiConfigName(String guiConfigName) {
        this.guiConfigName = guiConfigName;
    }

}
