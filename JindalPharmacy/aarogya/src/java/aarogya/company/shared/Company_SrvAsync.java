package aarogya.company.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.PCSData;

public interface Company_SrvAsync {

    public void getSites(byte[] bytCompanyCode, AsyncCallback<DmResult<PCSData>> result);
}
