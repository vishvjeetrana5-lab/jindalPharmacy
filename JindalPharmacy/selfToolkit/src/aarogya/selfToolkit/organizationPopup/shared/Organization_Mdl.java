package aarogya.selfToolkit.organizationPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Organization_Mdl extends KeyField implements Serializable, IsSerializable {

    private String orgShortName, insuranceCompanyCode, insuranceCompanyName;
    private String policyNo, claimId, claimDate, claimRemark;
    private String holderName, relationshipCode, relationshipName;
    private String corporateEmployeeCode, corporateEmployeeName, rankNo;
    private String feesDiscountPer, pharmacyDiscountPer;
    private String referralNo, referralValidUpTo;
    private String billingPer, rateListCode;
    private boolean tpa, pharmacyDiscount;

    public Organization_Mdl() {
    }

    public String getRateListCode() {
        return rateListCode;
    }

    public void setRateListCode(String rateListCode) {
        this.rateListCode = rateListCode;
    }

    public boolean isPharmacyDiscount() {
        return pharmacyDiscount;
    }

    public void setPharmacyDiscount(boolean pharmacyDiscount) {
        this.pharmacyDiscount = pharmacyDiscount;
    }

    public String getOrgShortName() {
        return orgShortName;
    }

    public void setOrgShortName(String orgShortName) {
        this.orgShortName = orgShortName;
    }

    public String getInsuranceCompanyCode() {
        return insuranceCompanyCode;
    }

    public void setInsuranceCompanyCode(String insuranceCompanyCode) {
        this.insuranceCompanyCode = insuranceCompanyCode;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public String getClaimRemark() {
        return claimRemark;
    }

    public void setClaimRemark(String claimRemark) {
        this.claimRemark = claimRemark;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getRelationshipCode() {
        return relationshipCode;
    }

    public void setRelationshipCode(String relationshipCode) {
        this.relationshipCode = relationshipCode;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public String getCorporateEmployeeCode() {
        return corporateEmployeeCode;
    }

    public void setCorporateEmployeeCode(String corporateEmployeeCode) {
        this.corporateEmployeeCode = corporateEmployeeCode;
    }

    public String getCorporateEmployeeName() {
        return corporateEmployeeName;
    }

    public void setCorporateEmployeeName(String corporateEmployeeName) {
        this.corporateEmployeeName = corporateEmployeeName;
    }

    public String getRankNo() {
        return rankNo;
    }

    public void setRankNo(String rankNo) {
        this.rankNo = rankNo;
    }

    public String getFeesDiscountPer() {
        return feesDiscountPer;
    }

    public void setFeesDiscountPer(String feesDiscountPer) {
        this.feesDiscountPer = feesDiscountPer;
    }

    public String getPharmacyDiscountPer() {
        return pharmacyDiscountPer;
    }

    public void setPharmacyDiscountPer(String pharmacyDiscountPer) {
        this.pharmacyDiscountPer = pharmacyDiscountPer;
    }

    public String getReferralNo() {
        return referralNo;
    }

    public void setReferralNo(String referralNo) {
        this.referralNo = referralNo;
    }

    public String getReferralValidUpTo() {
        return referralValidUpTo;
    }

    public void setReferralValidUpTo(String referralValidUpTo) {
        this.referralValidUpTo = referralValidUpTo;
    }

    public boolean isTpa() {
        return tpa;
    }

    public void setTpa(boolean tpa) {
        this.tpa = tpa;
    }

    public String getBillingPer() {
        return billingPer;
    }

    public void setBillingPer(String billingPer) {
        this.billingPer = billingPer;
    }

    private Organization_Mdl(String orgShortName, String insuranceCompanyCode, String insuranceCompanyName, String policyNo, String claimId, String claimDate, String claimRemark, String holderName, String relationshipCode, String relationshipName, String corporateEmployeeCode, String corporateEmployeeName, String rankNo, String feesDiscountPer, String pharmacyDiscountPer, String referralNo, String referralValidUpTo, boolean tpa, boolean pharmacyDiscount, String KeyFieldCode, String KeyFieldName, String billingPer) {
        super(KeyFieldCode, KeyFieldName);
        this.orgShortName = orgShortName;
        this.insuranceCompanyCode = insuranceCompanyCode;
        this.insuranceCompanyName = insuranceCompanyName;
        this.policyNo = policyNo;
        this.claimId = claimId;
        this.claimDate = claimDate;
        this.claimRemark = claimRemark;
        this.holderName = holderName;
        this.relationshipCode = relationshipCode;
        this.relationshipName = relationshipName;
        this.corporateEmployeeCode = corporateEmployeeCode;
        this.corporateEmployeeName = corporateEmployeeName;
        this.rankNo = rankNo;
        this.feesDiscountPer = feesDiscountPer;
        this.pharmacyDiscountPer = pharmacyDiscountPer;
        this.referralNo = referralNo;
        this.referralValidUpTo = referralValidUpTo;
        this.tpa = tpa;
        this.pharmacyDiscount = pharmacyDiscount;
        this.billingPer = billingPer;
    }

    public Organization_Mdl(Organization_Mdl mdl) {
        this(mdl.getOrgShortName(), mdl.getInsuranceCompanyCode(), mdl.getInsuranceCompanyName(), mdl.getPolicyNo(), mdl.getClaimId(), mdl.getClaimDate(), mdl.getClaimRemark(), mdl.getHolderName(), mdl.getRelationshipCode(), mdl.getRelationshipName(), mdl.getCorporateEmployeeCode(), mdl.getCorporateEmployeeName(), mdl.getRankNo(), mdl.getFeesDiscountPer(), mdl.getPharmacyDiscountPer(), mdl.getReferralNo(), mdl.getReferralValidUpTo(), mdl.isTpa(), mdl.isPharmacyDiscount(), mdl.getSharedKeyFieldCode(), mdl.getSharedKeyFieldName(), mdl.getBillingPer());
    }

}
