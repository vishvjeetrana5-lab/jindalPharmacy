package dataman.dmToolkit.singleLineMaster.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface SingleLineMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(SingleLineMaster_Mdl singleLineMaster);

    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile, SingleLineMaster_Mdl singleLineMaster);

    public DmResult<KeyFieldExtended> getMoveRec(SingleLineMaster_Mdl singleLineMaster, String strKey);

    public DmResult<String> saveRecord(SingleLineMaster_Mdl singleLineMaster, KeyFieldExtended mrTemp);

    public DmResult<String> deleteRecord(SingleLineMaster_Mdl singleLineMaster, String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getFind(DmHelper pageing, boolean blnToEndOfFile, SingleLineMaster_Mdl singleLineMaster);

    public DmResult<KeyFieldExtended> exportCSV(SingleLineMaster_Mdl singleLineMaster,String sessionId, String widgetName);
}
