package aarogya.mis.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.sql.Date;

public class ConsultantSharingFilter_Mdl implements Serializable, IsSerializable {

    private Long organizationCode;
    private String moduleName;
    private Date applicableFrom, applicableTo;
    private Integer roomCategoryCode, serviceCategoryCode, serviceGroupCode, serviceCode, headerConsultantIsAmount, headerReferredIsAmount, lineConsultantIsAmount, lineReferredIsAmount;
    private Double headerConsultantAmount, headerReferredAmount, lineConsultantAmount, lineReferredAmount;

    public ConsultantSharingFilter_Mdl() {
    }

    public Long getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(Long organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Date getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(Date applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public Date getApplicableTo() {
        return applicableTo;
    }

    public void setApplicableTo(Date applicableTo) {
        this.applicableTo = applicableTo;
    }

    public Integer getServiceCategoryCode() {
        return serviceCategoryCode;
    }

    public void setServiceCategoryCode(Integer serviceCategoryCode) {
        this.serviceCategoryCode = serviceCategoryCode;
    }

    public Integer getServiceGroupCode() {
        return serviceGroupCode;
    }

    public void setServiceGroupCode(Integer serviceGroupCode) {
        this.serviceGroupCode = serviceGroupCode;
    }

    public Integer getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(Integer serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Integer getRoomCategoryCode() {
        return roomCategoryCode;
    }

    public void setRoomCategoryCode(Integer roomCategoryCode) {
        this.roomCategoryCode = roomCategoryCode;
    }

    
    public Integer getHeaderConsultantIsAmount() {
        return headerConsultantIsAmount;
    }

    public void setHeaderConsultantIsAmount(Integer headerConsultantIsAmount) {
        this.headerConsultantIsAmount = headerConsultantIsAmount;
    }

    public Integer getHeaderReferredIsAmount() {
        return headerReferredIsAmount;
    }

    public void setHeaderReferredIsAmount(Integer headerReferredIsAmount) {
        this.headerReferredIsAmount = headerReferredIsAmount;
    }

    public Integer getLineConsultantIsAmount() {
        return lineConsultantIsAmount;
    }

    public void setLineConsultantIsAmount(Integer lineConsultantIsAmount) {
        this.lineConsultantIsAmount = lineConsultantIsAmount;
    }

    public Integer getLineReferredIsAmount() {
        return lineReferredIsAmount;
    }

    public void setLineReferredIsAmount(Integer lineReferredIsAmount) {
        this.lineReferredIsAmount = lineReferredIsAmount;
    }

    public Double getHeaderConsultantAmount() {
        return headerConsultantAmount;
    }

    public void setHeaderConsultantAmount(Double headerConsultantAmount) {
        this.headerConsultantAmount = headerConsultantAmount;
    }

    public Double getHeaderReferredAmount() {
        return headerReferredAmount;
    }

    public void setHeaderReferredAmount(Double headerReferredAmount) {
        this.headerReferredAmount = headerReferredAmount;
    }

    public Double getLineConsultantAmount() {
        return lineConsultantAmount;
    }

    public void setLineConsultantAmount(Double lineConsultantAmount) {
        this.lineConsultantAmount = lineConsultantAmount;
    }

    public Double getLineReferredAmount() {
        return lineReferredAmount;
    }

    public void setLineReferredAmount(Double lineReferredAmount) {
        this.lineReferredAmount = lineReferredAmount;
    }
    
     public ConsultantSharingFilter_Mdl clone() {
        ConsultantSharingFilter_Mdl local = new ConsultantSharingFilter_Mdl();

        local.setApplicableFrom(this.getApplicableFrom());
        local.setApplicableTo(this.getApplicableTo());
        local.setHeaderConsultantAmount(this.getHeaderConsultantAmount());
        local.setHeaderConsultantIsAmount(this.getHeaderConsultantIsAmount());
        local.setHeaderReferredAmount(this.getHeaderReferredAmount());
        local.setHeaderReferredIsAmount(this.getHeaderReferredIsAmount());
        local.setLineConsultantAmount(this.getLineConsultantAmount());
        local.setLineConsultantIsAmount(this.getLineConsultantIsAmount());
        local.setLineReferredAmount(this.getLineReferredAmount());
        local.setLineReferredIsAmount(this.getLineReferredIsAmount());
        local.setModuleName(this.getModuleName());
        local.setOrganizationCode(this.getOrganizationCode());
        local.setRoomCategoryCode(this.getRoomCategoryCode());
        local.setServiceCategoryCode(this.getServiceCategoryCode());
        local.setServiceCode(this.getServiceCode());
        local.setServiceGroupCode(this.getServiceGroupCode());

        return local;
    }

}
