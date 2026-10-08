package aarogya.dashBoard.dataModificationLog.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdDataModificationLog_Mdl extends KeyField implements Serializable, IsSerializable {

    private String logDateTime;
    private String logDate;
    private String userNameCode;
    private String userName;
    private String financialyear;
    private String modeCode;
    private String mode;
    private String vrTypeShortName;
    private String moduleCode;
    private String module;
    private String menu;
    private String modifyReason;
    private String recordName1;
    private String recordDesc1;
    private String recordName2;
    private String recordDesc2;
    private String shortSiteName;
    private String remark;

    private String oldData;
    private String newData;

    public GrdDataModificationLog_Mdl() {
    }

    public String getLogDateTime() {
        return logDateTime;
    }

    public void setLogDateTime(String logDateTime) {
        this.logDateTime = logDateTime;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getUserNameCode() {
        return userNameCode;
    }

    public void setUserNameCode(String userNameCode) {
        this.userNameCode = userNameCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFinancialyear() {
        return financialyear;
    }

    public void setFinancialyear(String financialyear) {
        this.financialyear = financialyear;
    }

    public String getModeCode() {
        return modeCode;
    }

    public void setModeCode(String modeCode) {
        this.modeCode = modeCode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getVrTypeShortName() {
        return vrTypeShortName;
    }

    public void setVrTypeShortName(String vrTypeShortName) {
        this.vrTypeShortName = vrTypeShortName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getModifyReason() {
        return modifyReason;
    }

    public void setModifyReason(String modifyReason) {
        this.modifyReason = modifyReason;
    }

    public String getRecordName1() {
        return recordName1;
    }

    public void setRecordName1(String recordName1) {
        this.recordName1 = recordName1;
    }

    public String getRecordDesc1() {
        return recordDesc1;
    }

    public void setRecordDesc1(String recordDesc1) {
        this.recordDesc1 = recordDesc1;
    }

    public String getRecordName2() {
        return recordName2;
    }

    public void setRecordName2(String recordName2) {
        this.recordName2 = recordName2;
    }

    public String getRecordDesc2() {
        return recordDesc2;
    }

    public void setRecordDesc2(String recordDesc2) {
        this.recordDesc2 = recordDesc2;
    }

    public String getShortSiteName() {
        return shortSiteName;
    }

    public void setShortSiteName(String shortSiteName) {
        this.shortSiteName = shortSiteName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }
}
