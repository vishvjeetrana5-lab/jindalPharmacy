package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ItemModelMaster_Mdl implements Serializable, IsSerializable {

    private String modelName;
    private String modelCode;
    private String his_Code;

    public ItemModelMaster_Mdl() {
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getHis_Code() {
        return his_Code;
    }

    public void setHis_Code(String his_Code) {
        this.his_Code = his_Code;
    }

}
