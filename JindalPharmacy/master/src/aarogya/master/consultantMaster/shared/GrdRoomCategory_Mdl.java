package aarogya.master.consultantMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdRoomCategory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String amount;
    private String serviceTag;
    private String serviceText;

    public GrdRoomCategory_Mdl() {
    }

    public String getServiceText() {
        return serviceText;
    }

    public void setServiceText(String serviceText) {
        this.serviceText = serviceText;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getServiceTag() {
        return serviceTag;
    }

    public void setServiceTag(String serviceTag) {
        this.serviceTag = serviceTag;
    }

    //<editor-fold defaultstate="collapsed" desc="clone">
    public GrdRoomCategory_Mdl clone() {
        GrdRoomCategory_Mdl local = new GrdRoomCategory_Mdl();

        super.copy(local);

        local.setAmount(this.getAmount());
        local.setServiceTag(this.getServiceTag());

        return local;
    }
//</editor-fold>

}
