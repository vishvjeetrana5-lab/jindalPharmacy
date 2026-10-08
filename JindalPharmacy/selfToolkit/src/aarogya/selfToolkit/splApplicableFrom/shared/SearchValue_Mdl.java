package aarogya.selfToolkit.splApplicableFrom.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SearchValue_Mdl implements Serializable, IsSerializable {

    private String keyCode;
    private String organizationTag;
    private String siteCode;
    private String dtApplicable;
    private PriceListEntryFrom priceListEntryFrom;

    public SearchValue_Mdl() {
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getOrganizationTag() {
        return organizationTag;
    }

    public void setOrganizationTag(String organizationTag) {
        this.organizationTag = organizationTag;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getDtApplicable() {
        return dtApplicable;
    }

    public void setDtApplicable(String dtApplicable) {
        this.dtApplicable = dtApplicable;
    }

    public PriceListEntryFrom getPriceListEntryFrom() {
        return priceListEntryFrom;
    }

    public void setPriceListEntryFrom(PriceListEntryFrom priceListEntryFrom) {
        this.priceListEntryFrom = priceListEntryFrom;
    }

}
