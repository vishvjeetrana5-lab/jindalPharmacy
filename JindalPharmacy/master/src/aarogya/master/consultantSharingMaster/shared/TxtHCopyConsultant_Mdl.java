package aarogya.master.consultantSharingMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHCopyConsultant_Mdl extends KeyField implements Serializable, IsSerializable {

    private String applicableDate;

    public TxtHCopyConsultant_Mdl() {
    }

    public String getApplicableDate() {
        return applicableDate;
    }

    public void setApplicableDate(String applicableDate) {
        this.applicableDate = applicableDate;
    }

}
