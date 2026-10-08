package aarogya.master.organizationServiceMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.Objects;

public class GrdService_Mdl extends KeyField implements Serializable, IsSerializable {

    private String orgGroupName;
    private String orgServiceName;
    private String orgServiceCode;
    private String rate;
    private String mappedServiceCode;
    private String mappedServiceName;
    private String printSNo;

    public GrdService_Mdl() {
    }

    public String getOrgGroupName() {
        return orgGroupName;
    }

    public void setOrgGroupName(String orgGroupName) {
        this.orgGroupName = orgGroupName;
    }

    public String getOrgServiceName() {
        return orgServiceName;
    }

    public void setOrgServiceName(String orgServiceName) {
        this.orgServiceName = orgServiceName;
    }

    public String getOrgServiceCode() {
        return orgServiceCode;
    }

    public void setOrgServiceCode(String orgServiceCode) {
        this.orgServiceCode = orgServiceCode;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getMappedServiceCode() {
        return mappedServiceCode;
    }

    public void setMappedServiceCode(String mappedServiceCode) {
        this.mappedServiceCode = mappedServiceCode;
    }

    public String getMappedServiceName() {
        return mappedServiceName;
    }

    public void setMappedServiceName(String mappedServiceName) {
        this.mappedServiceName = mappedServiceName;
    }

    public String getPrintSNo() {
        return printSNo;
    }

    public void setPrintSNo(String printSNo) {
        this.printSNo = printSNo;
    }
}
