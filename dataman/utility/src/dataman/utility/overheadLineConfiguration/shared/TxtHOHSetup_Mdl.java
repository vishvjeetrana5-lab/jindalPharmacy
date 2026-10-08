package dataman.utility.overheadLineConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHOHSetup_Mdl extends KeyField implements Serializable,IsSerializable{

    private String bannerName;

    public TxtHOHSetup_Mdl() {
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

}
