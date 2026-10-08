package aarogya.selfToolkit.investigationAdvise.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface InvestigationAdvise_Srv extends RemoteService {

    public DmResultPageing<List<GrdInvestigation_Mdl>> getTxtHInvestigation(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdFavorite_Mdl>> getFavoriteDetail(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm);

    public DmResult<String> addFavorite(GrdInvestigation_Mdl mrTemp, String consultant);

    public DmResult<String> removeFavorite(String detailCode, String consultantCode, boolean isPkg);

    public DmResultPageing<List<KeyFieldExtended>> getTemplateFavorite(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm);

    public DmResult<List<GrdInvestigation_Mdl>> getInvestigationViaTemplate(String templateDocId);
}
