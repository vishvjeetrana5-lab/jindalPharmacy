package aarogya.pharmacy.indent.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicineWithRequisition_Mdl implements Serializable, IsSerializable {

    private String requisitionName, requisitionCode;

    public MedicineWithRequisition_Mdl() {
    }

    public String getRequisitionName() {
        return requisitionName;
    }

    public void setRequisitionName(String requisitionName) {
        this.requisitionName = requisitionName;
    }

    public String getRequisitionCode() {
        return requisitionCode;
    }

    public void setRequisitionCode(String requisitionCode) {
        this.requisitionCode = requisitionCode;
    }
    List<GrdMedicineDetail_Mdl> grdItemDetailList = new ArrayList<>();

    public List<GrdMedicineDetail_Mdl> getGrdItemDetailList() {
        return grdItemDetailList;
    }

    public void setGrdItemDetailList(List<GrdMedicineDetail_Mdl> grdItemDetailList) {
        this.grdItemDetailList = grdItemDetailList;
    }

}
