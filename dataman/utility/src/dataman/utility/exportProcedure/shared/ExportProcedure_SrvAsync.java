package dataman.utility.exportProcedure.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ExportProcedure_SrvAsync {

    public void getProcedureList(AsyncCallback<DmResultPageing<List<GrdProcedure_Mdl>>> result);

    public void exportProcedure(ExportProcedure_Mdl export_Mdl, AsyncCallback<DmResult<KeyFieldExtended>> result);
}
