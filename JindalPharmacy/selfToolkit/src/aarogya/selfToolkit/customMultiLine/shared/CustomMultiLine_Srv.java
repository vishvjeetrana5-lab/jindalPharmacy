package aarogya.selfToolkit.customMultiLine.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface CustomMultiLine_Srv extends RemoteService {

    public DmResultPageing<List<GrdFavorite_Mdl>> getFavoriteDetail(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm);

    public DmResult<String> addFavorite(CustomMultiLine_Mdl mrTemp);

    public DmResult<String> removeFavorite(String nature, String detailCode, String consultantCode);
}
