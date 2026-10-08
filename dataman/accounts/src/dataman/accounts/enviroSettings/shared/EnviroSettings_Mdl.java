package dataman.accounts.enviroSettings.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class EnviroSettings_Mdl extends KeyField implements Serializable, IsSerializable {

    private String tdsRoundOffTag;
    private String tdsRoundOffText;
    private String gstRoundOffTag;
    private String gstRoundOffText;
    private String stockAdjustmentVTypeTag;
    private String stockAdjustmentVTypeText;
    private String copyFromSite;
    private String userEntryDateTime;
    private String voucherExcludesInTurnoverTag;
    private String voucherExcludesInTurnoverText;
    private String tdsOnTag;
    private String tdsOnText;

    public EnviroSettings_Mdl() {
    }

    public String getTdsRoundOffTag() {
        return tdsRoundOffTag;
    }

    public void setTdsRoundOffTag(String tdsRoundOffTag) {
        this.tdsRoundOffTag = tdsRoundOffTag;
    }

    public String getTdsRoundOffText() {
        return tdsRoundOffText;
    }

    public void setTdsRoundOffText(String tdsRoundOffText) {
        this.tdsRoundOffText = tdsRoundOffText;
    }

    public String getGstRoundOffTag() {
        return gstRoundOffTag;
    }

    public void setGstRoundOffTag(String gstRoundOffTag) {
        this.gstRoundOffTag = gstRoundOffTag;
    }

    public String getGstRoundOffText() {
        return gstRoundOffText;
    }

    public void setGstRoundOffText(String gstRoundOffText) {
        this.gstRoundOffText = gstRoundOffText;
    }

    public String getStockAdjustmentVTypeTag() {
        return stockAdjustmentVTypeTag;
    }

    public void setStockAdjustmentVTypeTag(String stockAdjustmentVTypeTag) {
        this.stockAdjustmentVTypeTag = stockAdjustmentVTypeTag;
    }

    public String getStockAdjustmentVTypeText() {
        return stockAdjustmentVTypeText;
    }

    public void setStockAdjustmentVTypeText(String stockAdjustmentVTypeText) {
        this.stockAdjustmentVTypeText = stockAdjustmentVTypeText;
    }

    public String getCopyFromSite() {
        return copyFromSite;
    }

    public void setCopyFromSite(String copyFromSite) {
        this.copyFromSite = copyFromSite;
    }

    public String getUserEntryDateTime() {
        return userEntryDateTime;
    }

    public void setUserEntryDateTime(String userEntryDateTime) {
        this.userEntryDateTime = userEntryDateTime;
    }

    public String getVoucherExcludesInTurnoverTag() {
        return voucherExcludesInTurnoverTag;
    }

    public void setVoucherExcludesInTurnoverTag(String voucherExcludesInTurnoverTag) {
        this.voucherExcludesInTurnoverTag = voucherExcludesInTurnoverTag;
    }

    public String getVoucherExcludesInTurnoverText() {
        return voucherExcludesInTurnoverText;
    }

    public void setVoucherExcludesInTurnoverText(String voucherExcludesInTurnoverText) {
        this.voucherExcludesInTurnoverText = voucherExcludesInTurnoverText;
    }

    public String getTdsOnTag() {
        return tdsOnTag;
    }

    public void setTdsOnTag(String tdsOnTag) {
        this.tdsOnTag = tdsOnTag;
    }

    public String getTdsOnText() {
        return tdsOnText;
    }

    public void setTdsOnText(String tdsOnText) {
        this.tdsOnText = tdsOnText;
    }
}
