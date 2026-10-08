package aarogya.company.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.PCSData;

public interface Company_Srv extends RemoteService {

    public DmResult<PCSData> getSites(byte[] bytCompanyCode);
}
