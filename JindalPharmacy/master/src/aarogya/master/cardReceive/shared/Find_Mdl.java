package aarogya.master.cardReceive.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String allotmentDate, person;

    public Find_Mdl() {
    }

    public String getAllotmentDate() {
        return allotmentDate;
    }

    public void setAllotmentDate(String allotmentDate) {
        this.allotmentDate = allotmentDate;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
}
