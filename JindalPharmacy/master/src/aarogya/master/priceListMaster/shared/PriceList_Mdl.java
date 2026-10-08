package aarogya.master.priceListMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class PriceList_Mdl extends KeyField implements Serializable, IsSerializable {

    private String priceListTypeTag;
    private String priceListTypeName;
    private String applicableOn;
    private String applicableOnName;

    public String getApplicableOnName() {
        return applicableOnName;
    }

    public void setApplicableOnName(String applicableOnName) {
        this.applicableOnName = applicableOnName;
    }

    public String getApplicableOn() {
        return applicableOn;
    }

    public void setApplicableOn(String applicableOn) {
        this.applicableOn = applicableOn;
    }

    public String getPriceListTypeTag() {
        return priceListTypeTag;
    }

    public void setPriceListTypeTag(String priceListTypeTag) {
        this.priceListTypeTag = priceListTypeTag;
    }

    public String getPriceListTypeName() {
        return priceListTypeName;
    }

    public void setPriceListTypeName(String priceListTypeName) {
        this.priceListTypeName = priceListTypeName;
    }

    public PriceList_Mdl() {
    }

}
