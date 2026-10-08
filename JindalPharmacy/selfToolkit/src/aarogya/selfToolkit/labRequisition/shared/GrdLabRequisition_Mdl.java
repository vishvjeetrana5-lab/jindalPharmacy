package aarogya.selfToolkit.labRequisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdLabRequisition_Mdl extends KeyField implements Serializable, IsSerializable {

    private String reqDate;
    private String reqTime;
    private String remark;
    private String reqQty;
    private String defaultDepartmentCode;
    private String defaultDepartmentName;
    private String siteCode;
    private String siteName;
    private String reqDocId;

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReqQty() {
        return reqQty;
    }

    public void setReqQty(String reqQty) {
        this.reqQty = reqQty;
    }

    public String getDefaultDepartmentCode() {
        return defaultDepartmentCode;
    }

    public void setDefaultDepartmentCode(String defaultDepartmentCode) {
        this.defaultDepartmentCode = defaultDepartmentCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getReqDocId() {
        return reqDocId;
    }

    public void setReqDocId(String reqDocId) {
        this.reqDocId = reqDocId;
    }

    public String getDefaultDepartmentName() {
        return defaultDepartmentName;
    }

    public void setDefaultDepartmentName(String defaultDepartmentName) {
        this.defaultDepartmentName = defaultDepartmentName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

}
