package aarogya.selfToolkit.medicineAdvise.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface MedicineAdvise_SrvAsync {

    public void getSuggestionMedicine(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile, String stockSite, AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>> result);

    public void getTxtHMedicineCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHFrequency(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHFrequency_Mdl>>> result);

    public void getTxtHDuration(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getFavoriteDetail(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm, AsyncCallback<DmResultPageing<List<GrdFavorite_Mdl>>> result);

    public void addFavorite(GrdMedicineDetail_Mdl mrTemp, String consultant, AsyncCallback<DmResult<String>> result);

    public void removeFavorite(String consultantCode, String itemCode, String doseText, String frequencyCode, AsyncCallback<DmResult<String>> result);

    public void saveNewRecord(AddNewRecord_Mdl mrTemp, AsyncCallback<DmResult<AddNewRecord_Mdl>> result);

    public void getTxtHInstruction(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHInstruction_Mdl>>> result);

    public void getTxtHCompany(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCompany_Mdl>>> result);

    public void getTxtHMedGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTemplateFavorite(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getMedicineViaTemplate(String docId, AsyncCallback<DmResult<List<GrdMedicineDetail_Mdl>>> result);
}
