package aarogya.selfToolkit.modifyReason.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdCancelReason_Mdl extends KeyField implements Serializable, IsSerializable {

    private String docId;
    private String v_SNo;
    private String mode;
    private String modifyReason;
    private String remark;
    private String dateTime;
    private String userName;

    public GrdCancelReason_Mdl() {
    }

    public GrdCancelReason_Mdl(GrdCancelReason_Mdl gcrMdl) {
        this(gcrMdl.getDocId(), gcrMdl.getV_SNo(), gcrMdl.getMode(), gcrMdl.getModifyReason(), gcrMdl.getRemark(), gcrMdl.getDateTime(), gcrMdl.getUserName(), gcrMdl.getSharedKeyFieldCode(), gcrMdl.getSharedKeyFieldName());
    }

    private GrdCancelReason_Mdl(String docId, String v_SNo, String mode, String modifyReason, String remark, String dateTime, String userName, String KeyFieldCode, String KeyFieldName) {
        super(KeyFieldCode, KeyFieldName);
        this.docId = docId;
        this.v_SNo = v_SNo;
        this.mode = mode;
        this.modifyReason = modifyReason;
        this.remark = remark;
        this.dateTime = dateTime;
        this.userName = userName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getModifyReason() {
        return modifyReason;
    }

    public void setModifyReason(String modifyReason) {
        this.modifyReason = modifyReason;
    }

}
