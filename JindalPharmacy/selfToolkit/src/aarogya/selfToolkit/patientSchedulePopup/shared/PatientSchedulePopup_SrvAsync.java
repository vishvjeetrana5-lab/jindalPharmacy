package aarogya.selfToolkit.patientSchedulePopup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PatientSchedulePopup_SrvAsync {

    public void getTxtHSchedule(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getUHIDDetails(String uhidRecId, String uhidDocId, AsyncCallback<DmResult<PatientDetail_Mdl>> result);

    public void getServerDateTime(AsyncCallback<String[]> result);

    public void saveRecord(PatientSchedulePopup_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getDefaultValue(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

}
