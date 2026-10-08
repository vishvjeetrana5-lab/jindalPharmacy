package dataman.accounts.profitLossDisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfitLossReport_Mdl implements Serializable, IsSerializable {

    private String totalRevenueAmount;
    private String totalExpenseAmount;
    List<GrdProfitLossReport_Mdl> listProfitLoss = new ArrayList<GrdProfitLossReport_Mdl>();

    public ProfitLossReport_Mdl() {

    }

    public String getTotalRevenueAmount() {
        return totalRevenueAmount;
    }

    public void setTotalRevenueAmount(String totalRevenueAmount) {
        this.totalRevenueAmount = totalRevenueAmount;
    }

    public String getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public void setTotalExpenseAmount(String totalExpenseAmount) {
        this.totalExpenseAmount = totalExpenseAmount;
    }

    public List<GrdProfitLossReport_Mdl> getListProfitLoss() {
        return listProfitLoss;
    }

    public void setListProfitLoss(List<GrdProfitLossReport_Mdl> listProfitLoss) {
        this.listProfitLoss = listProfitLoss;
    }

}
