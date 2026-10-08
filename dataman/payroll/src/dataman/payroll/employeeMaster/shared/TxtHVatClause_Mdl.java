package dataman.payroll.employeeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class TxtHVatClause_Mdl extends KeyField implements Serializable, IsSerializable {

    private String VatNonVat;

    public TxtHVatClause_Mdl() {
    }

    public String getVatNonVat() {
        return VatNonVat;
    }

    public void setVatNonVat(String VatNonVat) {
        this.VatNonVat = VatNonVat;
    }
}
