package aarogya.master.environmentSettingCompany.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class GrdPatientInfo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String nature;
    private String srNo;
    private String isVisibleTag;
    private String isVisibleText;
    private String isMandatoryTag;
    private String isMandatoryText;

    public GrdPatientInfo_Mdl() {
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getIsVisibleTag() {
        return isVisibleTag;
    }

    public void setIsVisibleTag(String isVisibleTag) {
        this.isVisibleTag = isVisibleTag;
    }

    public String getIsVisibleText() {
        return isVisibleText;
    }

    public void setIsVisibleText(String isVisibleText) {
        this.isVisibleText = isVisibleText;
    }

    public String getIsMandatoryTag() {
        return isMandatoryTag;
    }

    public void setIsMandatoryTag(String isMandatoryTag) {
        this.isMandatoryTag = isMandatoryTag;
    }

    public String getIsMandatoryText() {
        return isMandatoryText;
    }

    public void setIsMandatoryText(String isMandatoryText) {
        this.isMandatoryText = isMandatoryText;
    }

}
