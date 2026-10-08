package aarogya.selfToolkit.patientSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdPatientDetails_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherDate, patientName, guardianName, mobile, idNo;
    private boolean patientAdmitted;

    public GrdPatientDetails_Mdl() {
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public boolean isPatientAdmitted() {
        return patientAdmitted;
    }

    public void setPatientAdmitted(boolean patientAdmitted) {
        this.patientAdmitted = patientAdmitted;
    }
}
