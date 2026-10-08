package dataman.utility.gstStructure.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface GstStructure_SrvAsync {

    public void getFillRecords(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GstStructure_Mdl>>> result);
    
    public void saveRecord(GstStructure_Mdl grdItemTaxStructure, AsyncCallback<DmResult<String>> asyncCallback);
    
    public void getTxtHAccount(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);
 
}
