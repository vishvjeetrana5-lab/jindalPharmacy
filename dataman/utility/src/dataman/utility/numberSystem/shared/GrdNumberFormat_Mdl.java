package dataman.utility.numberSystem.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdNumberFormat_Mdl extends KeyField implements Serializable, IsSerializable {

    private String numberFormat;
    private String counterFormat;
    private String siteCode;
    private String siteName;
    private String isSiteWiseCode;
    private String isSiteWiseName;
    private String isYearWiseCode;
    private String isYearWiseName;

    public GrdNumberFormat_Mdl() {
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    public String getCounterFormat() {
        return counterFormat;
    }

    public void setCounterFormat(String counterFormat) {
        this.counterFormat = counterFormat;
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

    public String getIsSiteWiseCode() {
        return isSiteWiseCode;
    }

    public void setIsSiteWiseCode(String isSiteWiseCode) {
        this.isSiteWiseCode = isSiteWiseCode;
    }

    public String getIsSiteWiseName() {
        return isSiteWiseName;
    }

    public void setIsSiteWiseName(String isSiteWiseName) {
        this.isSiteWiseName = isSiteWiseName;
    }

    public String getIsYearWiseCode() {
        return isYearWiseCode;
    }

    public void setIsYearWiseCode(String isYearWiseCode) {
        this.isYearWiseCode = isYearWiseCode;
    }

    public String getIsYearWiseName() {
        return isYearWiseName;
    }

    public void setIsYearWiseName(String isYearWiseName) {
        this.isYearWiseName = isYearWiseName;
    }
}
