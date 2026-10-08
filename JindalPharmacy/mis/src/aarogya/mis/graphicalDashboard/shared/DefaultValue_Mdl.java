package aarogya.mis.graphicalDashboard.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String siteCode, siteName;
    private String currentDate, monthLastDate;

    public DefaultValue_Mdl() {
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getMonthLastDate() {
        return monthLastDate;
    }

    public void setMonthLastDate(String monthLastDate) {
        this.monthLastDate = monthLastDate;
    }
}
