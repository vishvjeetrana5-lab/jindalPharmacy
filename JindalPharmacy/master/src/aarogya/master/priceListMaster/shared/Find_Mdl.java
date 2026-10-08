package aarogya.master.priceListMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String priceListName;
    private String priceListCode;
    private String priceListType;
    private String priceListTypeTag;

    public Find_Mdl() {
    }
    
    public String getPriceListName() {
        return priceListName;
    }

    public void setPriceListName(String priceListName) {
        this.priceListName = priceListName;
    }

    public String getPriceListCode() {
        return priceListCode;
    }

    public void setPriceListCode(String priceListCode) {
        this.priceListCode = priceListCode;
    }

    public String getPriceListType() {
        return priceListType;
    }

    public void setPriceListType(String priceListType) {
        this.priceListType = priceListType;
    }

    public String getPriceListTypeTag() {
        return priceListTypeTag;
    }

    public void setPriceListTypeTag(String priceListTypeTag) {
        this.priceListTypeTag = priceListTypeTag;
    }



}
