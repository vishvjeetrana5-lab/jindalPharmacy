package dataman.accounts.bankLetterApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdBankLetterApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String preparedBy, modifiedBy, bulkPaymentLerrterDate, fromFinalPaymentDate,
            toFinalPaymentDate,approvalDateTime,approvedBy;

    private String isView;
    private String wtaCode;
    private String isChecked;
    private String isStatus;

    public GrdBankLetterApproval_Mdl() {
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getIsView() {
        return isView;
    }

    public void setIsView(String isView) {
        this.isView = isView;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getBulkPaymentLerrterDate() {
        return bulkPaymentLerrterDate;
    }

    public void setBulkPaymentLerrterDate(String bulkPaymentLerrterDate) {
        this.bulkPaymentLerrterDate = bulkPaymentLerrterDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getFromFinalPaymentDate() {
        return fromFinalPaymentDate;
    }

    public void setFromFinalPaymentDate(String fromFinalPaymentDate) {
        this.fromFinalPaymentDate = fromFinalPaymentDate;
    }

    public String getToFinalPaymentDate() {
        return toFinalPaymentDate;
    }

    public void setToFinalPaymentDate(String toFinalPaymentDate) {
        this.toFinalPaymentDate = toFinalPaymentDate;
    }

    public String getApprovalDateTime() {
        return approvalDateTime;
    }

    public void setApprovalDateTime(String approvalDateTime) {
        this.approvalDateTime = approvalDateTime;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

}
