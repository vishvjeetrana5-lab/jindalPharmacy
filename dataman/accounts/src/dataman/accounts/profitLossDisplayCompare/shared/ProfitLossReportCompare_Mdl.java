package dataman.accounts.profitLossDisplayCompare.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfitLossReportCompare_Mdl implements Serializable, IsSerializable {

    private String totalRevenueAmount;
    private String totalExpenseAmount;
    private String lstTotalRevenueAmount;
    private String lstTotalExpenseAmount;
    List<GrdProfitLossReportCompare_Mdl> listProfitLoss = new ArrayList<GrdProfitLossReportCompare_Mdl>();

    public ProfitLossReportCompare_Mdl() {

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

    public List<GrdProfitLossReportCompare_Mdl> getListProfitLoss() {
        return listProfitLoss;
    }

    public void setListProfitLoss(List<GrdProfitLossReportCompare_Mdl> listProfitLoss) {
        this.listProfitLoss = listProfitLoss;
    }

    public String getLstTotalRevenueAmount() {
        return lstTotalRevenueAmount;
    }

    public void setLstTotalRevenueAmount(String lstTotalRevenueAmount) {
        this.lstTotalRevenueAmount = lstTotalRevenueAmount;
    }

    public String getLstTotalExpenseAmount() {
        return lstTotalExpenseAmount;
    }

    public void setLstTotalExpenseAmount(String lstTotalExpenseAmount) {
        this.lstTotalExpenseAmount = lstTotalExpenseAmount;
    }

}
