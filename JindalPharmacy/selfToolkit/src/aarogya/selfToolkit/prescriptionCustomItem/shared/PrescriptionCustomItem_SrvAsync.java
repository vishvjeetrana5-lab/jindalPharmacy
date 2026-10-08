package aarogya.selfToolkit.prescriptionCustomItem.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PrescriptionCustomItem_SrvAsync {

    public void getTxtHPrescriptionCustomGroup(String nature, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHPrescriptionCustomItem(String nature, String groupCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHName_Mdl>>> result);

    public void getFavoriteDetail(String nature, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm, AsyncCallback<DmResultPageing<List<GrdFavorite_Mdl>>> result);

    public void addFavorite(String nature, GrdPrescriptionCustomItem_Mdl mrTemp, String consultantCode, AsyncCallback<DmResult<String>> result);

    public void removeFavorite(String nature, String detailCode, String consultantCode, AsyncCallback<DmResult<String>> result);

    public void getTemplateFavorite(String nature, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getItemViaTemplate(String docId, String nature, AsyncCallback<DmResult<List<GrdPrescriptionCustomItem_Mdl>>> result);
}
