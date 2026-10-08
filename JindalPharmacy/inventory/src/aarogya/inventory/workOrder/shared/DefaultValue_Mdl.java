package aarogya.inventory.workOrder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String voucherTypeCode;
    private String voucherTypeName;
    private String currentDate;
    private String guiTemplateCode;

    //New Fields
    private boolean isContinuousNewMode = false;
    private String newVoucherTypeCode;
    private String newVoucherTypeName;
    private String newGuiTemplateCode;
    private String newVoucherDate;

    public String getGuiTemplateCode() {
        return guiTemplateCode;
    }

    public void setGuiTemplateCode(String guiTemplateCode) {
        this.guiTemplateCode = guiTemplateCode;
    }

    public DefaultValue_Mdl() {
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public boolean getIsContinuousNewMode() {
        return isContinuousNewMode;
    }

    public void setIsContinuousNewMode(boolean isContinuousNewMode) {
        this.isContinuousNewMode = isContinuousNewMode;
    }

    public String getNewVoucherTypeCode() {
        return newVoucherTypeCode;
    }

    public void setNewVoucherTypeCode(String newVoucherTypeCode) {
        this.newVoucherTypeCode = newVoucherTypeCode;
    }

    public String getNewVoucherTypeName() {
        return newVoucherTypeName;
    }

    public void setNewVoucherTypeName(String newVoucherTypeName) {
        this.newVoucherTypeName = newVoucherTypeName;
    }

    public String getNewGuiTemplateCode() {
        return newGuiTemplateCode;
    }

    public void setNewGuiTemplateCode(String newGuiTemplateCode) {
        this.newGuiTemplateCode = newGuiTemplateCode;
    }

    public String getNewVoucherDate() {
        return newVoucherDate;
    }

    public void setNewVoucherDate(String newVoucherDate) {
        this.newVoucherDate = newVoucherDate;
    }

}
