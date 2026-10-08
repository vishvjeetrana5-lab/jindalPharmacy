package dataman.accounts.bulkChequePrinting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BulkChequePrinting_Mdl implements Serializable, IsSerializable {

    public BulkChequePrinting_Mdl() {
    }

    private List<GrdBulkChequePrinting_Mdl> grdChequeDetail_Mdl = new ArrayList<GrdBulkChequePrinting_Mdl>();
    private String docId_V_SNoCSV;
    private String fromDate;
    private String toDate;
    private String contraSub;
    private String voucherNoDocid;
    private String voucherNo;
    private String accountCode;
    private String v_type;
    private String recordTypeCode;
    private String recordTypeName;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getContraSub() {
        return contraSub;
    }

    public void setContraSub(String contraSub) {
        this.contraSub = contraSub;
    }

    public String getVoucherNoDocid() {
        return voucherNoDocid;
    }

    public void setVoucherNoDocid(String voucherNoDocid) {
        this.voucherNoDocid = voucherNoDocid;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getV_type() {
        return v_type;
    }

    public void setV_type(String v_type) {
        this.v_type = v_type;
    }

    public String getRecordTypeCode() {
        return recordTypeCode;
    }

    public void setRecordTypeCode(String recordTypeCode) {
        this.recordTypeCode = recordTypeCode;
    }

    public String getRecordTypeName() {
        return recordTypeName;
    }

    public void setRecordTypeName(String recordTypeName) {
        this.recordTypeName = recordTypeName;
    }

    public String getDocId_V_SNoCSV() {
        return docId_V_SNoCSV;
    }

    public void setDocId_V_SNoCSV(String docId_V_SNoCSV) {
        this.docId_V_SNoCSV = docId_V_SNoCSV;
    }

    public List<GrdBulkChequePrinting_Mdl> getGrdChequeDetail_Mdl() {
        return grdChequeDetail_Mdl;
    }

    public void setGrdChequeDetail_Mdl(List<GrdBulkChequePrinting_Mdl> grdChequeDetail_Mdl) {
        this.grdChequeDetail_Mdl = grdChequeDetail_Mdl;
    }

}
