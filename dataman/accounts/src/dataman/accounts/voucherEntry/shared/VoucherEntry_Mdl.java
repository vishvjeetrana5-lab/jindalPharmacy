package dataman.accounts.voucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VoucherEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private String VoucherTypeName;
    private String VoucherTypeCode;
    private String VoucherCategory;
    private String VoucherPrefix;
    private String VoucherDate;
    private String CosetCenterName;
    private String CosetCenterCode;
    private String ReferenceNo;
    private String ReferenceDate;
    private String AccountName;
    private String AccountNameCode;
    private String accountNameNature;
    private String accountManualCode;

    private String totalDebit;
    private String totalCredit;
    private String approvedBy;
    private String modifiedBy;
    private String preparedBy;
    private String isAccountNameEnable;
    private String wtaCode;
    private String remark;
    private List<GrdVoucherEntry_Mdl> grdData = new ArrayList<>();

    public VoucherEntry_Mdl() {
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public RecId getObjRecId() {
        return objRecId;
    }

    public void setObjRecId(RecId objRecId) {
        this.objRecId = objRecId;
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

    public List<GrdVoucherEntry_Mdl> getGrdData() {
        return grdData;
    }

    public void setGrdData(List<GrdVoucherEntry_Mdl> grdData) {
        this.grdData = grdData;
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

    public String getVoucherCategory() {
        return VoucherCategory;
    }

    public void setVoucherCategory(String VoucherCategory) {
        this.VoucherCategory = VoucherCategory;
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

    public String getVoucherDate() {
        return VoucherDate;
    }

    public void setVoucherDate(String VoucherDate) {
        this.VoucherDate = VoucherDate;
    }

    public String getCosetCenterName() {
        return CosetCenterName;
    }

    public void setCosetCenterName(String CosetCenterName) {
        this.CosetCenterName = CosetCenterName;
    }

    public String getCosetCenterCode() {
        return CosetCenterCode;
    }

    public void setCosetCenterCode(String CosetCenterCode) {
        this.CosetCenterCode = CosetCenterCode;
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

    public List<GrdVoucherEntry_Mdl> getgrdData() {
        return grdData;
    }

    public void setgrdData(List<GrdVoucherEntry_Mdl> strGrdData) {
        this.grdData = strGrdData;
    }

    public String getVoucherPrefix() {
        return VoucherPrefix;
    }

    public void setVoucherPrefix(String VoucherPrefix) {
        this.VoucherPrefix = VoucherPrefix;
    }

    public String getAccountNameNature() {
        return accountNameNature;
    }

    public void setAccountNameNature(String accountNameNature) {
        this.accountNameNature = accountNameNature;
    }

    public String getAccountManualCode() {
        return accountManualCode;
    }

    public void setAccountManualCode(String accountManualCode) {
        this.accountManualCode = accountManualCode;
    }

    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

 
    public VoucherEntry_Mdl clone() {
        VoucherEntry_Mdl voucherEntry = new VoucherEntry_Mdl();

        super.copy(voucherEntry);

        voucherEntry.setVoucherTypeName(this.getVoucherTypeName());
        voucherEntry.setVoucherTypeCode(this.getVoucherTypeCode());
        voucherEntry.setVoucherCategory(this.getVoucherCategory());

        voucherEntry.setVoucherPrefix(this.getVoucherPrefix());
        voucherEntry.setVoucherDate(this.getVoucherDate());

        voucherEntry.setCosetCenterName(this.getCosetCenterName());
        voucherEntry.setCosetCenterCode(this.getCosetCenterCode());

        voucherEntry.setReferenceNo(this.getReferenceNo());
        voucherEntry.setReferenceDate(this.getReferenceDate());

        voucherEntry.setAccountName(this.getAccountName());
        voucherEntry.setAccountNameCode(this.getAccountNameCode());
        voucherEntry.setAccountNameNature(this.getAccountNameNature());
        voucherEntry.setAccountManualCode(this.getAccountManualCode());
        voucherEntry.setTotalCredit(this.getTotalCredit());
        voucherEntry.setTotalDebit(this.getTotalDebit());
        voucherEntry.setRemark(this.getRemark());

        voucherEntry.setgrdData(cloneGridData(getgrdData()));

        return voucherEntry;
    }

    public List<GrdVoucherEntry_Mdl> cloneGridData(List<GrdVoucherEntry_Mdl> list) {
        List<GrdVoucherEntry_Mdl> listGrdVoucherEntry = null;

        if (list != null) {
            listGrdVoucherEntry = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                listGrdVoucherEntry.add(list.get(i).clone());
            }
        }

        return listGrdVoucherEntry;
    }

    public String getIsAccountNameEnable() {
        return isAccountNameEnable;
    }

    public void setIsAccountNameEnable(String isAccountNameEnable) {
        this.isAccountNameEnable = isAccountNameEnable;
    }

}
