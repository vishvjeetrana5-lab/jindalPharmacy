
package aarogya.pharmacy.saleBillReturn.shared;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class MedicineReturnRequest_Mdl  extends KeyField implements Serializable, IsSerializable {
    private String returnDate;
    private String returnBy;
    private String isChecked;
    private String patientName;
    private String patientRegDocID;
    private String patientUHID;
    private String patientRegID;
    private String patientUHIDDocID;
    private String amount;
    private String gstReasonCode;
    private String gstReasonName;
    
    public MedicineReturnRequest_Mdl() {
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getPatientRegDocID() {
        return patientRegDocID;
    }

    public void setPatientRegDocID(String patientRegDocID) {
        this.patientRegDocID = patientRegDocID;
    }

    public String getPatientUHID() {
        return patientUHID;
    }

    public void setPatientUHID(String patientUHID) {
        this.patientUHID = patientUHID;
    }

    public String getPatientRegID() {
        return patientRegID;
    }

    public void setPatientRegID(String patientRegID) {
        this.patientRegID = patientRegID;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnBy() {
        return returnBy;
    }

    public void setReturnBy(String returnBy) {
        this.returnBy = returnBy;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPatientUHIDDocID() {
        return patientUHIDDocID;
    }

    public void setPatientUHIDDocID(String patientUHIDDocID) {
        this.patientUHIDDocID = patientUHIDDocID;
    }

    public String getGstReasonCode() {
        return gstReasonCode;
    }

    public void setGstReasonCode(String gstReasonCode) {
        this.gstReasonCode = gstReasonCode;
    }

    public String getGstReasonName() {
        return gstReasonName;
    }

    public void setGstReasonName(String gstReasonName) {
        this.gstReasonName = gstReasonName;
    }
}
