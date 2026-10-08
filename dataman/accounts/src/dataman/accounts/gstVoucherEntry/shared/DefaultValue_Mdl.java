package dataman.accounts.gstVoucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String siteGstin;
    private String siteGstinDate;
    private String siteRegistrationType;
    private String siteStateCode;
    private String isSiteStateUT;
    private String roundOffGSTCode;
    private String roundOffGSTName;

    public DefaultValue_Mdl() {
    }

    public String getRoundOffGSTCode() {
        return roundOffGSTCode;
    }

    public void setRoundOffGSTCode(String roundOffGSTCode) {
        this.roundOffGSTCode = roundOffGSTCode;
    }

    public String getRoundOffGSTName() {
        return roundOffGSTName;
    }

    public void setRoundOffGSTName(String roundOffGSTName) {
        this.roundOffGSTName = roundOffGSTName;
    }

    public String getSiteGstin() {
        return siteGstin;
    }

    public void setSiteGstin(String siteGstin) {
        this.siteGstin = siteGstin;
    }

    public String getSiteGstinDate() {
        return siteGstinDate;
    }

    public void setSiteGstinDate(String siteGstinDate) {
        this.siteGstinDate = siteGstinDate;
    }

    public String getSiteRegistrationType() {
        return siteRegistrationType;
    }

    public void setSiteRegistrationType(String siteRegistrationType) {
        this.siteRegistrationType = siteRegistrationType;
    }

    public String getSiteStateCode() {
        return siteStateCode;
    }

    public void setSiteStateCode(String siteStateCode) {
        this.siteStateCode = siteStateCode;
    }

    public String getIsSiteStateUT() {
        return isSiteStateUT;
    }

    public void setIsSiteStateUT(String isSiteStateUT) {
        this.isSiteStateUT = isSiteStateUT;
    }
}
