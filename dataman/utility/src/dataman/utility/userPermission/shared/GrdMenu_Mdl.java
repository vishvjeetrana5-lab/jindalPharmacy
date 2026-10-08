package dataman.utility.userPermission.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdMenu_Mdl extends KeyField implements Serializable, IsSerializable {

    public enum EventType implements Serializable, IsSerializable {

        HOME(0), VIEW(1), ADD(2), EDIT(3), DELETE(4), PRINT(5), ENABLE(6), VIEW_DAYS(7), SAVE_DAYS(8), DELETE_DAYS(9), ALLOW_UPTO(10);

        private final int id;

        public int getId() {
            return id;
        }

        EventType(int id) {
            this.id = id;
        }

        public static EventType getViaKey(int id) {
            EventType eventType = null;
            if (id == HOME.getId()) {
                eventType = HOME;
            } else if (id == VIEW.getId()) {
                eventType = VIEW;
            } else if (id == ADD.getId()) {
                eventType = ADD;
            } else if (id == EDIT.getId()) {
                eventType = EDIT;
            } else if (id == DELETE.getId()) {
                eventType = DELETE;
            } else if (id == PRINT.getId()) {
                eventType = PRINT;
            } else if (id == ENABLE.getId()) {
                eventType = ENABLE;
            } else if (id == VIEW_DAYS.getId()) {
                eventType = VIEW_DAYS;
            } else if (id == SAVE_DAYS.getId()) {
                eventType = SAVE_DAYS;
            } else if (id == DELETE_DAYS.getId()) {
                eventType = DELETE_DAYS;
            } else if (id == ALLOW_UPTO.getId()) {
                eventType = ALLOW_UPTO;
            }
            return eventType;
        }
    }

    private String mnuModule;
    private String mnuName;
    private String mnuText;
    private String mnuLevel;
    private String parent;
    private String permission;
    private String reportFor;
    private String vp_Days;
    private String sp_days;
    private String dp_Days;
    private String allowValueUpto;
    private String groupName;
    private String groupText;
    private String serialNo;
    private String buMnuCount;
    private String buIsAdd;
    private String buIsEdit;
    private String buIsDelete;
    private String buIsPrint;
    private String buIsEnable;
    private String cuMnuCount;
    private String cuIsAdd;
    private String cuIsEdit;
    private String cuIsDelete;
    private String cuIsPrint;
    private String cuIsEnable;

    public GrdMenu_Mdl() {
    }

    public String getMnuModule() {
        return mnuModule;
    }

    public void setMnuModule(String mnuModule) {
        this.mnuModule = mnuModule;
    }

    public String getMnuName() {
        return mnuName;
    }

    public void setMnuName(String mnuName) {
        this.mnuName = mnuName;
    }

    public String getMnuText() {
        return mnuText;
    }

    public void setMnuText(String mnuText) {
        this.mnuText = mnuText;
    }

    public String getMnuLevel() {
        return mnuLevel;
    }

    public void setMnuLevel(String mnuLevel) {
        this.mnuLevel = mnuLevel;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getReportFor() {
        return reportFor;
    }

    public void setReportFor(String reportFor) {
        this.reportFor = reportFor;
    }

    public String getVp_Days() {
        return vp_Days;
    }

    public void setVp_Days(String vp_Days) {
        this.vp_Days = vp_Days;
    }

    public String getSp_days() {
        return sp_days;
    }

    public void setSp_days(String sp_days) {
        this.sp_days = sp_days;
    }

    public String getDp_Days() {
        return dp_Days;
    }

    public void setDp_Days(String dp_Days) {
        this.dp_Days = dp_Days;
    }

    public String getAllowValueUpto() {
        return allowValueUpto;
    }

    public void setAllowValueUpto(String allowValueUpto) {
        this.allowValueUpto = allowValueUpto;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupText() {
        return groupText;
    }

    public void setGroupText(String groupText) {
        this.groupText = groupText;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getBuMnuCount() {
        return buMnuCount;
    }

    public void setBuMnuCount(String buMnuCount) {
        this.buMnuCount = buMnuCount;
    }

    public String getBuIsAdd() {
        return buIsAdd;
    }

    public void setBuIsAdd(String buIsAdd) {
        this.buIsAdd = buIsAdd;
    }

    public String getBuIsEdit() {
        return buIsEdit;
    }

    public void setBuIsEdit(String buIsEdit) {
        this.buIsEdit = buIsEdit;
    }

    public String getBuIsDelete() {
        return buIsDelete;
    }

    public void setBuIsDelete(String buIsDelete) {
        this.buIsDelete = buIsDelete;
    }

    public String getBuIsPrint() {
        return buIsPrint;
    }

    public void setBuIsPrint(String buIsPrint) {
        this.buIsPrint = buIsPrint;
    }

    public String getBuIsEnable() {
        return buIsEnable;
    }

    public void setBuIsEnable(String buIsEnable) {
        this.buIsEnable = buIsEnable;
    }

    public String getCuMnuCount() {
        return cuMnuCount;
    }

    public void setCuMnuCount(String cuMnuCount) {
        this.cuMnuCount = cuMnuCount;
    }

    public String getCuIsAdd() {
        return cuIsAdd;
    }

    public void setCuIsAdd(String cuIsAdd) {
        this.cuIsAdd = cuIsAdd;
    }

    public String getCuIsEdit() {
        return cuIsEdit;
    }

    public void setCuIsEdit(String cuIsEdit) {
        this.cuIsEdit = cuIsEdit;
    }

    public String getCuIsDelete() {
        return cuIsDelete;
    }

    public void setCuIsDelete(String cuIsDelete) {
        this.cuIsDelete = cuIsDelete;
    }

    public String getCuIsPrint() {
        return cuIsPrint;
    }

    public void setCuIsPrint(String cuIsPrint) {
        this.cuIsPrint = cuIsPrint;
    }

    public String getCuIsEnable() {
        return cuIsEnable;
    }

    public void setCuIsEnable(String cuIsEnable) {
        this.cuIsEnable = cuIsEnable;
    }

}
