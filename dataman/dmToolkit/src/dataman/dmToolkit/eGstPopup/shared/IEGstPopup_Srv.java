package dataman.dmToolkit.eGstPopup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;

public interface IEGstPopup_Srv extends RemoteService {

    public DmResult<MasterEGst_Mdl> processEGst(MasterEGst_Mdl masterEGst_Mdl, int actionCode);

}
