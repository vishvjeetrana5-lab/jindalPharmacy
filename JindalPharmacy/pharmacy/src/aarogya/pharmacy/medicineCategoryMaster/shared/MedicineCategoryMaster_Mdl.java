package aarogya.pharmacy.medicineCategoryMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class MedicineCategoryMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vatTag;
    private String vatText;
    private String allowBifurcationTag;
    private String allowBifurcationText;
    private String isCommonTag;
    private String isCommonText;
    private String saleRateProffitMargine;
    private String isMarkupApplicable;

    public MedicineCategoryMaster_Mdl() {
    }

    public String getIsCommonTag() {
        return isCommonTag;
    }

    public void setIsCommonTag(String isCommonTag) {
        this.isCommonTag = isCommonTag;
    }

    public String getIsCommonText() {
        return isCommonText;
    }

    public void setIsCommonText(String isCommonText) {
        this.isCommonText = isCommonText;
    }

    public String getVatTag() {
        return vatTag;
    }

    public void setVatTag(String vatTag) {
        this.vatTag = vatTag;
    }

    public String getVatText() {
        return vatText;
    }

    public void setVatText(String vatText) {
        this.vatText = vatText;
    }

    public String getAllowBifurcationTag() {
        return allowBifurcationTag;
    }

    public void setAllowBifurcationTag(String allowBifurcationTag) {
        this.allowBifurcationTag = allowBifurcationTag;
    }

    public String getAllowBifurcationText() {
        return allowBifurcationText;
    }

    public void setAllowBifurcationText(String allowBifurcationText) {
        this.allowBifurcationText = allowBifurcationText;
    }

    public String getSaleRateProffitMargine() {
        return saleRateProffitMargine;
    }

    public void setSaleRateProffitMargine(String saleRateProffitMargine) {
        this.saleRateProffitMargine = saleRateProffitMargine;
    }

    public String getIsMarkupApplicable() {
        return isMarkupApplicable;
    }

    public void setIsMarkupApplicable(String isMarkupApplicable) {
        this.isMarkupApplicable = isMarkupApplicable;
    }

 

}
