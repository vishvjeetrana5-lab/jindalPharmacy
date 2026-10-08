package aarogya.inventory.physicalStock.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhysicalStock_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdItemIssue = new RecId();
    private String voucherTypeCode;
    private String voucherDate;
    private String departmentCode;
    private String departmentName;
    private String filterTypeCode;
    private String filterTypeName;
    private String filterValueCode;
    private String filterValueName;
    private String isAllowDuplicate;
    private String u_EntDt_LatestLine;
    private ModifyReason_Mdl modifyReasonMdl;
    private List<GrdItemDetail_Mdl> listItemDetail = new ArrayList<GrdItemDetail_Mdl>();

    public PhysicalStock_Mdl() {
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public RecId getRecIdItemIssue() {
        return recIdItemIssue;
    }

    public void setRecIdItemIssue(RecId recIdItemIssue) {
        this.recIdItemIssue = recIdItemIssue;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
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

    public String getFilterTypeCode() {
        return filterTypeCode;
    }

    public void setFilterTypeCode(String filterTypeCode) {
        this.filterTypeCode = filterTypeCode;
    }

    public String getFilterTypeName() {
        return filterTypeName;
    }

    public void setFilterTypeName(String filterTypeName) {
        this.filterTypeName = filterTypeName;
    }

    public String getFilterValueCode() {
        return filterValueCode;
    }

    public void setFilterValueCode(String filterValueCode) {
        this.filterValueCode = filterValueCode;
    }

    public String getFilterValueName() {
        return filterValueName;
    }

    public void setFilterValueName(String filterValueName) {
        this.filterValueName = filterValueName;
    }

    public String getIsAllowDuplicate() {
        return isAllowDuplicate;
    }

    public void setIsAllowDuplicate(String isAllowDuplicate) {
        this.isAllowDuplicate = isAllowDuplicate;
    }

    public String getU_EntDt_LatestLine() {
        return u_EntDt_LatestLine;
    }

    public void setU_EntDt_LatestLine(String u_EntDt_LatestLine) {
        this.u_EntDt_LatestLine = u_EntDt_LatestLine;
    }

    public List<GrdItemDetail_Mdl> getListItemDetail() {
        return listItemDetail;
    }

    public void setListItemDetail(List<GrdItemDetail_Mdl> listItemDetail) {
        this.listItemDetail = listItemDetail;
    }

}
