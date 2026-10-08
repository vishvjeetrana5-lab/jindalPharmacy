package aarogya.master.consultantMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.Objects;

public class GrdQueueSequence_Mdl extends KeyField implements Serializable, IsSerializable {

    private String prioritySerial;
    private String totalPatient;

    public GrdQueueSequence_Mdl() {
    }

    public String getPrioritySerial() {
        return prioritySerial;
    }

    public void setPrioritySerial(String prioritySerial) {
        this.prioritySerial = prioritySerial;
    }

    public String getTotalPatient() {
        return totalPatient;
    }

    public void setTotalPatient(String totalPatient) {
        this.totalPatient = totalPatient;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.getSharedKeyFieldCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrdQueueSequence_Mdl other = (GrdQueueSequence_Mdl) obj;
        if (!Objects.equals(this.getSharedKeyFieldCode(), other.getSharedKeyFieldCode())) {
            return false;
        }
        return true;
    }

    public GrdQueueSequence_Mdl clone() {
        GrdQueueSequence_Mdl local = new GrdQueueSequence_Mdl();
        super.copy(local);
        local.setPrioritySerial(this.getPrioritySerial());
        local.setTotalPatient(this.getTotalPatient());
        return local;
    }
}
