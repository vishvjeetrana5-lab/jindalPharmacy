package aarogya.site.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.PCSData;

public interface Site_Srv extends RemoteService {

    public DmResult<PCSData> validateSite(byte[] bytSiteCode);
}
