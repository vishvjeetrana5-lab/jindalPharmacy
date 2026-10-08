package aarogya.master.consultantMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdTiming_Mdl extends KeyField implements Serializable, IsSerializable {

    private String fromTime;
    private String toTime;
    private String opd;
    private String emergency;
    private String appointment;
    private String appointmentAdvance;
    private String patient;
    private String graceMin;
    private String reVisit;
    private String onlineCharge;
    private String onlinePatient;
    private String payClinic;

    
    public GrdTiming_Mdl() {
    }

    public String getOnlineCharge() {
        return onlineCharge;
    }

    public void setOnlineCharge(String onlineCharge) {
        this.onlineCharge = onlineCharge;
    }

    public String getOnlinePatient() {
        return onlinePatient;
    }

    public void setOnlinePatient(String onlinePatient) {
        this.onlinePatient = onlinePatient;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String FromTime) {
        this.fromTime = FromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String ToTime) {
        this.toTime = ToTime;
    }

    public String getOpd() {
        return opd;
    }

    public void setOpd(String opd) {
        this.opd = opd;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String Emergency) {
        this.emergency = Emergency;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String PreAppointment) {
        this.appointment = PreAppointment;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String Patient) {
        this.patient = Patient;
    }

    public String getGraceMin() {
        return graceMin;
    }

    public void setGraceMin(String GraceMin) {
        this.graceMin = GraceMin;
    }

    public String getReVisit() {
        return reVisit;
    }

    public void setReVisit(String reVisit) {
        this.reVisit = reVisit;
    }

    public String getAppointmentAdvance() {
        return appointmentAdvance;
    }

    public void setAppointmentAdvance(String appointmentAdvance) {
        this.appointmentAdvance = appointmentAdvance;
    }
    
    public String getPayClinic() {
        return payClinic;
    }

    public void setPayClinic(String payClinic) {
        this.payClinic = payClinic;
    }

}
