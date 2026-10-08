package aarogya.master.servicePriceList.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdCategoryDiscount_Mdl extends KeyField implements Serializable, IsSerializable {

    private String discountPercentage;
    private String orgnaizationCode;

    private boolean colorChange = false;

    public GrdCategoryDiscount_Mdl() {

    }

    public String getOrgnaizationCode() {
        return orgnaizationCode;
    }

    public void setOrgnaizationCode(String orgnaizationCode) {
        this.orgnaizationCode = orgnaizationCode;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public boolean isColorChange() {
        return colorChange;
    }

    public void setColorChange(boolean colorChange) {
        this.colorChange = colorChange;
    }
}
