package aarogya.pharmacy.medicineCategoryMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String allowBifurcation;
    private String isCommon;

    public Find_Mdl() {
    }

    public String getAllowBifurcation() {
        return allowBifurcation;
    }

    public void setAllowBifurcation(String allowBifurcation) {
        this.allowBifurcation = allowBifurcation;
    }

    public String getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }

}
