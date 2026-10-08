package dataman.accounts.budgetHeadMaster.shared;

import dataman.accounts.budgetHeadMaster.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class BudgetHeadMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String headUnder;
    private String headUnderName;
    private String manualCode;

    public BudgetHeadMaster_Mdl() {
    }

    public String getHeadUnder() {
        return headUnder;
    }

    public void setHeadUnder(String headUnder) {
        this.headUnder = headUnder;
    }

    public String getHeadUnderName() {
        return headUnderName;
    }

    public void setHeadUnderName(String headUnderName) {
        this.headUnderName = headUnderName;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    }
