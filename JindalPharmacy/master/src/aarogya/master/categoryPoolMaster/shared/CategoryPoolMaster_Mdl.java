package aarogya.master.categoryPoolMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class CategoryPoolMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String categoryTag;
    private String categoryText;
    private String serviceCode;
    private String serviceName;
    private String serviceCatCode;
    private String serviceCatName;

    private String moduleName;

    private String headerConTypeCode;
    private String headerConTypeName;

    private String headerConAmount;

    private String headerRefTypeCode;
    private String headerRefTypeName;

    private String headerRefAmount;

    private String lineConTypeCode;
    private String lineConTypeName;

    private String lineConAmount;

    private String lineRefTypeCode;
    private String lineRefTypeName;

    private String lineRefAmount;

    private String groupCode;
    private String groupName;

    private String orgCode;
    private String orgName;

    public CategoryPoolMaster_Mdl() {
    }

    public String getCategoryTag() {
        return categoryTag;
    }

    public void setCategoryTag(String categoryTag) {
        this.categoryTag = categoryTag;
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

    public String getHeaderConTypeCode() {
        return headerConTypeCode;
    }

    public void setHeaderConTypeCode(String headerConTypeCode) {
        this.headerConTypeCode = headerConTypeCode;
    }

    public String getHeaderConTypeName() {
        return headerConTypeName;
    }

    public void setHeaderConTypeName(String headerConTypeName) {
        this.headerConTypeName = headerConTypeName;
    }

    public String getHeaderConAmount() {
        return headerConAmount;
    }

    public void setHeaderConAmount(String headerConAmount) {
        this.headerConAmount = headerConAmount;
    }

    public String getHeaderRefTypeCode() {
        return headerRefTypeCode;
    }

    public void setHeaderRefTypeCode(String headerRefTypeCode) {
        this.headerRefTypeCode = headerRefTypeCode;
    }

    public String getHeaderRefTypeName() {
        return headerRefTypeName;
    }

    public void setHeaderRefTypeName(String headerRefTypeName) {
        this.headerRefTypeName = headerRefTypeName;
    }

    public String getHeaderRefAmount() {
        return headerRefAmount;
    }

    public void setHeaderRefAmount(String headerRefAmount) {
        this.headerRefAmount = headerRefAmount;
    }

    public String getLineConTypeCode() {
        return lineConTypeCode;
    }

    public void setLineConTypeCode(String lineConTypeCode) {
        this.lineConTypeCode = lineConTypeCode;
    }

    public String getLineConTypeName() {
        return lineConTypeName;
    }

    public void setLineConTypeName(String lineConTypeName) {
        this.lineConTypeName = lineConTypeName;
    }

    public String getLineConAmount() {
        return lineConAmount;
    }

    public void setLineConAmount(String lineConAmount) {
        this.lineConAmount = lineConAmount;
    }

    public String getLineRefTypeCode() {
        return lineRefTypeCode;
    }

    public void setLineRefTypeCode(String lineRefTypeCode) {
        this.lineRefTypeCode = lineRefTypeCode;
    }

    public String getLineRefTypeName() {
        return lineRefTypeName;
    }

    public void setLineRefTypeName(String lineRefTypeName) {
        this.lineRefTypeName = lineRefTypeName;
    }

    public String getLineRefAmount() {
        return lineRefAmount;
    }

    public void setLineRefAmount(String lineRefAmount) {
        this.lineRefAmount = lineRefAmount;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCatCode() {
        return serviceCatCode;
    }

    public void setServiceCatCode(String serviceCatCode) {
        this.serviceCatCode = serviceCatCode;
    }

    public String getServiceCatName() {
        return serviceCatName;
    }

    public void setServiceCatName(String serviceCatName) {
        this.serviceCatName = serviceCatName;
    }

}
