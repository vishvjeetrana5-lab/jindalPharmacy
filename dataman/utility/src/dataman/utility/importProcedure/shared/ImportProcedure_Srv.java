package dataman.utility.importProcedure.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ImportProcedure_Srv extends RemoteService {

    public DmResultPageing<List<GrdProcedure_Mdl>> getProcedureList(String filePath, String fileName);

    public DmResult<String> importProcedure(ImportProcedure_Mdl export_Mdl);

}
