package aarogya.master.referredMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReferredMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    //Consultant Name & Code is main
    private String tempKeyCode;
    private List<GrdClinicDetail_Mdl> grdClinic = new ArrayList<GrdClinicDetail_Mdl>();

    private String activeTag;
    private String activeText;
    private String natureCode;
    private String natureName;
    private String deptTag;
    private String deptText;
    private String speciTag;
    private String speciText;
    private String eMail;
    private String shortName;
    private String remark;
    private String doa;
    private String dob;
    private String mciReg;
    private String mobile;
    private String mobileHome;
    private String initialTag;
    private String initial;
    private String displayName;
    private String tdsCategory;
    private String tdsCategoryName;

    public ReferredMaster_Mdl() {
    }

    public String getTempKeyCode() {
        return tempKeyCode;
    }

    public void setTempKeyCode(String tempKeyCode) {
        this.tempKeyCode = tempKeyCode;
    }

    public List<GrdClinicDetail_Mdl> getGrdClinic() {
        return grdClinic;
    }

    public String getActiveTag() {
        return activeTag;
    }

    public void setActiveTag(String activeTag) {
        this.activeTag = activeTag;
    }

    public String getActiveText() {
        return activeText;
    }

    public void setActiveText(String activeText) {
        this.activeText = activeText;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public String getDeptTag() {
        return deptTag;
    }

    public void setDeptTag(String deptTag) {
        this.deptTag = deptTag;
    }

    public String getDeptText() {
        return deptText;
    }

    public void setDeptText(String deptText) {
        this.deptText = deptText;
    }

    public String getSpeciTag() {
        return speciTag;
    }

    public void setSpeciTag(String speciTag) {
        this.speciTag = speciTag;
    }

    public String getSpeciText() {
        return speciText;
    }

    public void setSpeciText(String speciText) {
        this.speciText = speciText;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMciReg() {
        return mciReg;
    }

    public void setMciReg(String mciReg) {
        this.mciReg = mciReg;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getInitialTag() {
        return initialTag;
    }

    public void setInitialTag(String initialTag) {
        this.initialTag = initialTag;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getMobileHome() {
        return mobileHome;
    }

    public void setMobileHome(String mobileHome) {
        this.mobileHome = mobileHome;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTdsCategory() {
        return tdsCategory;
    }

    public void setTdsCategory(String tdsCategory) {
        this.tdsCategory = tdsCategory;
    }

    public String getTdsCategoryName() {
        return tdsCategoryName;
    }

    public void setTdsCategoryName(String tdsCategoryName) {
        this.tdsCategoryName = tdsCategoryName;
    }

}
