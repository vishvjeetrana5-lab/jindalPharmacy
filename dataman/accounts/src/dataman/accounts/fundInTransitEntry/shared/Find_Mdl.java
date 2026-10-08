package dataman.accounts.fundInTransitEntry.shared;

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
    private String Narration;
    private String CostCenter;
    private String ChqNo;
    private String DtChq;
    private String DtClearing;
    private String AmtCr;
    private String CrParty;
    private String DrParty;
    private String TrnSite;
    private String TrnRecID;
    private String TrnType;
    private String TrnCrParty;
    private String TrnDrParty;
    private String TrnCostCenter;
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

    public String getNarration() {
        return Narration;
    }

    public void setNarration(String Narration) {
        this.Narration = Narration;
    }

    public String getCostCenter() {
        return CostCenter;
    }

    public void setCostCenter(String CostCenter) {
        this.CostCenter = CostCenter;
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

    public String getAmtCr() {
        return AmtCr;
    }

    public void setAmtCr(String AmtCr) {
        this.AmtCr = AmtCr;
    }

    public String getCrParty() {
        return CrParty;
    }

    public void setCrParty(String CrParty) {
        this.CrParty = CrParty;
    }

    public String getDrParty() {
        return DrParty;
    }

    public void setDrParty(String DrParty) {
        this.DrParty = DrParty;
    }

    public String getTrnSite() {
        return TrnSite;
    }

    public void setTrnSite(String TrnSite) {
        this.TrnSite = TrnSite;
    }

    public String getTrnRecID() {
        return TrnRecID;
    }

    public void setTrnRecID(String TrnRecID) {
        this.TrnRecID = TrnRecID;
    }

    public String getTrnType() {
        return TrnType;
    }

    public void setTrnType(String TrnType) {
        this.TrnType = TrnType;
    }

    public String getTrnCrParty() {
        return TrnCrParty;
    }

    public void setTrnCrParty(String TrnCrParty) {
        this.TrnCrParty = TrnCrParty;
    }

    public String getTrnDrParty() {
        return TrnDrParty;
    }

    public void setTrnDrParty(String TrnDrParty) {
        this.TrnDrParty = TrnDrParty;
    }

    public String getTrnCostCenter() {
        return TrnCostCenter;
    }

    public void setTrnCostCenter(String TrnCostCenter) {
        this.TrnCostCenter = TrnCostCenter;
    }

}
