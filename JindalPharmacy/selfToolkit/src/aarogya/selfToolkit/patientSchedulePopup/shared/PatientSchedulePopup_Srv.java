package aarogya.selfToolkit.patientSchedulePopup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PatientSchedulePopup_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSchedule(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<PatientDetail_Mdl> getUHIDDetails(String uhidRecId, String uhidDocId);

    public String[] getServerDateTime();

    public DmResult<String> saveRecord(PatientSchedulePopup_Mdl mrTemp, String voucherPrefix);

    public DmResult<DefaultValue_Mdl> getDefaultValue();

}
