package dataman.accounts.balanceSheet.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BalanceSheetReport_Mdl implements Serializable, IsSerializable {

    private String totalLiabilitiesAmount;
    private String totalAssetsAmount;
    private String totalDifference;
    List<GrdBalanceSheetReport_Mdl> listBalanceSheet = new ArrayList<GrdBalanceSheetReport_Mdl>();

    public BalanceSheetReport_Mdl() {
    }

    public String getTotalLiabilitiesAmount() {
        return totalLiabilitiesAmount;
    }

    public void setTotalLiabilitiesAmount(String totalLiabilitiesAmount) {
        this.totalLiabilitiesAmount = totalLiabilitiesAmount;
    }

    public String getTotalAssetsAmount() {
        return totalAssetsAmount;
    }

    public void setTotalAssetsAmount(String totalAssetsAmount) {
        this.totalAssetsAmount = totalAssetsAmount;
    }

    public String getTotalDifference() {
        return totalDifference;
    }

    public void setTotalDifference(String totalDifference) {
        this.totalDifference = totalDifference;
    }

    public List<GrdBalanceSheetReport_Mdl> getListBalanceSheet() {
        return listBalanceSheet;
    }

    public void setListBalanceSheet(List<GrdBalanceSheetReport_Mdl> listBalanceSheet) {
        this.listBalanceSheet = listBalanceSheet;
    }

}
