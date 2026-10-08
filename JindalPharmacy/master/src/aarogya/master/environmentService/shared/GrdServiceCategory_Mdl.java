package aarogya.master.environmentService.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdServiceCategory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String serviceCategoryTag, serviceCategoryText, defConsultantTag, defConsultantText, defDepartmentTag, defDepartmentText,
            linkedServiceTag, linkedServiceText;
    private List<GrdEnviroSerive_Mdl> lstEnviroService = new ArrayList<>();
    private List<GrdBloodComponent_Mdl> lstBloodComponent = new ArrayList<>();

    private boolean haveServices;

    public GrdServiceCategory_Mdl() {
    }

    public List<GrdEnviroSerive_Mdl> getLstEnviroService() {
        return lstEnviroService;
    }

    public String getServiceCategoryTag() {
        return serviceCategoryTag;
    }

    public void setServiceCategoryTag(String serviceCategoryTag) {
        this.serviceCategoryTag = serviceCategoryTag;
    }

    public String getServiceCategoryText() {
        return serviceCategoryText;
    }

    public void setServiceCategoryText(String serviceCategoryText) {
        this.serviceCategoryText = serviceCategoryText;
    }

    public String getDefConsultantTag() {
        return defConsultantTag;
    }

    public void setDefConsultantTag(String defConsultantTag) {
        this.defConsultantTag = defConsultantTag;
    }

    public String getDefConsultantText() {
        return defConsultantText;
    }

    public void setDefConsultantText(String defConsultantText) {
        this.defConsultantText = defConsultantText;
    }

    public String getDefDepartmentTag() {
        return defDepartmentTag;
    }

    public void setDefDepartmentTag(String defDepartmentTag) {
        this.defDepartmentTag = defDepartmentTag;
    }

    public String getDefDepartmentText() {
        return defDepartmentText;
    }

    public void setDefDepartmentText(String defDepartmentText) {
        this.defDepartmentText = defDepartmentText;
    }

    public String getLinkedServiceTag() {
        return linkedServiceTag;
    }

    public void setLinkedServiceTag(String linkedServiceTag) {
        this.linkedServiceTag = linkedServiceTag;
    }

    public String getLinkedServiceText() {
        return linkedServiceText;
    }

    public void setLinkedServiceText(String linkedServiceText) {
        this.linkedServiceText = linkedServiceText;
    }

    public boolean isHaveServices() {
        return haveServices;
    }

    public void setHaveServices(boolean haveServices) {
        this.haveServices = haveServices;
    }

    public List<GrdBloodComponent_Mdl> getLstBloodComponent() {
        return lstBloodComponent;
    }

    public void setLstBloodComponent(List<GrdBloodComponent_Mdl> lstBloodComponent) {
        this.lstBloodComponent = lstBloodComponent;
    }

}
