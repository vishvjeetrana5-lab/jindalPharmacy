package aarogya.nabh.patientInfectionEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PatientInfectionEntry_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<PatientInfectionEntry_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UserPermission userPermission, PatientInfectionEntry_Mdl mrTemp, String voucherPrefix);

    public DmResult<String> deleteRecord(UserPermission userPermission, String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<PatientDetail_Mdl> getUHIDDetails(String regNo, String regDocId, String type);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRegType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHInfectionType(DmHelper pageing, boolean blnToEndOfFile);
    
}
