package dataman.accounts.finalPaymentEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinalPaymentEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recId = new RecId();
    private String v_Date;
    private String voucherTypeTag;
    private String voucherTypeText;
    private String moduleTag;
    private String moduleText;
    private String supplierTag;
    private String supplierText;
    private String supplierDisplayName;
    private String partyAcBalance;
    private String bankCashLedgerBalance;
    private String selectedBillDr;
    private String lessOtherCr;
    private String netValueCr;
    private String paidAmt;
    private String originalAmt;
    private String tdsDeduction;
    private String approvedBy;
    private String approvedDt;
    private String remark;
    private String tdsCategoryCode;
    private String tdsCategoryName;
    private String bankLetterDocid;
    private String drAdjustAmt;

    private List<GrdPendingFPReqDetail_Mdl> lstPendingFPayReq = new ArrayList<>();
    private List<GrdOtherAc_Mdl> lstOtherAc = new ArrayList<>();
    private List<GridDrdAdjust_Mdl> lstdrAdjustAmt = new ArrayList<>();
    private HashMap<String, List<HashMap<String, LedgerAdj_Mdl>>> listHmMain = new HashMap<>();

    private PaymentPopUp_Mdl paymentPopUp_Mdl = new PaymentPopUp_Mdl();

    public FinalPaymentEntry_Mdl() {

    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getVoucherTypeTag() {
        return voucherTypeTag;
    }

    public void setVoucherTypeTag(String voucherTypeTag) {
        this.voucherTypeTag = voucherTypeTag;
    }

    public String getVoucherTypeText() {
        return voucherTypeText;
    }

    public void setVoucherTypeText(String voucherTypeText) {
        this.voucherTypeText = voucherTypeText;
    }

    public String getModuleTag() {
        return moduleTag;
    }

    public void setModuleTag(String moduleTag) {
        this.moduleTag = moduleTag;
    }

    public String getModuleText() {
        return moduleText;
    }

    public void setModuleText(String moduleText) {
        this.moduleText = moduleText;
    }

    public String getSupplierTag() {
        return supplierTag;
    }

    public void setSupplierTag(String supplierTag) {
        this.supplierTag = supplierTag;
    }

    public String getSupplierText() {
        return supplierText;
    }

    public void setSupplierText(String supplierText) {
        this.supplierText = supplierText;
    }

    public String getPartyAcBalance() {
        return partyAcBalance;
    }

    public void setPartyAcBalance(String partyAcBalance) {
        this.partyAcBalance = partyAcBalance;
    }

    public String getBankCashLedgerBalance() {
        return bankCashLedgerBalance;
    }

    public void setBankCashLedgerBalance(String bankCashLedgerBalance) {
        this.bankCashLedgerBalance = bankCashLedgerBalance;
    }

    public String getSelectedBillDr() {
        return selectedBillDr;
    }

    public void setSelectedBillDr(String selectedBillDr) {
        this.selectedBillDr = selectedBillDr;
    }

    public String getLessOtherCr() {
        return lessOtherCr;
    }

    public void setLessOtherCr(String lessOtherCr) {
        this.lessOtherCr = lessOtherCr;
    }

    public String getNetValueCr() {
        return netValueCr;
    }

    public void setNetValueCr(String netValueCr) {
        this.netValueCr = netValueCr;
    }

    public String getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(String paidAmt) {
        this.paidAmt = paidAmt;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedDt() {
        return approvedDt;
    }

    public void setApprovedDt(String approvedDt) {
        this.approvedDt = approvedDt;
    }

    public String getOriginalAmt() {
        return originalAmt;
    }

    public void setOriginalAmt(String originalAmt) {
        this.originalAmt = originalAmt;
    }

    public String getTdsDeduction() {
        return tdsDeduction;
    }

    public void setTdsDeduction(String tdsDeduction) {
        this.tdsDeduction = tdsDeduction;
    }

    public List<GrdPendingFPReqDetail_Mdl> getLstPendingFPayReq() {
        return lstPendingFPayReq;
    }

    public List<GrdOtherAc_Mdl> getLstOtherAc() {
        return lstOtherAc;
    }

    public List<GridDrdAdjust_Mdl> getLstdrAdjustAmt() {
        return lstdrAdjustAmt;
    }

    public void setLstdrAdjustAmt(List<GridDrdAdjust_Mdl> lstdrAdjustAmt) {
        this.lstdrAdjustAmt = lstdrAdjustAmt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTdsCategoryCode() {
        return tdsCategoryCode;
    }

    public void setTdsCategoryCode(String tdsCategoryCode) {
        this.tdsCategoryCode = tdsCategoryCode;
    }

    public String getTdsCategoryName() {
        return tdsCategoryName;
    }

    public void setTdsCategoryName(String tdsCategoryName) {
        this.tdsCategoryName = tdsCategoryName;
    }

    public String getDrAdjustAmt() {
        return drAdjustAmt;
    }

    public void setDrAdjustAmt(String drAdjustAmt) {
        this.drAdjustAmt = drAdjustAmt;
    }

    public PaymentPopUp_Mdl getPaymentPopUp_Mdl() {
        return paymentPopUp_Mdl;
    }

    public void setPaymentPopUp_Mdl(PaymentPopUp_Mdl paymentPopUp_Mdl) {
        this.paymentPopUp_Mdl = paymentPopUp_Mdl;
    }

    public String getSupplierDisplayName() {
        return supplierDisplayName;
    }

    public void setSupplierDisplayName(String supplierDisplayName) {
        this.supplierDisplayName = supplierDisplayName;
    }

    public String getBankLetterDocid() {
        return bankLetterDocid;
    }

    public void setBankLetterDocid(String bankLetterDocid) {
        this.bankLetterDocid = bankLetterDocid;
    }

    public HashMap<String, List<HashMap<String, LedgerAdj_Mdl>>> getListHmMain() {
        return listHmMain;
    }

    public void setListHmMain(HashMap<String, List<HashMap<String, LedgerAdj_Mdl>>> listHmMain) {
        this.listHmMain = listHmMain;
    }

}
