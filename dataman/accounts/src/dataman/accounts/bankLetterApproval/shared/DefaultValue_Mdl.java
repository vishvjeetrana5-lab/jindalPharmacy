package dataman.accounts.bankLetterApproval.shared;


import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {


    private String fromDate;
    private String toDate;
    private String billStatusCode;
    private String billStatusName;

    public DefaultValue_Mdl() {
    }

    public String getBillStatusCode() {
        return billStatusCode;
    }

    public void setBillStatusCode(String billStatusCode) {
        this.billStatusCode = billStatusCode;
    }

    public String getBillStatusName() {
        return billStatusName;
    }

    public void setBillStatusName(String billStatusName) {
        this.billStatusName = billStatusName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

  

}
