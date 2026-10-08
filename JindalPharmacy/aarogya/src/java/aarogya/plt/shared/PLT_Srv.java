package aarogya.plt.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.PCSData;

public interface PLT_Srv extends RemoteService {

    public DmResult<PCSData> getCompanys(byte[] bytPLTCode);
}
