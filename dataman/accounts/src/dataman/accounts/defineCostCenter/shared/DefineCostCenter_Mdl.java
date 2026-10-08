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

public class DefineCostCenter_Mdl extends KeyField implements Serializable, IsSerializable {

    private String UnderCostCode;
    private String UnderCostName;
    private String ActiveCode;
    private String ActiveName;
    private String Description;
    private String ManualCode;

    public DefineCostCenter_Mdl() {
    }

    public String getUnderCostCode() {
        return UnderCostCode;
    }

    public void setUnderCostCode(String UnderCostCode) {
        this.UnderCostCode = UnderCostCode;
    }

    public String getUnderCostName() {
        return UnderCostName;
    }

    public void setUnderCostName(String UnderCostName) {
        this.UnderCostName = UnderCostName;
    }

    public String getActiveCode() {
        return ActiveCode;
    }

    public void setActiveCode(String ActiveCode) {
        this.ActiveCode = ActiveCode;
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

}
