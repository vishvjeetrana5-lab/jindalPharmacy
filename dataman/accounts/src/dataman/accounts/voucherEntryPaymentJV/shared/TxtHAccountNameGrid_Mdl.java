package dataman.accounts.voucherEntryPaymentJV.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHAccountNameGrid_Mdl extends KeyField implements Serializable, IsSerializable {

    private String City;
    private String SCode;
    private String Nature;
    private String CostCenter;
    private String Group;
    private String natureManualCode;
    private String tdsCatCode;
    private String tdsCatName;
    private String bank;
    private String accountNo;
    private String branch;
    private String swiftCode;
    private String bankAddress;
    private String ifscCode;
    private boolean isDefault;

    public TxtHAccountNameGrid_Mdl() {
    }

    public String getTdsCatName() {
        return tdsCatName;
    }

    public void setTdsCatName(String tdsCatName) {
        this.tdsCatName = tdsCatName;
    }

    public String getTdsCatCode() {
        return tdsCatCode;
    }

    public void setTdsCatCode(String tdsCatCode) {
        this.tdsCatCode = tdsCatCode;
    }

    public String getNatureManualCode() {
        return natureManualCode;
    }

    public void setNatureManualCode(String natureManualCode) {
        this.natureManualCode = natureManualCode;
    }

    public String getSCode() {
        return SCode;
    }

    public void setSCode(String SCode) {
        this.SCode = SCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getNature() {
        return Nature;
    }

    public void setNature(String Nature) {
        this.Nature = Nature;
    }

    public String getCostCenter() {
        return CostCenter;
    }

    public void setCostCenter(String CostCenter) {
        this.CostCenter = CostCenter;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String Group) {
        this.Group = Group;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
