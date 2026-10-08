package aarogya.pharmacy.saleBillWholeSale.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SearchUHID_Mdl implements Serializable, IsSerializable {

    private String docIdUHID, recIdUHID;
    private String docIdReg, recIdReg;
    private String voucherCategory, voucherDate;

    public SearchUHID_Mdl() {
    }

    public String getDocIdUHID() {
        return docIdUHID;
    }

    public void setDocIdUHID(String docIdUHID) {
        this.docIdUHID = docIdUHID;
    }

    public String getRecIdUHID() {
        return recIdUHID;
    }

    public void setRecIdUHID(String recIdUHID) {
        this.recIdUHID = recIdUHID;
    }

    public String getDocIdReg() {
        return docIdReg;
    }

    public void setDocIdReg(String docIdReg) {
        this.docIdReg = docIdReg;
    }

    public String getRecIdReg() {
        return recIdReg;
    }

    public void setRecIdReg(String recIdReg) {
        this.recIdReg = recIdReg;
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

    private SearchUHID_Mdl(String docIdUHID, String recIdUHID, String docIdReg, String recIdReg, String voucherCategory, String voucherDate) {
        this.docIdUHID = docIdUHID;
        this.recIdUHID = recIdUHID;
        this.docIdReg = docIdReg;
        this.recIdReg = recIdReg;
        this.voucherCategory = voucherCategory;
        this.voucherDate = voucherDate;
    }

    public SearchUHID_Mdl(SearchUHID_Mdl mdl) {
        this(mdl.getDocIdUHID(), mdl.getRecIdUHID(), mdl.getDocIdReg(), mdl.getRecIdReg(), mdl.getVoucherCategory(), mdl.getVoucherDate());
    }

}
