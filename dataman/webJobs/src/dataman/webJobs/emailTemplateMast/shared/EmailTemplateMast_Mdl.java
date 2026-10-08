package dataman.webJobs.emailTemplateMast.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmailTemplateMast_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<GrdEmail_Mdl> grdEmail = new ArrayList<GrdEmail_Mdl>();
    private List<GrdImage_Mdl> grdImageDetail = new ArrayList<GrdImage_Mdl>();
    private List<KeyFieldExtended> lstStaffInfo = new ArrayList<KeyFieldExtended>();
    private String tempName;
    private String tempGroupCode;
    private String tempGroupName;
    private DmWebJobDataSources datasource = DmWebJobDataSources.FUNCTION;
    private String functionTag;
    private String functionText;
    private String functionQuery;
    private String fileName;
    private String filePath;
    private String uploadFilePath;
    private String uploadFileName;
    private String subject;
    private boolean staffInfo = false;

    private String msg;

    private List<GrdFieldName_Mdl> grdFieldMdl = new ArrayList<GrdFieldName_Mdl>();

    public EmailTemplateMast_Mdl() {
    }

    public List<GrdFieldName_Mdl> getGrdFieldMdl() {
        return grdFieldMdl;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getTempGroupCode() {
        return tempGroupCode;
    }

    public void setTempGroupCode(String tempGroupCode) {
        this.tempGroupCode = tempGroupCode;
    }

    public String getTempGroupName() {
        return tempGroupName;
    }

    public void setTempGroupName(String tempGroupName) {
        this.tempGroupName = tempGroupName;
    }

    public DmWebJobDataSources getDataSource() {
        return datasource;
    }

    public void setDataSource(DmWebJobDataSources dataSource) {
        this.datasource = dataSource;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<GrdEmail_Mdl> getGrdEmail() {
        return grdEmail;
    }

    public String getFunctionQuery() {
        return functionQuery;
    }

    public void setFunctionQuery(String functionQuery) {
        this.functionQuery = functionQuery;
    }

    public List<GrdImage_Mdl> getGrdImageDetail() {
        return grdImageDetail;
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
}
