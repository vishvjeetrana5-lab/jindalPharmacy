package dataman.accounts.balanceSheetCompare.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String total;
    private List<BalanceSheetDisplayCompare_Mdl> profitLossDisplayList = new ArrayList<BalanceSheetDisplayCompare_Mdl>();

    public DefaultValue_Mdl() {
    }

    public List<BalanceSheetDisplayCompare_Mdl> getProfitLossDisplayList() {
        return profitLossDisplayList;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
