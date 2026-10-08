package dataman.accounts.defineCostCenter.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String code;
    private String UCostCenter;
    private String ActiveName;
    private String Description;
    private String ManualCode;

    public Find_Mdl() {
    }

    public String getUCostCenter() {
        return UCostCenter;
    }

    public void setUCostCenter(String UCostCenter) {
        this.UCostCenter = UCostCenter;
    }

    public String getActiveName() {
        return ActiveName;
    }

    public void setActiveName(String ActiveName) {
        this.ActiveName = ActiveName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getManualCode() {
        return ManualCode;
    }

    public void setManualCode(String ManualCode) {
        this.ManualCode = ManualCode;
    }

    public String getcode() {
        return code;
    }

    public void setcode(String CODE) {
        this.code = CODE;
    }

}
