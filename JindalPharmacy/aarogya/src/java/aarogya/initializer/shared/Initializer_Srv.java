package aarogya.initializer.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface Initializer_Srv extends RemoteService {

    public DmResult<GrdInitializer_Mdl> execute(GrdInitializer_Mdl grdData);

    public DmResult<String> getDefaultUHID();

    public DmResult<List<GrdInitializer_Mdl>> getInitializerClass(String yearStart);
}
