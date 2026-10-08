package aarogya.inventory.gateOutSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface GateOutSearch_Srv extends RemoteService {

    public DmResultPageing<List<GrdGateOutDetailsSearch_Mdl>> getGateOutSearchDetail(String itemCode, String v_Date, String voucherCategory, GateOutSearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentGateOutSearch(DmHelper helper, boolean blnToEndOfFile);

}
