package aarogya.pharmacy.requisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHPriority_Mdl extends KeyField implements Serializable, IsSerializable {

    private String priorityFullName;
    private String priorityIndex;

    public TxtHPriority_Mdl() {
    }

    public String getPriorityFullName() {
        return priorityFullName;
    }

    public void setPriorityFullName(String PriorityFullName) {
        this.priorityFullName = PriorityFullName;
    }

    public String getPriorityIndex() {
        return priorityIndex;
    }

    public void setPriorityIndex(String priorityIndex) {
        this.priorityIndex = priorityIndex;
    }

}
