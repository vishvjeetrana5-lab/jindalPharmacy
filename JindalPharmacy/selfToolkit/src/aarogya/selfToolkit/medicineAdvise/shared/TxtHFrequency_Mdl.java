package aarogya.selfToolkit.medicineAdvise.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHFrequency_Mdl extends KeyField implements Serializable, IsSerializable {

    private String time;
    private String presentation;

    public TxtHFrequency_Mdl() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

}
