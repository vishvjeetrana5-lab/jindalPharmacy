package aarogya.pharmacy.saleBillSearch.shared;

import aarogya.pharmacy.changePaymentSource.shared.ChangePaymentSource_Mdl.SaleBillSearchType;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import java.util.List;

public interface SaleBillSearch_Srv extends RemoteService {

    public DmResultPageing<List<GrdSaleBillSearch_Mdl>> getFind(String type, DmHelper helper, boolean blnToEndOfFile, SearchValueWise_Mdl param);
}
