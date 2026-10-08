package aarogya.master.additionalUserPermission.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdAdditionalUserPermission_Mdl extends KeyField implements Serializable, IsSerializable {

    private String userDescription;
    private String linkedAcName;
    private String linkedAcCode;
    private String departmentCode;
    private String departmentName;
    private String nursingStationCode;
    private String nursingStationName;
    private String allowDepartmentCode;
    private String allowDepartmentName;
    private String allowLocationCode;
    private String allowLocationName;
    private String serviceName;
    private String serviceCatName;
    private String allowDiscountPercent;
    private List<GrdService_Mdl> listServiceDetail = new ArrayList<GrdService_Mdl>();
    private List<GridPaymentMode_Mdl> listPaymentMode = new ArrayList<GridPaymentMode_Mdl>();

    private String allowDiscForNDSName, allowDiscForNDSCode;

    public GrdAdditionalUserPermission_Mdl() {
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getLinkedAcName() {
        return linkedAcName;
    }

    public void setLinkedAcName(String linkedAcName) {
        this.linkedAcName = linkedAcName;
    }

    public String getLinkedAcCode() {
        return linkedAcCode;
    }

    public void setLinkedAcCode(String linkedAcCode) {
        this.linkedAcCode = linkedAcCode;
    }

    public String getAllowDepartmentCode() {
        return allowDepartmentCode;
    }

    public void setAllowDepartmentCode(String allowDepartmentCode) {
        this.allowDepartmentCode = allowDepartmentCode;
    }

    public String getAllowDepartmentName() {
        return allowDepartmentName;
    }

    public void setAllowDepartmentName(String allowDepartmentName) {
        this.allowDepartmentName = allowDepartmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getNursingStationCode() {
        return nursingStationCode;
    }

    public void setNursingStationCode(String nursingStationCode) {
        this.nursingStationCode = nursingStationCode;
    }

    public String getNursingStationName() {
        return nursingStationName;
    }

    public void setNursingStationName(String nursingStationName) {
        this.nursingStationName = nursingStationName;
    }

    public List<GrdService_Mdl> getListServiceDetail() {
        return listServiceDetail;
    }

    public List<GridPaymentMode_Mdl> getListPaymentMode() {
        return listPaymentMode;
    }

    public void setListPaymentMode(List<GridPaymentMode_Mdl> listPaymentMode) {
        this.listPaymentMode = listPaymentMode;
    }

    public void setListServiceDetail(List<GrdService_Mdl> listServiceDetail) {
        this.listServiceDetail = listServiceDetail;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCatName() {
        return serviceCatName;
    }

    public void setServiceCatName(String serviceCatName) {
        this.serviceCatName = serviceCatName;
    }

    public String getAllowDiscountPercent() {
        return allowDiscountPercent;
    }

    public void setAllowDiscountPercent(String allowDiscountPercent) {
        this.allowDiscountPercent = allowDiscountPercent;
    }

    public String getAllowDiscForNDSName() {
        return allowDiscForNDSName;
    }

    public void setAllowDiscForNDSName(String allowDiscForNDSName) {
        this.allowDiscForNDSName = allowDiscForNDSName;
    }

    public String getAllowDiscForNDSCode() {
        return allowDiscForNDSCode;
    }

    public void setAllowDiscForNDSCode(String allowDiscForNDSCode) {
        this.allowDiscForNDSCode = allowDiscForNDSCode;
    }

    public String getAllowLocationCode() {
        return allowLocationCode;
    }

    public void setAllowLocationCode(String allowLocationCode) {
        this.allowLocationCode = allowLocationCode;
    }

    public String getAllowLocationName() {
        return allowLocationName;
    }

    public void setAllowLocationName(String allowLocationName) {
        this.allowLocationName = allowLocationName;
    }

}
