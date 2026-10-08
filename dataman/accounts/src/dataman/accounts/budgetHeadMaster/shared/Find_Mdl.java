package dataman.accounts.budgetHeadMaster.shared;

import dataman.accounts.budgetHeadMaster.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String headUnderName;

    public Find_Mdl() {
    }

    public String getHeadUnderName() {
        return headUnderName;
    }

    public void setHeadUnderName(String headUnderName) {
        this.headUnderName = headUnderName;
    }
}
