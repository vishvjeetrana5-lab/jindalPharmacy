package aarogya.initializer.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface Initializer_SrvAsync {

    public void execute(GrdInitializer_Mdl grdMdl, AsyncCallback<DmResult<GrdInitializer_Mdl>> result);

    public void getDefaultUHID(AsyncCallback<DmResult<String>> result);

    public void getInitializerClass(String yearStart, AsyncCallback<DmResult<List<GrdInitializer_Mdl>>> result);
}
