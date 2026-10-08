package dataman.accounts.gstVoucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String V_Date;
    private String VoucherType;
    private String VoucherDate;
    private String Account1;
    private String Account2;
    private String Debit;
    private String Credit;
    private String Naration;
    private String PreparedBy;
    private String ModifiyedBy;
    private String PostedBy;
    private String entryDtTime;

    public Find_Mdl() {
    }

    public String getV_Date() {
        return V_Date;
    }

    public void setV_Date(String V_Date) {
        this.V_Date = V_Date;
    }

    public String getVoucherType() {
        return VoucherType;
    }

    public void setVoucherType(String VoucherType) {
        this.VoucherType = VoucherType;
    }

    public String getVoucherDate() {
        return VoucherDate;
    }

    public void setVoucherDate(String VoucherDate) {
        this.VoucherDate = VoucherDate;
    }

    public String getAccount1() {
        return Account1;
    }

    public void setAccount1(String Account1) {
        this.Account1 = Account1;
    }

    public String getAccount2() {
        return Account2;
    }

    public void setAccount2(String Account2) {
        this.Account2 = Account2;
    }

    public String getDebit() {
        return Debit;
    }

    public void setDebit(String Debit) {
        this.Debit = Debit;
    }

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String Credit) {
        this.Credit = Credit;
    }

    public String getNaration() {
        return Naration;
    }

    public void setNaration(String Naration) {
        this.Naration = Naration;
    }

    public String getPreparedBy() {
        return PreparedBy;
    }

    public void setPreparedBy(String PreparedBy) {
        this.PreparedBy = PreparedBy;
    }

    public String getModifiyedBy() {
        return ModifiyedBy;
    }

    public void setModifiyedBy(String ModifiyedBy) {
        this.ModifiyedBy = ModifiyedBy;
    }

    public String getPostedBy() {
        return PostedBy;
    }

    public void setPostedBy(String PostedBy) {
        this.PostedBy = PostedBy;
    }

    public String getEntryDtTime() {
        return entryDtTime;
    }

    public void setEntryDtTime(String entryDtTime) {
        this.entryDtTime = entryDtTime;
    }

}
