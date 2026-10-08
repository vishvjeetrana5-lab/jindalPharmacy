package aarogya.master.serviceMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String organizationCode;
    private String siteCode;
    private String diagnosticCatCode;
    private String diagnosticCatName;

    public DefaultValue_Mdl() {
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getDiagnosticCatCode() {
        return diagnosticCatCode;
    }

    public void setDiagnosticCatCode(String diagnosticCatCode) {
        this.diagnosticCatCode = diagnosticCatCode;
    }

    public String getDiagnosticCatName() {
        return diagnosticCatName;
    }

    public void setDiagnosticCatName(String diagnosticCatName) {
        this.diagnosticCatName = diagnosticCatName;
    }

}
