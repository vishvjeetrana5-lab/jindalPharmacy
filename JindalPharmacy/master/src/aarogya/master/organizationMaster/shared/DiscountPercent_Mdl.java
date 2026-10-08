package aarogya.master.organizationMaster.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiscountPercent_Mdl implements Serializable, IsSerializable {

    private String pharmacyDiscountPercentage;
    private boolean pharmacyDiscountAllow = false;
    private MedicineSaleOn medicineSaleOn = MedicineSaleOn.MRP;
    private List<GrdCategoryDiscount_Mdl> lstCategoryDiscount = new ArrayList<GrdCategoryDiscount_Mdl>();

    public DiscountPercent_Mdl() {
    }

    public List<GrdCategoryDiscount_Mdl> getLstCategoryDiscount() {
        return lstCategoryDiscount;
    }

    public String getPharmacyDiscountPercentage() {
        return pharmacyDiscountPercentage;
    }

    public void setPharmacyDiscountPercentage(String pharmacyDiscountPercentage) {
        this.pharmacyDiscountPercentage = pharmacyDiscountPercentage;
    }

    public boolean isPharmacyDiscountAllow() {
        return pharmacyDiscountAllow;
    }

    public void setPharmacyDiscountAllow(boolean pharmacyDiscountAllow) {
        this.pharmacyDiscountAllow = pharmacyDiscountAllow;
    }

    public MedicineSaleOn getMedicineSaleOn() {
        return medicineSaleOn;
    }

    public void setMedicineSaleOn(MedicineSaleOn medicineSaleOn) {
        this.medicineSaleOn = medicineSaleOn;
    }
}
