package dataman.accounts.bankLetterCreation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String date;

    private String fromFinalPaymentDate;
    private String toFinalPaymentDate;

    private String preparedby;
    private String approvedBy;
    private String modifiedBy;
    private String isDiscarded;

    public Find_Mdl() {
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFromFinalPaymentDate() {
        return fromFinalPaymentDate;
    }

    public void setFromFinalPaymentDate(String fromFinalPaymentDate) {
        this.fromFinalPaymentDate = fromFinalPaymentDate;
    }

    public String getToFinalPaymentDate() {
        return toFinalPaymentDate;
    }

    public void setToFinalPaymentDate(String toFinalPaymentDate) {
        this.toFinalPaymentDate = toFinalPaymentDate;
    }

    public String getPreparedby() {
        return preparedby;
    }

    public void setPreparedby(String preparedby) {
        this.preparedby = preparedby;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getIsDiscarded() {
        return isDiscarded;
    }

    public void setIsDiscarded(String isDiscarded) {
        this.isDiscarded = isDiscarded;
    }

}
