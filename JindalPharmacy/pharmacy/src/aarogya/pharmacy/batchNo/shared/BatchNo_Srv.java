package aarogya.pharmacy.batchNo.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
public interface BatchNo_Srv extends RemoteService {

    public DmResultPageing<List<BatchNo_Mdl>> getBatchNoDetail(BatchNoSearchValue_Mdl searchValue_Mdl,MedicineSaleOn sRateType, String profitPercentage, boolean isAllowDiscountToParty, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> getDefaultValue(String itemCode);

}
