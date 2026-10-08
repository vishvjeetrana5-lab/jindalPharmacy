package aarogya.selfToolkit.uhidPopup.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface UhidPopup_SrvAsync {

    public void getUHIDDetails(String IpdRecId, String IpdDoc, String docIdRegistration, String recIdRegistration, AsyncCallback<DmResult<UhidPopup_Mdl>> result);

    public void getTxtHIdType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHPrefixPatient(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
