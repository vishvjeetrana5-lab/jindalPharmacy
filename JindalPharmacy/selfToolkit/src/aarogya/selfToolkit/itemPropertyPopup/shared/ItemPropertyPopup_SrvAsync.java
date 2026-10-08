package aarogya.selfToolkit.itemPropertyPopup.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.organizationPopup.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ItemPropertyPopup_SrvAsync {

    public void getTxtHColour(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSize(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getBatchNo(String itemCode, boolean isIbCodeRequired, boolean isOpening,ItemBatchType batchType,DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<BatchNo_Mdl>>> result);

    public void getIbCode(ItemProperty_Mdl mdl, AsyncCallback<DmResult<String>> result);
}
