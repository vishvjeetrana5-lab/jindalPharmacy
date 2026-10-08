package aarogya.master.managePatientSchedule.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ShuffleDays_Mdl implements Serializable, IsSerializable {

    private String docId, schduleDate;
    private int shuffleDays;

    public ShuffleDays_Mdl() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getSchduleDate() {
        return schduleDate;
    }

    public void setSchduleDate(String schduleDate) {
        this.schduleDate = schduleDate;
    }

    public int getShuffleDays() {
        return shuffleDays;
    }

    public void setShuffleDays(int shuffleDays) {
        this.shuffleDays = shuffleDays;
    }
}
