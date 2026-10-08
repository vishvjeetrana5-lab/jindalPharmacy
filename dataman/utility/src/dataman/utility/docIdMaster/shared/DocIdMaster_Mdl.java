package dataman.utility.docIdMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.List;

public class DocIdMaster_Mdl extends KeyField implements Serializable,IsSerializable{

    private String siteName;
    private String siteCode;
    private String voucherPrefix;

    private List<GrdDocIdMaster_Mdl> grdData;

    public DocIdMaster_Mdl() {
    }

    public List<GrdDocIdMaster_Mdl> getGrdData() {
        return grdData;
    }

    public void setGrdData(List<GrdDocIdMaster_Mdl> grdData) {
        this.grdData = grdData;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getVoucherPrefix() {
        return voucherPrefix;
    }

    public void setVoucherPrefix(String voucherPrefix) {
        this.voucherPrefix = voucherPrefix;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

}
