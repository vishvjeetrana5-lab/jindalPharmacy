package aarogya.inventory.projectMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ProjectMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String startDate;
    private String endDate;
    private String projectHeadName;
    private String projectHeadCode;
    private String projectAcName;
    private String projectAcCode;
    private String projectDescription;
    private String approvedBudjet;
    private String preparedBy;
    private String preparedDate;
    private String u_Name;
    private String u_EntDt;
    private boolean active;

    public ProjectMaster_Mdl() {
    }

    public String getU_Name() {
        return u_Name;
    }

    public void setU_Name(String u_Name) {
        this.u_Name = u_Name;
    }

    public String getU_EntDt() {
        return u_EntDt;
    }

    public void setU_EntDt(String u_EntDt) {
        this.u_EntDt = u_EntDt;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getProjectHeadName() {
        return projectHeadName;
    }

    public void setProjectHeadName(String projectHeadName) {
        this.projectHeadName = projectHeadName;
    }

    public String getProjectHeadCode() {
        return projectHeadCode;
    }

    public void setProjectHeadCode(String projectHeadCode) {
        this.projectHeadCode = projectHeadCode;
    }

    public String getProjectAcName() {
        return projectAcName;
    }

    public void setProjectAcName(String projectAcName) {
        this.projectAcName = projectAcName;
    }

    public String getProjectAcCode() {
        return projectAcCode;
    }

    public void setProjectAcCode(String projectAcCode) {
        this.projectAcCode = projectAcCode;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getApprovedBudjet() {
        return approvedBudjet;
    }

    public void setApprovedBudjet(String approvedBudjet) {
        this.approvedBudjet = approvedBudjet;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getPreparedDate() {
        return preparedDate;
    }

    public void setPreparedDate(String preparedDate) {
        this.preparedDate = preparedDate;
    }

}
