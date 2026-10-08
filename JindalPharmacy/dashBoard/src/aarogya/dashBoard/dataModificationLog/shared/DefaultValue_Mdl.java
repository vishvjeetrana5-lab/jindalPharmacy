package aarogya.dashBoard.dataModificationLog.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String currentSiteCode;
    private String currentSiteName;
    private String fromDate;
    private String toDate;

    public DefaultValue_Mdl() {

    }

    public String getCurrentSiteCode() {
        return currentSiteCode;
    }

    public void setCurrentSiteCode(String currentSiteCode) {
        this.currentSiteCode = currentSiteCode;
    }

    public String getCurrentSiteName() {
        return currentSiteName;
    }

    public void setCurrentSiteName(String currentSiteName) {
        this.currentSiteName = currentSiteName;
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

}
