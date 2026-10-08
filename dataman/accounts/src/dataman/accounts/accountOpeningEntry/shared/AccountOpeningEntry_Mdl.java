package dataman.accounts.accountOpeningEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountOpeningEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private String voucherTypeName;
    private String voucherTypeCode;
    private String vPrefix;
    private String date;
    private String ledgerVoucherDate;
    private String accountName;
    private String accountNameCode;
    private String accountNameAccDesc;
    private String costCenterName;
    private String costCenterCode;
    private String narration;
    private String refNo;
    private String refDate;
    private String debit;
    private String credit;
    private String chqNo;
    private String chqDate;
    private String favourOf;
    private String chqClearDate;
    private String reconcilationRemark;
    private String contraSub;
    private String approvedBy;
    private List<GrdAccountOpeningEntry_Mdl> strGrdData = new ArrayList<GrdAccountOpeningEntry_Mdl>();

    public AccountOpeningEntry_Mdl() {
    }

    public RecId getObjRecId() {
        return objRecId;
    }

    public void setObjRecId(RecId objRecId) {
        this.objRecId = objRecId;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getContraSub() {
        return contraSub;
    }

    public void setContraSub(String contraSub) {
        this.contraSub = contraSub;
    }

    public List<GrdAccountOpeningEntry_Mdl> getStrGrdData() {
        return strGrdData;
    }

    public String getReconcilationRemark() {
        return reconcilationRemark;
    }

    public void setReconcilationRemark(String reconcilationRemark) {
        this.reconcilationRemark = reconcilationRemark;
    }

    public String getChqClearDate() {
        return chqClearDate;
    }

    public void setChqClearDate(String chqClearDate) {
        this.chqClearDate = chqClearDate;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getvPrefix() {
        return vPrefix;
    }

    public void setvPrefix(String vPrefix) {
        this.vPrefix = vPrefix;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNameCode() {
        return accountNameCode;
    }

    public void setAccountNameCode(String accountNameCode) {
        this.accountNameCode = accountNameCode;
    }

    public String getCostCenterName() {
        return costCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }

    public String getCostCenterCode() {
        return costCenterCode;
    }

    public void setCostCenterCode(String costCenterCode) {
        this.costCenterCode = costCenterCode;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String RefNo) {
        this.refNo = RefNo;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
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

    public String getChqNo() {
        return chqNo;
    }

    public void setChqNo(String chqNo) {
        this.chqNo = chqNo;
    }

    public String getChqDate() {
        return chqDate;
    }

    public void setChqDate(String chqDate) {
        this.chqDate = chqDate;
    }

    public String getFavourOf() {
        return favourOf;
    }

    public void setFavourOf(String favourOf) {
        this.favourOf = favourOf;
    }

    public String getAccountNameAccDesc() {
        return accountNameAccDesc;
    }

    public void setAccountNameAccDesc(String accountNameAccDesc) {
        this.accountNameAccDesc = accountNameAccDesc;
    }

    public String getLedgerVoucherDate() {
        return ledgerVoucherDate;
    }

    public void setLedgerVoucherDate(String ledgerVoucherDate) {
        this.ledgerVoucherDate = ledgerVoucherDate;
    }    
}
