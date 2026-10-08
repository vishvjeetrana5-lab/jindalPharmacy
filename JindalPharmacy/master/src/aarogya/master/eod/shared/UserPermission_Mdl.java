package aarogya.master.eod.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class UserPermission_Mdl implements Serializable, IsSerializable {

    private boolean roomPosting, labConsumption, opdAcPosting, ipdAcPosting, dayCareAcPosting, diagnosticAcPosting,
            emergencyAcPosting, pharmacyAcPosting, misAppPush;

    public UserPermission_Mdl() {
    }

    public boolean isRoomPosting() {
        return roomPosting;
    }

    public void setRoomPosting(boolean roomPosting) {
        this.roomPosting = roomPosting;
    }

    public boolean isLabConsumption() {
        return labConsumption;
    }

    public void setLabConsumption(boolean labConsumption) {
        this.labConsumption = labConsumption;
    }

    public boolean isOpdAcPosting() {
        return opdAcPosting;
    }

    public void setOpdAcPosting(boolean opdAcPosting) {
        this.opdAcPosting = opdAcPosting;
    }

    public boolean isIpdAcPosting() {
        return ipdAcPosting;
    }

    public void setIpdAcPosting(boolean ipdAcPosting) {
        this.ipdAcPosting = ipdAcPosting;
    }

    public boolean isDayCareAcPosting() {
        return dayCareAcPosting;
    }

    public void setDayCareAcPosting(boolean dayCareAcPosting) {
        this.dayCareAcPosting = dayCareAcPosting;
    }

    public boolean isDiagnosticAcPosting() {
        return diagnosticAcPosting;
    }

    public void setDiagnosticAcPosting(boolean diagnosticAcPosting) {
        this.diagnosticAcPosting = diagnosticAcPosting;
    }

    public boolean isEmergencyAcPosting() {
        return emergencyAcPosting;
    }

    public void setEmergencyAcPosting(boolean emergencyAcPosting) {
        this.emergencyAcPosting = emergencyAcPosting;
    }

    public boolean isPharmacyAcPosting() {
        return pharmacyAcPosting;
    }

    public void setPharmacyAcPosting(boolean pharmacyAcPosting) {
        this.pharmacyAcPosting = pharmacyAcPosting;
    }

    public boolean isMisAppPush() {
        return misAppPush;
    }

    public void setMisAppPush(boolean misAppPush) {
        this.misAppPush = misAppPush;
    }
}
