package dataman.accounts.fundTransferEntry.shared;

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

public class FundTransferEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private String voucherPrefix;
    private String DtEntry;
    private String CurrentSiteTag;
    private String CurrentSiteText;
    private String CostCenterTag;
    private String CostCenterText;
    private String CrAccountTag;
    private String CrAccountText;
    private String CrAccountDesc;
    private String DrAccountTag;
    private String DrAccountText;
    private String DrAccountDesc;
    private String Amount;
    private String ChaqNo;
    private String DtCheque;
    private String DtClearing;
    private String Remark;
    private String TrnCostCenterText;
    private String TrnCostCenterTag;
    private String TrnCrAccountTag;
    private String TrnCrAccountText;
    private String TrnCrAccountDesc;
    private String TrnDrAccountTag;
    private String TrnDrAccountText;
    private String TrnDrAccountDesc;
    private String TrnRemark;
    private String TrnToSiteText;
    private String TrnToSiteTag;
    private String approvedBy;

    public FundTransferEntry_Mdl() {
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

    public String getDtEntry() {
        return DtEntry;
    }

    public void setDtEntry(String DtEntry) {
        this.DtEntry = DtEntry;
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

    public String getCostCenterTag() {
        return CostCenterTag;
    }

    public void setCostCenterTag(String CostCenterTag) {
        this.CostCenterTag = CostCenterTag;
    }

    public String getCostCenterText() {
        return CostCenterText;
    }

    public void setCostCenterText(String CostCenterText) {
        this.CostCenterText = CostCenterText;
    }

    public String getCrAccountTag() {
        return CrAccountTag;
    }

    public void setCrAccountTag(String CrAccountTag) {
        this.CrAccountTag = CrAccountTag;
    }

    public String getCrAccountText() {
        return CrAccountText;
    }

    public void setCrAccountText(String CrAccountText) {
        this.CrAccountText = CrAccountText;
    }

    public String getCrAccountDesc() {
        return CrAccountDesc;
    }

    public void setCrAccountDesc(String CrAccountDesc) {
        this.CrAccountDesc = CrAccountDesc;
    }

    public String getDrAccountTag() {
        return DrAccountTag;
    }

    public void setDrAccountTag(String DrAccountTag) {
        this.DrAccountTag = DrAccountTag;
    }

    public String getDrAccountText() {
        return DrAccountText;
    }

    public void setDrAccountText(String DrAccountText) {
        this.DrAccountText = DrAccountText;
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

    public String getChaqNo() {
        return ChaqNo;
    }

    public void setChaqNo(String ChaqNo) {
        this.ChaqNo = ChaqNo;
    }

    public String getDtCheque() {
        return DtCheque;
    }

    public void setDtCheque(String DtCheque) {
        this.DtCheque = DtCheque;
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

    public String getTrnCostCenterText() {
        return TrnCostCenterText;
    }

    public void setTrnCostCenterText(String TrnCostCenterText) {
        this.TrnCostCenterText = TrnCostCenterText;
    }

    public String getTrnCostCenterTag() {
        return TrnCostCenterTag;
    }

    public void setTrnCostCenterTag(String TrnCostCenterTag) {
        this.TrnCostCenterTag = TrnCostCenterTag;
    }

    public String getTrnCrAccountTag() {
        return TrnCrAccountTag;
    }

    public void setTrnCrAccountTag(String TrnCrAccountTag) {
        this.TrnCrAccountTag = TrnCrAccountTag;
    }

    public String getTrnCrAccountText() {
        return TrnCrAccountText;
    }

    public void setTrnCrAccountText(String TrnCrAccountText) {
        this.TrnCrAccountText = TrnCrAccountText;
    }

    public String getTrnCrAccountDesc() {
        return TrnCrAccountDesc;
    }

    public void setTrnCrAccountDesc(String TrnCrAccountDesc) {
        this.TrnCrAccountDesc = TrnCrAccountDesc;
    }

    public String getTrnDrAccountTag() {
        return TrnDrAccountTag;
    }

    public void setTrnDrAccountTag(String TrnDrAccountTag) {
        this.TrnDrAccountTag = TrnDrAccountTag;
    }

    public String getTrnDrAccountText() {
        return TrnDrAccountText;
    }

    public void setTrnDrAccountText(String TrnDrAccountText) {
        this.TrnDrAccountText = TrnDrAccountText;
    }

    public String getTrnDrAccountDesc() {
        return TrnDrAccountDesc;
    }

    public void setTrnDrAccountDesc(String TrnDrAccountDesc) {
        this.TrnDrAccountDesc = TrnDrAccountDesc;
    }

    public String getTrnRemark() {
        return TrnRemark;
    }

    public void setTrnRemark(String TrnRemark) {
        this.TrnRemark = TrnRemark;
    }

    public String getTrnToSiteText() {
        return TrnToSiteText;
    }

    public void setTrnToSiteText(String TrnToSiteText) {
        this.TrnToSiteText = TrnToSiteText;
    }

    public String getTrnToSiteTag() {
        return TrnToSiteTag;
    }

    public void setTrnToSiteTag(String TrnToSiteTag) {
        this.TrnToSiteTag = TrnToSiteTag;
    }
}
