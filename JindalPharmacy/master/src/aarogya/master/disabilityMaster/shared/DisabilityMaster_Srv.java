package aarogya.master.disabilityMaster.shared;

import aarogya.master.disabilityMaster.shared.*;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface DisabilityMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestionDisCode(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<String>> getSuggestionDisName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<DisabilityMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(DisabilityMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

  
}
