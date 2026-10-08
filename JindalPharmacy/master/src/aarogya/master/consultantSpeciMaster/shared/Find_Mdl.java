package aarogya.master.consultantSpeciMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String natureName;
    private String prescritionTemplateName;
    private String presTemplateInitialAssesmentName;

    public Find_Mdl() {
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public String getPrescritionTemplateName() {
        return prescritionTemplateName;
    }

    public void setPrescritionTemplateName(String prescritionTemplateName) {
        this.prescritionTemplateName = prescritionTemplateName;
    }

    public String getPresTemplateInitialAssesmentName() {
        return presTemplateInitialAssesmentName;
    }

    public void setPresTemplateInitialAssesmentName(String presTemplateInitialAssesmentName) {
        this.presTemplateInitialAssesmentName = presTemplateInitialAssesmentName;
    }
}
