package aarogya.master.consultantMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String department;
    private String specialization;
    private String shortName;
    private String mobile;
    private String email;
    private String annivDate;
    private String dob;
    private String opdValidUpto;
    private String mciRegNo;
    private String chamberNo;
    private String isActive;
    private String remark;

    public Find_Mdl() {
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String Department) {
        this.department = Department;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String Specialization) {
        this.specialization = Specialization;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String Mobile) {
        this.mobile = Mobile;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String ShortName) {
        this.shortName = ShortName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAnnivDate() {
        return annivDate;
    }

    public void setAnnivDate(String annivDate) {
        this.annivDate = annivDate;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getOpdValidUpto() {
        return opdValidUpto;
    }

    public void setOpdValidUpto(String opdValidUpto) {
        this.opdValidUpto = opdValidUpto;
    }

    public String getMciRegNo() {
        return mciRegNo;
    }

    public void setMciRegNo(String mciRegNo) {
        this.mciRegNo = mciRegNo;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getChamberNo() {
        return chamberNo;
    }

    public void setChamberNo(String chamberNo) {
        this.chamberNo = chamberNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
