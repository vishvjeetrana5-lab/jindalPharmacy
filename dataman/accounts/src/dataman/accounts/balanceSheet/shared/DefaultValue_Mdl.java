package dataman.accounts.balanceSheet.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String dtFrom;
    private String dtTo;
    private String amountLiabilities;
    private String amountAssests;
    private String amountDifference;
    private List<BalanceSheet_Mdl> lstBalanceSheet = new ArrayList<BalanceSheet_Mdl>();

    public DefaultValue_Mdl() {

    }

    public List<BalanceSheet_Mdl> getLstBalanceSheet() {
        return lstBalanceSheet;
    }

    public String getAmountLiabilities() {
        return amountLiabilities;
    }

    public void setAmountLiabilities(String amountLiabilities) {
        this.amountLiabilities = amountLiabilities;
    }

    public String getAmountAssests() {
        return amountAssests;
    }

    public void setAmountAssests(String amountAssests) {
        this.amountAssests = amountAssests;
    }

    public String getAmountDifference() {
        return amountDifference;
    }

    public void setAmountDifference(String amountDifference) {
        this.amountDifference = amountDifference;
    }

    public String getDtFrom() {
        return dtFrom;
    }

    public void setDtFrom(String dtFrom) {
        this.dtFrom = dtFrom;
    }

    public String getDtTo() {
        return dtTo;
    }

    public void setDtTo(String dtTo) {
        this.dtTo = dtTo;
    }
}
