package dataman.accounts.profitLossDisplayCompare.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String total;
    private List<ProfitLossDisplayCompare_Mdl> profitLossDisplayList = new ArrayList<ProfitLossDisplayCompare_Mdl>();

    public DefaultValue_Mdl() {
    }

    public List<ProfitLossDisplayCompare_Mdl> getProfitLossDisplayList() {
        return profitLossDisplayList;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
