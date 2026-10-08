package aarogya.common.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.PCSData;

public interface Common_Srv extends RemoteService {

    public DmResult<PCSData> reInitialize(PCSEnum pcsEnum);

    public DmResult<UserPermission> getUserPermission();

    public DmResult<Integer> getPACWebPort();
}
