package aarogya.pharmacy.wholeSaleSettings.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface WholeSaleSetting_SrvAsync {

    public void getTxtHCentralStore(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);
    
    public void saveRecord(WholeSaleSetting_Mdl mrTemp, String topControlMode,AsyncCallback<DmResult<String>> result);
    
    public void getMoveRec(String strKey, AsyncCallback<DmResult<WholeSaleSetting_Mdl>> result);
    
     public void getDefaultValue(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

}
