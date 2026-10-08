package aarogya.selfToolkit.permanentAddressPopup.shared;

import dataman.dmBase.self.shared.KeyField;

public class PermanentAddressPopup_Mdl extends KeyField {

    private String permanentAddress1, permanentAddress2, patientUHIDDocid,
            permanentZoneCode, permanentPinCode, permanentCityCode,
            permanentZone, permanentCity;

    public PermanentAddressPopup_Mdl() {
    }

    public String getPatientUHIDDocid() {
        return patientUHIDDocid;
    }

    public void setPatientUHIDDocid(String patientUHIDDocid) {
        this.patientUHIDDocid = patientUHIDDocid;
    }

    public String getPermanentZone() {
        return permanentZone;
    }

    public void setPermanentZone(String permanentZone) {
        this.permanentZone = permanentZone;
    }

    public String getPermanentCity() {
        return permanentCity;
    }

    public void setPermanentCity(String permanentCity) {
        this.permanentCity = permanentCity;
    }

    public String getPermanentAddress1() {
        return permanentAddress1;
    }

    public void setPermanentAddress1(String permanentAddress1) {
        this.permanentAddress1 = permanentAddress1;
    }

    public String getPermanentAddress2() {
        return permanentAddress2;
    }

    public void setPermanentAddress2(String permanentAddress2) {
        this.permanentAddress2 = permanentAddress2;
    }

    public String getPermanentZoneCode() {
        return permanentZoneCode;
    }

    public void setPermanentZoneCode(String permanentZoneCode) {
        this.permanentZoneCode = permanentZoneCode;
    }

    public String getPermanentPinCode() {
        return permanentPinCode;
    }

    public void setPermanentPinCode(String permanentPinCode) {
        this.permanentPinCode = permanentPinCode;
    }

    public String getPermanentCityCode() {
        return permanentCityCode;
    }

    public void setPermanentCityCode(String permanentCityCode) {
        this.permanentCityCode = permanentCityCode;
    }

}
