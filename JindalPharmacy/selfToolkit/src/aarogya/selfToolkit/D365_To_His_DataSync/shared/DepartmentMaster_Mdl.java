package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class DepartmentMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String code;
    private String displayName;
    private String shortName;
    private String natureName;
    private String natureCode;
    private String drugLicenceNo;
    private String filterNature;
    private String siteCode;
    private String siteName;

    public DepartmentMaster_Mdl() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDrugLicenceNo() {
        return drugLicenceNo;
    }

    public void setDrugLicenceNo(String drugLicenceNo) {
        this.drugLicenceNo = drugLicenceNo;
    }

    public String getFilterNature() {
        return filterNature;
    }

    public void setFilterNature(String filterNature) {
        this.filterNature = filterNature;
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

}
