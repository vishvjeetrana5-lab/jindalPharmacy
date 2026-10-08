package dataman.dmToolkit.eGstPopup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;

public interface IEGstPopup_SrvAsync {
    
    

    public void processEGst(MasterEGst_Mdl masterEGst_Mdl,int actionCode, AsyncCallback<DmResult<MasterEGst_Mdl>> asyncCallback);
        
}
