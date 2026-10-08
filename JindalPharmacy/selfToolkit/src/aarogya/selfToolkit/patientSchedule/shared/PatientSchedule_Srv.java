package aarogya.selfToolkit.patientSchedule.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;
import java.util.List;

public interface PatientSchedule_Srv extends RemoteService {

    public DmResult<List<GrdSchedules_Mdl>> getScheduleGrid(String uhid);
}
