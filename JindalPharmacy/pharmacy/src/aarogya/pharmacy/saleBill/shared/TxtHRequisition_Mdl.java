package aarogya.pharmacy.saleBill.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHRequisition_Mdl extends KeyField implements Serializable, IsSerializable {

    private String departmentName, departmentCode;
    private String departmentToName, departmentToCode;
    private String requisitionBy, requisitionDate;
    private String docIdRegistration, recIdRegistration;
    private String patientName, roomNo;

    public TxtHRequisition_Mdl() {
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRequisitionBy() {
        return requisitionBy;
    }

    public void setRequisitionBy(String requisitionBy) {
        this.requisitionBy = requisitionBy;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(String requisitionDate) {
        this.requisitionDate = requisitionDate;
    }

    public String getDocIdRegistration() {
        return docIdRegistration;
    }

    public void setDocIdRegistration(String docIdRegistration) {
        this.docIdRegistration = docIdRegistration;
    }

    public String getRecIdRegistration() {
        return recIdRegistration;
    }

    public void setRecIdRegistration(String recIdRegistration) {
        this.recIdRegistration = recIdRegistration;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getDepartmentToName() {
        return departmentToName;
    }

    public void setDepartmentToName(String departmentToName) {
        this.departmentToName = departmentToName;
    }

    public String getDepartmentToCode() {
        return departmentToCode;
    }

    public void setDepartmentToCode(String departmentToCode) {
        this.departmentToCode = departmentToCode;
    }
}
