package dataman.utility.importProcedure.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ImportProcedure_SrvAsync {

    public void getProcedureList(String filePath, String fileName, AsyncCallback<DmResultPageing<List<GrdProcedure_Mdl>>> result);

    public void importProcedure(ImportProcedure_Mdl export_Mdl, AsyncCallback<DmResult<String>> result);
}
