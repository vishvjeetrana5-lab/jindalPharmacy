package aarogya.localSetting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHMachineSpecification_Mdl extends KeyField implements Serializable, IsSerializable {

    private String communicationMode;
    private String transmissionMode;
    private String protocoll;
    private String companyName;

    public TxtHMachineSpecification_Mdl() {
    }

    public String getTransmissionMode() {
        return transmissionMode;
    }

    public void setTransmissionMode(String transmissionMode) {
        this.transmissionMode = transmissionMode;
    }

    public String getProtocoll() {
        return protocoll;
    }

    public void setProtocoll(String protocoll) {
        this.protocoll = protocoll;
    }

    public String getCommunicationMode() {
        return communicationMode;
    }

    public void setCommunicationMode(String communicationMode) {
        this.communicationMode = communicationMode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
