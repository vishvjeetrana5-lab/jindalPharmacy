package dataman.payroll.leaveEntryApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LeaveEntryApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String approvedBy;
    private String statusCode;
    private String statusName;
    private String applyDate;
    private List<GrdLeaveEntryApproval_Mdl> grdLeaveEntryDetail_Mdl = new ArrayList<GrdLeaveEntryApproval_Mdl>();

    public LeaveEntryApproval_Mdl() {
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

 

    public List<GrdLeaveEntryApproval_Mdl> getGrdLeaveEntryDetail_Lst() {
        return grdLeaveEntryDetail_Mdl;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
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



    public List<GrdLeaveEntryApproval_Mdl> getGrdLeaveEntryDetail_Mdl() {
        return grdLeaveEntryDetail_Mdl;
    }

    public void setGrdLeaveEntryDetail_Mdl(List<GrdLeaveEntryApproval_Mdl> grdLeaveEntryDetail_Mdl) {
        this.grdLeaveEntryDetail_Mdl = grdLeaveEntryDetail_Mdl;
    }

   
}
