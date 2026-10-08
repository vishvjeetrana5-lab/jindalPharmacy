package aarogya.nabh.patientInfectionEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdPatientInfectionEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private String patientInfectionReaction;
    private String service;
    private String date;
    private String Time;

    public String getDate() {
        return date;
    }

    public GrdPatientInfectionEntry_Mdl() {
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getPatientInfectionReaction() {
        return patientInfectionReaction;
    }

    public void setPatientInfectionReaction(String patientInfectionReaction) {
        this.patientInfectionReaction = patientInfectionReaction;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
