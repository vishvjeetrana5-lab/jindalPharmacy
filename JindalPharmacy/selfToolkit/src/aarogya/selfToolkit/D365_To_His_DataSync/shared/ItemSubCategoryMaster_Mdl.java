package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ItemSubCategoryMaster_Mdl implements Serializable, IsSerializable {

    private String name;
    private String D365_Code;
    private String His_Code;

    public ItemSubCategoryMaster_Mdl() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getD365_Code() {
        return D365_Code;
    }

    public void setD365_Code(String D365_Code) {
        this.D365_Code = D365_Code;
    }

    public String getHis_Code() {
        return His_Code;
    }

    public void setHis_Code(String His_Code) {
        this.His_Code = His_Code;
    }

}
