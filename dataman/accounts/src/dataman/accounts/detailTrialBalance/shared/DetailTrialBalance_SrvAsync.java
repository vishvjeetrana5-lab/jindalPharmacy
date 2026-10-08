package dataman.accounts.detailTrialBalance.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface DetailTrialBalance_SrvAsync {

    public void getDefault(AcDisplaySetting_Mdl aldsm, AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getAcLedgerDetial(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm, AsyncCallback<DmResultPageing<List<DetailTrialBalance_Mdl>>> result);
}
