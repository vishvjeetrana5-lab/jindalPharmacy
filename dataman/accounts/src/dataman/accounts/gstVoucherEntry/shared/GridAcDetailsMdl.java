package dataman.accounts.gstVoucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.itemAdjustment.shared.GrdItemAdjustment_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GridAcDetailsMdl extends KeyField implements Serializable, IsSerializable {

    private String hashCode;
    private String vSNo;
    private String accMCode;
    private String accCode;
    private String accName;
    private String accNatureManualCode;
    private String accStateCode;
    private String accPartyType;
    private String accGstin;
    private String accGstinDate;
    private String narration;
    private String debit;
    private String credit;
    private String amount;
    
    private String hsnCode;
    private String hsnName;
    private String hsnSac;
    private String taxableAmt;
    private String grossAmt;
    private String netAmt;
    private String itemGstNature;

    private String qty;
    private String rate;

    private String cgstPer;
    private String cgstAmt;

    private String sgstPer;
    private String sgstAmt;

    private String ugstPer;
    private String ugstAmt;

    private String sgstUgstPer;
    private String sgstUgstAmt;

    private String igstPer;
    private String igstAmt;

    private String roundOffGST;
    private String isRCM;
    
    private String cGstAcNameText;
    private String cGstAcNameTag;
    
    private String sGstAcNameText;
    private String sGstAcNameTag;
    
    private String iGstAcNameText;
    private String iGstAcNameTag;
    
    private List<GrdItemAdjustment_Mdl> lstItemAdjustments = new ArrayList<>();

    public GridAcDetailsMdl() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getAccNatureManualCode() {
        return accNatureManualCode;
    }

    public void setAccNatureManualCode(String accNatureManualCode) {
        this.accNatureManualCode = accNatureManualCode;
    }

    public String getAccStateCode() {
        return accStateCode;
    }

    public void setAccStateCode(String accStateCode) {
        this.accStateCode = accStateCode;
    }

    public String getAccPartyType() {
        return accPartyType;
    }

    public void setAccPartyType(String accPartyType) {
        this.accPartyType = accPartyType;
    }

    public String getAccGstin() {
        return accGstin;
    }

    public void setAccGstin(String accGstin) {
        this.accGstin = accGstin;
    }

    public String getAccGstinDate() {
        return accGstinDate;
    }

    public void setAccGstinDate(String accGstinDate) {
        this.accGstinDate = accGstinDate;
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

    public String getAccMCode() {
        return accMCode;
    }

    public void setAccMCode(String accMCode) {
        this.accMCode = accMCode;
    }

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public List<GrdItemAdjustment_Mdl> getLstItemAdjustments() {
        return lstItemAdjustments;
    }

    public void setLstItemAdjustments(List<GrdItemAdjustment_Mdl> lstItemAdjustments) {
        this.lstItemAdjustments = lstItemAdjustments;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getHsnName() {
        return hsnName;
    }

    public void setHsnName(String hsnName) {
        this.hsnName = hsnName;
    }

    public String getHsnSac() {
        return hsnSac;
    }

    public void setHsnSac(String hsnSac) {
        this.hsnSac = hsnSac;
    }

    public String getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(String taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public String getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(String grossAmt) {
        this.grossAmt = grossAmt;
    }

    public String getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(String netAmt) {
        this.netAmt = netAmt;
    }

    public String getItemGstNature() {
        return itemGstNature;
    }

    public void setItemGstNature(String itemGstNature) {
        this.itemGstNature = itemGstNature;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCgstPer() {
        return cgstPer;
    }

    public void setCgstPer(String cgstPer) {
        this.cgstPer = cgstPer;
    }

    public String getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(String cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public String getSgstPer() {
        return sgstPer;
    }

    public void setSgstPer(String sgstPer) {
        this.sgstPer = sgstPer;
    }

    public String getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(String sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public String getUgstPer() {
        return ugstPer;
    }

    public void setUgstPer(String ugstPer) {
        this.ugstPer = ugstPer;
    }

    public String getUgstAmt() {
        return ugstAmt;
    }

    public void setUgstAmt(String ugstAmt) {
        this.ugstAmt = ugstAmt;
    }

    public String getSgstUgstPer() {
        return sgstUgstPer;
    }

    public void setSgstUgstPer(String sgstUgstPer) {
        this.sgstUgstPer = sgstUgstPer;
    }

    public String getSgstUgstAmt() {
        return sgstUgstAmt;
    }

    public void setSgstUgstAmt(String sgstUgstAmt) {
        this.sgstUgstAmt = sgstUgstAmt;
    }

    public String getIgstPer() {
        return igstPer;
    }

    public void setIgstPer(String igstPer) {
        this.igstPer = igstPer;
    }

    public String getIgstAmt() {
        return igstAmt;
    }

    public void setIgstAmt(String igstAmt) {
        this.igstAmt = igstAmt;
    }

    public String getRoundOffGST() {
        return roundOffGST;
    }

    public void setRoundOffGST(String roundOffGST) {
        this.roundOffGST = roundOffGST;
    }

    public String getIsRCM() {
        return isRCM;
    }

    public void setIsRCM(String isRCM) {
        this.isRCM = isRCM;
    }

    public String getcGstAcNameText() {
        return cGstAcNameText;
    }

    public void setcGstAcNameText(String cGstAcNameText) {
        this.cGstAcNameText = cGstAcNameText;
    }

    public String getcGstAcNameTag() {
        return cGstAcNameTag;
    }

    public void setcGstAcNameTag(String cGstAcNameTag) {
        this.cGstAcNameTag = cGstAcNameTag;
    }

    public String getsGstAcNameText() {
        return sGstAcNameText;
    }

    public void setsGstAcNameText(String sGstAcNameText) {
        this.sGstAcNameText = sGstAcNameText;
    }

    public String getsGstAcNameTag() {
        return sGstAcNameTag;
    }

    public void setsGstAcNameTag(String sGstAcNameTag) {
        this.sGstAcNameTag = sGstAcNameTag;
    }

    public String getiGstAcNameText() {
        return iGstAcNameText;
    }

    public void setiGstAcNameText(String iGstAcNameText) {
        this.iGstAcNameText = iGstAcNameText;
    }

    public String getiGstAcNameTag() {
        return iGstAcNameTag;
    }

    public void setiGstAcNameTag(String iGstAcNameTag) {
        this.iGstAcNameTag = iGstAcNameTag;
    }

}
