package aarogya.master.categoryPoolMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String serviceText;
    private String categoryText;
    private String moduleName;
    private String serviceName;
    private String headerConTypeName;
    private String headerRefTypeName;
    private String lineConTypeName;
    private String lineRefTypeName;
    private String groupName;
    private String orgName;
    private String lineReferredAmount;
    private String lineConsultantAmount;
    private String headerReferredAmount;
    private String headerConsultantAmount;

    public Find_Mdl() {
    }

    public String getServiceText() {
        return serviceText;
    }

    public void setServiceText(String serviceText) {
        this.serviceText = serviceText;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHeaderConTypeName() {
        return headerConTypeName;
    }

    public void setHeaderConTypeName(String headerConTypeName) {
        this.headerConTypeName = headerConTypeName;
    }

    public String getHeaderRefTypeName() {
        return headerRefTypeName;
    }

    public void setHeaderRefTypeName(String headerRefTypeName) {
        this.headerRefTypeName = headerRefTypeName;
    }

    public String getLineConTypeName() {
        return lineConTypeName;
    }

    public void setLineConTypeName(String lineConTypeName) {
        this.lineConTypeName = lineConTypeName;
    }

    public String getLineRefTypeName() {
        return lineRefTypeName;
    }

    public void setLineRefTypeName(String lineRefTypeName) {
        this.lineRefTypeName = lineRefTypeName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getLineReferredAmount() {
        return lineReferredAmount;
    }

    public void setLineReferredAmount(String lineReferredAmount) {
        this.lineReferredAmount = lineReferredAmount;
    }

    public String getLineConsultantAmount() {
        return lineConsultantAmount;
    }

    public void setLineConsultantAmount(String lineConsultantAmount) {
        this.lineConsultantAmount = lineConsultantAmount;
    }

    public String getHeaderReferredAmount() {
        return headerReferredAmount;
    }

    public void setHeaderReferredAmount(String headerReferredAmount) {
        this.headerReferredAmount = headerReferredAmount;
    }

    public String getHeaderConsultantAmount() {
        return headerConsultantAmount;
    }

    public void setHeaderConsultantAmount(String headerConsultantAmount) {
        this.headerConsultantAmount = headerConsultantAmount;
    }

}
