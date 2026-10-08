package dataman.utility.gstStructure.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface GstStructure_Srv extends RemoteService {

    public DmResultPageing<List<GstStructure_Mdl>> getFillRecords(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<String> saveRecord(GstStructure_Mdl grdItemTaxStructure);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccount(DmHelper pageing, boolean blnToEndOfFile);

}
