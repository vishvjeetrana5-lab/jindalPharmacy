package dataman.accounts.voucherEntryPaymentJV.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SaveRecord_Mdl implements Serializable, IsSerializable {

    private String docId;
    private String recId;
    private String voucherTypeName;
    private String voucherTypeCode;
    private String voucherCategory;
    private String voucherDate;
    private String accountName;
    private String accountNameCode;
    private String accountNameNature;

    public SaveRecord_Mdl() {
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNameCode() {
        return accountNameCode;
    }

    public void setAccountNameCode(String accountNameCode) {
        this.accountNameCode = accountNameCode;
    }

    public String getAccountNameNature() {
        return accountNameNature;
    }

    public void setAccountNameNature(String accountNameNature) {
        this.accountNameNature = accountNameNature;
    }
}
