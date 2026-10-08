package aarogya.master.servicePriceList.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServicePriceMaster_Mdl implements Serializable, IsSerializable {

    private String organizationCode, organizationName, shortName, orgnizationType;
    private String applicableDate, applicableTo, siteCode;
    private String serviceDiscountPer, testDiscountPer, feesDiscountPer, pharmacyDiscountPer;
    private MedicineSaleOn medicineSaleOn = MedicineSaleOn.MRP;
    private boolean allowPharmacyDiscount = false, active = false;
    private List<GrdCategoryDiscount_Mdl> lstServiceCategory = new ArrayList<>();

    public ServicePriceMaster_Mdl() {
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getServiceDiscountPer() {
        return serviceDiscountPer;
    }

    public void setServiceDiscountPer(String serviceDiscountPer) {
        this.serviceDiscountPer = serviceDiscountPer;
    }

    public String getTestDiscountPer() {
        return testDiscountPer;
    }

    public void setTestDiscountPer(String testDiscountPer) {
        this.testDiscountPer = testDiscountPer;
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

    public MedicineSaleOn getMedicineSaleOn() {
        return medicineSaleOn;
    }

    public void setMedicineSaleOn(MedicineSaleOn medicineSaleOn) {
        this.medicineSaleOn = medicineSaleOn;
    }

    public boolean isAllowPharmacyDiscount() {
        return allowPharmacyDiscount;
    }

    public void setAllowPharmacyDiscount(boolean allowPharmacyDiscount) {
        this.allowPharmacyDiscount = allowPharmacyDiscount;
    }

    public List<GrdCategoryDiscount_Mdl> getLstServiceCategory() {
        return lstServiceCategory;
    }

    public String getOrgnizationType() {
        return orgnizationType;
    }

    public void setOrgnizationType(String orgnizationType) {
        this.orgnizationType = orgnizationType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }
}
