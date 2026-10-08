package aarogya.master.generateGstCsv.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Filter_Mdl implements Serializable, IsSerializable {

    private String sessionId;
    private String fromDate;
    private String toDate;
    private String type;

    public Filter_Mdl() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
