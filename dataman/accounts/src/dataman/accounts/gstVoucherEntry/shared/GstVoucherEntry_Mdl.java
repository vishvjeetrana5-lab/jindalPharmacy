package dataman.accounts.gstVoucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GstVoucherEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    //sharedkeyfieldCode=DocID
    private RecId objRecId = new RecId();
    private String VoucherTypeName;
    private String VoucherTypeCode;
    private String VoucherCategory;
    private String VoucherPrefix;
    private String VoucherDate;
    private String ReferenceNo;
    private String ReferenceDate;
    private String refDocId;
    private String refV_SNo;
    private String refV_Type;
    private String refRecId;
    private String refV_Date;
    private String refDocValue;
    private String gstReasonCode;
    private String gstReasonName;
    private String AccountName;
    private String AccountNameCode;
    private String accountNameNature;
    private String totalDebit;
    private String totalCredit;
    private String approvedBy;
    private String modifiedBy;
    private String preparedBy;
    private String wtaCode;
    private String remark;
    private String placeOfSupplyCode;
    private String placeOfSupplyName;
    private String consigneeType;

    private String gstTypeCode;
    private String gstTypeName;

    private DefaultValue_Mdl defaultValueMdl;
    private List<GridAcDetailsMdl> grdAcDetails= new ArrayList<GridAcDetailsMdl>();
//    private List<GridHSNDetailsMdl> grdHsnDetails= new ArrayList<GridHSNDetailsMdl>();

    public GstVoucherEntry_Mdl() {
    }

    public String getPlaceOfSupplyCode() {
        return placeOfSupplyCode;
    }

    public void setPlaceOfSupplyCode(String placeOfSupplyCode) {
        this.placeOfSupplyCode = placeOfSupplyCode;
    }

    public String getPlaceOfSupplyName() {
        return placeOfSupplyName;
    }

    public void setPlaceOfSupplyName(String placeOfSupplyName) {
        this.placeOfSupplyName = placeOfSupplyName;
    }

    public String getGstTypeCode() {
        return gstTypeCode;
    }

    public void setGstTypeCode(String gstTypeCode) {
        this.gstTypeCode = gstTypeCode;
    }

    public String getGstTypeName() {
        return gstTypeName;
    }

    public void setGstTypeName(String gstTypeName) {
        this.gstTypeName = gstTypeName;
    }

    public String getConsigneeType() {
        return consigneeType;
    }

    public void setConsigneeType(String consigneeType) {
        this.consigneeType = consigneeType;
    }

    public RecId getObjRecId() {
        return objRecId;
    }

    public void setObjRecId(RecId objRecId) {
        this.objRecId = objRecId;
    }

    public String getVoucherTypeName() {
        return VoucherTypeName;
    }

    public void setVoucherTypeName(String VoucherTypeName) {
        this.VoucherTypeName = VoucherTypeName;
    }

    public String getVoucherTypeCode() {
        return VoucherTypeCode;
    }

    public void setVoucherTypeCode(String VoucherTypeCode) {
        this.VoucherTypeCode = VoucherTypeCode;
    }

    public String getVoucherCategory() {
        return VoucherCategory;
    }

    public void setVoucherCategory(String VoucherCategory) {
        this.VoucherCategory = VoucherCategory;
    }

    public String getVoucherPrefix() {
        return VoucherPrefix;
    }

    public void setVoucherPrefix(String VoucherPrefix) {
        this.VoucherPrefix = VoucherPrefix;
    }

    public String getVoucherDate() {
        return VoucherDate;
    }

    public void setVoucherDate(String VoucherDate) {
        this.VoucherDate = VoucherDate;
    }

    public String getReferenceNo() {
        return ReferenceNo;
    }

    public void setReferenceNo(String ReferenceNo) {
        this.ReferenceNo = ReferenceNo;
    }

    public String getReferenceDate() {
        return ReferenceDate;
    }

    public void setReferenceDate(String ReferenceDate) {
        this.ReferenceDate = ReferenceDate;
    }

    public String getRefDocId() {
        return refDocId;
    }

    public void setRefDocId(String refDocId) {
        this.refDocId = refDocId;
    }

    public String getRefV_SNo() {
        return refV_SNo;
    }

    public void setRefV_SNo(String refV_SNo) {
        this.refV_SNo = refV_SNo;
    }
    
    public String getRefV_Type() {
        return refV_Type;
    }

    public void setRefV_Type(String refV_Type) {
        this.refV_Type = refV_Type;
    }

    public String getRefRecId() {
        return refRecId;
    }

    public void setRefRecId(String refRecId) {
        this.refRecId = refRecId;
    }

    public String getRefV_Date() {
        return refV_Date;
    }

    public void setRefV_Date(String refV_Date) {
        this.refV_Date = refV_Date;
    }

    
    public String getRefDocValue() {
        return refDocValue;
    }

    public void setRefDocValue(String refDocValue) {
        this.refDocValue = refDocValue;
    }


    public String getGstReasonCode() {
        return gstReasonCode;
    }

    public void setGstReasonCode(String gstReasonCode) {
        this.gstReasonCode = gstReasonCode;
    }

    public String getGstReasonName() {
        return gstReasonName;
    }

    public void setGstReasonName(String gstReasonName) {
        this.gstReasonName = gstReasonName;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String AccountName) {
        this.AccountName = AccountName;
    }

    public String getAccountNameCode() {
        return AccountNameCode;
    }

    public void setAccountNameCode(String AccountNameCode) {
        this.AccountNameCode = AccountNameCode;
    }

    public String getAccountNameNature() {
        return accountNameNature;
    }

    public void setAccountNameNature(String accountNameNature) {
        this.accountNameNature = accountNameNature;
    }

    public String getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(String totalDebit) {
        this.totalDebit = totalDebit;
    }

    public String getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(String totalCredit) {
        this.totalCredit = totalCredit;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public DefaultValue_Mdl getDefaultValueMdl() {
        return defaultValueMdl;
    }

    public void setDefaultValueMdl(DefaultValue_Mdl defaultValueMdl) {
        this.defaultValueMdl = defaultValueMdl;
    }

    public List<GridAcDetailsMdl> getGrdAcDetails() {
        return grdAcDetails;
    }

    public void setGrdAcDetails(List<GridAcDetailsMdl> grdAcDetails) {
        this.grdAcDetails = grdAcDetails;
    }

//    public List<GridHSNDetailsMdl> getGrdHsnDetails() {
//        return grdHsnDetails;
//    }
//
//    public void setGrdHsnDetails(List<GridHSNDetailsMdl> grdHsnDetails) {
//        this.grdHsnDetails = grdHsnDetails;
//    }

}
