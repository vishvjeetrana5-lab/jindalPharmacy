package aarogya.selfToolkit.iplApplicableWithOption.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.PriceListType;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class RateList_Mdl extends KeyField implements Serializable, IsSerializable {

    private PriceListType rateListTypeEnum = PriceListType.FIX;

    public PriceListType getRateListTypeEnum() {
        return rateListTypeEnum;
    }

    public void setRateListTypeEnum(PriceListType rateListTypeEnum) {
        this.rateListTypeEnum = rateListTypeEnum;
    }
    private String rateListType;

    public RateList_Mdl() {
    }

    public String getRateListType() {
        return rateListType;
    }

    public void setRateListType(String rateListType) {
        this.rateListType = rateListType;
    }

}
