package aarogya.inventory.unitMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface UnitMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<UnitMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UnitMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGSTUnit(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<String>> getSuggestionUnit(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> iniDefaultValues();

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);

}
