package aarogya.inventory.itemMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface ItemMaster_SrvAsync extends IWalkThrough_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getSuggestionItem(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<ItemMaster_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(ItemMaster_Mdl mrTemp, boolean isActive, AsyncCallback<DmResult<String>> result);

    public void setActive(String activeBy, boolean isActive, String docId, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, boolean isActiveCall, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getSKU(String sku, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getSKUDetails(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Sku_Mdl>>> result);

    public void getTxtHCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCategory_Mdl>>> result);

    public void getTxtHItemGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHGroup_Mdl>>> result);

    public void getTxtHItemType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemType_Mdl>>> result);

    public void getTxtHApplicableBatch(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

//    public void getTxtHHSN(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHHSN_Mdl>>> result);
    public void getTxtHItemName(String itemCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemsName_Mdl>>> result);

    public void getTxtHRestrictedDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHApprovedVendors(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHApprovedVendors_Mdl>>> result);

    public void getTxtHVED(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHProperty(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemPrperty_Mdl>>> result);

    public void getTxtHHSN(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHHSN_Mdl>>> result);

    public void exportCSV(String sessionId, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getTxtHLabRequired(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHImpurity(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHGSTNature(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHITSBasedOn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHGstType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHSubCode(String subgroupNature, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHManufacturer(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHManufacturer_Mdl>>> result);

    public void getTxtHDivision(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHDivision_Mdl>>> result);

    public void getTxtHItemMake(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemMake_Mdl>>> result);
    
    public void getTxtHMarketingCo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMarketingCo_Mdl>>> result);

    public void getTxtHItemSpeciality(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void getGroupRestrictedDepartment (String strKey,AsyncCallback<DmResult<List<GrdRestrictedDept_Mdl>>> result);
    
    public void getTxtHMaterialCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMaterialCategory_Mdl>>> result);
    
    public void getTxtHMaterialType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void getTxtHItemModel(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void getTxtHItemStrength(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);
    
    public void getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile,AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void getTxtHBudgerHead(DmHelper pageing, boolean blnToEndOfFile,AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
}
