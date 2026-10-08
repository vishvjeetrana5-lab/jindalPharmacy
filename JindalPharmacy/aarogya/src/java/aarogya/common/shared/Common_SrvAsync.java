package aarogya.common.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.PCSData;

public interface Common_SrvAsync {

    public void reInitialize(PCSEnum pcsEnum, AsyncCallback<DmResult<PCSData>> result);

    public void getUserPermission(AsyncCallback<DmResult<UserPermission>> result);

    public void getPACWebPort(AsyncCallback<DmResult<Integer>> result);
}
