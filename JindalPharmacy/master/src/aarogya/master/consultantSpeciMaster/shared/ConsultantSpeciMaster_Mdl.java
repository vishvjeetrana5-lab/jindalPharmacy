package aarogya.master.consultantSpeciMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ConsultantSpeciMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String natureCode;
    private String natureName;
    private String prescritionTemplateCode;
    private String prescritionTemplateName;
    private String presTemplateInitialAssesmentCode;
    private String presTemplateInitialAssesmentName;

    public ConsultantSpeciMaster_Mdl() {
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public String getPrescritionTemplateCode() {
        return prescritionTemplateCode;
    }

    public void setPrescritionTemplateCode(String prescritionTemplateCode) {
        this.prescritionTemplateCode = prescritionTemplateCode;
    }

    public String getPrescritionTemplateName() {
        return prescritionTemplateName;
    }

    public void setPrescritionTemplateName(String prescritionTemplateName) {
        this.prescritionTemplateName = prescritionTemplateName;
    }

    public String getPresTemplateInitialAssesmentCode() {
        return presTemplateInitialAssesmentCode;
    }

    public void setPresTemplateInitialAssesmentCode(String presTemplateInitialAssesmentCode) {
        this.presTemplateInitialAssesmentCode = presTemplateInitialAssesmentCode;
    }

    public String getPresTemplateInitialAssesmentName() {
        return presTemplateInitialAssesmentName;
    }

    public void setPresTemplateInitialAssesmentName(String presTemplateInitialAssesmentName) {
        this.presTemplateInitialAssesmentName = presTemplateInitialAssesmentName;
    }
}
