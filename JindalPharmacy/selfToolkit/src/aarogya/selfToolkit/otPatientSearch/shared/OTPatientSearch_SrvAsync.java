package aarogya.selfToolkit.otPatientSearch.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

public interface OTPatientSearch_SrvAsync {

    public void getFind(DmHelper helper, boolean blnToEndOfFile, OTSearchValue_Mdl param, AsyncCallback<DmResultPageing<List<GrdOTPatientSearch_Mdl>>> result);
}
