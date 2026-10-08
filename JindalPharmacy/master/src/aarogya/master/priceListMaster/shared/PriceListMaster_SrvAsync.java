package aarogya.master.priceListMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PriceListMaster_SrvAsync {

    public void applyDiscountPercent(double percentValue, SearchValue_Mdl param, AsyncCallback<DmResult<String>> result);

    public void getDefaultValue(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void deleteRecord(String serviceCode, SearchValue_Mdl param, AsyncCallback<DmResult<String>> result);

    public void saveRecord(GrdItemDetail_Mdl mrTemp, SearchValue_Mdl param, AsyncCallback<DmResult<String>> result);

    //public void savePopup(RateListMaster_Mdl mrTemp, AsyncCallback<DmResult<String>> result);
    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getItemDetailGrid(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param, AsyncCallback<DmResultPageing<List<GrdItemDetail_Mdl>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHPriceListType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getListFromCSV(String keyValue, String applicableDate, String serverDirectory, String serverFileName, AsyncCallback<DmResult<String>> result);

    public void savePriceList(PriceList_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHPriceList(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<PriceList_Mdl>>> result);

    public void getTxtHApplicableOn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);
}
