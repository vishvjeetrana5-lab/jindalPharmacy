package aarogya.pharmacy.saleBill.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHOldOpd_Mdl extends KeyField implements Serializable, IsSerializable {

    private String date;
    private String consultant;
    private String consultantCode;
    private String prescDocid;

    public TxtHOldOpd_Mdl() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getConsultant() {
        return consultant;
    }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }


    public String getPrescDocid() {
        return prescDocid;
    }

    public void setPrescDocid(String prescDocid) {
        this.prescDocid = prescDocid;
    }

   
}
