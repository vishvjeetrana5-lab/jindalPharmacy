package aarogya.inventory.supplierMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String siteCode;
    private String voucherTag;
    private String voucherText;
    private String voucherCategory;
    private String guiTemplate;
    private String acGroupCode;
    private String acGroupName;
    private String moduleName;
    private DmFileRuleType fileRuleType = new DmFileRuleType();
    private boolean gstApplicable = false;

    public DefaultValue_Mdl() {
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getGuiTemplate() {
        return guiTemplate;
    }

    public void setGuiTemplate(String guiTemplate) {
        this.guiTemplate = guiTemplate;
    }

    public DmFileRuleType getFileRuleType() {
        return fileRuleType;
    }

    public void setFileRuleType(DmFileRuleType fileRuleType) {
        this.fileRuleType = fileRuleType;
    }

    public String getAcGroupCode() {
        return acGroupCode;
    }

    public void setAcGroupCode(String acGroupCode) {
        this.acGroupCode = acGroupCode;
    }

    public String getAcGroupName() {
        return acGroupName;
    }

    public void setAcGroupName(String acGroupName) {
        this.acGroupName = acGroupName;
    }

    public boolean isGstApplicable() {
        return gstApplicable;
    }

    public void setGstApplicable(boolean gstApplicable) {
        this.gstApplicable = gstApplicable;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
