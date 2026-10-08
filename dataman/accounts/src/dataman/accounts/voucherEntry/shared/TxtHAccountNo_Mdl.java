package dataman.accounts.voucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHAccountNo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String bank;
    private String bankAddress;
    private String branch;
    private String ifscCode;
    private String swiftNo;
    private boolean defaultAcNo;

    public TxtHAccountNo_Mdl() {
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getSwiftNo() {
        return swiftNo;
    }

    public void setSwiftNo(String swiftNo) {
        this.swiftNo = swiftNo;
    }

    public boolean isDefaultAcNo() {
        return defaultAcNo;
    }

    public void setDefaultAcNo(boolean defaultAcNo) {
        this.defaultAcNo = defaultAcNo;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    

}
