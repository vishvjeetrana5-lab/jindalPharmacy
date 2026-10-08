package aarogya.selfToolkit.labRequisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LabRequisition_Mdl extends KeyField implements Serializable, IsSerializable {

    private DefaultValue_Mdl defaultValue;
    private RecId recIdRequisition = new RecId();

    private String userEntDtLatestLine;
    private String docIdRegistration, docIdPrescription;
    private String VoucherTypeTag;
    private String voucherDate;
    private String departmentByTag, departmentByText;
    private String requisitionByTag, requisitionByText;
    private String departmentToTag, departmentToText;
    private String priorityTypeCode;
    // private HashMap<String, List<GrdLabRequisition_Mdl>> hmReq = new HashMap<>();
    private List<GrdLabRequisition_Mdl> lstRequisition = new ArrayList<GrdLabRequisition_Mdl>();

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

    public LabRequisition_Mdl() {
    }

    public DefaultValue_Mdl getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(DefaultValue_Mdl defaultValue) {
        this.defaultValue = defaultValue;
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

    public String getVoucherTypeTag() {
        return VoucherTypeTag;
    }

    public void setVoucherTypeTag(String VoucherTypeTag) {
        this.VoucherTypeTag = VoucherTypeTag;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getPriorityTypeCode() {
        return priorityTypeCode;
    }

    public void setPriorityTypeCode(String priorityTypeCode) {
        this.priorityTypeCode = priorityTypeCode;
    }

    public List<GrdLabRequisition_Mdl> getLstRequisition() {
        return lstRequisition;
    }

    public void setLstRequisition(List<GrdLabRequisition_Mdl> lstRequisition) {
        this.lstRequisition = lstRequisition;
    }

}
