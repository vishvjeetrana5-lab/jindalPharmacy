package aarogya.site.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.PCSData;

public interface Site_SrvAsync {

    public void validateSite(byte[] bytSiteCode, AsyncCallback<DmResult<PCSData>> result);
}
