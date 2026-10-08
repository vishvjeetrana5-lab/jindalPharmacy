package aarogya.selfToolkit.itemPropertyPopup.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ItemPropertyPopup_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHColour(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSize(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<BatchNo_Mdl>> getBatchNo(String itemCode, boolean isIbCodeRequired, boolean isOpening,ItemBatchType batchType, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> getIbCode(ItemProperty_Mdl mdl);

}
