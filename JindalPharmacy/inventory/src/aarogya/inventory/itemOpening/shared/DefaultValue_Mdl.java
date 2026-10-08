package aarogya.inventory.itemOpening.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable,IsSerializable{

    private String voucherTypeCode;
    private String voucherTypeName;
    private String departmentTag;
    private String departmentText;
    private boolean inventoryAccountingActive = false;

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

    public boolean isInventoryAccountingActive() {
        return inventoryAccountingActive;
    }

    public void setInventoryAccountingActive(boolean inventoryAccountingActive) {
        this.inventoryAccountingActive = inventoryAccountingActive;
    }
}
