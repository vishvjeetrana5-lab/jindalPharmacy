package aarogya.inventory.supplierMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.HashMap;
import java.util.List;

public interface SupplierMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(String moduleName);

    public DmResultPageing<List<String>> getAccountNameSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<SupplierMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UserPermission userPermission, SupplierMaster_Mdl mrTemp, String moduleName);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountNamePrefix(DmHelper pageing, boolean blnToEndOfFile);
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCompany(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHAccountGroup_Mdl>> getTxtHAccountGroup(String supplierCode, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHZone(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPartyCategory(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTDSCategory(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHReferencedTo_Mdl>> getTxtHReferencedTo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDuplicate(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLedgerGroup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPartyType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile, String moduleName);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValues(String moduleName);

    public DmResultPageing<List<TxtHManufacturer_Mdl>> getTxtHManufacturer(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper helper, boolean blnToEndOfFile);

    public String getTempKeyCode();

    public String[] getServerDateTime();

    public DmResult<String> setApproval(String approvedBy, String docId);

}
