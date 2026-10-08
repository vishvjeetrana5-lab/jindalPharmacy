package aarogya.master.poolGroupDetails.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class PoolGroupDetailsGridEdit_Mdl extends KeyField implements Serializable, IsSerializable {

//    private String consultantCode;
//    private String consultantName;
    private String valueCode;
    private String valueName;
    private String amount;
    private String vSNo;

    public PoolGroupDetailsGridEdit_Mdl() {
    }

//    public String getConsultantCode() {
//        return consultantCode;
//    }
//
//    public void setConsultantCode(String consultantCode) {
//        this.consultantCode = consultantCode;
//    }
//
//    public String getConsultantName() {
//        return consultantName;
//    }
//
//    public void setConsultantName(String consultantName) {
//        this.consultantName = consultantName;
//    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

}
