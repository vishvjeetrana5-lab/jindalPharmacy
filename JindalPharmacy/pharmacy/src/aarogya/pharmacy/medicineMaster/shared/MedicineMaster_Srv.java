package aarogya.pharmacy.medicineMaster.shared;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.user.client.rpc.RemoteService;
import java.util.List;

public interface MedicineMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<MedicineMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(MedicineMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemsName_Mdl>> getTxtHItemName(String itemCode, boolean isFromScheme, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRestrictedDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHApprovedVendors_Mdl>> getTxtHApprovedVendors(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper pageing, boolean blnToEndOfFile, boolean isActiveCall);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<String>> getSuggestionMedicine(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<Sku_Mdl>> getSKU(String sku, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCategory_Mdl>> getTxtHCategory(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMaterialCategory_Mdl>> getTxtHMaterialCategory(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMedGrp_Mdl>> getTxtHMedGroup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHHSN_Mdl>> getTxtHHSN(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Sku_Mdl>> getSKUDetails(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHComposition_Mdl>> getTxtHComposition(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSubCategory(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHManufacturer_Mdl>> getTxtHManufacturer(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHDivision_Mdl>> getTxtHDivision(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultVal();

    public DmResult<KeyFieldExtended> exportJson(String sessionId);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHITS(String nature, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<HSNDetail_Mdl> getHSNDetail(String hsnCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemModel(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemStrength(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMarginRateFix(DmHelper pageing, boolean blnToEndOfFile);

}
