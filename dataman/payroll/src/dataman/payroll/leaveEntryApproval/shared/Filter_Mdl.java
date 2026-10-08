package dataman.payroll.leaveEntryApproval.shared;

//mport aarogya.tpa.tpaFilingProcess.shared.TPAFilingProcess_Vld.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class Filter_Mdl extends KeyField implements Serializable, IsSerializable {

    private String status;
    private String statusCode;
    private String fromDate;
    private String toDate;

    public Filter_Mdl() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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
