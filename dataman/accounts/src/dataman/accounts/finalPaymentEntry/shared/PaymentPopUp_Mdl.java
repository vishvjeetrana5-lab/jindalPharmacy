
package dataman.accounts.finalPaymentEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaymentPopUp_Mdl extends KeyField implements Serializable, IsSerializable  {
   
    List<GrdPayment_Mdl>lstPaymentMdl= new ArrayList<>();
    public PaymentPopUp_Mdl()
    {
        
    }

    public List<GrdPayment_Mdl> getLstPaymentMdl() {
        return lstPaymentMdl;
    }

    
}
