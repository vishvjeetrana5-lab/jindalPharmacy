
package aarogya.pharmacy.egstParam.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.gst.shared.EGstParam_Mdl;
import java.util.List;


public interface EgstParam_SrvAsync {
    
     public void saveRecord(EGstParam_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<EGstParam_Mdl>> result);

    public void getDefaultValue(AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getTxtHGstIn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHBaseUrl(int irpTypeCode, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<IrpInfoPopup_Mdl>>> result);
    
}
