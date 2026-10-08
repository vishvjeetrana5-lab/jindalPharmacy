
package aarogya.pharmacy.wholeSaleBillSettelement.shared;

import aarogya.pharmacy.wholeSaleBillSettelement.client.WholeSaleBillSettelement;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;


public interface WholeSaleBillSettelement_Srv extends RemoteService {
    
    public DmResult<List<KeyFieldExtended>> getNavigator(String module);

    public DmResult<WholeSaleBillSettelement_Mdl> getMoveRec(String strKey, String module);

    public DmResult<String> saveRecord(WholeSaleBillSettelement_Mdl mrTemp,String module);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(String module,DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getVendorName(DmHelper helper, boolean blnToEndOfFile);
    
    
 
}
