package aarogya.ribbon.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;

public interface Ribbon_Srv extends RemoteService {

    public DmResult<String> rdsTransaction(String companyCode, boolean isExhaustiveRepair);

    public DmResult<KeyFieldExtended> getMenuInfo(String menuCode);

    public DmResult<String> rdsCompany(boolean isExhaustiveRepair);

    public DmResult<String> repairMenu();

    public DmResult<UserInfo_Mdl> getUserInfo(String userName);
}
