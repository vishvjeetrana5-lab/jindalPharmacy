package aarogya.pharmacy.medicineIssue.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SelectedRequisition_Mdl implements Serializable, IsSerializable {

    private String docId, v_SNo;

    public SelectedRequisition_Mdl() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }
}
