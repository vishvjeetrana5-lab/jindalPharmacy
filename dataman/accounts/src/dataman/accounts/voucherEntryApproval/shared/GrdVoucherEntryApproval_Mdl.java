package dataman.accounts.voucherEntryApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdVoucherEntryApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String wtaCode;
    private String refNo;
    private String refDate;
    private String amount;
    private String voucherDate;
    private String costCenter;
    private String narration;
    private String firstActName;
    private String headerActName;
    private String SiteName;
    private String ApprovedBy;
    private String Approval;
    private String isStatus;
    private String isChecked;
    private String voucherType;
    private String voucherCategory;
    private String isView;

    public GrdVoucherEntryApproval_Mdl() {
    }

    public String getIsView() {
        return isView;
    }

    public void setIsView(String isView) {
        this.isView = isView;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getApprovedBy() {
        return ApprovedBy;
    }

    public void setApprovedBy(String ApprovedBy) {
        this.ApprovedBy = ApprovedBy;
    }

    public String getApproval() {
        return Approval;
    }

    public void setApproval(String Approval) {
        this.Approval = Approval;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String SiteName) {
        this.SiteName = SiteName;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    
    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getFirstActName() {
        return firstActName;
    }

    public void setFirstActName(String firstActName) {
        this.firstActName = firstActName;
    }

    public String getHeaderActName() {
        return headerActName;
    }

    public void setHeaderActName(String headerActName) {
        this.headerActName = headerActName;
    }

}
