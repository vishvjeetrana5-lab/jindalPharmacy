package dataman.dmBase.dmScheduler.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GrdImage_Mdl implements Serializable, IsSerializable {

    private String imageCode;
    private String imageCodeValue;
    private String ruleCode;
    private String ruleCodeValue;

    public GrdImage_Mdl() {
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public String getImageCodeValue() {
        return imageCodeValue;
    }

    public void setImageCodeValue(String imageCodeValue) {
        this.imageCodeValue = imageCodeValue;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleCodeValue() {
        return ruleCodeValue;
    }

    public void setRuleCodeValue(String ruleCodeValue) {
        this.ruleCodeValue = ruleCodeValue;
    }

}
