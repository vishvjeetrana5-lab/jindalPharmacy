package dataman.accounts.advanceRequestEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String reqDate;
    private String moduleTag;
    private String moduleText;
    private String poNoTag;
    private String poNoText;
    private String advanceReqAmt;
    private String dueDate;
    private String preparedBy;
    private String modifiedBy;
    private String isStatus;


    public Find_Mdl() {

    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getModuleTag() {
        return moduleTag;
    }

    public void setModuleTag(String moduleTag) {
        this.moduleTag = moduleTag;
    }

    public String getModuleText() {
        return moduleText;
    }

    public void setModuleText(String moduleText) {
        this.moduleText = moduleText;
    }

    public String getPoNoTag() {
        return poNoTag;
    }

    public void setPoNoTag(String poNoTag) {
        this.poNoTag = poNoTag;
    }

    public String getPoNoText() {
        return poNoText;
    }

    public void setPoNoText(String poNoText) {
        this.poNoText = poNoText;
    }

    public String getAdvanceReqAmt() {
        return advanceReqAmt;
    }

    public void setAdvanceReqAmt(String advanceReqAmt) {
        this.advanceReqAmt = advanceReqAmt;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }




}
