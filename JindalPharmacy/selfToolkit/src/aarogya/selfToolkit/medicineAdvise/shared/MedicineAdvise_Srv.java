package aarogya.selfToolkit.medicineAdvise.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface MedicineAdvise_Srv extends RemoteService {

    public DmResultPageing<List<String>> getSuggestionMedicine(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile, String stockSite);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCategory(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHFrequency_Mdl>> getTxtHFrequency(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDuration(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdFavorite_Mdl>> getFavoriteDetail(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm);

    public DmResult<String> addFavorite(GrdMedicineDetail_Mdl mrTemp, String consultant);

    public DmResult<String> removeFavorite(String consultantCode, String itemCode, String doseText, String frequencyCode);

    public DmResult<AddNewRecord_Mdl> saveNewRecord(AddNewRecord_Mdl mrTemp);

    public DmResultPageing<List<TxtHInstruction_Mdl>> getTxtHInstruction(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedGroup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTemplateFavorite(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm);

    public DmResult<List<GrdMedicineDetail_Mdl>> getMedicineViaTemplate(String docId);
}
