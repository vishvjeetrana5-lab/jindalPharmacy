package dataman.utility.fileTypeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileTypeMaster_Mdl extends KeyField implements Serializable,IsSerializable{

    private List<GrdFileTypeMaster_Mdl> grdData = new ArrayList<GrdFileTypeMaster_Mdl>();

    private String description ;
    public FileTypeMaster_Mdl() {
    }

    public List<GrdFileTypeMaster_Mdl> getGrdData() {
        return grdData;
    }
 
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    

}
