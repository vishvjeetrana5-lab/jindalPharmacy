package dataman.accounts.trialBalance.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrialBalanceReport_Mdl implements Serializable, IsSerializable {

    private String totalCredit;
    private String totalDebit;
    private String totalDifference;

    public TrialBalanceReport_Mdl() {
    }

    public String getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(String totalCredit) {
        this.totalCredit = totalCredit;
    }

    public String getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(String totalDebit) {
        this.totalDebit = totalDebit;
    }

    public String getTotalDifference() {
        return totalDifference;
    }

    public void setTotalDifference(String totalDifference) {
        this.totalDifference = totalDifference;
    }
    private String fromToDate;

    List<GrdTrialBalanceReport_Mdl> listTrialBalance = new ArrayList<GrdTrialBalanceReport_Mdl>();

    public String getFromToDate() {
        return fromToDate;
    }

    public void setFromToDate(String fromToDate) {
        this.fromToDate = fromToDate;
    }

    public List<GrdTrialBalanceReport_Mdl> getListTrialBalance() {
        return listTrialBalance;
    }

}
