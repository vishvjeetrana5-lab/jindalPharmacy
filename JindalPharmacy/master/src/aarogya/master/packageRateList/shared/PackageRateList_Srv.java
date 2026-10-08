package aarogya.master.packageRateList.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PackageRateList_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<PackageRateList_Mdl> getMoveRec(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> checkApplicableDate(String fromDate, String toDate);

    public DmResultPageing<List<TxtHPackage_Mdl>> getTxtHPackage(DmHelper pageing, boolean blnToEndOfFile);
}
