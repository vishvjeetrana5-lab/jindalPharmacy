package dataman.accounts.voucherEntry.shared;

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

public class CostCenterGrid_Mdl extends KeyField implements Serializable, IsSerializable {

    private String code;
    private String name;
    private String amount;

    public CostCenterGrid_Mdl() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CostCenterGrid_Mdl clone() {
        CostCenterGrid_Mdl costCenterGrid = new CostCenterGrid_Mdl();

        costCenterGrid.setCode(this.getCode());
        costCenterGrid.setName(this.getName());
        costCenterGrid.setAmount(this.getAmount());

        return costCenterGrid;
    }

}
