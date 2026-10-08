package aarogya.master.consultantMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsultantMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    //Consultant Name & Code is main
    private String tempKeyCode;
    private List<GrdClinicDetail_Mdl> grdClinic = new ArrayList<GrdClinicDetail_Mdl>();
    private List<GrdService_Mdl> grdService = new ArrayList<GrdService_Mdl>();
    private List<GrdTiming_Mdl> grdTime = new ArrayList<GrdTiming_Mdl>();
    private List<GrdBankDetail_Mdl> grdBankDetail = new ArrayList<GrdBankDetail_Mdl>();
    private List<GrdQueueSequence_Mdl> lstQueue = new ArrayList<GrdQueueSequence_Mdl>();
    private String activeTag;
//    private String isOPDConsultant;
//    private String isOPDConsultantName;
    private String activeText;
    private String queueTag, queueText;
    private String departmentTag, departmentText;
    private String specilizationTag, specilizationText;
    private String eMail;
    private String shortName;
    private String remark;
    private String doa;
    private String dob;
    private String mciReg;
    private String mobile;
    private String mobileHome;
    private String validUpTo;
    private String applicableFrom;
    private String organizaton;
    private String service;
    private String initial;
    private String displayName;
    private String locationTag;
    private String locationText;
    private String consultantNatureTag;
    private String consultantNatureText;
    private String deviceCode, deviceBlock;
    private String tdsCategory;
    private String tdsCategoryName;
    private String noOfVisit;
    private String Pan;
    private String regionalDescription;
    private List<GrdSiteDetail_Mdl> grdSitedetail = new ArrayList<GrdSiteDetail_Mdl>();

    private String linkDepartmentName;
    private String linkDepartmentCode;
    private String rateListCode;

    public String getPan() {
        return Pan;
    }

    public String getRateListCode() {
        return rateListCode;
    }

    public void setRateListCode(String rateListCode) {
        this.rateListCode = rateListCode;
    }

    public void setPan(String Pan) {
        this.Pan = Pan;
    }

    public ConsultantMaster_Mdl() {
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

    public List<GrdService_Mdl> getGrdService() {
        return grdService;
    }

    public List<GrdBankDetail_Mdl> getGrdBankDetail() {
        return grdBankDetail;
    }

    public List<GrdQueueSequence_Mdl> getLstQueue() {
        return lstQueue;
    }

    public List<GrdTiming_Mdl> getGrdTime() {
        return grdTime;
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

    public String getDepartmentTag() {
        return departmentTag;
    }

    public void setDepartmentTag(String departmentTag) {
        this.departmentTag = departmentTag;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    public String getSpecilizationTag() {
        return specilizationTag;
    }

    public void setSpecilizationTag(String specilizationTag) {
        this.specilizationTag = specilizationTag;
    }

    public String getSpecilizationText() {
        return specilizationText;
    }

    public void setSpecilizationText(String specilizationText) {
        this.specilizationText = specilizationText;
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

    public String getValidUpTo() {
        return validUpTo;
    }

    public void setValidUpTo(String validUpTo) {
        this.validUpTo = validUpTo;
    }

    public String getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(String applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public String getOrganizaton() {
        return organizaton;
    }

    public void setOrganizaton(String organizaton) {
        this.organizaton = organizaton;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
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

    public String getLocationTag() {
        return locationTag;
    }

    public void setLocationTag(String locationTag) {
        this.locationTag = locationTag;
    }

    public String getLocationText() {
        return locationText;
    }

    public void setLocationText(String locationText) {
        this.locationText = locationText;
    }

    public String getConsultantNatureTag() {
        return consultantNatureTag;
    }

    public void setConsultantNatureTag(String consultantNatureTag) {
        this.consultantNatureTag = consultantNatureTag;
    }

    public String getConsultantNatureText() {
        return consultantNatureText;
    }

    public void setConsultantNatureText(String consultantNatureText) {
        this.consultantNatureText = consultantNatureText;
    }

    public String getQueueTag() {
        return queueTag;
    }

    public void setQueueTag(String queueTag) {
        this.queueTag = queueTag;
    }

    public String getQueueText() {
        return queueText;
    }

    public void setQueueText(String queueText) {
        this.queueText = queueText;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceBlock() {
        return deviceBlock;
    }

    public void setDeviceBlock(String deviceBlock) {
        this.deviceBlock = deviceBlock;
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

    public String getNoOfVisit() {
        return noOfVisit;
    }

    public void setNoOfVisit(String noOfVisit) {
        this.noOfVisit = noOfVisit;
    }

    public String getRegionalDescription() {
        return regionalDescription;
    }

    public void setRegionalDescription(String regionalDescription) {
        this.regionalDescription = regionalDescription;
    }

    public List<GrdSiteDetail_Mdl> getGrdSitedetail() {
        return grdSitedetail;
    }

    public void setGrdSitedetail(List<GrdSiteDetail_Mdl> grdSitedetail) {
        this.grdSitedetail = grdSitedetail;
    }

    public String getLinkDepartmentName() {
        return linkDepartmentName;
    }

    public void setLinkDepartmentName(String linkDepartmentName) {
        this.linkDepartmentName = linkDepartmentName;
    }

    public String getLinkDepartmentCode() {
        return linkDepartmentCode;
    }

    public void setLinkDepartmentCode(String linkDepartmentCode) {
        this.linkDepartmentCode = linkDepartmentCode;
    }

}
