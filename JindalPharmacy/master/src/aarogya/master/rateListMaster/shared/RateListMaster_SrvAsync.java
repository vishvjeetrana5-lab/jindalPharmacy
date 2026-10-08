package aarogya.master.rateListMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface RateListMaster_SrvAsync {

    public void getMoveRec(String strKey, AsyncCallback<DmResult<RateListMaster_Mdl>> result);

    public void applyDiscountPercent(double percentValue, SearchValue_Mdl param, AsyncCallback<DmResult<String>> result);

    public void getDefaultValue(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void deleteRecord(String serviceCode, SearchValue_Mdl param, AsyncCallback<DmResult<String>> result);

    public void saveRecord(GrdServices_Mdl mrTemp, SearchValue_Mdl param, AsyncCallback<DmResult<String>> result);

    public void savePopup(RateListMaster_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getServiceNature(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHServiceCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getServiceAndInvestigation(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param, AsyncCallback<DmResultPageing<List<GrdServices_Mdl>>> result);

    public void getRoomCategoryViaService(SearchValue_Mdl param, AsyncCallback<DmResult<List<GrdRoomCategory_Mdl>>> result);

    public void getTxtHMedicineSaleOn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRateListType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getListFromCSV(String keyValue, String applicableDate, String serverDirectory, String serverFileName, AsyncCallback<DmResult<String>> result);

    public void saveRateList(RateList_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHRateList(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<RateList_Mdl>>> result);
}
