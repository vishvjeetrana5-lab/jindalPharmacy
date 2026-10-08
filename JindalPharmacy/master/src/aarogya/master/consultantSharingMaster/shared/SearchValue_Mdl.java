package aarogya.master.consultantSharingMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SearchValue_Mdl implements Serializable, IsSerializable {

    private String consultantCode;
    private String dtApplicable;
    private String serviceCode;
    private String roomCategoryCode;
    private String organizationCode;
    private String moduleName;
    private String serviceCategoryCode;
    private String serviceGroupCode;

    public SearchValue_Mdl() {
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getDtApplicable() {
        return dtApplicable;
    }

    public void setDtApplicable(String dtApplicable) {
        this.dtApplicable = dtApplicable;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getRoomCategoryCode() {
        return roomCategoryCode;
    }

    public void setRoomCategoryCode(String roomCategoryCode) {
        this.roomCategoryCode = roomCategoryCode;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getServiceCategoryCode() {
        return serviceCategoryCode;
    }

    public void setServiceCategoryCode(String serviceCategoryCode) {
        this.serviceCategoryCode = serviceCategoryCode;
    }

    public String getServiceGroupCode() {
        return serviceGroupCode;
    }

    public void setServiceGroupCode(String serviceGroupCode) {
        this.serviceGroupCode = serviceGroupCode;
    }

}
