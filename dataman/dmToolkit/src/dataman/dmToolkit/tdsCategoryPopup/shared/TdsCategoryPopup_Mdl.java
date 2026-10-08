package dataman.dmToolkit.tdsCategoryPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TdsCategoryPopup_Mdl extends KeyField implements Serializable, IsSerializable {

    private String tdsCategoryName;
    private String tdsCategoryCode;
    private String tdsDeductFormName;
    private String tdsDeductFormCode;
    private String tdsDeductManualCode;
    private String tdsOnAmount;
    private String orignalAmount;
    private String tdsForTag;
    private String tdsForText;
    private String debit;
    private String credit;
    private boolean itrFill;
    private String panCardNo;
    private String aadhaarNo;
    private String closingBalance;
    private String ytdCR;
    private String ytdDR;
    private String ytdTDSonAmt;
    private String turnover;
    private String voucherDate;
    private String voucherTypeCode;
    private String voucherPrefix;
    private boolean tdsDetailBind;
    private String trnDocID;
    private String vCategory;
    private RecId RecId = new RecId();
    private String initialTurnoverAmount;
    private String poDocID;
    private String commonNarration;
    
    private List<TdsCategoryGrid_Mdl> tdsGridList = new ArrayList<TdsCategoryGrid_Mdl>();

    public TdsCategoryPopup_Mdl() {
    }

    public String getvCategory() {
        return vCategory;
    }

    public void setvCategory(String vCategory) {
        this.vCategory = vCategory;
    }

  

    

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getYtdCR() {
        return ytdCR;
    }

    public void setYtdCR(String ytdCR) {
        this.ytdCR = ytdCR;
    }

    public String getYtdDR() {
        return ytdDR;
    }

    public void setYtdDR(String ytdDR) {
        this.ytdDR = ytdDR;
    }

    public String getYtdTDSonAmt() {
        return ytdTDSonAmt;
    }

    public void setYtdTDSonAmt(String ytdTDSonAmt) {
        this.ytdTDSonAmt = ytdTDSonAmt;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherPrefix() {
        return voucherPrefix;
    }

    public void setVoucherPrefix(String voucherPrefix) {
        this.voucherPrefix = voucherPrefix;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public RecId getRecId() {
        return RecId;
    }

    public void setRecId(RecId RecId) {
        this.RecId = RecId;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public boolean isItrFill() {
        return itrFill;
    }

    public void setItrFill(boolean itrFill) {
        this.itrFill = itrFill;
    }

    public String getPanCardNo() {
        return panCardNo;
    }

    public void setPanCardNo(String panCardNo) {
        this.panCardNo = panCardNo;
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

    public List<TdsCategoryGrid_Mdl> getTdsGridList() {
        return tdsGridList;
    }

    public void setTdsGridList(List<TdsCategoryGrid_Mdl> tdsGridList) {
        this.tdsGridList = tdsGridList;
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

    public String getTdsOnAmount() {
        return tdsOnAmount;
    }

    public void setTdsOnAmount(String tdsOnAmount) {
        this.tdsOnAmount = tdsOnAmount;
    }

    public String getOrignalAmount() {
        return orignalAmount;
    }

    public void setOrignalAmount(String orignalAmount) {
        this.orignalAmount = orignalAmount;
    }

    public String getTdsForTag() {
        return tdsForTag;
    }

    public void setTdsForTag(String tdsForTag) {
        this.tdsForTag = tdsForTag;
    }

    public boolean isTdsDetailBind() {
        return tdsDetailBind;
    }

    public void setTdsDetailBind(boolean tdsDetailBind) {
        this.tdsDetailBind = tdsDetailBind;
    }

    public String getTrnDocID() {
        return trnDocID;
    }

    public void setTrnDocID(String trnDocID) {
        this.trnDocID = trnDocID;
    }

    public String getTdsDeductManualCode() {
        return tdsDeductManualCode;
    }

    public void setTdsDeductManualCode(String tdsDeductManualCode) {
        this.tdsDeductManualCode = tdsDeductManualCode;
    }

    public String getTdsForText() {
        return tdsForText;
    }

    public void setTdsForText(String tdsForText) {
        this.tdsForText = tdsForText;
    }

    public TdsCategoryPopup_Mdl clone() {
        TdsCategoryPopup_Mdl tDS_Mdl = new TdsCategoryPopup_Mdl();
        List<TdsCategoryGrid_Mdl> listTdsCatGrdData = new ArrayList();

        tDS_Mdl.setOrignalAmount(this.getOrignalAmount());
        tDS_Mdl.setTdsCategoryCode(this.getTdsCategoryCode());
        tDS_Mdl.setTdsCategoryName(this.getTdsCategoryName());
        tDS_Mdl.setTdsDeductFormCode(this.getTdsDeductFormCode());
        tDS_Mdl.setTdsDeductFormName(this.getTdsDeductFormName());
        tDS_Mdl.setTdsDeductManualCode(this.getTdsDeductManualCode());
        tDS_Mdl.setTdsForTag(this.getTdsForTag());
        tDS_Mdl.setTdsForText(this.getTdsForText());
        tDS_Mdl.setTdsOnAmount(this.getTdsOnAmount());
        tDS_Mdl.setTdsDetailBind(this.isTdsDetailBind());

        if (getTdsGridList() != null) {
            for (int i = 0; i < getTdsGridList().size(); i++) {
                listTdsCatGrdData.add(getTdsGridList().get(i).clone());
            }
            tDS_Mdl.setTdsGridList(listTdsCatGrdData);
        }

        return tDS_Mdl;
    }

    public String getInitialTurnoverAmount() {
        return initialTurnoverAmount;
    }

    public void setInitialTurnoverAmount(String initialTurnoverAmount) {
        this.initialTurnoverAmount = initialTurnoverAmount;
    }

    public String getPoDocID() {
        return poDocID;
    }

    public void setPoDocID(String poDocID) {
        this.poDocID = poDocID;
    }

    public String getCommonNarration() {
        return commonNarration;
    }

    public void setCommonNarration(String commonNarration) {
        this.commonNarration = commonNarration;
    }

}
