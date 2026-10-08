package aarogya.selfToolkit.otPatientSearch.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import java.util.List;

public interface OTPatientSearch_Srv extends RemoteService {

    public DmResultPageing<List<GrdOTPatientSearch_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile, OTSearchValue_Mdl param);
}
