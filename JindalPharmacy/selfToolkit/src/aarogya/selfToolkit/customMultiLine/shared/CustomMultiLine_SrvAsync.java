package aarogya.selfToolkit.customMultiLine.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface CustomMultiLine_SrvAsync {

    public void getFavoriteDetail(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm, AsyncCallback<DmResultPageing<List<GrdFavorite_Mdl>>> result);

    public void addFavorite(CustomMultiLine_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void removeFavorite(String nature, String detailCode, String consultantCode, AsyncCallback<DmResult<String>> result);
}
