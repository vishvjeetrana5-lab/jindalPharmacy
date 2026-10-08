package dataman.accounts.bankLetterCreation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BankLetterCreation_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recid = new RecId();

    private String vType;
    private String vNo;
    private String date;

    private String fromFinalPaymentDate;
    private String toFinalPaymentDate;

    private String approvedBy;
    private String modifiedBy;
    private String preparedBy;

    private String wtaCode;
    private String isDiscarded;
private ModifyReason_Mdl modifyReason = new ModifyReason_Mdl();
    private List<GrdBankLetterCreation_Mdl> grdData = new ArrayList<GrdBankLetterCreation_Mdl>();

    public BankLetterCreation_Mdl() {
    }

    public RecId getRecid() {
        return recid;
    }

    public void setRecid(RecId recid) {
        this.recid = recid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<GrdBankLetterCreation_Mdl> getGrdData() {
        return grdData;
    }

    public void setGrdData(List<GrdBankLetterCreation_Mdl> grdData) {
        this.grdData = grdData;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
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

    public String getvType() {
        return vType;
    }

    public void setvType(String vType) {
        this.vType = vType;
    }

    public String getvNo() {
        return vNo;
    }

    public void setvNo(String vNo) {
        this.vNo = vNo;
    }

    public String getIsDiscarded() {
        return isDiscarded;
    }

    public void setIsDiscarded(String isDiscarded) {
        this.isDiscarded = isDiscarded;
    }

    public ModifyReason_Mdl getModifyReason() {
        return modifyReason;
    }

    public void setModifyReason(ModifyReason_Mdl modifyReason) {
        this.modifyReason = modifyReason;
    }

}
