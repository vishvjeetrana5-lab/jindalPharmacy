package aarogya.inventory.projectIndicator.shared;

import aarogya.inventory.projectIndicator.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;

public class ProjectIndicator_Mdl extends KeyField implements Serializable, IsSerializable {

    private String preparedBy;
    private String preparedDate;
    private String u_Name;
    private String u_EntDt;
    private String vType;
    private String vTypeDescription;
    private String vPrefix;
    private String vDate;
    private RecId recID=new RecId();
    private String remark;
    private String vNo;
    
    private String guiConfigCode;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();

    public ProjectIndicator_Mdl() {
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

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }

    public DmGUI_Mdl getGuiMdl() {
        return guiMdl;
    }

    public void setGuiMdl(DmGUI_Mdl guiMdl) {
        this.guiMdl = guiMdl;
    }

    public String getvType() {
        return vType;
    }

    public void setvType(String vType) {
        this.vType = vType;
    }

    public String getvTypeDescription() {
        return vTypeDescription;
    }

    public void setvTypeDescription(String vTypeDescription) {
        this.vTypeDescription = vTypeDescription;
    }

    
    public String getvPrefix() {
        return vPrefix;
    }

    public void setvPrefix(String vPrefix) {
        this.vPrefix = vPrefix;
    }

    public RecId getRecID() {
        return recID;
    }

    public void setRecID(RecId recID) {
        this.recID = recID;
    }

    public String getvDate() {
        return vDate;
    }

    public void setvDate(String vDate) {
        this.vDate = vDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getvNo() {
        return vNo;
    }

    public void setvNo(String vNo) {
        this.vNo = vNo;
    }

}
