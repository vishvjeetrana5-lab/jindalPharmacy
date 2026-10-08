package dataman.accounts.fundInTransitEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class FundInTransitEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private String voucherPrefix;
    private String voucherTypeCode;
    private String DtEntry;
    private String TransferNoTag;
    private String TransferNoText;
    private String CurrentSiteTag;
    private String CurrentSiteText;
    private String CostCenterText;
    private String CostCenterTag;
    private String CrAccountText;
    private String CrAccountTag;
    private String CrAccountDesc;
    private String DrAccountText;
    private String DrAccountTag;
    private String DrAccountDesc;
    private String Amount;
    private String ChqNo;
    private String DtChq;
    private String DtClearing;
    private String Remark;
    private String TransSiteTag;
    private String TransSiteText;
    private String TransCostCenterTag;
    private String TransCostCenterText;
    private String TransDrAccountText;
    private String TransDrAccountTag;
    private String TransCrAccountText;
    private String TransCrAccountTag;
    private String TransRemark;
    private String approvedBy;

    public FundInTransitEntry_Mdl() {
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

    public String getVoucherPrefix() {
        return voucherPrefix;
    }

    public void setVoucherPrefix(String voucherPrefix) {
        this.voucherPrefix = voucherPrefix;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getDtEntry() {
        return DtEntry;
    }

    public void setDtEntry(String DtEntry) {
        this.DtEntry = DtEntry;
    }

    public String getTransferNoTag() {
        return TransferNoTag;
    }

    public void setTransferNoTag(String TransferNoTag) {
        this.TransferNoTag = TransferNoTag;
    }

    public String getTransferNoText() {
        return TransferNoText;
    }

    public void setTransferNoText(String TransferNoText) {
        this.TransferNoText = TransferNoText;
    }

    public String getCurrentSiteTag() {
        return CurrentSiteTag;
    }

    public void setCurrentSiteTag(String CurrentSiteTag) {
        this.CurrentSiteTag = CurrentSiteTag;
    }

    public String getCurrentSiteText() {
        return CurrentSiteText;
    }

    public void setCurrentSiteText(String CurrentSiteText) {
        this.CurrentSiteText = CurrentSiteText;
    }

    public String getCostCenterText() {
        return CostCenterText;
    }

    public void setCostCenterText(String CostCenterText) {
        this.CostCenterText = CostCenterText;
    }

    public String getCostCenterTag() {
        return CostCenterTag;
    }

    public void setCostCenterTag(String CostCenterTag) {
        this.CostCenterTag = CostCenterTag;
    }

    public String getCrAccountText() {
        return CrAccountText;
    }

    public void setCrAccountText(String CrAccountText) {
        this.CrAccountText = CrAccountText;
    }

    public String getCrAccountTag() {
        return CrAccountTag;
    }

    public void setCrAccountTag(String CrAccountTag) {
        this.CrAccountTag = CrAccountTag;
    }

    public String getCrAccountDesc() {
        return CrAccountDesc;
    }

    public void setCrAccountDesc(String CrAccountDesc) {
        this.CrAccountDesc = CrAccountDesc;
    }

    public String getDrAccountText() {
        return DrAccountText;
    }

    public void setDrAccountText(String DrAccountText) {
        this.DrAccountText = DrAccountText;
    }

    public String getDrAccountTag() {
        return DrAccountTag;
    }

    public void setDrAccountTag(String DrAccountTag) {
        this.DrAccountTag = DrAccountTag;
    }

    public String getDrAccountDesc() {
        return DrAccountDesc;
    }

    public void setDrAccountDesc(String DrAccountDesc) {
        this.DrAccountDesc = DrAccountDesc;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public String getChqNo() {
        return ChqNo;
    }

    public void setChqNo(String ChqNo) {
        this.ChqNo = ChqNo;
    }

    public String getDtChq() {
        return DtChq;
    }

    public void setDtChq(String DtChq) {
        this.DtChq = DtChq;
    }

    public String getDtClearing() {
        return DtClearing;
    }

    public void setDtClearing(String DtClearing) {
        this.DtClearing = DtClearing;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getTransSiteTag() {
        return TransSiteTag;
    }

    public void setTransSiteTag(String TransSiteTag) {
        this.TransSiteTag = TransSiteTag;
    }

    public String getTransSiteText() {
        return TransSiteText;
    }

    public void setTransSiteText(String TransSiteText) {
        this.TransSiteText = TransSiteText;
    }

    public String getTransCostCenterTag() {
        return TransCostCenterTag;
    }

    public void setTransCostCenterTag(String TransCostCenterTag) {
        this.TransCostCenterTag = TransCostCenterTag;
    }

    public String getTransCostCenterText() {
        return TransCostCenterText;
    }

    public void setTransCostCenterText(String TransCostCenterText) {
        this.TransCostCenterText = TransCostCenterText;
    }

    public String getTransDrAccountText() {
        return TransDrAccountText;
    }

    public void setTransDrAccountText(String TransDrAccountText) {
        this.TransDrAccountText = TransDrAccountText;
    }

    public String getTransDrAccountTag() {
        return TransDrAccountTag;
    }

    public void setTransDrAccountTag(String TransDrAccountTag) {
        this.TransDrAccountTag = TransDrAccountTag;
    }

    public String getTransCrAccountText() {
        return TransCrAccountText;
    }

    public void setTransCrAccountText(String TransCrAccountText) {
        this.TransCrAccountText = TransCrAccountText;
    }

    public String getTransCrAccountTag() {
        return TransCrAccountTag;
    }

    public void setTransCrAccountTag(String TransCrAccountTag) {
        this.TransCrAccountTag = TransCrAccountTag;
    }

    public String getTransRemark() {
        return TransRemark;
    }

    public void setTransRemark(String TransRemark) {
        this.TransRemark = TransRemark;
    }
}
