package aarogya.mis.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHReg_Mdl extends KeyField implements Serializable, IsSerializable {

    private String mobile;
    private String uhidNo;
//    private String regNo;
    private String patientName;

    public TxtHReg_Mdl() {
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUhidNo() {
        return uhidNo;
    }

    public void setUhidNo(String uhidNo) {
        this.uhidNo = uhidNo;
    }

//    public String getRegNo() {
//        return regNo;
//    }
//
//    public void setRegNo(String regNo) {
//        this.regNo = regNo;
//    }
//

    public String getPatientName() {
        return patientName;
    }

    public void setPersonName(String patientName) {
        this.patientName = patientName;
    }

    
    
   

}
