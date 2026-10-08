package aarogya.ribbon.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;

public interface Ribbon_SrvAsync {

    public void rdsTransaction(String companyCode, boolean isExhaustiveRepair, AsyncCallback<DmResult<String>> result);

    public void getMenuInfo(String menuCode, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void rdsCompany(boolean isExhaustiveRepair, AsyncCallback<DmResult<String>> result);

    public void repairMenu(AsyncCallback<DmResult<String>> result);

    public void getUserInfo(String userName, AsyncCallback<DmResult<UserInfo_Mdl>> result);

}
