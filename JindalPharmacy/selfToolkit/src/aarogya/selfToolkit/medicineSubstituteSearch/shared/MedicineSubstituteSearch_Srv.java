package aarogya.selfToolkit.medicineSubstituteSearch.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface MedicineSubstituteSearch_Srv extends RemoteService {

    public DmResultPageing<List<GrdMedicineSubstituteDetails_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param);
}
