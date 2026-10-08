package dataman.dmToolkit.tdsCategoryPopup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface TdsCategoryPopup_SrvAsync {

    public void getTxtHTdsCategory(String voucherCategory, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHTdsDeduct(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTdsDeduct_Mdl>>> result);

    public void getTxtHTdsFor(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTDSData(GetTDSData_Mdl tdsData_Mdl, AsyncCallback<DmResult<TdsCategoryPopup_Mdl>> result);

}
