package dataman.accounts.wholesaleBillSettlement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WholesaleBillSettlement_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recId = new RecId();
    private String v_Date;
    private String voucherTypeTag;
    private String voucherTypeText;
    private String supplierTag;
    private String supplierText;
    private String AccountName;
    private String AccountNameCode;
    private String creditAmt;
    private String totalBillAmt;
    private String adjustmentAmt;
    private String netvalueAmt;
    private String netAmt;
    private String remark;
    private String approvedBy;

    private List<GrdPendingFPReqDetail_Mdl> lstPendingFPayReq = new ArrayList<>();
    private List<GridDrAdjustBills_Mdl> lstdrAdjustAmt = new ArrayList<>();
    private HashMap<String, List<HashMap<String, LedgerAdj_Mdl>>> listHmMain = new HashMap<>();

    public WholesaleBillSettlement_Mdl() {

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

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String AccountName) {
        this.AccountName = AccountName;
    }

    public String getAccountNameCode() {
        return AccountNameCode;
    }

    public void setAccountNameCode(String AccountNameCode) {
        this.AccountNameCode = AccountNameCode;
    }

    public String getAdjustmentAmt() {
        return adjustmentAmt;
    }

    public void setAdjustmentAmt(String adjustmentAmt) {
        this.adjustmentAmt = adjustmentAmt;
    }

    public String getCreditAmt() {
        return creditAmt;
    }

    public void setCreditAmt(String creditAmt) {
        this.creditAmt = creditAmt;
    }

    public String getTotalBillAmt() {
        return totalBillAmt;
    }

    public void setTotalBillAmt(String totalBillAmt) {
        this.totalBillAmt = totalBillAmt;
    }

    public String getNetvalueAmt() {
        return netvalueAmt;
    }

    public void setNetvalueAmt(String netvalueAmt) {
        this.netvalueAmt = netvalueAmt;
    }

    public String getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(String netAmt) {
        this.netAmt = netAmt;
    }

    public List<GridDrAdjustBills_Mdl> getLstdrAdjustAmt() {
        return lstdrAdjustAmt;
    }

    public void setLstdrAdjustAmt(List<GridDrAdjustBills_Mdl> lstdrAdjustAmt) {
        this.lstdrAdjustAmt = lstdrAdjustAmt;
    }

    public List<GrdPendingFPReqDetail_Mdl> getLstPendingFPayReq() {
        return lstPendingFPayReq;
    }

    public HashMap<String, List<HashMap<String, LedgerAdj_Mdl>>> getListHmMain() {
        return listHmMain;
    }

    public void setListHmMain(HashMap<String, List<HashMap<String, LedgerAdj_Mdl>>> listHmMain) {
        this.listHmMain = listHmMain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

}
