package dataman.utility.voucherType.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VoucherType_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String description;
    private String moduleCode;
    private String moduleName;
    private String categoryCode;
    private String categoryName;
    private String serialNo;
    private String parentVoucherCode;
    private String parentVoucherName;
    private String menutextCode;
    private String menutextName;
    private String menuname;
    private String menuUsedInModule;
    private String acceptEntryAfter;
    private String defaultReportCode;
    private String defaultReportName;
    private String paperSize;
    private String landscapeCode;
    private String landscapeName;
    private String defaultAcCode;
    private String defaultAcName;

    private String balancingAcCode;
    private String balancingAcName;

    private String activeCode;
    private String activeName;
    private String formApplied;
    private String sysGroup;
    private String guiTemplateCode;
    private String guiTemplateName;
    private String isBankCashBookCode;
    private String isBankCashBookName;
    private String documentTypeCode;
    private String documentTypeName;
    private String documentNatureCode;
    private String documentNatureName;
    private String documentMailTemplateCode, documentMailUserCode;
    private String documentMailTemplateCodeName, documentMailUserName;
    private String documentMailReportName;
    private List<GrdACFilteration_Mdl> grdFilteration = new ArrayList<GrdACFilteration_Mdl>();
    private List<GrdFormApplied_Mdl> grdAppliedForm = new ArrayList<GrdFormApplied_Mdl>();
    private List<GrdWebJobs_Mdl> grdWebJobs = new ArrayList<GrdWebJobs_Mdl>();
    private List<GrdReceiptType_Mdl> grdReceiptType = new ArrayList<GrdReceiptType_Mdl>();
    private List<GrdReportConfig_Mdl> grdReportConfig = new ArrayList<>();
    private List<KeyFieldExtended> grdItemCategory = new ArrayList<KeyFieldExtended>();

    public VoucherType_Mdl() {
    }

    public String getBalancingAcCode() {
        return balancingAcCode;
    }

    public void setBalancingAcCode(String balancingAcCode) {
        this.balancingAcCode = balancingAcCode;
    }

    public String getBalancingAcName() {
        return balancingAcName;
    }

    public void setBalancingAcName(String balancingAcName) {
        this.balancingAcName = balancingAcName;
    }

    public List<KeyFieldExtended> getGrdItemCategory() {
        return grdItemCategory;
    }

    public void setGrdItemCategory(List<KeyFieldExtended> grdItemCategory) {
        this.grdItemCategory = grdItemCategory;
    }

    public List<GrdReceiptType_Mdl> getGrdReceiptType() {
        return grdReceiptType;
    }

    public void setGrdReceiptType(List<GrdReceiptType_Mdl> grdReceiptType) {
        this.grdReceiptType = grdReceiptType;
    }

    public String getGuiTemplateCode() {
        return guiTemplateCode;
    }

    public void setGuiTemplateCode(String guiTemplateCode) {
        this.guiTemplateCode = guiTemplateCode;
    }

    public String getGuiTemplateName() {
        return guiTemplateName;
    }

    public void setGuiTemplateName(String guiTemplateName) {
        this.guiTemplateName = guiTemplateName;
    }

    public List<GrdACFilteration_Mdl> getGrdFilteration() {
        return grdFilteration;
    }

    public List<GrdFormApplied_Mdl> getGrdAppliedForm() {
        return grdAppliedForm;
    }

    public String getFormApplied() {
        return formApplied;
    }

    public void setFormApplied(String formApplied) {
        this.formApplied = formApplied;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getParentVoucherCode() {
        return parentVoucherCode;
    }

    public void setParentVoucherCode(String parentVoucherCode) {
        this.parentVoucherCode = parentVoucherCode;
    }

    public String getParentVoucherName() {
        return parentVoucherName;
    }

    public void setParentVoucherName(String parentVoucherName) {
        this.parentVoucherName = parentVoucherName;
    }

    public String getMenutextCode() {
        return menutextCode;
    }

    public void setMenutextCode(String menutextCode) {
        this.menutextCode = menutextCode;
    }

    public String getMenutextName() {
        return menutextName;
    }

    public void setMenutextName(String menutextName) {
        this.menutextName = menutextName;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getMenuUsedInModule() {
        return menuUsedInModule;
    }

    public void setMenuUsedInModule(String menuUsedInModule) {
        this.menuUsedInModule = menuUsedInModule;
    }

    public String getAcceptEntryAfter() {
        return acceptEntryAfter;
    }

    public void setAcceptEntryAfter(String acceptEntryAfter) {
        this.acceptEntryAfter = acceptEntryAfter;
    }

    public String getDefaultReportCode() {
        return defaultReportCode;
    }

    public void setDefaultReportCode(String defaultReportCode) {
        this.defaultReportCode = defaultReportCode;
    }

    public String getDefaultReportName() {
        return defaultReportName;
    }

    public void setDefaultReportName(String defaultReportName) {
        this.defaultReportName = defaultReportName;
    }

    public String getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(String paperSize) {
        this.paperSize = paperSize;
    }

    public String getLandscapeCode() {
        return landscapeCode;
    }

    public void setLandscapeCode(String landscapeCode) {
        this.landscapeCode = landscapeCode;
    }

    public String getLandscapeName() {
        return landscapeName;
    }

    public void setLandscapeName(String landscapeName) {
        this.landscapeName = landscapeName;
    }

    public String getDefaultAcCode() {
        return defaultAcCode;
    }

    public void setDefaultAcCode(String defaultAcCode) {
        this.defaultAcCode = defaultAcCode;
    }

    public String getDefaultAcName() {
        return defaultAcName;
    }

    public void setDefaultAcName(String defaultAcName) {
        this.defaultAcName = defaultAcName;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getSysGroup() {
        return sysGroup;
    }

    public void setSysGroup(String sysGroup) {
        this.sysGroup = sysGroup;
    }

    public List<GrdWebJobs_Mdl> getGrdWebJobs() {
        return grdWebJobs;
    }

    public String getIsBankCashBookCode() {
        return isBankCashBookCode;
    }

    public void setIsBankCashBookCode(String isBankCashBookCode) {
        this.isBankCashBookCode = isBankCashBookCode;
    }

    public String getIsBankCashBookName() {
        return isBankCashBookName;
    }

    public void setIsBankCashBookName(String isBankCashBookName) {
        this.isBankCashBookName = isBankCashBookName;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    public String getDocumentNatureCode() {
        return documentNatureCode;
    }

    public void setDocumentNatureCode(String documentNatureCode) {
        this.documentNatureCode = documentNatureCode;
    }

    public String getDocumentNatureName() {
        return documentNatureName;
    }

    public void setDocumentNatureName(String documentNatureName) {
        this.documentNatureName = documentNatureName;
    }

    public List<GrdReportConfig_Mdl> getGrdReportConfig() {
        return grdReportConfig;
    }

    public String getDocumentMailTemplateCode() {
        return documentMailTemplateCode;
    }

    public void setDocumentMailTemplateCode(String documentMailTemplateCode) {
        this.documentMailTemplateCode = documentMailTemplateCode;
    }

    public String getDocumentMailUserCode() {
        return documentMailUserCode;
    }

    public void setDocumentMailUserCode(String documentMailUserCode) {
        this.documentMailUserCode = documentMailUserCode;
    }

    public String getDocumentMailTemplateCodeName() {
        return documentMailTemplateCodeName;
    }

    public void setDocumentMailTemplateCodeName(String documentMailTemplateCodeName) {
        this.documentMailTemplateCodeName = documentMailTemplateCodeName;
    }

    public String getDocumentMailUserName() {
        return documentMailUserName;
    }

    public void setDocumentMailUserName(String documentMailUserName) {
        this.documentMailUserName = documentMailUserName;
    }

    public String getDocumentMailReportName() {
        return documentMailReportName;
    }

    public void setDocumentMailReportName(String documentMailReportName) {
        this.documentMailReportName = documentMailReportName;
    }

}
