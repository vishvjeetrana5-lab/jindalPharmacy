package aarogya.master.organizationMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.util.List;

public interface OrganizationMaster_SrvAsync {

    public void getNavigator(boolean isOrganization, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getSuggestion(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<OrganizationMaster_Mdl>> result);

    public void deleteRecord(String strKey, String guiConfigCode, AsyncCallback<DmResult<String>> result);

    public void saveRecord(OrganizationMaster_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, boolean isOrganization, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHNature(DmHelper helper, boolean blnToEndOfFile, boolean isOrganization, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHTDSAc(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHActive(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void exportCSV(String sessionId, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getDefaultVal(String entryPoint, AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getTxtHRateList(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<RateList_Mdl>>> result);

    public void getRoomCategoryViaService(SearchValue_Mdl param, AsyncCallback<DmResult<List<GrdRoomCategory_Mdl>>> result);

    public void getTxtHService(DmHelper helper, String serviceCatCode, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHServiceCat(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMedicineSaleOn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getOrganizationPer(String organizationCode, String rateListCode, AsyncCallback<DmResult<List<GrdCategoryDiscount_Mdl>>> result);
    
    public void getTxtHDiscountGivenBy(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDiscountReason(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
