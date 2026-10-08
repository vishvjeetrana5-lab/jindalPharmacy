package dataman.dmToolkit.narrationSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface NarrationSearch_Srv extends RemoteService{
    public DmResultPageing<List<NarrationSearch_Mdl>> getNarration(String searchValue, DmHelper helper, boolean blnToEndOfFile);
}
