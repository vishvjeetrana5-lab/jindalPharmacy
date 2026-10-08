package dataman.accounts.finalPaymentEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmToolkit.tdsCategoryPopup.shared.TdsCategoryPopup_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdPayment_Mdl extends KeyField implements Serializable, IsSerializable {

    private String netPay;
    private String insNo;
    private String insDate;
    private String favourOf;
    private String remark;

    private String cardTypeCode;
    private String cardTypeName;
    private String cardTypeNatureCode;
    private String cardTypeNatureName;
    private String cardTypeShortName;
    private String acNameCode;
    private String acNameName;
    private String acNature;

    private String tdsCategoryName;
    private String tdsCategoryCode;
    private String tdsDeductFormName;
    private String tdsDeductFormCode;
    private String tdsDeductManualCode;
    private String tdsOnAmount;
    private String originalAmount;
    private String tdsDeduction;
    private String tdsFor;
    private String debit;
    private String credit;
    private String ledgerDocId;
    private String ledgerVSno;
    TdsCategoryPopup_Mdl tdsCatMdl = new TdsCategoryPopup_Mdl();
    private List<TdsCategoryGrid_Mdl> tdsGridList = new ArrayList<>();

    public GrdPayment_Mdl() {
    }

    public TdsCategoryPopup_Mdl getTdsCatMdl() {
        return tdsCatMdl;
    }

    public void setTdsCatMdl(TdsCategoryPopup_Mdl tdsCatMdl) {
        this.tdsCatMdl = tdsCatMdl;
    }

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public String getCardTypeNatureCode() {
        return cardTypeNatureCode;
    }

    public void setCardTypeNatureCode(String cardTypeNatureCode) {
        this.cardTypeNatureCode = cardTypeNatureCode;
    }

    public String getCardTypeNatureName() {
        return cardTypeNatureName;
    }

    public void setCardTypeNatureName(String cardTypeNatureName) {
        this.cardTypeNatureName = cardTypeNatureName;
    }

    public String getCardTypeShortName() {
        return cardTypeShortName;
    }

    public void setCardTypeShortName(String cardTypeShortName) {
        this.cardTypeShortName = cardTypeShortName;
    }

    public String getAcNameCode() {
        return acNameCode;
    }

    public void setAcNameCode(String acNameCode) {
        this.acNameCode = acNameCode;
    }

    public String getAcNameName() {
        return acNameName;
    }

    public void setAcNameName(String acNameName) {
        this.acNameName = acNameName;
    }

    public String getAcNature() {
        return acNature;
    }

    public String getNetPay() {
        return netPay;
    }

    public void setNetPay(String netPay) {
        this.netPay = netPay;
    }

    public void setAcNature(String acNature) {
        this.acNature = acNature;
    }

    public String getInsNo() {
        return insNo;
    }

    public void setInsNo(String insNo) {
        this.insNo = insNo;
    }

    public String getInsDate() {
        return insDate;
    }

    public void setInsDate(String insDate) {
        this.insDate = insDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTdsCategoryName() {
        return tdsCategoryName;
    }

    public void setTdsCategoryName(String tdsCategoryName) {
        this.tdsCategoryName = tdsCategoryName;
    }

    public String getTdsCategoryCode() {
        return tdsCategoryCode;
    }

    public void setTdsCategoryCode(String tdsCategoryCode) {
        this.tdsCategoryCode = tdsCategoryCode;
    }

    public String getTdsDeductFormName() {
        return tdsDeductFormName;
    }

    public void setTdsDeductFormName(String tdsDeductFormName) {
        this.tdsDeductFormName = tdsDeductFormName;
    }

    public String getTdsDeductFormCode() {
        return tdsDeductFormCode;
    }

    public void setTdsDeductFormCode(String tdsDeductFormCode) {
        this.tdsDeductFormCode = tdsDeductFormCode;
    }

    public String getTdsDeductManualCode() {
        return tdsDeductManualCode;
    }

    public void setTdsDeductManualCode(String tdsDeductManualCode) {
        this.tdsDeductManualCode = tdsDeductManualCode;
    }

    public String getTdsOnAmount() {
        return tdsOnAmount;
    }

    public void setTdsOnAmount(String tdsOnAmount) {
        this.tdsOnAmount = tdsOnAmount;
    }

    public String getTdsFor() {
        return tdsFor;
    }

    public void setTdsFor(String tdsFor) {
        this.tdsFor = tdsFor;
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

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getTdsDeduction() {
        return tdsDeduction;
    }

    public String getLedgerDocId() {
        return ledgerDocId;
    }

    public void setLedgerDocId(String ledgerDocId) {
        this.ledgerDocId = ledgerDocId;
    }

    public void setTdsDeduction(String tdsDeduction) {
        this.tdsDeduction = tdsDeduction;
    }

    public String getLedgerVSno() {
        return ledgerVSno;
    }

    public String getFavourOf() {
        return favourOf;
    }

    public void setFavourOf(String favourOf) {
        this.favourOf = favourOf;
    }

    public void setLedgerVSno(String ledgerVSno) {
        this.ledgerVSno = ledgerVSno;
    }

    public List<TdsCategoryGrid_Mdl> getTdsGridList() {
        return tdsGridList;
    }

}
