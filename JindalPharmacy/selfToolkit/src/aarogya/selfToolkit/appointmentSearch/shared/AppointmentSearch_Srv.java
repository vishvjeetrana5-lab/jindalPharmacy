package aarogya.selfToolkit.appointmentSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface AppointmentSearch_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSitting(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdAppointmentDetails_Mdl>> getAppoinmentSearch(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param);

}
