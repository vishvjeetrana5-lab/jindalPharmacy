package aarogya.pharmacy.vehicleTypeMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import java.util.List;

public interface VehicleTypeMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestionVehicleType(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<VehicleTypeMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(VehicleTypeMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey, ModifyReason_Mdl modifyReasonMdl);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGSTUnit(DmHelper pageing, boolean blnToEndOfFile);

}
