package dataman.accounts.detailTrialBalance.shared;

import com.google.gwt.user.client.rpc.RemoteService;
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

public interface DetailTrialBalance_Srv extends RemoteService {

    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acLedgerMdl);

    public DmResultPageing<List<DetailTrialBalance_Mdl>> getAcLedgerDetial(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm);
}
