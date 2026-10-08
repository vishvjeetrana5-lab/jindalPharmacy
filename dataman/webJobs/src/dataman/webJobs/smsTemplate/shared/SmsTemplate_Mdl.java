package dataman.webJobs.smsTemplate.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SmsTemplate_Mdl extends KeyField implements Serializable, IsSerializable {

    private String tempName;
    private String templateGroupCode;
    private String templateGroupName;
    private String apiTemplateId;
    private DmWebJobDataSources dataSource = DmWebJobDataSources.FUNCTION;
    private String functionTag;
    private String functionText;
    private String fileName;
    private String filePath;
    private String uploadFilePath;
    private String uploadFileName;
    private String msg;
    private String functionQuery;
    private List<KeyFieldExtended> lstStaffInfo = new ArrayList<KeyFieldExtended>();
    private boolean staffInfo = false;

    public SmsTemplate_Mdl() {
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getTemplateGroupCode() {
        return templateGroupCode;
    }

    public void setTemplateGroupCode(String templateGroupCode) {
        this.templateGroupCode = templateGroupCode;
    }

    public String getTemplateGroupName() {
        return templateGroupName;
    }

    public void setTemplateGroupName(String templateGroupName) {
        this.templateGroupName = templateGroupName;
    }

    public DmWebJobDataSources getDataSource() {
        return dataSource;
    }

    public void setDataSource(DmWebJobDataSources dataSource) {
        this.dataSource = dataSource;
    }

    public String getFunctionTag() {
        return functionTag;
    }

    public void setFunctionTag(String functionTag) {
        this.functionTag = functionTag;
    }

    public String getFunctionText() {
        return functionText;
    }

    public void setFunctionText(String functionText) {
        this.functionText = functionText;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFunctionQuery() {
        return functionQuery;
    }

    public void setFunctionQuery(String functionQuery) {
        this.functionQuery = functionQuery;
    }

    public List<KeyFieldExtended> getLstStaffInfo() {
        return lstStaffInfo;
    }

    public boolean isStaffInfo() {
        return staffInfo;
    }

    public void setStaffInfo(boolean staffInfo) {
        this.staffInfo = staffInfo;
    }

    public String getApiTemplateId() {
        return apiTemplateId;
    }

    public void setApiTemplateId(String apiTemplateId) {
        this.apiTemplateId = apiTemplateId;
    }
}
