package aarogya.master.servicePriceList.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GrdServices_Mdl extends KeyField implements Serializable, IsSerializable {

    private String serviceCode;
    private String categoryText;
    private String rate;
    private String printSNo;
    private String discountPercentage;
    private String discountAmount;
    private String organizationServiceCode;
    private String organizationServiceName;
    private String serviceType;
    private List<GrdRoomCategory_Mdl> lstRoomCategory = new ArrayList<GrdRoomCategory_Mdl>();
    private boolean colorChange = false;

    public GrdServices_Mdl() {
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public boolean isColorChange() {
        return colorChange;
    }

    public void setColorChange(boolean colorChange) {
        this.colorChange = colorChange;
    }

    public List<GrdRoomCategory_Mdl> getLstRoomCategory() {
        return lstRoomCategory;
    }

    public String getPrintSNo() {
        return printSNo;
    }

    public void setPrintSNo(String printSNo) {
        this.printSNo = printSNo;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getOrganizationServiceCode() {
        return organizationServiceCode;
    }

    public void setOrganizationServiceCode(String organizationServiceCode) {
        this.organizationServiceCode = organizationServiceCode;
    }

    public String getOrganizationServiceName() {
        return organizationServiceName;
    }

    public void setOrganizationServiceName(String organizationServiceName) {
        this.organizationServiceName = organizationServiceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public GrdServices_Mdl clone() {
        GrdServices_Mdl local = new GrdServices_Mdl();
        List<GrdRoomCategory_Mdl> lstRoomCategory = new ArrayList<GrdRoomCategory_Mdl>();

        super.copy(local);

        local.setCategoryText(this.getCategoryText());
        local.setRate(this.getRate());
        local.setPrintSNo(this.getPrintSNo());
        local.setDiscountAmount(this.getDiscountAmount());
        local.setDiscountPercentage(this.getDiscountPercentage());
        local.setOrganizationServiceCode(this.getOrganizationServiceCode());
        local.setOrganizationServiceName(this.getOrganizationServiceName());
        local.setServiceType(this.getServiceType());

        if (getLstRoomCategory() != null) {
            for (int i = 0; i < getLstRoomCategory().size(); i++) {
                lstRoomCategory.add(getLstRoomCategory().get(i).clone());
            }
            this.lstRoomCategory.addAll(lstRoomCategory);
        }

        return local;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(DmShared.iNull(this.serviceCode).toString() + ":" + DmShared.iNull(this.organizationServiceCode).toString());
//        hash = 23 * hash + Objects.hashCode(this.serviceCode);
//        hash = 23 * hash + Objects.hashCode(DmShared.iNull(this.organizationServiceCode).longValue());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrdServices_Mdl other = (GrdServices_Mdl) obj;
        if (!Objects.equals(this.serviceCode, other.serviceCode)) {
            return false;
        }
        if (!Objects.equals(this.organizationServiceCode, other.organizationServiceCode)) {
            return false;
        }
        return true;
    }
}
