package dataman.dmToolkit.accountAdjustment.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdAdjustment_Mdl extends KeyField implements Serializable, IsSerializable {

    private String isSelected;
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
    private String amount;
    private String amountDrCr;
    private String adjusted;
    private String adjustedDrCr;
    private String adjustment;
    private String adjustmentDrCr;
    private String history;

    public GrdAdjustment_Mdl() {
    }

    public GrdAdjustment_Mdl(GrdAdjustment_Mdl gaMdl) {
        this(gaMdl.getIsSelected(), gaMdl.getV_TypeCode(), gaMdl.getV_TypeName(), gaMdl.getV_No(), gaMdl.getV_SNo(), gaMdl.getV_Date(), gaMdl.getRefNo(), gaMdl.getRefDate(), gaMdl.getAcCode(), gaMdl.getAcName(), gaMdl.getAcManualCode(), gaMdl.getCityCode(), gaMdl.getCityName(), gaMdl.getNarration(), gaMdl.getAmount(), gaMdl.getAmountDrCr(), gaMdl.getAdjusted(), gaMdl.getAdjustedDrCr(), gaMdl.getAdjustment(), gaMdl.getAdjustmentDrCr(), gaMdl.getHistory(), gaMdl.getSharedKeyFieldCode(), gaMdl.getSharedKeyFieldName());
    }

    public GrdAdjustment_Mdl(String isSelected, String v_TypeCode, String v_TypeName, String v_No, String v_SNo, String v_Date, String refNo, String refDate, String acCode, String acName, String acManualCode, String cityCode, String cityName, String narration, String amount, String amountDrCr, String adjusted, String adjustedDrCr, String adjustment, String adjustmentDrCr, String history, String KeyFieldCode, String KeyFieldName) {
        super(KeyFieldCode, KeyFieldName);
        this.isSelected = isSelected;
        this.v_TypeCode = v_TypeCode;
        this.v_TypeName = v_TypeName;
        this.v_No = v_No;
        this.v_SNo = v_SNo;
        this.v_Date = v_Date;
        this.refNo = refNo;
        this.refDate = refDate;
        this.acCode = acCode;
        this.acName = acName;
        this.acManualCode = acManualCode;
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.narration = narration;
        this.amount = amount;
        this.amountDrCr = amountDrCr;
        this.adjusted = adjusted;
        this.adjustedDrCr = adjustedDrCr;
        this.adjustment = adjustment;
        this.adjustmentDrCr = adjustmentDrCr;
        this.history = history;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountDrCr() {
        return amountDrCr;
    }

    public void setAmountDrCr(String amountDrCr) {
        this.amountDrCr = amountDrCr;
    }

    public String getAdjusted() {
        return adjusted;
    }

    public void setAdjusted(String adjusted) {
        this.adjusted = adjusted;
    }

    public String getAdjustedDrCr() {
        return adjustedDrCr;
    }

    public void setAdjustedDrCr(String adjustedDrCr) {
        this.adjustedDrCr = adjustedDrCr;
    }

    public String getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(String adjustment) {
        this.adjustment = adjustment;
    }

    public String getAdjustmentDrCr() {
        return adjustmentDrCr;
    }

    public void setAdjustmentDrCr(String adjustmentDrCr) {
        this.adjustmentDrCr = adjustmentDrCr;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public GrdAdjustment_Mdl clone() {
        return new GrdAdjustment_Mdl(this);
    }

}
