package aarogya.inventory.itemTransfer.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String voucherTypeCode;
    private String voucherTypeName;
    private String currentDate;
    private String departmentTag;
    private String departmentText;
    private String siteCode;
    private String sharedModifiedBy;
    private boolean inventoryAccountingActive;
    private String daysToRestrictItemTransferWithoutApproval;
    private String requisitionMandatoryInTransfer;

    public DefaultValue_Mdl() {
    }

    public String getSharedModifiedBy() {
        return sharedModifiedBy;
    }

    public void setSharedModifiedBy(String sharedModifiedBy) {
        this.sharedModifiedBy = sharedModifiedBy;
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

    public String getDepartmentTag() {
        return departmentTag;
    }

    public void setDepartmentTag(String departmentTag) {
        this.departmentTag = departmentTag;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public boolean isInventoryAccountingActive() {
        return inventoryAccountingActive;
    }

    public void setInventoryAccountingActive(boolean inventoryAccountingActive) {
        this.inventoryAccountingActive = inventoryAccountingActive;
    }

    public String getDaysToRestrictItemTransferWithoutApproval() {
        return daysToRestrictItemTransferWithoutApproval;
    }

    public void setDaysToRestrictItemTransferWithoutApproval(String daysToRestrictItemTransferWithoutApproval) {
        this.daysToRestrictItemTransferWithoutApproval = daysToRestrictItemTransferWithoutApproval;
    }

    public String getRequisitionMandatoryInTransfer() {
        return requisitionMandatoryInTransfer;
    }

    public void setRequisitionMandatoryInTransfer(String requisitionMandatoryInTransfer) {
        this.requisitionMandatoryInTransfer = requisitionMandatoryInTransfer;
    }
}
