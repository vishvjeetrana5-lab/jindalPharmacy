package dataman.utility.overheadLineConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHFormula_Mdl extends KeyField implements Serializable,IsSerializable{

    private String OverheadCode;

    public TxtHFormula_Mdl() {
    }

    public String getOverheadCode() {
        return OverheadCode;
    }

    public void setOverheadCode(String OverheadCode) {
        this.OverheadCode = OverheadCode;
    }

}
