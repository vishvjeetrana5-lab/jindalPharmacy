package aarogya.selfToolkit.patientSearchRoomWise.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.patientSearch.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PatientSearchRoomWise_SrvAsync {

    public void getFind(DmHelper helper, boolean blnToEndOfFile, SearchValueRoomWise_Mdl param, AsyncCallback<DmResultPageing<List<GrdPatientDetailsRoomWise_Mdl>>> result);
}
