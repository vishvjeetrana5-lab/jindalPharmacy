package dataman.payroll.earningDeductionMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdEarnDed_Mdl extends KeyField implements Serializable,IsSerializable{

    private String shortName;

    public GrdEarnDed_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}
