package aarogya.nabh.roomHelp.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.MappingNature;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.dmGUI.shared.DmGUIRow_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rd_Mdl extends KeyField implements Serializable, IsSerializable {

    private String regDocId;
    private String v_Date;
    private String v_Time;
    private String guiCode;
    private String guiName;
    private String voucherTypeTag;
    private String voucherTypeText;
    private String isCancelled;
    private String IsDataExist;
    private String reportNamePrefix;
    private String templateCode;
    private ModifyReason_Mdl modifyReason = new ModifyReason_Mdl();
    private RecId recId = new RecId();
    private DmGUI_Mdl gui_Mdl = new DmGUI_Mdl();
    private MappingNature mappingNature;

    public Rd_Mdl() {
    }

    public String getRegDocId() {
        return regDocId;
    }

    public void setRegDocId(String regDocId) {
        this.regDocId = regDocId;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getV_Time() {
        return v_Time;
    }

    public void setV_Time(String v_Time) {
        this.v_Time = v_Time;
    }

    public String getGuiCode() {
        return guiCode;
    }

    public void setGuiCode(String guiCode) {
        this.guiCode = guiCode;
    }

    public String getVoucherTypeTag() {
        return voucherTypeTag;
    }

    public void setVoucherTypeTag(String voucherTypeTag) {
        this.voucherTypeTag = voucherTypeTag;
    }

    public String getVoucherTypeText() {
        return voucherTypeText;
    }

    public void setVoucherTypeText(String voucherTypeText) {
        this.voucherTypeText = voucherTypeText;
    }

    public String getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(String isCancelled) {
        this.isCancelled = isCancelled;
    }

    public ModifyReason_Mdl getModifyReason() {
        return modifyReason;
    }

    public void setModifyReason(ModifyReason_Mdl modifyReason) {
        this.modifyReason = modifyReason;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public DmGUI_Mdl getGui_Mdl() {
        return gui_Mdl;
    }

    public void setGui_Mdl(DmGUI_Mdl gui_Mdl) {
        this.gui_Mdl = gui_Mdl;
    }

    public String getIsDataExist() {
        return IsDataExist;
    }

    public void setIsDataExist(String IsDataExist) {
        this.IsDataExist = IsDataExist;
    }

    public String getReportNamePrefix() {
        return reportNamePrefix;
    }

    public void setReportNamePrefix(String reportNamePrefix) {
        this.reportNamePrefix = reportNamePrefix;
    }

    public String getGuiName() {
        return guiName;
    }

    public void setGuiName(String guiName) {
        this.guiName = guiName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public MappingNature getMappingNature() {
        return mappingNature;
    }

    public void setMappingNature(MappingNature mappingNature) {
        this.mappingNature = mappingNature;
    }

}
