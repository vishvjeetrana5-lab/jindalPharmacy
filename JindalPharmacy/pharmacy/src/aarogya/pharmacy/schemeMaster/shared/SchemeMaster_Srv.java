package aarogya.pharmacy.schemeMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface SchemeMaster_Srv extends RemoteService {

    public DmResult<SchemeMaster_Mdl> getMoveRec(String strKey, String v_Date, String medicineTotalQty);

    public DmResult<String> saveRecord(SchemeMaster_Mdl mrTemp);

    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile);

}
