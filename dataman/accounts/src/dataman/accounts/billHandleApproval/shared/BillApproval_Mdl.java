package dataman.accounts.billHandleApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BillApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String FromDate;
    private String ToDate;

    private String locationName;
    private String locationCode;

    private String vSNo;
    private String StatusCode;
    private String StatusName;

    private String approvedBy;
    private String isStatus;
    private String approvedDateTime;

    private String wtaCode;

    private List<GrdBillDetail_Mdl> grdItemDetail_Mdl = new ArrayList<GrdBillDetail_Mdl>();
    private List<GrdBillApproval_Mdl> requisitionApprovalEntry_Mdl = new ArrayList<GrdBillApproval_Mdl>();

    public BillApproval_Mdl() {
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getApprovedDateTime() {
        return approvedDateTime;
    }

    public void setApprovedDateTime(String approvedDateTime) {
        this.approvedDateTime = approvedDateTime;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public List<GrdBillDetail_Mdl> getGrdItemDetail_Mdl() {
        return grdItemDetail_Mdl;
    }

    public void setGrdItemDetail_Mdl(List<GrdBillDetail_Mdl> grdItemDetail_Mdl) {
        this.grdItemDetail_Mdl = grdItemDetail_Mdl;
    }

    public List<GrdBillApproval_Mdl> getRequisitionApprovalEntry_Mdl() {
        return requisitionApprovalEntry_Mdl;
    }

    public void setRequisitionApprovalEntry_Mdl(List<GrdBillApproval_Mdl> requisitionApprovalEntry_Mdl) {
        this.requisitionApprovalEntry_Mdl = requisitionApprovalEntry_Mdl;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String StatusCode) {
        this.StatusCode = StatusCode;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String StatusName) {
        this.StatusName = StatusName;
    }

}
