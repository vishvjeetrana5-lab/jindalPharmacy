package aarogya.master.patientScheduleMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PatientScheduleMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String scheduleGroup;

    private String scheduleGroupCode;
    private String shortName;
    private List<GrdPatntSchdulMster_Mdl> grdPatientSchdulMsterList = new ArrayList<GrdPatntSchdulMster_Mdl>();

    public PatientScheduleMaster_Mdl() {
    }

    public String getScheduleGroup() {
        return scheduleGroup;
    }

    public void setScheduleGroup(String scheduleGroup) {
        this.scheduleGroup = scheduleGroup;
    }

    public String getScheduleGroupCode() {
        return scheduleGroupCode;
    }

    public void setScheduleGroupCode(String scheduleGroupCode) {
        this.scheduleGroupCode = scheduleGroupCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<GrdPatntSchdulMster_Mdl> getGrdPatientSchdulMsterList() {
        return grdPatientSchdulMsterList;
    }

}
