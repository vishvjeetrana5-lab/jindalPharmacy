package aarogya.pharmacy.vehicleTypeMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import java.util.List;

public interface VehicleTypeMaster_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getSuggestionVehicleType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<VehicleTypeMaster_Mdl>> result);

    public void deleteRecord(String strKey, ModifyReason_Mdl modifyReasonMdl, AsyncCallback<DmResult<String>> result);

    public void saveRecord(VehicleTypeMaster_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHGSTUnit(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
}
