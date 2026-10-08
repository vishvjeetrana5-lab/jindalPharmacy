package aarogya.master.eod.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class IPD_Bill_AcPosting_Mdl extends KeyField implements Serializable, IsSerializable {

    private String docID;
    private String v_Type;
    private String v_No;
    private String recIDPrefix;
    private String recID;
    private String v_Date;
    private String amount;
    private String cancellAgainstSbDocId ;
   

    public IPD_Bill_AcPosting_Mdl() {
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

    public String getV_No() {
        return v_No;
    }

    public void setV_No(String v_No) {
        this.v_No = v_No;
    }

    public String getRecIDPrefix() {
        return recIDPrefix;
    }

    public void setRecIDPrefix(String recIDPrefix) {
        this.recIDPrefix = recIDPrefix;
    }

    public String getRecID() {
        return recID;
    }

    public void setRecID(String recID) {
        this.recID = recID;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCancellAgainstSbDocId() {
        return cancellAgainstSbDocId;
    }

    public void setCancellAgainstSbDocId(String cancellAgainstSbDocId) {
        this.cancellAgainstSbDocId = cancellAgainstSbDocId;
    }


}
