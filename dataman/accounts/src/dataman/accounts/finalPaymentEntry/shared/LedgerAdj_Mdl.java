package dataman.accounts.finalPaymentEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class LedgerAdj_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vr_DocId;
    private String vr_V_SNo;
    private String vr_SubCode;
    private String vr_V_Date;
    private String adj_DocId;
    private String adj_V_SNo;
    private String adj_SubCode;
    private String adj_V_Date;
    private String adjustment;

    public LedgerAdj_Mdl() {

    }

    public String getVr_DocId() {
        return vr_DocId;
    }

    public void setVr_DocId(String vr_DocId) {
        this.vr_DocId = vr_DocId;
    }

    public String getVr_V_SNo() {
        return vr_V_SNo;
    }

    public void setVr_V_SNo(String vr_V_SNo) {
        this.vr_V_SNo = vr_V_SNo;
    }

    public String getVr_SubCode() {
        return vr_SubCode;
    }

    public void setVr_SubCode(String vr_SubCode) {
        this.vr_SubCode = vr_SubCode;
    }

    public String getVr_V_Date() {
        return vr_V_Date;
    }

    public void setVr_V_Date(String vr_V_Date) {
        this.vr_V_Date = vr_V_Date;
    }

    public String getAdj_DocId() {
        return adj_DocId;
    }

    public void setAdj_DocId(String adj_DocId) {
        this.adj_DocId = adj_DocId;
    }

    public String getAdj_SubCode() {
        return adj_SubCode;
    }

    public void setAdj_SubCode(String adj_SubCode) {
        this.adj_SubCode = adj_SubCode;
    }

    public String getAdj_V_Date() {
        return adj_V_Date;
    }

    public void setAdj_V_Date(String adj_V_Date) {
        this.adj_V_Date = adj_V_Date;
    }

    public String getAdj_V_SNo() {
        return adj_V_SNo;
    }

    public void setAdj_V_SNo(String adj_V_SNo) {
        this.adj_V_SNo = adj_V_SNo;
    }

    public String getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(String adjustment) {
        this.adjustment = adjustment;
    }

}
