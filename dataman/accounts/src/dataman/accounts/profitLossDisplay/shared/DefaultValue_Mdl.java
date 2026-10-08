package dataman.accounts.profitLossDisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String total;
    private List<ProfitLossDisplay_Mdl> profitLossDisplayList = new ArrayList<ProfitLossDisplay_Mdl>();

    public DefaultValue_Mdl() {
    }

    public List<ProfitLossDisplay_Mdl> getProfitLossDisplayList() {
        return profitLossDisplayList;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
