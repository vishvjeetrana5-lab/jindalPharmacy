package aarogya.master.environmentSettingCompany.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentSettingCompany_Mdl extends KeyField implements Serializable, IsSerializable {

    private String acGroupConsCode;
    private String acGroupConsName;
    private String acGroupDepartCode;
    private String acGroupDepartName;
    private String acGroupOrgCode;
    private String acGroupOrgName;
    private String acGroupRefCode;
    private String acGroupRefName;
    private String acGroupEmpCode;
    private String acGroupEmpName;
    private String acGroupPharmacySupplierCode;
    private String acGroupPharmacySupplierName;
    private String acGroupInventorySupplierCode;
    private String acGroupInventorySupplierName;
    private String acGroupManufacturerCode;
    private String acGroupManufacturerName;
    private String acGroupDivisionCode;
    private String acGroupDivisionName;
    private String acGroupCustomerCode;
    private String acGroupCustomerName;
    private String inventoryAccountingActive;
    
    private List<GrdPatientInfo_Mdl> grdPatientInfoList = new ArrayList<GrdPatientInfo_Mdl>();
    private List<GrdPriority_Mdl> grdPriorityList = new ArrayList<GrdPriority_Mdl>();
    private List<GrdPatientClass_Mdl> grdPatientClassList = new ArrayList<GrdPatientClass_Mdl>();

    public EnvironmentSettingCompany_Mdl() {
    }

    public List<GrdPatientClass_Mdl> getGrdPatientClassList() {
        return grdPatientClassList;
    }

    public void setGrdPatientClassList(List<GrdPatientClass_Mdl> grdPatientClassList) {
        this.grdPatientClassList = grdPatientClassList;
    }

    public String getAcGroupCustomerCode() {
        return acGroupCustomerCode;
    }

    public void setAcGroupCustomerCode(String acGroupCustomerCode) {
        this.acGroupCustomerCode = acGroupCustomerCode;
    }

    public String getAcGroupCustomerName() {
        return acGroupCustomerName;
    }

    public void setAcGroupCustomerName(String acGroupCustomerName) {
        this.acGroupCustomerName = acGroupCustomerName;
    }

    public String getAcGroupDivisionCode() {
        return acGroupDivisionCode;
    }

    public void setAcGroupDivisionCode(String acGroupDivisionCode) {
        this.acGroupDivisionCode = acGroupDivisionCode;
    }

    public String getAcGroupDivisionName() {
        return acGroupDivisionName;
    }

    public void setAcGroupDivisionName(String acGroupDivisionName) {
        this.acGroupDivisionName = acGroupDivisionName;
    }

    public List<GrdPatientInfo_Mdl> getGrdPatientInfoList() {
        return grdPatientInfoList;
    }

    public String getAcGroupConsCode() {
        return acGroupConsCode;
    }

    public void setAcGroupConsCode(String acGroupConsCode) {
        this.acGroupConsCode = acGroupConsCode;
    }

    public String getAcGroupConsName() {
        return acGroupConsName;
    }

    public void setAcGroupConsName(String acGroupConsName) {
        this.acGroupConsName = acGroupConsName;
    }

    public String getAcGroupDepartCode() {
        return acGroupDepartCode;
    }

    public void setAcGroupDepartCode(String acGroupDepartCode) {
        this.acGroupDepartCode = acGroupDepartCode;
    }

    public String getAcGroupDepartName() {
        return acGroupDepartName;
    }

    public void setAcGroupDepartName(String acGroupDepartName) {
        this.acGroupDepartName = acGroupDepartName;
    }

    public String getAcGroupOrgCode() {
        return acGroupOrgCode;
    }

    public void setAcGroupOrgCode(String acGroupOrgCode) {
        this.acGroupOrgCode = acGroupOrgCode;
    }

    public String getAcGroupOrgName() {
        return acGroupOrgName;
    }

    public void setAcGroupOrgName(String acGroupOrgName) {
        this.acGroupOrgName = acGroupOrgName;
    }

    public List<GrdPriority_Mdl> getGrdPriorityList() {
        return grdPriorityList;
    }

    public String getAcGroupRefCode() {
        return acGroupRefCode;
    }

    public void setAcGroupRefCode(String acGroupRefCode) {
        this.acGroupRefCode = acGroupRefCode;
    }

    public String getAcGroupRefName() {
        return acGroupRefName;
    }

    public void setAcGroupRefName(String acGroupRefName) {
        this.acGroupRefName = acGroupRefName;
    }

    public String getAcGroupEmpCode() {
        return acGroupEmpCode;
    }

    public void setAcGroupEmpCode(String acGroupEmpCode) {
        this.acGroupEmpCode = acGroupEmpCode;
    }

    public String getAcGroupEmpName() {
        return acGroupEmpName;
    }

    public void setAcGroupEmpName(String acGroupEmpName) {
        this.acGroupEmpName = acGroupEmpName;
    }

    public String getAcGroupPharmacySupplierCode() {
        return acGroupPharmacySupplierCode;
    }

    public void setAcGroupPharmacySupplierCode(String acGroupPharmacySupplierCode) {
        this.acGroupPharmacySupplierCode = acGroupPharmacySupplierCode;
    }

    public String getAcGroupPharmacySupplierName() {
        return acGroupPharmacySupplierName;
    }

    public void setAcGroupPharmacySupplierName(String acGroupPharmacySupplierName) {
        this.acGroupPharmacySupplierName = acGroupPharmacySupplierName;
    }

    public String getAcGroupManufacturerCode() {
        return acGroupManufacturerCode;
    }

    public void setAcGroupManufacturerCode(String acGroupManufacturerCode) {
        this.acGroupManufacturerCode = acGroupManufacturerCode;
    }

    public String getAcGroupManufacturerName() {
        return acGroupManufacturerName;
    }

    public void setAcGroupManufacturerName(String acGroupManufacturerName) {
        this.acGroupManufacturerName = acGroupManufacturerName;
    }

    public String getAcGroupInventorySupplierCode() {
        return acGroupInventorySupplierCode;
    }

    public void setAcGroupInventorySupplierCode(String acGroupInventorySupplierCode) {
        this.acGroupInventorySupplierCode = acGroupInventorySupplierCode;
    }

    public String getAcGroupInventorySupplierName() {
        return acGroupInventorySupplierName;
    }

    public void setAcGroupInventorySupplierName(String acGroupInventorySupplierName) {
        this.acGroupInventorySupplierName = acGroupInventorySupplierName;
    }

    public String getInventoryAccountingActive() {
        return inventoryAccountingActive;
    }

    public void setInventoryAccountingActive(String inventoryAccountingActive) {
        this.inventoryAccountingActive = inventoryAccountingActive;
    }
}
