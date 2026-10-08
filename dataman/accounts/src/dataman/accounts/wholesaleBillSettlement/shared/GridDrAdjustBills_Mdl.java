package dataman.accounts.wholesaleBillSettlement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GridDrAdjustBills_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vr_V_SNo;
    private String adj_V_SNo;
    private String vr_v_date;
    private String voucherNo;
    private String refNo;
    private String voucherDate;
    private String supplierTag;
    private String supplierText;
    private String contraAC;
    private String voucherType;
    private String amtcr;
    private String adjAmt;
    private String pendingAdjustment;
    private String checked;

    public GridDrAdjustBills_Mdl() {

    }

    public String getVr_V_SNo() {
        return vr_V_SNo;
    }

    public void setVr_V_SNo(String vr_V_SNo) {
        this.vr_V_SNo = vr_V_SNo;
    }

    public String getAdj_V_SNo() {
        return adj_V_SNo;
    }

    public void setAdj_V_SNo(String adj_V_SNo) {
        this.adj_V_SNo = adj_V_SNo;
    }

    public String getVr_v_date() {
        return vr_v_date;
    }

    public void setVr_v_date(String vr_v_date) {
        this.vr_v_date = vr_v_date;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
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

    public String getContraAC() {
        return contraAC;
    }

    public void setContraAC(String contraAC) {
        this.contraAC = contraAC;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getAmtcr() {
        return amtcr;
    }

    public void setAmtcr(String amtcr) {
        this.amtcr = amtcr;
    }

    public String getAdjAmt() {
        return adjAmt;
    }

    public void setAdjAmt(String adjAmt) {
        this.adjAmt = adjAmt;
    }

    public String getPendingAdjustment() {
        return pendingAdjustment;
    }

    public void setPendingAdjustment(String pendingAdjustment) {
        this.pendingAdjustment = pendingAdjustment;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

}
