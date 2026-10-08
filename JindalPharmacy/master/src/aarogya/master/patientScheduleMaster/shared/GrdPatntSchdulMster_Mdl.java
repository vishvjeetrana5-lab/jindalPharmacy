package aarogya.master.patientScheduleMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdPatntSchdulMster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String procedures,
            cycleTime,
            days,
            printSNo,
            consultantCode,
            consultant,
            machineCode,
            machine,
            guiTemplateCode,
            guiTemplate;

    public GrdPatntSchdulMster_Mdl() {
    }

    public String getProcedures() {
        return procedures;
    }

    public void setProcedures(String procedures) {
        this.procedures = procedures;
    }

    public String getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(String cycleTime) {
        this.cycleTime = cycleTime;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getPrintSNo() {
        return printSNo;
    }

    public void setPrintSNo(String printSNo) {
        this.printSNo = printSNo;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getConsultant() {
        return consultant;
    }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
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

}
