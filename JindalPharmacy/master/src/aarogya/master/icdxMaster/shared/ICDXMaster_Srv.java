package aarogya.master.icdxMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ICDXMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestionICDXCode(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<String>> getSuggestionICDXName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<ICDXMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ICDXMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);
}
