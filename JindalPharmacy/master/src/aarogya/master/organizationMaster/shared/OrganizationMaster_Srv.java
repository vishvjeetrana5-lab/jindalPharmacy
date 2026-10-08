package aarogya.master.organizationMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.util.List;

public interface OrganizationMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(boolean isOrganization);

    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<String>> getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<OrganizationMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(OrganizationMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey, String guiConfigCode);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile, boolean isOrganization);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile, boolean isOrganization);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTDSAc(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);

    public DmResult<DefaultValue_Mdl> getDefaultVal(String entryPoint);

    public DmResultPageing<List<RateList_Mdl>> getTxtHRateList(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdRoomCategory_Mdl>> getRoomCategoryViaService(SearchValue_Mdl param);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHService(DmHelper helper, String serviceCatCode, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceCat(DmHelper helper, boolean blnToEndOfFile);


    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineSaleOn(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdCategoryDiscount_Mdl>> getOrganizationPer(String organizationCode, String rateListCode);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDiscountGivenBy(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDiscountReason(DmHelper helper, boolean blnToEndOfFile);

}
