package aarogya.selfToolkit.patientSchedule.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;
import java.util.List;

public interface PatientSchedule_SrvAsync {

    public void getScheduleGrid(String uhid, AsyncCallback<DmResult<List<GrdSchedules_Mdl>>> result);

}
