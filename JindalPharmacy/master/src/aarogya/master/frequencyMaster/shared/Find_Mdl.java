package aarogya.master.frequencyMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String frequencyTime;
    private String presentation;
    private String frequencyValue;

    public Find_Mdl() {
    }

    public String getFrequencyTime() {
        return frequencyTime;
    }

    public void setFrequencyTime(String frequencyTime) {
        this.frequencyTime = frequencyTime;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getFrequencyValue() {
        return frequencyValue;
    }

    public void setFrequencyValue(String frequencyValue) {
        this.frequencyValue = frequencyValue;
    }

}
