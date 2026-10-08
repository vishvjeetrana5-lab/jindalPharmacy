package dataman.accounts.voucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ChequeDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String chequeNo;
    private String chequeDate;
    private String favourOf;
    private String clearDate;
    private String clearRemark;
    private String accountNo;
    private String accountCode;
    private String accountName;
    private String branch;
    private String bank;
    private String branchAddress;
    private String swiftCode;
    private String ifscCode;
    private String isCrossChequeCode;
    private String isCrossChequeText;
    private boolean chequeDetailBind;
   // private boolean defaultAc;
    
    

    public ChequeDetail_Mdl() {
    }

    public String getIsCrossChequeCode() {
        return isCrossChequeCode;
    }

    public void setIsCrossChequeCode(String isCrossChequeCode) {
        this.isCrossChequeCode = isCrossChequeCode;
    }

    public String getIsCrossChequeText() {
        return isCrossChequeText;
    }

    public void setIsCrossChequeText(String isCrossChequeText) {
        this.isCrossChequeText = isCrossChequeText;
    }

    public String getClearRemark() {
        return clearRemark;
    }

    public void setClearRemark(String clearRemark) {
        this.clearRemark = clearRemark;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getFavourOf() {
        return favourOf;
    }

    public void setFavourOf(String favourOf) {
        this.favourOf = favourOf;
    }

    public String getClearDate() {
        return clearDate;
    }

    public void setClearDate(String clearDate) {
        this.clearDate = clearDate;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public boolean isChequeDetailBind() {
        return chequeDetailBind;
    }

    public void setChequeDetailBind(boolean chequeDetailBind) {
        this.chequeDetailBind = chequeDetailBind;
    }

  
    

//    public boolean isDefaultAc() {
//        return defaultAc;
//    }
//
//    public void setDefaultAc(boolean defaultAc) {
//        this.defaultAc = defaultAc;
//    }

    public ChequeDetail_Mdl clone() {
        ChequeDetail_Mdl chequeDetailGrid = new ChequeDetail_Mdl();

        chequeDetailGrid.setChequeDate(this.getChequeDate());
        chequeDetailGrid.setChequeNo(this.getChequeNo());
        chequeDetailGrid.setClearDate(this.getClearDate());
        chequeDetailGrid.setFavourOf(this.getFavourOf());
        chequeDetailGrid.setIsCrossChequeCode(this.getIsCrossChequeCode());
        chequeDetailGrid.setIsCrossChequeText(this.getIsCrossChequeText());
        chequeDetailGrid.setClearRemark(this.getClearRemark());
        chequeDetailGrid.setAccountNo(this.getAccountNo());
        chequeDetailGrid.setAccountCode(this.getAccountCode());
        chequeDetailGrid.setAccountName(this.getAccountName());
        chequeDetailGrid.setBank(this.getBank());
        chequeDetailGrid.setBranchAddress(this.getBranchAddress());
        chequeDetailGrid.setBranch(this.getBranch());
        chequeDetailGrid.setIfscCode(this.getIfscCode());
        chequeDetailGrid.setSwiftCode(this.getSwiftCode());
        chequeDetailGrid.setChequeDetailBind(this.isChequeDetailBind());
       // chequeDetailGrid.setDefaultAc(this.isDefaultAc());

        return chequeDetailGrid;
    }

}
