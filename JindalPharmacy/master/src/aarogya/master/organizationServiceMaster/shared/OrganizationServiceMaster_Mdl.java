package aarogya.master.organizationServiceMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class OrganizationServiceMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String organizationCode;
    private String organizationName;
    private String serviceGroup;
    private String orgServiceCode;
    private String printSno;

    public OrganizationServiceMaster_Mdl() {
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public String getOrgServiceCode() {
        return orgServiceCode;
    }

    public void setOrgServiceCode(String orgServiceCode) {
        this.orgServiceCode = orgServiceCode;
    }

    public String getPrintSno() {
        return printSno;
    }

    public void setPrintSno(String printSno) {
        this.printSno = printSno;
    }

}
