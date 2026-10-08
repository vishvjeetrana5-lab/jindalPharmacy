package aarogya.pharmacy.medicineModification.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHMedGrp_Mdl extends KeyField implements Serializable, IsSerializable {

    private String saleDisPer;

    public TxtHMedGrp_Mdl() {
    }

    public String getSaleDisPer() {
        return saleDisPer;
    }

    public void setSaleDisPer(String saleDisPer) {
        this.saleDisPer = saleDisPer;
    }
}
