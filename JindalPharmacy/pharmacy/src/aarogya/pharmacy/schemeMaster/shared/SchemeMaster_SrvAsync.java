package aarogya.pharmacy.schemeMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface SchemeMaster_SrvAsync {

    public void getMoveRec(String strKey, String v_Date, String medicineTotalQty, AsyncCallback<DmResult<SchemeMaster_Mdl>> result);

    public void saveRecord(SchemeMaster_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>> result);

}
