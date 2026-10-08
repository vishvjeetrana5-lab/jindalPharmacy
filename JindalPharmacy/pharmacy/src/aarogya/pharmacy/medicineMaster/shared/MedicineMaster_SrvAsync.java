package aarogya.pharmacy.medicineMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface MedicineMaster_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<MedicineMaster_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(MedicineMaster_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, boolean isActiveCall, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItemName(String itemCode, boolean isFromScheme, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemsName_Mdl>>> result);

    public void getTxtHRestrictedDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHApprovedVendors(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHApprovedVendors_Mdl>>> result);

    public void getTxtHMedNature(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getSKUDetails(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Sku_Mdl>>> result);

    public void getTxtHComposition(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHComposition_Mdl>>> result);

    public void getTxtHCompany(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCompany_Mdl>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSubCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHHSN(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHHSN_Mdl>>> result);

    public void getSuggestionMedicine(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getTxtHMedGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedGrp_Mdl>>> result);

    public void getTxtHCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCategory_Mdl>>> result);

    public void getTxtHMaterialCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMaterialCategory_Mdl>>> result);

    public void getSKU(String sku, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Sku_Mdl>>> result);

    public void getTxtHManufacturer(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHManufacturer_Mdl>>> result);

    public void getTxtHDivision(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHDivision_Mdl>>> result);

    public void getDefaultVal(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void exportJson(String sessionId, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getTxtHITS(String nature, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getHSNDetail(String hsnCode, AsyncCallback<DmResult<HSNDetail_Mdl>> result);

    public void getTxtHItemModel(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItemStrength(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMarginRateFix(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);
}
