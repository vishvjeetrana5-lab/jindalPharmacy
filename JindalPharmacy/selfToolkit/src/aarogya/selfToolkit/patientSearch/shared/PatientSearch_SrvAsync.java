package aarogya.selfToolkit.patientSearch.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PatientSearch_SrvAsync {

    public void getFind(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param, AsyncCallback<DmResultPageing<List<GrdPatientDetails_Mdl>>> result);
}
