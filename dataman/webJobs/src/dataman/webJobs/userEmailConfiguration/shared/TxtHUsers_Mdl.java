package dataman.webJobs.userEmailConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class TxtHUsers_Mdl extends KeyField implements Serializable,IsSerializable{

    private String description;

    public TxtHUsers_Mdl() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
