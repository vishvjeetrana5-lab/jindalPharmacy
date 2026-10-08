package aarogya.master.frequencyMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface FrequencyMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestionForFrequencyName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<FrequencyMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(FrequencyMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> getDefaultValues();

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);
}
