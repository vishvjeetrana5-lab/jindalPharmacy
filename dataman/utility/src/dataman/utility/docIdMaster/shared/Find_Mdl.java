package dataman.utility.docIdMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable,IsSerializable{

    private String serachCode;
    private String voucherType;
    private String voucherDescription;
    private String prefix;
    private String voucherSerise;
    private String siteName;
    private String siteCode;

    public Find_Mdl() {
    }

    public String getSerachCode() {
        return serachCode;
    }

    public void setSerachCode(String serachCode) {
        this.serachCode = serachCode;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public void setVoucherDescription(String voucherDescription) {
        this.voucherDescription = voucherDescription;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getVoucherSerise() {
        return voucherSerise;
    }

    public void setVoucherSerise(String voucherSerise) {
        this.voucherSerise = voucherSerise;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

}
