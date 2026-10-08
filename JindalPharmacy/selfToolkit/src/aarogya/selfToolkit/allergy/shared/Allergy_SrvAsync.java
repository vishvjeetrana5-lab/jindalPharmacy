package aarogya.selfToolkit.allergy.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface Allergy_SrvAsync {

    public void getTemplateFavorite(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getAllergyViaTemplate(String docId, AsyncCallback<DmResult<List<GrdAllergy_Mdl>>> result);

    public void getTxtHAllergy(String nature, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHAllergy_Mdl>>> result);

    public void getTxtHAllergyNature(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRemark(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getFavoriteDetail(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm, AsyncCallback<DmResultPageing<List<GrdFavorite_Mdl>>> result);

    public void addFavorite(GrdAllergy_Mdl mrTemp, String consultant, AsyncCallback<DmResult<String>> result);

    public void removeFavorite(String detailCode, String consultantCode, AsyncCallback<DmResult<String>> result);

    public void saveAllergy(GrdAllergy_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getIsPatientPrescbToAllergicMedicine(String prescriptionDocId, String compostionCode, String consulTantCode, AsyncCallback<DmResult<String>> result);
}
