package aarogya.pharmacy.medicineCompositionMaster.shared;

import aarogya.pharmacy.medicineCompositionMaster.shared.*;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface MedicineCompositionMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<MedicineCompositionMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(MedicineCompositionMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getIsActive(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHResGroup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);
}
