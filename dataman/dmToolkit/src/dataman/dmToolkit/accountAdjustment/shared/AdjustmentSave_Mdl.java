package dataman.dmToolkit.accountAdjustment.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdjustmentSave_Mdl implements Serializable, IsSerializable {

    private String vr_DocId;
    private String vr_V_SNo;
    private String vr_SubCode;
    private String vr_V_Date;
    private List<GrdAdjustment_Mdl> lstAdjustments = new ArrayList<>();

    public AdjustmentSave_Mdl() {
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

    public List<GrdAdjustment_Mdl> getLstAdjustments() {
        return lstAdjustments;
    }

    public void setLstAdjustments(List<GrdAdjustment_Mdl> lstAdjustments) {
        this.lstAdjustments = lstAdjustments;
    }

}
