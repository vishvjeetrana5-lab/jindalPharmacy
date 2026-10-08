package dataman.dmToolkit.narrationSearch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface NarrationSearch_SrvAsync {

    public void getNarration(String searchValue, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<NarrationSearch_Mdl>>> asyncCallback);
}
