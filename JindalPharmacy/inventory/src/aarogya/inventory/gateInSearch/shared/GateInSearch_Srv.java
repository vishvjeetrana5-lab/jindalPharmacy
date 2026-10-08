package aarogya.inventory.gateInSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface GateInSearch_Srv extends RemoteService {

    public DmResultPageing<List<GrdGateInDetailsSearch_Mdl>> getGateInSearchDetail(String itemCode, String v_Date, String voucherCategory, GateInSearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentGateOutSearch(DmHelper helper, boolean blnToEndOfFile);

}
