package dataman.accounts.voucherAdjustmentEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.accountAdjustment.shared.GrdAdjustment_Mdl;
import dataman.dmToolkit.itemAdjustment.shared.GrdItemAdjustment_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdLedger_Mdl extends KeyField implements Serializable, IsSerializable {

    private String v_TypeCode;
    private String v_TypeName;
    private String v_No;
    private String v_SNo;
    private String v_Date;
    private String refNo;
    private String refDate;
    private String acCode;
    private String acName;
    private String acManualCode;
    private String cityCode;
    private String cityName;
    private String narration;
    private String debit;
    private String credit;
    private String adjustment;
    private String itemAdjustment;
    private List<GrdAdjustment_Mdl> lstAdjustment = new ArrayList<>();
    private List<GrdItemAdjustment_Mdl> lstItemAdjustment = new ArrayList<>();

    public GrdLedger_Mdl() {
    }

    public String getV_TypeCode() {
        return v_TypeCode;
    }

    public void setV_TypeCode(String v_TypeCode) {
        this.v_TypeCode = v_TypeCode;
    }

    public String getV_TypeName() {
        return v_TypeName;
    }

    public void setV_TypeName(String v_TypeName) {
        this.v_TypeName = v_TypeName;
    }

    public String getV_No() {
        return v_No;
    }

    public void setV_No(String v_No) {
        this.v_No = v_No;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public String getAcCode() {
        return acCode;
    }

    public void setAcCode(String acCode) {
        this.acCode = acCode;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getAcManualCode() {
        return acManualCode;
    }

    public void setAcManualCode(String acManualCode) {
        this.acManualCode = acManualCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
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

    public String getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(String adjustment) {
        this.adjustment = adjustment;
    }

    public String getItemAdjustment() {
        return itemAdjustment;
    }

    public void setItemAdjustment(String itemAdjustment) {
        this.itemAdjustment = itemAdjustment;
    }

    public List<GrdAdjustment_Mdl> getLstAdjustment() {
        return lstAdjustment;
    }

    public void setLstAdjustment(List<GrdAdjustment_Mdl> lstAdjustment) {
        this.lstAdjustment = lstAdjustment;
    }

    public List<GrdItemAdjustment_Mdl> getLstItemAdjustment() {
        return lstItemAdjustment;
    }

    public void setLstItemAdjustment(List<GrdItemAdjustment_Mdl> lstItemAdjustment) {
        this.lstItemAdjustment = lstItemAdjustment;
    }

}
