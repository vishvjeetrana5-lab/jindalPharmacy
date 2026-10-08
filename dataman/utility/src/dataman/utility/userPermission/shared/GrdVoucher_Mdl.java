package dataman.utility.userPermission.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdVoucher_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String category;
    private String viewPrevious;
    private String savePrevious;
    private String deletePrevious;
    private String allowValueUpto;
    private String auditAllowCode;
    private String auditAllowName;

    public GrdVoucher_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getViewPrevious() {
        return viewPrevious;
    }

    public void setViewPrevious(String viewPrevious) {
        this.viewPrevious = viewPrevious;
    }

    public String getSavePrevious() {
        return savePrevious;
    }

    public void setSavePrevious(String savePrevious) {
        this.savePrevious = savePrevious;
    }

    public String getDeletePrevious() {
        return deletePrevious;
    }

    public void setDeletePrevious(String deletePrevious) {
        this.deletePrevious = deletePrevious;
    }

    public String getAllowValueUpto() {
        return allowValueUpto;
    }

    public void setAllowValueUpto(String allowValueUpto) {
        this.allowValueUpto = allowValueUpto;
    }

    public String getAuditAllowCode() {
        return auditAllowCode;
    }

    public void setAuditAllowCode(String auditAllowCode) {
        this.auditAllowCode = auditAllowCode;
    }

    public String getAuditAllowName() {
        return auditAllowName;
    }

    public void setAuditAllowName(String auditAllowName) {
        this.auditAllowName = auditAllowName;
    }
}
