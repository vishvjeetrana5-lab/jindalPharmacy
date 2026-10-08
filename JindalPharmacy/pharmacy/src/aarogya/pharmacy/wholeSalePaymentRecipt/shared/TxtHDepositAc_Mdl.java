package aarogya.pharmacy.wholeSalePaymentRecipt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class TxtHDepositAc_Mdl extends KeyField implements Serializable, IsSerializable {

    private String nature;

    public TxtHDepositAc_Mdl() {
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

}
