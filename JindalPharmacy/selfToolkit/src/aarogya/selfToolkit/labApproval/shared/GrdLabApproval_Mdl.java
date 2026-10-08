package aarogya.selfToolkit.labApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdLabApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String isChecked;
    private String IsView;
    private String labType;
    private String labDateTime;
    private String technician;
    private String testTemplate;
    private String sampleNo;
    private String itemName;
    private String sampleConsumeQty;
    private String unit;
    private String approvedBy;
    private String wtaCode;
    private String Approval;

    public GrdLabApproval_Mdl() {
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getIsView() {
        return IsView;
    }

    public void setIsView(String IsView) {
        this.IsView = IsView;
    }

    public String getLabType() {
        return labType;
    }

    public void setLabType(String labType) {
        this.labType = labType;
    }

    public String getLabDateTime() {
        return labDateTime;
    }

    public void setLabDateTime(String labDateTime) {
        this.labDateTime = labDateTime;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public String getTestTemplate() {
        return testTemplate;
    }

    public void setTestTemplate(String testTemplate) {
        this.testTemplate = testTemplate;
    }

    public String getSampleNo() {
        return sampleNo;
    }

    public void setSampleNo(String sampleNo) {
        this.sampleNo = sampleNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSampleConsumeQty() {
        return sampleConsumeQty;
    }

    public void setSampleConsumeQty(String sampleConsumeQty) {
        this.sampleConsumeQty = sampleConsumeQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getApproval() {
        return Approval;
    }

    public void setApproval(String Approval) {
        this.Approval = Approval;
    }

}
