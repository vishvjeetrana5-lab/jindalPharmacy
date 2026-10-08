package dataman.accounts.bulkChequePrinting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Print_Mdl implements Serializable, IsSerializable {

    public Print_Mdl() {
    }

    private String docId;
    private String chqNo;
    private String amount;
    private String favourOf;
    private String v_Sno;

    public String getV_Sno() {
        return v_Sno;
    }

    public void setV_Sno(String v_Sno) {
        this.v_Sno = v_Sno;
    }

    public String getFavourOf() {
        return favourOf;
    }

    public void setFavourOf(String favourOf) {
        this.favourOf = favourOf;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getChqNo() {
        return chqNo;
    }

    public void setChqNo(String chqNo) {
        this.chqNo = chqNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
