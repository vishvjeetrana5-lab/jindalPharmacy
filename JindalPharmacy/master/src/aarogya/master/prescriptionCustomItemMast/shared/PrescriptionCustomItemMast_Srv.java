package aarogya.master.prescriptionCustomItemMast.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PrescriptionCustomItemMast_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestionName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<PrescriptionCustomItemMast_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(PrescriptionCustomItemMast_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroupName(String nature, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> getDefaultValues();

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);
}
