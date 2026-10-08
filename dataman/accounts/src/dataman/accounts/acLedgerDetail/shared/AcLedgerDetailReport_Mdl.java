package dataman.accounts.acLedgerDetail.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class AcLedgerDetailReport_Mdl implements Serializable, IsSerializable {

    private String totalCredit;
    private String totalDebit;
    private String totalDifference;
    private String totalBalance;

    public AcLedgerDetailReport_Mdl() {

    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
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

    public String getFromToDate() {
        return fromToDate;
    }

    public void setFromToDate(String fromToDate) {
        this.fromToDate = fromToDate;
    }

}
