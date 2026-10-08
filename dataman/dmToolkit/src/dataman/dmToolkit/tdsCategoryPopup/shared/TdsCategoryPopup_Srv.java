package dataman.dmToolkit.tdsCategoryPopup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface TdsCategoryPopup_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTdsCategory(String voucherCategory, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHTdsDeduct_Mdl>> getTxtHTdsDeduct(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTdsFor(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<TdsCategoryPopup_Mdl> getTDSData(GetTDSData_Mdl tdsData_Mdl);

}
