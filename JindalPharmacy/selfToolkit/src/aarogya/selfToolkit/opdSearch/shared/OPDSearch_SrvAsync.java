package aarogya.selfToolkit.opdSearch.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;
import java.util.List;

public interface OPDSearch_SrvAsync {

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getFind(int cancelDaysOPD, int serviceDaysOPD, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param, AsyncCallback<DmResultPageing<List<GrdRegistration_Mdl>>> result);
}
