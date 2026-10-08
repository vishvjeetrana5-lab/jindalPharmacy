package aarogya.selfToolkit.discountPopup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface DiscountPopup_SrvAsync {

    public void getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile, String vType, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHGroupName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDiscountReason(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
