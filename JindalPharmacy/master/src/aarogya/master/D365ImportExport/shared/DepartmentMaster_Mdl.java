package aarogya.master.D365ImportExport.shared;

public class DepartmentMaster_Mdl {

    private String displayName;
    private String shortName;
    private String natureName;
    private String natureCode;
    private String drugLicenceNo;

    public DepartmentMaster_Mdl() {
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
}
