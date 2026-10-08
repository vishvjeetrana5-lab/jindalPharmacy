package dataman.dmToolkit.accountAdjustment.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Filter_Mdl implements Serializable, IsSerializable {

    private String voucherNo;
    private String voucherDate;
    private String refNo;
    private String voucherTypeCode;
    private String voucherTypeName;
    private String acCode;
    private String acName;
    private boolean showDebit;

    public Filter_Mdl() {
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getAcCode() {
        return acCode;
    }

    public void setAcCode(String acCode) {
        this.acCode = acCode;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public boolean isShowDebit() {
        return showDebit;
    }

    public void setShowDebit(boolean showDebit) {
        this.showDebit = showDebit;
    }

}
