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

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String VDate;
    private String VType;
    private String CostCenter;
    private String CrAccount;
    private String DrAccount;
    private String Amount;
    private String ChaqNo;
    private String DtCheque;
    private String DtClearing;
    private String Narration;
    private String TransToSite;
    private String TrnRecId;
    private String TrnVType;
    private String TrnCostCenter;
    private String TrnCrAccount;
    private String TrnDrAccount;
    private String approvedBy;
    private String preparedBy;
    private String modifiedBy;
    private String entryDtTime;

    public Find_Mdl() {
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getEntryDtTime() {
        return entryDtTime;
    }

    public void setEntryDtTime(String entryDtTime) {
        this.entryDtTime = entryDtTime;
    }

    public String getVDate() {
        return VDate;
    }

    public void setVDate(String VDate) {
        this.VDate = VDate;
    }

    public String getVType() {
        return VType;
    }

    public void setVType(String VType) {
        this.VType = VType;
    }

    public String getCostCenter() {
        return CostCenter;
    }

    public void setCostCenter(String CostCenter) {
        this.CostCenter = CostCenter;
    }

    public String getCrAccount() {
        return CrAccount;
    }

    public void setCrAccount(String CrAccount) {
        this.CrAccount = CrAccount;
    }

    public String getDrAccount() {
        return DrAccount;
    }

    public void setDrAccount(String DrAccount) {
        this.DrAccount = DrAccount;
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

    public String getNarration() {
        return Narration;
    }

    public void setNarration(String Narration) {
        this.Narration = Narration;
    }

    public String getTransToSite() {
        return TransToSite;
    }

    public void setTransToSite(String TransToSite) {
        this.TransToSite = TransToSite;
    }

    public String getTrnRecId() {
        return TrnRecId;
    }

    public void setTrnRecId(String TrnRecId) {
        this.TrnRecId = TrnRecId;
    }

    public String getTrnVType() {
        return TrnVType;
    }

    public void setTrnVType(String TrnVType) {
        this.TrnVType = TrnVType;
    }

    public String getTrnCostCenter() {
        return TrnCostCenter;
    }

    public void setTrnCostCenter(String TrnCostCenter) {
        this.TrnCostCenter = TrnCostCenter;
    }

    public String getTrnCrAccount() {
        return TrnCrAccount;
    }

    public void setTrnCrAccount(String TrnCrAccount) {
        this.TrnCrAccount = TrnCrAccount;
    }

    public String getTrnDrAccount() {
        return TrnDrAccount;
    }

    public void setTrnDrAccount(String TrnDrAccount) {
        this.TrnDrAccount = TrnDrAccount;
    }

}
