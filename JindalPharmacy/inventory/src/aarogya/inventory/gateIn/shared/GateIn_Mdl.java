package aarogya.inventory.gateIn.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;

public class GateIn_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recId = new RecId();
    private String voucherTypeCode;
    private String voucherTypeName;
    private String partyCode;
    private String partyName;
    private String gateOutBy;
    private String gateInDate;
    private String gateInTime;
    private String remark;
    private ModifyReason_Mdl modifyReasonMdl;
    private String Date;
    private String Time;
    private String u_EntDt_LatestLine;
    private String unitCode;
    private String unitName;
    private String preparedate;
    private String mode;
    private String modeName;
    private String estdDate;
    private String passTypeCode;
    private String passTypeName;
    private String purposeText;
    private String departmentCode;
    private String departmentName;
    private String destination;
    private String isCancel;

    private List<GateInGridEdit_Mdl> gateDetails = new ArrayList<GateInGridEdit_Mdl>();

    public GateIn_Mdl() {
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getGateOutBy() {
        return gateOutBy;
    }

    public void setGateOutBy(String gateOutBy) {
        this.gateOutBy = gateOutBy;
    }

    public String getGateOutDate() {
        return gateInDate;
    }

    public void setGateOutDate(String gateOutDate) {
        this.gateInDate = gateOutDate;
    }

    public String getGateOutTime() {
        return gateInTime;
    }

    public void setGateOutTime(String gateOutTime) {
        this.gateInTime = gateOutTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public List<GateInGridEdit_Mdl> getGateDetails() {
        return gateDetails;
    }

    public void setGateDetails(List<GateInGridEdit_Mdl> gateDetails) {
        this.gateDetails = gateDetails;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getU_EntDt_LatestLine() {
        return u_EntDt_LatestLine;
    }

    public void setU_EntDt_LatestLine(String u_EntDt_LatestLine) {
        this.u_EntDt_LatestLine = u_EntDt_LatestLine;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getPreparedate() {
        return preparedate;
    }

    public void setPreparedate(String preparedate) {
        this.preparedate = preparedate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getEstdDate() {
        return estdDate;
    }

    public void setEstdDate(String estdDate) {
        this.estdDate = estdDate;
    }

    public String getPassTypeCode() {
        return passTypeCode;
    }

    public void setPassTypeCode(String passTypeCode) {
        this.passTypeCode = passTypeCode;
    }

    public String getPassTypeName() {
        return passTypeName;
    }

    public void setPassTypeName(String passTypeName) {
        this.passTypeName = passTypeName;
    }

    public String getPurposeText() {
        return purposeText;
    }

    public void setPurposeText(String purposeText) {
        this.purposeText = purposeText;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }

}
