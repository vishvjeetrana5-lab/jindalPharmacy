package aarogya.selfToolkit.opdQueueDisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdOPDQueue_Mdl extends KeyField implements Serializable, IsSerializable {

    private String tokenAssigned, tokenRunning;
    private boolean current = false;

    public GrdOPDQueue_Mdl() {
    }

    public String getTokenRunning() {
        return tokenRunning;
    }

    public void setTokenRunning(String tokenRunning) {
        this.tokenRunning = tokenRunning;
    }

    public String getTokenAssigned() {
        return tokenAssigned;
    }

    public void setTokenAssigned(String tokenAssigned) {
        this.tokenAssigned = tokenAssigned;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
