package aarogya.selfToolkit.prescriptionCustomItem.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PrescriptionCustomItem_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPrescriptionCustomGroup(String nature, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHName_Mdl>> getTxtHPrescriptionCustomItem(String nature, String groupCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdFavorite_Mdl>> getFavoriteDetail(String nature, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm);

    public DmResult<String> addFavorite(String nature, GrdPrescriptionCustomItem_Mdl mrTemp, String consultantCode);

    public DmResult<String> removeFavorite(String nature, String detailCode, String consultantCode);

    public DmResultPageing<List<KeyFieldExtended>> getTemplateFavorite(String nature, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm);

    public DmResult<List<GrdPrescriptionCustomItem_Mdl>> getItemViaTemplate(String docId, String nature);

}
