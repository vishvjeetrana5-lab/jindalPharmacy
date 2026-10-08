package aarogya.pharmacy.medicineGroupMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String saleDiscount;
    private String isCommon;
    private String isCommonText;

    public Find_Mdl() {
    }

    public String getIsCommonText() {
        return isCommonText;
    }

    public void setIsCommonText(String isCommonText) {
        this.isCommonText = isCommonText;
    }

    public String getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }

    public String getSaleDiscount() {
        return saleDiscount;
    }

    public void setSaleDiscount(String saleDiscount) {
        this.saleDiscount = saleDiscount;
    }

}
