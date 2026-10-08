package dataman.accounts.budgetDisbursement.shared;

import dataman.accounts.budgetDisbursement.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdBudgetDisbursementDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String budgetHeadTag;
    private String budgetHeadText;
    private String underHeadText;
    private String costcenterTag;
    private String costcenterText;
    private String vSno;
    private String plusMinus;
    private String plusMinusText;
    private String budget;
    private String fromDate;
    private String toDate;
    private String remarks;

    public String getBudgetHeadTag() {
        return budgetHeadTag;
    }

    public void setBudgetHeadTag(String budgetHeadTag) {
        this.budgetHeadTag = budgetHeadTag;
    }

    public String getBudgetHeadText() {
        return budgetHeadText;
    }

    public void setBudgetHeadText(String budgetHeadText) {
        this.budgetHeadText = budgetHeadText;
    }

    public String getUnderHeadText() {
        return underHeadText;
    }

    public void setUnderHeadText(String underHeadText) {
        this.underHeadText = underHeadText;
    }

    public String getCostcenterTag() {
        return costcenterTag;
    }

    public void setCostcenterTag(String costcenterTag) {
        this.costcenterTag = costcenterTag;
    }

    public String getCostcenterText() {
        return costcenterText;
    }

    public void setCostcenterText(String costcenterText) {
        this.costcenterText = costcenterText;
    }

    public String getvSno() {
        return vSno;
    }

    public void setvSno(String vSno) {
        this.vSno = vSno;
    }

    public String getPlusMinus() {
        return plusMinus;
    }

    public void setPlusMinus(String plusMinus) {
        this.plusMinus = plusMinus;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPlusMinusText() {
        return plusMinusText;
    }

    public void setPlusMinusText(String plusMinusText) {
        this.plusMinusText = plusMinusText;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    
}
