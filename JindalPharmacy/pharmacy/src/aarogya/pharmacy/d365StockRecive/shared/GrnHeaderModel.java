package aarogya.pharmacy.d365StockRecive.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrnHeaderModel extends KeyField implements Serializable, IsSerializable {

    private RecId recIdGRN = new RecId();
    private String d365DocId;
    private String vType;
    private String vPrefix;
    private String vDate;
    private String d365DepartmentCode;
    private String departmentName;
    private String departmentCode;
    private String d365SupplierSubCode;
    private String SupplierSubCode;
    private String placeOfSupply;
    private String placeOfSupplyCode;
    private String remark;
    private String d365PChallanNo;
    private String d365PChallanDate;

    private String ohAmtGross;
    private String ohAmtDiscount;
    private String ohAmtTaxable;
    private String ohAmtCGST;
    private String ohAmtSGST;
    private String ohAmtIGST;
    private String ohAmtUGST;
    private String ohAmtTotal;
    private String ohAtFinalDiscount;
    private String ohAmtFinalDiscount;
    private String ohAmtRoundOff;
    private String ohAmtNet;
    private String ohAmtLandedValue;

    private String d365TimeStamp;
    private String preparedBy;
    private String preparedDate;
    private String approvedBy;
    private String approvalDateTime;

    private String businessUnitCode;
    private String businessUnitName;
    private String rcmApplicable;
    private String partyType;
    private String gsTin;

    private String eWayBillNo;
    private String eWayBillDt;
    private String lrNo;
    private String lrDate;
    private String gstLocation;

    private String dmStatus;
    private String dmTimeStamp;
    private String dmDocId;
    private String siteCode;

    // Additional Fields
    private String voucherTypeName;
    private String voucherCategory;

    private List<GrnLineModel> linedata = new ArrayList<GrnLineModel>();

    public GrnHeaderModel() {
    }

    public RecId getRecIdGRN() {
        return recIdGRN;
    }

    public void setRecIdGRN(RecId recIdGRN) {
        this.recIdGRN = recIdGRN;
    }

    public String getD365DocId() {
        return d365DocId;
    }

    public void setD365DocId(String d365DocId) {
        this.d365DocId = d365DocId;
    }

    public String getvType() {
        return vType;
    }

    public void setvType(String vType) {
        this.vType = vType;
    }

    public String getvPrefix() {
        return vPrefix;
    }

    public void setvPrefix(String vPrefix) {
        this.vPrefix = vPrefix;
    }

    public String getvDate() {
        return vDate;
    }

    public void setvDate(String vDate) {
        this.vDate = vDate;
    }

    public String getD365DepartmentCode() {
        return d365DepartmentCode;
    }

    public void setD365DepartmentCode(String d365DepartmentCode) {
        this.d365DepartmentCode = d365DepartmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getD365SupplierSubCode() {
        return d365SupplierSubCode;
    }

    public void setD365SupplierSubCode(String d365SupplierSubCode) {
        this.d365SupplierSubCode = d365SupplierSubCode;
    }

    public String getSupplierSubCode() {
        return SupplierSubCode;
    }

    public void setSupplierSubCode(String SupplierSubCode) {
        this.SupplierSubCode = SupplierSubCode;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getPlaceOfSupplyCode() {
        return placeOfSupplyCode;
    }

    public void setPlaceOfSupplyCode(String placeOfSupplyCode) {
        this.placeOfSupplyCode = placeOfSupplyCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getD365PChallanNo() {
        return d365PChallanNo;
    }

    public void setD365PChallanNo(String d365PChallanNo) {
        this.d365PChallanNo = d365PChallanNo;
    }

    public String getD365PChallanDate() {
        return d365PChallanDate;
    }

    public void setD365PChallanDate(String d365PChallanDate) {
        this.d365PChallanDate = d365PChallanDate;
    }

    public String getOhAmtGross() {
        return ohAmtGross;
    }

    public void setOhAmtGross(String ohAmtGross) {
        this.ohAmtGross = ohAmtGross;
    }

    public String getOhAmtDiscount() {
        return ohAmtDiscount;
    }

    public void setOhAmtDiscount(String ohAmtDiscount) {
        this.ohAmtDiscount = ohAmtDiscount;
    }

    public String getOhAmtTaxable() {
        return ohAmtTaxable;
    }

    public void setOhAmtTaxable(String ohAmtTaxable) {
        this.ohAmtTaxable = ohAmtTaxable;
    }

    public String getOhAmtCGST() {
        return ohAmtCGST;
    }

    public void setOhAmtCGST(String ohAmtCGST) {
        this.ohAmtCGST = ohAmtCGST;
    }

    public String getOhAmtSGST() {
        return ohAmtSGST;
    }

    public void setOhAmtSGST(String ohAmtSGST) {
        this.ohAmtSGST = ohAmtSGST;
    }

    public String getOhAmtIGST() {
        return ohAmtIGST;
    }

    public void setOhAmtIGST(String ohAmtIGST) {
        this.ohAmtIGST = ohAmtIGST;
    }

    public String getOhAmtUGST() {
        return ohAmtUGST;
    }

    public void setOhAmtUGST(String ohAmtUGST) {
        this.ohAmtUGST = ohAmtUGST;
    }

    public String getOhAmtTotal() {
        return ohAmtTotal;
    }

    public void setOhAmtTotal(String ohAmtTotal) {
        this.ohAmtTotal = ohAmtTotal;
    }

    public String getOhAtFinalDiscount() {
        return ohAtFinalDiscount;
    }

    public void setOhAtFinalDiscount(String ohAtFinalDiscount) {
        this.ohAtFinalDiscount = ohAtFinalDiscount;
    }

    public String getOhAmtFinalDiscount() {
        return ohAmtFinalDiscount;
    }

    public void setOhAmtFinalDiscount(String ohAmtFinalDiscount) {
        this.ohAmtFinalDiscount = ohAmtFinalDiscount;
    }

    public String getOhAmtRoundOff() {
        return ohAmtRoundOff;
    }

    public void setOhAmtRoundOff(String ohAmtRoundOff) {
        this.ohAmtRoundOff = ohAmtRoundOff;
    }

    public String getOhAmtNet() {
        return ohAmtNet;
    }

    public void setOhAmtNet(String ohAmtNet) {
        this.ohAmtNet = ohAmtNet;
    }

    public String getOhAmtLandedValue() {
        return ohAmtLandedValue;
    }

    public void setOhAmtLandedValue(String ohAmtLandedValue) {
        this.ohAmtLandedValue = ohAmtLandedValue;
    }

    public String getD365TimeStamp() {
        return d365TimeStamp;
    }

    public void setD365TimeStamp(String d365TimeStamp) {
        this.d365TimeStamp = d365TimeStamp;
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

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovalDateTime() {
        return approvalDateTime;
    }

    public void setApprovalDateTime(String approvalDateTime) {
        this.approvalDateTime = approvalDateTime;
    }

    public String getBusinessUnitCode() {
        return businessUnitCode;
    }

    public void setBusinessUnitCode(String businessUnitCode) {
        this.businessUnitCode = businessUnitCode;
    }

    public String getBusinessUnitName() {
        return businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    public String getRcmApplicable() {
        return rcmApplicable;
    }

    public void setRcmApplicable(String rcmApplicable) {
        this.rcmApplicable = rcmApplicable;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getGsTin() {
        return gsTin;
    }

    public void setGsTin(String gsTin) {
        this.gsTin = gsTin;
    }

    public String geteWayBillNo() {
        return eWayBillNo;
    }

    public void seteWayBillNo(String eWayBillNo) {
        this.eWayBillNo = eWayBillNo;
    }

    public String geteWayBillDt() {
        return eWayBillDt;
    }

    public void seteWayBillDt(String eWayBillDt) {
        this.eWayBillDt = eWayBillDt;
    }

    public String getLrNo() {
        return lrNo;
    }

    public void setLrNo(String lrNo) {
        this.lrNo = lrNo;
    }

    public String getLrDate() {
        return lrDate;
    }

    public void setLrDate(String lrDate) {
        this.lrDate = lrDate;
    }

    public String getGstLocation() {
        return gstLocation;
    }

    public void setGstLocation(String gstLocation) {
        this.gstLocation = gstLocation;
    }

    public String getDmStatus() {
        return dmStatus;
    }

    public void setDmStatus(String dmStatus) {
        this.dmStatus = dmStatus;
    }

    public String getDmTimeStamp() {
        return dmTimeStamp;
    }

    public void setDmTimeStamp(String dmTimeStamp) {
        this.dmTimeStamp = dmTimeStamp;
    }

    public String getDmDocId() {
        return dmDocId;
    }

    public void setDmDocId(String dmDocId) {
        this.dmDocId = dmDocId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public List<GrnLineModel> getLinedata() {
        return linedata;
    }

    public void setLinedata(List<GrnLineModel> linedata) {
        this.linedata = linedata;
    }

}
