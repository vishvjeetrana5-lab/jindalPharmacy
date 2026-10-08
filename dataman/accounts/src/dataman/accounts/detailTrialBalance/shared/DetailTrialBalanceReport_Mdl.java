package dataman.accounts.detailTrialBalance.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailTrialBalanceReport_Mdl implements Serializable, IsSerializable {

    private String totalCredit;
    private String totalDebit;
    private String totalDifference;
    private String totalOpening;
    private String openingDrCr;
    private String totalClosing;
    private String closingDrCr;

    public DetailTrialBalanceReport_Mdl() {
    }

    public String getTotalOpening() {
        return totalOpening;
    }

    public void setTotalOpening(String totalOpening) {
        this.totalOpening = totalOpening;
    }

    public String getOpeningDrCr() {
        return openingDrCr;
    }

    public void setOpeningDrCr(String openingDrCr) {
        this.openingDrCr = openingDrCr;
    }

    public String getTotalClosing() {
        return totalClosing;
    }

    public void setTotalClosing(String totalClosing) {
        this.totalClosing = totalClosing;
    }

    public String getClosingDrCr() {
        return closingDrCr;
    }

    public void setClosingDrCr(String closingDrCr) {
        this.closingDrCr = closingDrCr;
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
