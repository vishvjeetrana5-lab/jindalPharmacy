package aarogya.nabh.roomHelp.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdRoomHelp_Mdl extends KeyField implements Serializable, IsSerializable {

    private String date;
    private String time;
    private String templateCode;
    private String templateName;
    private String lastModifiedBy;
    private String guiName;
    private String guiCode;
    private String u_Name;
    private String preparedBy;
    private String isCancelled;
    private String colorStatus;
    private String voucherTag;
    private String voucherText;
    private String refRecId;
    private String natureCode;
    private String natureName;

    public GrdRoomHelp_Mdl() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getGuiName() {
        return guiName;
    }

    public void setGuiName(String guiName) {
        this.guiName = guiName;
    }

    public String getGuiCode() {
        return guiCode;
    }

    public void setGuiCode(String guiCode) {
        this.guiCode = guiCode;
    }

    public String getU_Name() {
        return u_Name;
    }

    public void setU_Name(String u_Name) {
        this.u_Name = u_Name;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(String isCancelled) {
        this.isCancelled = isCancelled;
    }

    public String getColorStatus() {
        return colorStatus;
    }

    public void setColorStatus(String colorStatus) {
        this.colorStatus = colorStatus;
    }

    public String getVoucherTag() {
        return voucherTag;
    }

    public void setVoucherTag(String voucherTag) {
        this.voucherTag = voucherTag;
    }

    public String getVoucherText() {
        return voucherText;
    }

    public void setVoucherText(String voucherText) {
        this.voucherText = voucherText;
    }

    public String getRefRecId() {
        return refRecId;
    }

    public void setRefRecId(String refRecId) {
        this.refRecId = refRecId;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }
}
