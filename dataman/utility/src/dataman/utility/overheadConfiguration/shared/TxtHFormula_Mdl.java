package dataman.utility.overheadConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHFormula_Mdl extends KeyField implements Serializable,IsSerializable{

    private String overHeadCode;

    public TxtHFormula_Mdl() {
    }

    public String getOverHeadCode() {
        return overHeadCode;
    }

    public void setOverHeadCode(String overHeadCode) {
        this.overHeadCode = overHeadCode;
    }

}
