package dataman.webJobs.userEmailConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.List;

public class UserEmailConfiguration_Mdl extends KeyField implements Serializable,IsSerializable{

    private List<GrdUserEmaiEdit_Mdl> grdData;

    public UserEmailConfiguration_Mdl() {
    }

    public List<GrdUserEmaiEdit_Mdl> getGrdData() {
        return grdData;
    }

}
