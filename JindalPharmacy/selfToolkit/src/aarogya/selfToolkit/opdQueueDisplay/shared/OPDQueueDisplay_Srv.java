package aarogya.selfToolkit.opdQueueDisplay.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface OPDQueueDisplay_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile, String vrDate);

    public DmResult<List<GrdOPDQueue_Mdl>> getConsultationQueue(String consultant, String vrDate);
}
