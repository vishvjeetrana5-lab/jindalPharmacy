package dataman.accounts.voucherEntryApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Setting_Mdl extends KeyField implements Serializable, IsSerializable {

    private String FromDate;
    private String ToDate;
    private String CostCenterName;
    private String CostCenterCode;
    private String docId;
    private String recId;
    private String statusCode;
    private String statusName;
    private String voucherTypeCode;

    public Setting_Mdl() {
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String FromDate) {
        this.FromDate = FromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String ToDate) {
        this.ToDate = ToDate;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCostCenterName() {
        return CostCenterName;
    }

    public void setCostCenterName(String CostCenterName) {
        this.CostCenterName = CostCenterName;
    }

    public String getCostCenterCode() {
        return CostCenterCode;
    }

    public void setCostCenterCode(String CostCenterCode) {
        this.CostCenterCode = CostCenterCode;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

}
