package aarogya.selfToolkit.allergy.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface Allergy_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTemplateFavorite(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm);

    public DmResult<List<GrdAllergy_Mdl>> getAllergyViaTemplate(String docId);

    public DmResultPageing<List<TxtHAllergy_Mdl>> getTxtHAllergy(String nature, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAllergyNature(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRemark(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdFavorite_Mdl>> getFavoriteDetail(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm);

    public DmResult<String> addFavorite(GrdAllergy_Mdl mrTemp, String consultant);

    public DmResult<String> removeFavorite(String detailCode, String consultantCode);

    public DmResult<String> saveAllergy(GrdAllergy_Mdl mrTemp);

    public DmResult<String> getIsPatientPrescbToAllergicMedicine(String prescriptionDocId, String compostionCode, String consulTantCode);
}
