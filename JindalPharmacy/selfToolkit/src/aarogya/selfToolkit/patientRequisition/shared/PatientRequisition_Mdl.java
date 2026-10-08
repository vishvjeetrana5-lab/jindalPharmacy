package aarogya.selfToolkit.patientRequisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PatientRequisition_Mdl extends KeyField implements Serializable, IsSerializable {

    private DefaultValue_Mdl defaultValue;
    private RecId recIdRequisition = new RecId();
    private String requisitionByTag, requisitionByText;
    private String departmentByTag, departmentByText;
    private String departmentToTag, departmentToText;
    private String userEntDtLatestLine;
    private String docIdRegistration, docIdPrescription;
    private String priorityTypeCode;
    private String deptToSiteCode;

    private List<GrdRequisitionMedicine_Mdl> lstMedicine = new ArrayList<GrdRequisitionMedicine_Mdl>();

    public PatientRequisition_Mdl() {
    }

    public List<GrdRequisitionMedicine_Mdl> getLstMedicine() {
        return lstMedicine;
    }

    public DefaultValue_Mdl getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(DefaultValue_Mdl defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRequisitionByTag() {
        return requisitionByTag;
    }

    public void setRequisitionByTag(String requisitionByTag) {
        this.requisitionByTag = requisitionByTag;
    }

    public String getRequisitionByText() {
        return requisitionByText;
    }

    public void setRequisitionByText(String requisitionByText) {
        this.requisitionByText = requisitionByText;
    }

    public String getDepartmentByTag() {
        return departmentByTag;
    }

    public void setDepartmentByTag(String departmentByTag) {
        this.departmentByTag = departmentByTag;
    }

    public String getDepartmentByText() {
        return departmentByText;
    }

    public void setDepartmentByText(String departmentByText) {
        this.departmentByText = departmentByText;
    }

    public String getDepartmentToTag() {
        return departmentToTag;
    }

    public void setDepartmentToTag(String departmentToTag) {
        this.departmentToTag = departmentToTag;
    }

    public String getDepartmentToText() {
        return departmentToText;
    }

    public void setDepartmentToText(String departmentToText) {
        this.departmentToText = departmentToText;
    }

    public RecId getRecIdRequisition() {
        return recIdRequisition;
    }

    public void setRecIdRequisition(RecId recIdRequisition) {
        this.recIdRequisition = recIdRequisition;
    }

    public String getUserEntDtLatestLine() {
        return userEntDtLatestLine;
    }

    public void setUserEntDtLatestLine(String userEntDtLatestLine) {
        this.userEntDtLatestLine = userEntDtLatestLine;
    }

    public String getDocIdRegistration() {
        return docIdRegistration;
    }

    public void setDocIdRegistration(String docIdRegistration) {
        this.docIdRegistration = docIdRegistration;
    }

    public String getDocIdPrescription() {
        return docIdPrescription;
    }

    public void setDocIdPrescription(String docIdPrescription) {
        this.docIdPrescription = docIdPrescription;
    }

    public String getPriorityTypeCode() {
        return priorityTypeCode;
    }

    public void setPriorityTypeCode(String priorityTypeCode) {
        this.priorityTypeCode = priorityTypeCode;
    }

    public String getDeptToSiteCode() {
        return deptToSiteCode;
    }

    public void setDeptToSiteCode(String deptToSiteCode) {
        this.deptToSiteCode = deptToSiteCode;
    }

}
