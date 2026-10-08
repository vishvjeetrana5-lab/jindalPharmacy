package dataman.accounts.budgetDisbursement.shared;

import dataman.accounts.budgetDisbursement.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String allocationTypeTag, allocationTypeText,voucherCategory;
    private String currentDate;
    private String currentTime;

    public DefaultValue_Mdl() {
    }

    public String getAllocationTypeTag() {
        return allocationTypeTag;
    }

    public void setAllocationTypeTag(String allocationTypeTag) {
        this.allocationTypeTag = allocationTypeTag;
    }

    public String getAllocationTypeText() {
        return allocationTypeText;
    }

    public void setAllocationTypeText(String allocationTypeText) {
        this.allocationTypeText = allocationTypeText;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

}
