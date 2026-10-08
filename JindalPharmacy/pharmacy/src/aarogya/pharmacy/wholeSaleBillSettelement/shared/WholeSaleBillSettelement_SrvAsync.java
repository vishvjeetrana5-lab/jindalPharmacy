
package aarogya.pharmacy.wholeSaleBillSettelement.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;


public interface WholeSaleBillSettelement_SrvAsync {
    
     public void getNavigator(String module,AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, String module, AsyncCallback<DmResult<WholeSaleBillSettelement_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(WholeSaleBillSettelement_Mdl mrTemp,String module, AsyncCallback<DmResult<String>> result);

    public void getFind(String module,DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getVendorName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    
}
