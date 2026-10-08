package aarogya.inventory.gateInSearch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface GateInSearch_SrvAsync {

    public void getGateInSearchDetail(String itemCode, String v_Date, String voucherCategory, GateInSearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdGateInDetailsSearch_Mdl>>> result);

    public void getTxtHDepartmentGateOutSearch(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
