package aarogya.pharmacy.medicineModification.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdComposition_Mdl extends KeyField implements Serializable, IsSerializable {

    public GrdComposition_Mdl() {
    }

    private String power;
    private String v_SNo;
    private String baseText;
    private String baseTag;
    private String unitText;
    private String unitTag;
    private String restrictGroupCode;
    private String restrictGroupName;

    public String getRestrictGroupCode() {
        return restrictGroupCode;
    }

    public void setRestrictGroupCode(String restrictGroupCode) {
        this.restrictGroupCode = restrictGroupCode;
    }

    public String getRestrictGroupName() {
        return restrictGroupName;
    }

    public void setRestrictGroupName(String restrictGroupName) {
        this.restrictGroupName = restrictGroupName;
    }

    public String getUnitText() {
        return unitText;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
    }

    public String getUnitTag() {
        return unitTag;
    }

    public void setUnitTag(String unitTag) {
        this.unitTag = unitTag;
    }

    public String getBaseText() {
        return baseText;
    }

    public void setBaseText(String baseText) {
        this.baseText = baseText;
    }

    public String getBaseTag() {
        return baseTag;
    }

    public void setBaseTag(String baseTag) {
        this.baseTag = baseTag;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

}
