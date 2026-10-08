package aarogya.master.frequencyMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class FrequencyMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String regionalLanguageName;
    private String frequencyTime;
    private String presentation;
    private String frequencyValue;

    public FrequencyMaster_Mdl() {
    }

    public String getRegionalLanguageName() {
        return regionalLanguageName;
    }

    public void setRegionalLanguageName(String regionalLanguageName) {
        this.regionalLanguageName = regionalLanguageName;
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
