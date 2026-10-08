package dataman.utility.numberSystem.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdCounter_Mdl extends KeyField implements Serializable, IsSerializable {

    private String numberFormat;
    private String counterFormat;
    private String siteCode;
    private String siteName;
    private String voucherPre;

    public GrdCounter_Mdl() {
    }

    public String getVoucherPre() {
        return voucherPre;
    }

    public void setVoucherPre(String voucherPre) {
        this.voucherPre = voucherPre;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    public String getCounterFormat() {
        return counterFormat;
    }

    public void setCounterFormat(String counterFormat) {
        this.counterFormat = counterFormat;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

}
