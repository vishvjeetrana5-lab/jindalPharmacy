package dataman.accounts.bulkChequePrinting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ChangeChqNo_Mdl extends KeyField implements Serializable, IsSerializable {

    public ChangeChqNo_Mdl() {
    }

    private String chqNo;
    private String chqDate;
    private String isCrossChqCode;
    private String isCrossChqText;
    private String docId;
    private String v_SNo;
    private String prvChqNo;

    public String getIsCrossChqCode() {
        return isCrossChqCode;
    }

    public void setIsCrossChqCode(String isCrossChqCode) {
        this.isCrossChqCode = isCrossChqCode;
    }

    public String getIsCrossChqText() {
        return isCrossChqText;
    }

    public void setIsCrossChqText(String isCrossChqText) {
        this.isCrossChqText = isCrossChqText;
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

    public String getPrvChqNo() {
        return prvChqNo;
    }

    public void setPrvChqNo(String prvChqNo) {
        this.prvChqNo = prvChqNo;
    }

    public String getChqNo() {
        return chqNo;
    }

    public void setChqNo(String chqNo) {
        this.chqNo = chqNo;
    }

    public String getChqDate() {
        return chqDate;
    }

    public void setChqDate(String chqDate) {
        this.chqDate = chqDate;
    }

}
