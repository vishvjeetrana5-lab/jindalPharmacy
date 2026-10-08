package aarogya.selfToolkit.opdQueueDisplay.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface OPDQueueDisplay_SrvAsync {

    public void getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile, String vrDate, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getConsultationQueue(String consultant, String vrDate, AsyncCallback<DmResult<List<GrdOPDQueue_Mdl>>> result);
}
