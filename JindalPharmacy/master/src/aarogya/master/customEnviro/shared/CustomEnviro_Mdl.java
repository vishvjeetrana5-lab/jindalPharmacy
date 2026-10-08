package aarogya.master.customEnviro.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class CustomEnviro_Mdl extends KeyField implements Serializable, IsSerializable {

    private String unApproveUptoDays;
    private String editUptoDays;
    private String isPbillMrpLessthenPoTag;
    private String isPbillMrpLessthenPoText;

    public CustomEnviro_Mdl() {
    }

    public String getUnApproveUptoDays() {
        return unApproveUptoDays;
    }

    public void setUnApproveUptoDays(String unApproveUptoDays) {
        this.unApproveUptoDays = unApproveUptoDays;
    }

    public String getEditUptoDays() {
        return editUptoDays;
    }

    public void setEditUptoDays(String editUptoDays) {
        this.editUptoDays = editUptoDays;
    }

    public String getIsPbillMrpLessthenPoTag() {
        return isPbillMrpLessthenPoTag;
    }

    public void setIsPbillMrpLessthenPoTag(String isPbillMrpLessthenPoTag) {
        this.isPbillMrpLessthenPoTag = isPbillMrpLessthenPoTag;
    }

    public String getIsPbillMrpLessthenPoText() {
        return isPbillMrpLessthenPoText;
    }

    public void setIsPbillMrpLessthenPoText(String isPbillMrpLessthenPoText) {
        this.isPbillMrpLessthenPoText = isPbillMrpLessthenPoText;
    }

}
