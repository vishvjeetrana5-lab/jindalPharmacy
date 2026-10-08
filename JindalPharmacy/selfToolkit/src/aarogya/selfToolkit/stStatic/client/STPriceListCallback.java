package aarogya.selfToolkit.stStatic.client;

import aarogya.selfToolkit.stStatic.shared.ServicePriceList_Mdl;
import dataman.dmBase.self.shared.DmResult;
import java.util.List;

public interface STPriceListCallback {

    public void refresh(DmResult<List<ServicePriceList_Mdl>> result);
}
