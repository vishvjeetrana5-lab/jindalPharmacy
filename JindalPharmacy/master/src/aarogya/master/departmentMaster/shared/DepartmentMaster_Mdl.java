package aarogya.master.departmentMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class DepartmentMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String displayName;
    private String shortName;
    private String natureName;
    private String natureCode;
    private String drugLicenceNo;
    private String address1;
    private String address2;

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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    
    
}
