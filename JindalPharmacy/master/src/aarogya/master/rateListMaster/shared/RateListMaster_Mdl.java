package aarogya.master.rateListMaster.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RateListMaster_Mdl implements Serializable, IsSerializable {

    private String shortName;
    private String rateListCode, rateListName;
    private String applicableDate, applicableTo, siteCode, rateListTypeCode, rateLisTypeName;
    private String pharmacyDiscountPer;
    private MedicineSaleOn medicineSaleOn = MedicineSaleOn.MRP;
    private boolean allowPharmacyDiscount = false;
    private List<GrdCategoryDiscount_Mdl> lstServiceCategory = new ArrayList<>();

    public RateListMaster_Mdl() {
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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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

    public String getRateLisTypeName() {
        return rateLisTypeName;
    }

    public void setRateLisTypeName(String rateLisTypeName) {
        this.rateLisTypeName = rateLisTypeName;
    }

    public String getRateListTypeCode() {
        return rateListTypeCode;
    }

    public void setRateListTypeCode(String rateListTypeCode) {
        this.rateListTypeCode = rateListTypeCode;
    }

}
