package aarogya.selfToolkit.stStatic.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;

public class PatientTreatmentId_Mdl implements Serializable, IsSerializable {

    private String docId;
    private String voucherType;
    private String voucherDate;
    private String regDocId;
    private String departmentCode, departmentName;
    private String nursingStationCode, nursingStation;
    private String roomCode, roomNo, roomCategory;
    private RecId recId = new RecId();
    private String entryPointDocId;

    public PatientTreatmentId_Mdl() {
    }

    public String getEntryPointDocId() {
        return entryPointDocId;
    }

    public void setEntryPointDocId(String entryPointDocId) {
        this.entryPointDocId = entryPointDocId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getRegDocId() {
        return regDocId;
    }

    public void setRegDocId(String regDocId) {
        this.regDocId = regDocId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getNursingStationCode() {
        return nursingStationCode;
    }

    public void setNursingStationCode(String nursingStationCode) {
        this.nursingStationCode = nursingStationCode;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getNursingStation() {
        return nursingStation;
    }

    public void setNursingStation(String nursingStation) {
        this.nursingStation = nursingStation;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomCategory() {
        return roomCategory;
    }

    public void setRoomCategory(String roomCategory) {
        this.roomCategory = roomCategory;
    }
}
