package aarogya.master.receiptBookMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReceiptBookMapping_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<GrdReceiptMapping_Mdl> grdMappingReceipt = new ArrayList<GrdReceiptMapping_Mdl>();

    public ReceiptBookMapping_Mdl() {
    }

    public List<GrdReceiptMapping_Mdl> getGrdMappingReceipt() {
        return grdMappingReceipt;
    }

    public void setGrdMappingReceipt(List<GrdReceiptMapping_Mdl> grdMappingReceipt) {
        this.grdMappingReceipt = grdMappingReceipt;
    }

}
