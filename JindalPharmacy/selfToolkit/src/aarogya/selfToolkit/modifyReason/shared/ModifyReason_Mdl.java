package aarogya.selfToolkit.modifyReason.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModifyReason_Mdl extends KeyField implements Serializable, IsSerializable {

    private String v_type, recId, v_date;
    private String preChangeValue, preU_Name;
    private Long preU_EntDt;
    private String postChangeValue;
    private String remark, mode;
    private List<GrdCancelReason_Mdl> lstCancelReason = new ArrayList<GrdCancelReason_Mdl>();

    //===========================Manage Log===================
    private String moduleName, menuName, uniqueKey, gridHeader;
    private HashMap<String, String> logPrevious = new HashMap<>();
    private HashMap<String, String> logCurrent = new HashMap<>();

    private List<String> gridPrevious = new ArrayList<>();
    private List<String> gridCurrent = new ArrayList<>();
    private List<String> gridPreviousKeysList = new ArrayList<>();
    private List<String> gridCurrentKeysList = new ArrayList<>();

    private HashMap<String, String> gridLogPrevious = new HashMap<>();
    private HashMap<String, String> gridLogCurrent = new HashMap<>();

    public ModifyReason_Mdl() {
    }

    public ModifyReason_Mdl(ModifyReason_Mdl mrMdl) {
        this(mrMdl.getMode(), mrMdl.getRemark(), mrMdl.getLstCancelReason(), mrMdl.getSharedKeyFieldCode(), mrMdl.getSharedKeyFieldName());
    }

    private ModifyReason_Mdl(String mode, String remark, List<GrdCancelReason_Mdl> lstCancelReason, String KeyFieldCode, String KeyFieldName) {
        super(KeyFieldCode, KeyFieldName);
        this.mode = mode;
        this.remark = remark;

        this.lstCancelReason = new ArrayList<>();
        for (GrdCancelReason_Mdl gcrMdl : lstCancelReason) {
            this.lstCancelReason.add(new GrdCancelReason_Mdl(gcrMdl));
        }
    }

    public List<GrdCancelReason_Mdl> getLstCancelReason() {
        return lstCancelReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getV_type() {
        return v_type;
    }

    public void setV_type(String v_type) {
        this.v_type = v_type;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getV_date() {
        return v_date;
    }

    public void setV_date(String v_date) {
        this.v_date = v_date;
    }

    public String getPreChangeValue() {
        return preChangeValue;
    }

    public void setPreChangeValue(String preChangeValue) {
        this.preChangeValue = preChangeValue;
    }

    public String getPreU_Name() {
        return preU_Name;
    }

    public void setPreU_Name(String preU_Name) {
        this.preU_Name = preU_Name;
    }

    public Long getPreU_EntDt() {
        return preU_EntDt;
    }

    public void setPreU_EntDt(Long preU_EntDt) {
        this.preU_EntDt = preU_EntDt;
    }

    public String getPostChangeValue() {
        return postChangeValue;
    }

    public void setPostChangeValue(String postChangeValue) {
        this.postChangeValue = postChangeValue;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public HashMap<String, String> getLogPrevious() {
        return logPrevious;
    }

    public void setLogPrevious(HashMap<String, String> logPrevious) {
        this.logPrevious = logPrevious;
    }

    public HashMap<String, String> getLogCurrent() {
        return logCurrent;
    }

    public void setLogCurrent(HashMap<String, String> logCurrent) {
        this.logCurrent = logCurrent;
    }

    public String getGridHeader() {
        return gridHeader;
    }

    public void setGridHeader(String gridHeader) {
        this.gridHeader = gridHeader;
    }

    public List<String> getGridPreviousKeysList() {
        return gridPreviousKeysList;
    }

    public void setGridPreviousKeysList(List<String> gridPreviousKeysList) {
        this.gridPreviousKeysList = gridPreviousKeysList;
    }

    public List<String> getGridCurrentKeysList() {
        return gridCurrentKeysList;
    }

    public void setGridCurrentKeysList(List<String> gridCurrentKeysList) {
        this.gridCurrentKeysList = gridCurrentKeysList;
    }

    public HashMap<String, String> getGridLogPrevious() {
        return gridLogPrevious;
    }

    public void setGridLogPrevious(HashMap<String, String> gridLogPrevious) {
        this.gridLogPrevious = gridLogPrevious;
    }

    public HashMap<String, String> getGridLogCurrent() {
        return gridLogCurrent;
    }

    public void setGridLogCurrent(HashMap<String, String> gridLogCurrent) {
        this.gridLogCurrent = gridLogCurrent;
    }

    public List<String> getGridPrevious() {
        return gridPrevious;
    }

    public void setGridPrevious(List<String> gridPrevious) {
        this.gridPrevious = gridPrevious;
    }

    public List<String> getGridCurrent() {
        return gridCurrent;
    }

    public void setGridCurrent(List<String> gridCurrent) {
        this.gridCurrent = gridCurrent;
    }

}
