package dataman.accounts.trialBalanceCompare.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.dmBase.self.shared.*;

public interface TrialBalanceCompare_SrvAsync {

    public void getDefault(AcDisplaySetting_Mdl acDisplaySetting, AsyncCallback<DmResult<DefaultValue_Mdl>> result);
}
