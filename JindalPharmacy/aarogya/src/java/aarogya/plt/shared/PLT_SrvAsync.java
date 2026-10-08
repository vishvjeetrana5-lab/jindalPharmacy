package aarogya.plt.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.PCSData;

public interface PLT_SrvAsync {

    public void getCompanys(byte[] bytPLTCode, AsyncCallback<DmResult<PCSData>> result);
}
