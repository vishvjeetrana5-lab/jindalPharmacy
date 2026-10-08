package aarogya.master.environmentSettingCompany.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdPatientClass_Mdl extends KeyField implements Serializable, IsSerializable {

    private String patientClassCode;
    private String patientClassName;
    private String rateListCode;
    private String rateListName;

    public GrdPatientClass_Mdl() {
    }

    public String getPatientClassCode() {
        return patientClassCode;
    }

    public void setPatientClassCode(String patientClassCode) {
        this.patientClassCode = patientClassCode;
    }

    public String getPatientClassName() {
        return patientClassName;
    }

    public void setPatientClassName(String patientClassName) {
        this.patientClassName = patientClassName;
    }

    public String getRateListCode() {
        return rateListCode;
    }

    public void setRateListCode(String rateListCode) {
        this.rateListCode = rateListCode;
    }

    public String getRateListName() {
        return rateListName;
    }

    public void setRateListName(String rateListName) {
        this.rateListName = rateListName;
    }

}
