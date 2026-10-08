package dataman.utility.productLicensedTo.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ProductLicensedTo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String latestCompCode;
    private String latestCYear;

    public ProductLicensedTo_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLatestCompCode() {
        return latestCompCode;
    }

    public void setLatestCompCode(String latestCompCode) {
        this.latestCompCode = latestCompCode;
    }

    public String getLatestCYear() {
        return latestCYear;
    }

    public void setLatestCYear(String latestCYear) {
        this.latestCYear = latestCYear;
    }

}
