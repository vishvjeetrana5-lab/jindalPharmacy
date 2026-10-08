package aarogya.master.organizationMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrganizationMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String remark;
    private String gstinNo;
    private String isNatureTag;
    private String isNatureText;
    private boolean TPA = false;
    private String agreementNo;
    private String agreementDate;
    private String activeText;
    private String activeTag;
    private String tdsAcText;
    private String tdsAcTag;

    private String tpaControlAcText;
    private String tpaControlAcTag;

    private String excessecurityAcText;
    private String excessecurityAcTag;
    private String discountReason;
    private String discountGivenByText;
    private String discountGivenByTag;

    private String revisionDate;
    private String tarrifRevisionDate;
    private String biilingPer;
    private String mobileNo;
    private String email;
    private String organizationGroupCode;
    private String organizationGroup;
    private String rateListCode;
    private String rateListName;
    private String rateList_Sno;
    private String applicableDate, applicableTo, siteCode;
    private DiscountPercent_Mdl discountMdl;

    private List<GrdContactPerson_Mdl> listContectPerson = new ArrayList<GrdContactPerson_Mdl>();

    private List<GrdRateList_Mdl> lstRateList = new ArrayList<>();
    private List<GrdServices_Mdl> lstService = new ArrayList();
    private GrdDiscountPercent_Mdl discountPercentMdl = new GrdDiscountPercent_Mdl();
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private String guiConfigCode;

    public OrganizationMaster_Mdl() {
    }

    public String getOrganizationGroupCode() {
        return organizationGroupCode;
    }

    public void setOrganizationGroupCode(String organizationGroupCode) {
        this.organizationGroupCode = organizationGroupCode;
    }

    public String getOrganizationGroup() {
        return organizationGroup;
    }

    public void setOrganizationGroup(String organizationGroup) {
        this.organizationGroup = organizationGroup;
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

    public String getRateList_Sno() {
        return rateList_Sno;
    }

    public void setRateList_Sno(String rateList_Sno) {
        this.rateList_Sno = rateList_Sno;
    }

    public String getApplicableDate() {
        return applicableDate;
    }

    public void setApplicableDate(String applicableDate) {
        this.applicableDate = applicableDate;
    }

    public String getApplicableTo() {
        return applicableTo;
    }

    public void setApplicableTo(String applicableTo) {
        this.applicableTo = applicableTo;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public DiscountPercent_Mdl getDiscountMdl() {
        return discountMdl;
    }

    public void setDiscountMdl(DiscountPercent_Mdl discountMdl) {
        this.discountMdl = discountMdl;
    }

    public List<GrdRateList_Mdl> getLstRateList() {
        return lstRateList;
    }

    public void setLstRateList(List<GrdRateList_Mdl> lstRateList) {
        this.lstRateList = lstRateList;
    }

    public List<GrdServices_Mdl> getLstService() {
        return lstService;
    }

    public void setLstService(List<GrdServices_Mdl> lstService) {
        this.lstService = lstService;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }

    public DmGUI_Mdl getGuiMdl() {
        return guiMdl;
    }

    public void setGuiMdl(DmGUI_Mdl guiMdl) {
        this.guiMdl = guiMdl;
    }

    public String getBiilingPer() {
        return biilingPer;
    }

    public void setBiilingPer(String biilingPer) {
        this.biilingPer = biilingPer;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getGstinNo() {
        return gstinNo;
    }

    public void setGstinNo(String gstinNo) {
        this.gstinNo = gstinNo;
    }

    public String getIsNatureTag() {
        return isNatureTag;
    }

    public void setIsNatureTag(String isNatureTag) {
        this.isNatureTag = isNatureTag;
    }

    public String getIsNatureText() {
        return isNatureText;
    }

    public void setIsNatureText(String isNatureText) {
        this.isNatureText = isNatureText;
    }

    public boolean isTPA() {
        return TPA;
    }

    public void setTPA(boolean TPA) {
        this.TPA = TPA;
    }

    public String getAgreementNo() {
        return agreementNo;
    }

    public void setAgreementNo(String agreementNo) {
        this.agreementNo = agreementNo;
    }

    public String getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(String agreementDate) {
        this.agreementDate = agreementDate;
    }

    public String getActiveText() {
        return activeText;
    }

    public void setActiveText(String activeText) {
        this.activeText = activeText;
    }

    public String getActiveTag() {
        return activeTag;
    }

    public void setActiveTag(String activeTag) {
        this.activeTag = activeTag;
    }

    public String getTdsAcText() {
        return tdsAcText;
    }

    public void setTdsAcText(String tdsAcText) {
        this.tdsAcText = tdsAcText;
    }

    public String getTdsAcTag() {
        return tdsAcTag;
    }

    public void setTdsAcTag(String tdsAcTag) {
        this.tdsAcTag = tdsAcTag;
    }

    public String getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(String revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getTarrifRevisionDate() {
        return tarrifRevisionDate;
    }

    public void setTarrifRevisionDate(String tarrifRevisionDate) {
        this.tarrifRevisionDate = tarrifRevisionDate;
    }

    public List<GrdContactPerson_Mdl> getListContectPerson() {
        return listContectPerson;
    }

    public void setListContectPerson(List<GrdContactPerson_Mdl> listContectPerson) {
        this.listContectPerson = listContectPerson;
    }

    public GrdDiscountPercent_Mdl getDiscountPercentMdl() {
        return discountPercentMdl;
    }

    public void setDiscountPercentMdl(GrdDiscountPercent_Mdl discountPercentMdl) {
        this.discountPercentMdl = discountPercentMdl;
    }

    public String getTpaControlAcText() {
        return tpaControlAcText;
    }

    public void setTpaControlAcText(String tpaControlAcText) {
        this.tpaControlAcText = tpaControlAcText;
    }

    public String getTpaControlAcTag() {
        return tpaControlAcTag;
    }

    public void setTpaControlAcTag(String tpaControlAcTag) {
        this.tpaControlAcTag = tpaControlAcTag;
    }

    public String getExcessecurityAcText() {
        return excessecurityAcText;
    }

    public void setExcessecurityAcText(String excessecurityAcText) {
        this.excessecurityAcText = excessecurityAcText;
    }

    public String getExcessecurityAcTag() {
        return excessecurityAcTag;
    }

    public void setExcessecurityAcTag(String excessecurityAcTag) {
        this.excessecurityAcTag = excessecurityAcTag;
    }

    public String getDiscountReason() {
        return discountReason;
    }

    public void setDiscountReason(String discountReason) {
        this.discountReason = discountReason;
    }

    public String getDiscountGivenByText() {
        return discountGivenByText;
    }

    public void setDiscountGivenByText(String discountGivenByText) {
        this.discountGivenByText = discountGivenByText;
    }

    public String getDiscountGivenByTag() {
        return discountGivenByTag;
    }

    public void setDiscountGivenByTag(String discountGivenByTag) {
        this.discountGivenByTag = discountGivenByTag;
    }

}
