package aarogya.selfToolkit.opdSearch.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;
import java.util.List;

public interface OPDSearch_Srv extends RemoteService {

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResultPageing<List<GrdRegistration_Mdl>> getFind(int cancelDaysOPD, int serviceDaysOPD, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param);
}
