package dataman.accounts.acGroupDetailCompare.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class RptAcGroupDetailCompare_Mdl implements Serializable, IsSerializable {

    private String totalCredit;
    private String totalDebit;
    private String totalDifference;
    private String fromToDate;
    private boolean accountGroup;

    public RptAcGroupDetailCompare_Mdl() {
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

    public String getFromToDate() {
        return fromToDate;
    }

    public void setFromToDate(String fromToDate) {
        this.fromToDate = fromToDate;
    }

    public boolean isAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(boolean accountGroup) {
        this.accountGroup = accountGroup;
    }

}
