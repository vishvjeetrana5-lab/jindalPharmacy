package aarogya.pharmacy.physicalStockClearance.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class PhysicalStockClearance_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdObj = new RecId();
    private String voucherTypeCode;
    private String voucherDate;
    private String departmentCode;
    private String departmentName;
    private String filterTypeCode;
    private String filterTypeName;
    private String filterValueCode;
    private String filterValueName;
    private String postedBy;
    private String u_EntDt_LatestLine;

    public PhysicalStockClearance_Mdl() {
    }

    public RecId getRecIdObj() {
        return recIdObj;
    }

    public void setRecIdObj(RecId recIdObj) {
        this.recIdObj = recIdObj;
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

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getU_EntDt_LatestLine() {
        return u_EntDt_LatestLine;
    }

    public void setU_EntDt_LatestLine(String u_EntDt_LatestLine) {
        this.u_EntDt_LatestLine = u_EntDt_LatestLine;
    }

}
