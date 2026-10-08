package aarogya.selfToolkit.uhidPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class UhidPopup_Mdl extends KeyField implements Serializable, IsSerializable {

    private String firstName, middleName, lastName, patientName, patientInitial, guardianInitial;
    private String guardianName;
    private String mobileNo;
    private String email;
    private String idType, idTypeCode, idNo;
    private String docIdUHID, recIdUHID;
    private String docIdRegistration, recIdRegistration;

    public UhidPopup_Mdl() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdTypeCode() {
        return idTypeCode;
    }

    public void setIdTypeCode(String idTypeCode) {
        this.idTypeCode = idTypeCode;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getDocIdUHID() {
        return docIdUHID;
    }

    public void setDocIdUHID(String docIdUHID) {
        this.docIdUHID = docIdUHID;
    }

    public String getRecIdUHID() {
        return recIdUHID;
    }

    public void setRecIdUHID(String recIdUHID) {
        this.recIdUHID = recIdUHID;
    }

    public String getDocIdRegistration() {
        return docIdRegistration;
    }

    public void setDocIdRegistration(String docIdRegistration) {
        this.docIdRegistration = docIdRegistration;
    }

    public String getRecIdRegistration() {
        return recIdRegistration;
    }

    public void setRecIdRegistration(String recIdRegistration) {
        this.recIdRegistration = recIdRegistration;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientInitial() {
        return patientInitial;
    }

    public void setPatientInitial(String patientInitial) {
        this.patientInitial = patientInitial;
    }

    public String getGuardianInitial() {
        return guardianInitial;
    }

    public void setGuardianInitial(String guardianInitial) {
        this.guardianInitial = guardianInitial;
    }

}
