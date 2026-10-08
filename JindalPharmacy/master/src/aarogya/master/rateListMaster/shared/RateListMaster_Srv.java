package aarogya.master.rateListMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface RateListMaster_Srv extends RemoteService {

    public DmResult<RateListMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> applyDiscountPercent(double percentValue, SearchValue_Mdl param);

    public DmResult<DefaultValue_Mdl> getDefaultValue();

    public DmResult<String> deleteRecord(String serviceCode, SearchValue_Mdl param);

    public DmResult<String> saveRecord(GrdServices_Mdl mrTemp, SearchValue_Mdl param);

    public DmResult<String> savePopup(RateListMaster_Mdl mrTemp);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getServiceNature(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceCategory(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdServices_Mdl>> getServiceAndInvestigation(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param);

    public DmResult<List<GrdRoomCategory_Mdl>> getRoomCategoryViaService(SearchValue_Mdl param);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineSaleOn(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRateListType(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> getListFromCSV(String keyValue, String applicableDate, String serverDirectory, String serverFileName);

    public DmResult<String> saveRateList(RateList_Mdl mrTemp);

    public DmResultPageing<List<RateList_Mdl>> getTxtHRateList(DmHelper pageing, boolean blnToEndOfFile);
}
