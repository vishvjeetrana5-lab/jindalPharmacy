package aarogya.inventory.gateOutSearch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface GateOutSearch_SrvAsync {

    public void getGateOutSearchDetail(String itemCode, String v_Date, String voucherCategory, GateOutSearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdGateOutDetailsSearch_Mdl>>> result);

    public void getTxtHDepartmentGateOutSearch(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
