package dataman.dmToolkit.singleLineMaster.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface SingleLineMaster_SrvAsync {

    public void getNavigator(SingleLineMaster_Mdl singleLineMaster, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getSuggestion(DmHelper helper, boolean blnToEndOfFile, SingleLineMaster_Mdl singleLineMaster, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getMoveRec(SingleLineMaster_Mdl singleLineMaster, String strKey, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void deleteRecord(SingleLineMaster_Mdl singleLineMaster, String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(SingleLineMaster_Mdl singleLineMaster, KeyFieldExtended keyField, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, SingleLineMaster_Mdl singleLineMaster, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void exportCSV(SingleLineMaster_Mdl singleLineMaster,String sessionId, String widgetName, AsyncCallback<DmResult<KeyFieldExtended>> result);
}
