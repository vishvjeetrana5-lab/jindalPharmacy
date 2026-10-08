package aarogya.inventory.indent.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemWithRequisition_Mdl implements Serializable,IsSerializable{

    private String requisitionName, requisitionCode;

    public ItemWithRequisition_Mdl() {
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
    List<GrdItemDetail_Mdl> grdItemDetailList = new ArrayList<>();

    public List<GrdItemDetail_Mdl> getGrdItemDetailList() {
        return grdItemDetailList;
    }

    public void setGrdItemDetailList(List<GrdItemDetail_Mdl> grdItemDetailList) {
        this.grdItemDetailList = grdItemDetailList;
    }

}
