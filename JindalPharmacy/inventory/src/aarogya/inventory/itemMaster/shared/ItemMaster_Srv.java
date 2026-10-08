package aarogya.inventory.itemMaster.shared;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface ItemMaster_Srv extends RemoteService, IWalkThrough_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestionItem(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<ItemMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ItemMaster_Mdl mrTemp, boolean isApprove);

    public DmResult<String> setActive(String activeBy, boolean isActive, String docId);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper pageing, boolean blnToEndOfFile, boolean isActiveCall);

    public DmResultPageing<List<KeyFieldExtended>> getSKU(String sku, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Sku_Mdl>> getSKUDetails(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCategory_Mdl>> getTxtHCategory(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHGroup_Mdl>> getTxtHItemGroup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemType_Mdl>> getTxtHItemType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHApplicableBatch(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile);

//    public DmResultPageing<List<TxtHHSN_Mdl>> getTxtHHSN(DmHelper pageing, boolean blnToEndOfFile);
    public DmResultPageing<List<TxtHItemsName_Mdl>> getTxtHItemName(String itemCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRestrictedDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHApprovedVendors_Mdl>> getTxtHApprovedVendors(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVED(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemPrperty_Mdl>> getTxtHProperty(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHHSN_Mdl>> getTxtHHSN(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLabRequired(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHImpurity(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGSTNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHITSBasedOn(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSubCode(String subgroupNature, DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<TxtHManufacturer_Mdl>> getTxtHManufacturer(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHDivision_Mdl>> getTxtHDivision(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<TxtHItemMake_Mdl>> getTxtHItemMake(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<TxtHMarketingCo_Mdl>> getTxtHMarketingCo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemSpeciality(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<List<GrdRestrictedDept_Mdl>> getGroupRestrictedDepartment(String strKey);
    
    public DmResultPageing<List<TxtHMaterialCategory_Mdl>> getTxtHMaterialCategory(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMaterialType(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemModel(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemStrength(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<DefaultValue_Mdl> getDefaultValues();
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBudgerHead(DmHelper pageing, boolean blnToEndOfFile);
    
}
