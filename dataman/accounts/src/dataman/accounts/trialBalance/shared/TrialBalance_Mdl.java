package dataman.accounts.trialBalance.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TrialBalance_Mdl extends KeyField implements Serializable, IsSerializable {

    private String childGroupCode;
    private String childGroupName;
    private String childSubCode;
    private String childSubName;
    private String childBalance;
    private String debit;
    private String credit;

    public TrialBalance_Mdl() {
    }

    public String getChildGroupCode() {
        return childGroupCode;
    }

    public void setChildGroupCode(String childGroupCode) {
        this.childGroupCode = childGroupCode;
    }

    public String getChildGroupName() {
        return childGroupName;
    }

    public void setChildGroupName(String childGroupName) {
        this.childGroupName = childGroupName;
    }

    public String getChildSubCode() {
        return childSubCode;
    }

    public void setChildSubCode(String childSubCode) {
        this.childSubCode = childSubCode;
    }

    public String getChildSubName() {
        return childSubName;
    }

    public void setChildSubName(String childSubName) {
        this.childSubName = childSubName;
    }

    public String getChildBalance() {
        return childBalance;
    }

    public void setChildBalance(String childBalance) {
        this.childBalance = childBalance;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

}
