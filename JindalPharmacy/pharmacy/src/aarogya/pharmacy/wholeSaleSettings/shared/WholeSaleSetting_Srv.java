
package aarogya.pharmacy.wholeSaleSettings.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;


public interface WholeSaleSetting_Srv extends RemoteService {
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCentralStore(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<WholeSaleSetting_Mdl> getMoveRec(String strKey);
    
    public DmResult<String> saveRecord(WholeSaleSetting_Mdl mrTemp, String topControlMode);
    
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);
    
     public DmResult<DefaultValue_Mdl> getDefaultValue();
    
}
