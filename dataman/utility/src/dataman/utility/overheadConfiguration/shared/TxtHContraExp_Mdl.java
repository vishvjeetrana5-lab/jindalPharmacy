package dataman.utility.overheadConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHContraExp_Mdl extends KeyField implements Serializable,IsSerializable{

    private String description;

    public TxtHContraExp_Mdl() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
