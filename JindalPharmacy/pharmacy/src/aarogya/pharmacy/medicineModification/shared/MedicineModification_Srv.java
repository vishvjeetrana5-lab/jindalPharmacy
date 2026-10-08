package aarogya.pharmacy.medicineModification.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface MedicineModification_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult< MedicineModification_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(MedicineModification_Mdl mrTemp);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMedGrp_Mdl>> getTxtHMedGroup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHComposition_Mdl>> getTxtHComposition(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper pageing, boolean blnToEndOfFile, boolean isActiveCall);

    public DmResultPageing<List<Sku_Mdl>> getSKUDetails(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValue();

    public DmResultPageing<List<String>> getSuggestionMedicine(DmHelper helper, boolean blnToEndOfFile);

}
