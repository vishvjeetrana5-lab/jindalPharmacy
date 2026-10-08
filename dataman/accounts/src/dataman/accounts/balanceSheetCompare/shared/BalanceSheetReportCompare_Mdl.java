package dataman.accounts.balanceSheetCompare.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BalanceSheetReportCompare_Mdl implements Serializable, IsSerializable {

    private String totalAssetAmount;
    private String totalLiabilityAmount;
    private String lstTotalAssetAmount;
    private String lstTotalLiabilityAmount;
    List<GrdBalanceSheetReportCompare_Mdl> listBalanceSheet = new ArrayList<GrdBalanceSheetReportCompare_Mdl>();

    public BalanceSheetReportCompare_Mdl() {

    }

    public List<GrdBalanceSheetReportCompare_Mdl> getListBalanceSheet() {
        return listBalanceSheet;
    }

    public void setListProfitLoss(List<GrdBalanceSheetReportCompare_Mdl> listBalanceSheet) {
        this.listBalanceSheet = listBalanceSheet;
    }

    public String getTotalAssetAmount() {
        return totalAssetAmount;
    }

    public void setTotalAssetAmount(String totalAssetAmount) {
        this.totalAssetAmount = totalAssetAmount;
    }

    public String getTotalLiabilityAmount() {
        return totalLiabilityAmount;
    }

    public void setTotalLiabilityAmount(String totalLiabilityAmount) {
        this.totalLiabilityAmount = totalLiabilityAmount;
    }

    public String getLstTotalAssetAmount() {
        return lstTotalAssetAmount;
    }

    public void setLstTotalAssetAmount(String lstTotalAssetAmount) {
        this.lstTotalAssetAmount = lstTotalAssetAmount;
    }

    public String getLstTotalLiabilityAmount() {
        return lstTotalLiabilityAmount;
    }

    public void setLstTotalLiabilityAmount(String lstTotalLiabilityAmount) {
        this.lstTotalLiabilityAmount = lstTotalLiabilityAmount;
    }

    public void setListBalanceSheet(List<GrdBalanceSheetReportCompare_Mdl> listBalanceSheet) {
        this.listBalanceSheet = listBalanceSheet;
    }

}
