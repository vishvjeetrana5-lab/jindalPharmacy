package aarogya.master.consultantMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdService_Mdl extends KeyField implements Serializable, IsSerializable {

    private String amount;
    private List<GrdRoomCategory_Mdl> lstRoomCategory = new ArrayList<GrdRoomCategory_Mdl>();

    public GrdService_Mdl() {
    }

    public List<GrdRoomCategory_Mdl> getLstRoomCategory() {
        return lstRoomCategory;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public GrdService_Mdl clone() {
        GrdService_Mdl local = new GrdService_Mdl();
        List<GrdRoomCategory_Mdl> lstCategory = new ArrayList<GrdRoomCategory_Mdl>();

        super.copy(local);

        local.setAmount(this.getAmount());
        if (this.getLstRoomCategory() != null) {
            for (int i = 0; i < getLstRoomCategory().size(); i++) {
                lstCategory.add(this.getLstRoomCategory().get(i).clone());
            }
            this.getLstRoomCategory().addAll(lstCategory);
        }

        return local;
    }

}
