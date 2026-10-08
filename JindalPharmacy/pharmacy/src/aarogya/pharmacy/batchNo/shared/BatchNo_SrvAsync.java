package aarogya.pharmacy.batchNo.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface BatchNo_SrvAsync {

    public void getBatchNoDetail(BatchNoSearchValue_Mdl searchValue_Mdl,STEnum.MedicineSaleOn sRateType, String profitPercentage, boolean isAllowDiscountToParty, DmHelper helper, boolean blnToEndOfFile,  AsyncCallback<DmResultPageing<List<BatchNo_Mdl>>> result);

    public void getDefaultValue(String itemCode, AsyncCallback<DmResult<String>> result);

}
