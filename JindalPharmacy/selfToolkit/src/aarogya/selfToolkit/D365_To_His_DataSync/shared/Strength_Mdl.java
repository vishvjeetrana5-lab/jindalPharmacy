package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Strength_Mdl implements Serializable, IsSerializable {

    private String strengthCode;
    private String strengthName;
    private String his_Code;

    public Strength_Mdl() {
    }

    public String getStrengthCode() {
        return strengthCode;
    }

    public void setStrengthCode(String strengthCode) {
        this.strengthCode = strengthCode;
    }

    public String getStrengthName() {
        return strengthName;
    }

    public void setStrengthName(String strengthName) {
        this.strengthName = strengthName;
    }

    public String getHis_Code() {
        return his_Code;
    }

    public void setHis_Code(String his_Code) {
        this.his_Code = his_Code;
    }

}
