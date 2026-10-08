package aarogya.pharmacy.materialCategoryMast.shared;

import aarogya.pharmacy.materialCategoryMast.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class MaterialCategoryMast_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vatTag;
    private String vatText;
    private String isCommonTag;
    private String isCommonText;

    public MaterialCategoryMast_Mdl() {
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
}
