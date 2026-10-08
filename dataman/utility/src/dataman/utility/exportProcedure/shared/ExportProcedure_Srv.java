package dataman.utility.exportProcedure.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ExportProcedure_Srv extends RemoteService {

    public DmResultPageing<List<GrdProcedure_Mdl>> getProcedureList();

    public DmResult<KeyFieldExtended> exportProcedure(ExportProcedure_Mdl export_Mdl);

}
