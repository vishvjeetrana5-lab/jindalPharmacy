package dataman.accounts.budgetDisbursement.shared;

import dataman.accounts.budgetDisbursement.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TxtHBudgetHead_Mdl extends KeyField implements Serializable, IsSerializable {

    private String budgetHeadName, budgetHeadCode, underHeadName,costCenterTag,costCenterText,balanceBudget
            ,fromDate,toDate;

    public TxtHBudgetHead_Mdl() {
    }

    public String getBudgetHeadName() {
        return budgetHeadName;
    }

    public void setBudgetHeadName(String budgetHeadName) {
        this.budgetHeadName = budgetHeadName;
    }

    public String getBudgetHeadCode() {
        return budgetHeadCode;
    }

    public void setBudgetHeadCode(String budgetHeadCode) {
        this.budgetHeadCode = budgetHeadCode;
    }

    public String getUnderHeadName() {
        return underHeadName;
    }

    public void setUnderHeadName(String underHeadName) {
        this.underHeadName = underHeadName;
    }

    public String getCostCenterTag() {
        return costCenterTag;
    }

    public void setCostCenterTag(String costCenterTag) {
        this.costCenterTag = costCenterTag;
    }

    public String getCostCenterText() {
        return costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this.costCenterText = costCenterText;
    }

    public String getBalanceBudget() {
        return balanceBudget;
    }

    public void setBalanceBudget(String balanceBudget) {
        this.balanceBudget = balanceBudget;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

}
