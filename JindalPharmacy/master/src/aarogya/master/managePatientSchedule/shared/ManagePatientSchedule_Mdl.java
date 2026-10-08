package aarogya.master.managePatientSchedule.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManagePatientSchedule_Mdl extends KeyField implements Serializable, IsSerializable {

    private PatientDetail_Mdl patientDetailMdl = new PatientDetail_Mdl();
    private List<GrdSchedules_Mdl> grdPatnSchdlMdl = new ArrayList();

    private String uhid, uhidRec, scheduleNo, entryDate, active, scheduleHlp, shdlcount;

    public ManagePatientSchedule_Mdl() {
    }

    public PatientDetail_Mdl getPatientDetailMdl() {
        return patientDetailMdl;
    }

    public void setPatientDetailMdl(PatientDetail_Mdl patientDetailMdl) {
        this.patientDetailMdl = patientDetailMdl;
    }

    public String getScheduleHlp() {
        return scheduleHlp;
    }

    public void setScheduleHlp(String scheduleHlp) {
        this.scheduleHlp = scheduleHlp;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getUhid() {
        return uhid;
    }

    public void setUhid(String uhid) {
        this.uhid = uhid;
    }

    public String getUhidRec() {
        return uhidRec;
    }

    public void setUhidRec(String uhidRec) {
        this.uhidRec = uhidRec;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public List<GrdSchedules_Mdl> getGrdPatnSchdlMdl() {
        return grdPatnSchdlMdl;
    }

    public void setGrdPatnSchdlMdl(List<GrdSchedules_Mdl> grdPatnSchdlMdl) {
        this.grdPatnSchdlMdl = grdPatnSchdlMdl;
    }

    public String getShdlcount() {
        return shdlcount;
    }

    public void setShdlcount(String shdlcount) {
        this.shdlcount = shdlcount;
    }

}
