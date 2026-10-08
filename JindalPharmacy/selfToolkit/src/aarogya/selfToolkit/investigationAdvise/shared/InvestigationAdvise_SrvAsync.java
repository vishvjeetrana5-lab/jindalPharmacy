package aarogya.selfToolkit.investigationAdvise.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface InvestigationAdvise_SrvAsync {

    public void getTxtHInvestigation(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdInvestigation_Mdl>>> result);

    public void getFavoriteDetail(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm, AsyncCallback<DmResultPageing<List<GrdFavorite_Mdl>>> result);

    public void addFavorite(GrdInvestigation_Mdl mrTemp, String consultant, AsyncCallback<DmResult<String>> result);

    public void removeFavorite(String detailCode, String consultantCode, boolean isPkg, AsyncCallback<DmResult<String>> result);

    public void getTemplateFavorite(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getInvestigationViaTemplate(String templateDocId, AsyncCallback<DmResult<List<GrdInvestigation_Mdl>>> result);
}
