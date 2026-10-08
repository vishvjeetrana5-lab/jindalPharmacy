package aarogya.selfToolkit.uhidPopup.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface UhidPopup_Srv extends RemoteService {

    public DmResult<UhidPopup_Mdl> getUHIDDetails(String IpdRecId, String IpdDocid, String docIdRegistration, String recIdRegistration);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIdType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPrefixPatient(DmHelper helper, boolean blnToEndOfFile);

}
