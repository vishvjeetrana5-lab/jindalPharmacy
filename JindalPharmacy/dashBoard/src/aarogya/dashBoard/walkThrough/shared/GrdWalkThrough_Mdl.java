package aarogya.dashBoard.walkThrough.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdWalkThrough_Mdl extends KeyField implements Serializable, IsSerializable {

    private String category;
    private String pending;

    public GrdWalkThrough_Mdl() {
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
