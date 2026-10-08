package dataman.accounts.budgetAllocationEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BudgetAllocationEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private String dtAllocation;
    private String allocationTypeTag;
    private String voucherCategory;
    private String allocationTypeText;
    private String budgetNameText;
    private String fromDate;
    private String toDate;
    private String remark;
    private RecId recid = new RecId();

    private String preparedBy;
    private String modifiedBy;
    private String approvedBy;
    private String totalBudget;
    
    private List<GrdBudgetAllocationDetail_Mdl> grdBudgetHeadDetails = new ArrayList<GrdBudgetAllocationDetail_Mdl>();

    public BudgetAllocationEntry_Mdl() {
    }

    public String getDtAllocation() {
        return dtAllocation;
    }

    public void setDtAllocation(String dtAllocation) {
        this.dtAllocation = dtAllocation;
    }

    public String getAllocationTypeTag() {
        return allocationTypeTag;
    }

    public void setAllocationTypeTag(String allocationTypeTag) {
        this.allocationTypeTag = allocationTypeTag;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getAllocationTypeText() {
        return allocationTypeText;
    }

    public void setAllocationTypeText(String allocationTypeText) {
        this.allocationTypeText = allocationTypeText;
    }

    public String getBudgetNameText() {
        return budgetNameText;
    }

    public void setBudgetNameText(String budgetNameText) {
        this.budgetNameText = budgetNameText;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public RecId getRecid() {
        return recid;
    }

    public void setRecid(RecId recid) {
        this.recid = recid;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public List<GrdBudgetAllocationDetail_Mdl> getGrdBudgetHeadDetails() {
        return grdBudgetHeadDetails;
    }

    public void setGrdBudgetHeadDetails(List<GrdBudgetAllocationDetail_Mdl> grdBudgetHeadDetails) {
        this.grdBudgetHeadDetails = grdBudgetHeadDetails;
    }

    public String getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(String totalBudget) {
        this.totalBudget = totalBudget;
    }

}
