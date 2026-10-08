package aarogya.master.managePatientSchedule.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdSchedules_Mdl extends KeyField implements Serializable, IsSerializable {

    private String scheduleDate, doneDate, tokenNo, SchdleNo, scheduleCode, scheduleName, vDate, vSno, active;
    private String remark, description;
    private String consultantCode, consultantName;
    private String machineCode, machine;

    private String guiTemplateCode, guiTemplate, guiConfigCode;

    private String roomDiaryDocId, roomDiaryVSno, bookDate;
    private String roomNo, roomCode;
    private String cycleTime, printSno;
    private String entryDateTime;
    private String fileCollection;
    private boolean doneStatus = false, changeSchedule = false;

    public GrdSchedules_Mdl() {
    }

    public String getDescription() {
        return description;
    }

    public String getvSno() {
        return vSno;
    }

    public void setvSno(String vSno) {
        this.vSno = vSno;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }

    public String getRoomDiaryDocId() {
        return roomDiaryDocId;
    }

    public void setRoomDiaryDocId(String roomDiaryDocId) {
        this.roomDiaryDocId = roomDiaryDocId;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getGuiTemplateCode() {
        return guiTemplateCode;
    }

    public void setGuiTemplateCode(String guiTemplateCode) {
        this.guiTemplateCode = guiTemplateCode;
    }

    public String getGuiTemplate() {
        return guiTemplate;
    }

    public void setGuiTemplate(String guiTemplate) {
        this.guiTemplate = guiTemplate;
    }

    public String getRoomDiaryVSno() {
        return roomDiaryVSno;
    }

    public void setRoomDiaryVSno(String roomDiaryVSno) {
        this.roomDiaryVSno = roomDiaryVSno;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(String cycleTime) {
        this.cycleTime = cycleTime;
    }

    public String getPrintSno() {
        return printSno;
    }

    public void setPrintSno(String printSno) {
        this.printSno = printSno;
    }

    public String getEntryDateTime() {
        return entryDateTime;
    }

    public void setEntryDateTime(String entryDateTime) {
        this.entryDateTime = entryDateTime;
    }

    public String getFileCollection() {
        return fileCollection;
    }

    public void setFileCollection(String fileCollection) {
        this.fileCollection = fileCollection;
    }

    public boolean isDoneStatus() {
        return doneStatus;
    }

    public void setDoneStatus(boolean doneStatus) {
        this.doneStatus = doneStatus;
    }

    public boolean isChangeSchedule() {
        return changeSchedule;
    }

    public void setChangeSchedule(boolean changeSchedule) {
        this.changeSchedule = changeSchedule;
    }

//    public DmGUI_Mdl getGuiMdl() {
//        return guiMdl;
//    }
//
//    public void setGuiMdl(DmGUI_Mdl guiMdl) {
//        this.guiMdl = guiMdl;
//    }
    public String getSchdleNo() {
        return SchdleNo;
    }

    public void setSchdleNo(String SchdleNo) {
        this.SchdleNo = SchdleNo;
    }

    public String getvDate() {
        return vDate;
    }

    public void setvDate(String vDate) {
        this.vDate = vDate;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

}
