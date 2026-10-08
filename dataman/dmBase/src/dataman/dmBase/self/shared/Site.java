package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Site implements Serializable, IsSerializable {

    private byte[] siteCode;
    private byte[] siteName, branchName;

    public Site() {
    }

    public String getSiteCode() {
        return new String(siteCode);
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode.getBytes();
    }

    public String getSiteName() {
        return new String(siteName);
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName.getBytes();
    }
    
    public String getBranchName() {
        return new String(branchName);
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName.getBytes();
    }
}
