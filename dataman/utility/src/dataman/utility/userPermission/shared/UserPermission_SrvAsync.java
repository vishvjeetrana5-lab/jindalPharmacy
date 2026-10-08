package dataman.utility.userPermission.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.utility.userPermission.shared.GrdMenu_Mdl.EventType;
import java.util.List;

public interface UserPermission_SrvAsync {

    public void deleteVoucherPermission(String userName, String vTypeSName, String roleName, AsyncCallback<DmResult<String>> result);

    public void saveVoucherPermission(GrdVoucher_Mdl mrTemp, String userName, String roleName, AsyncCallback<DmResult<String>> result);

    public void saveMenuPermission(EventType eventType, GrdMenu_Mdl mrTemp, String userName, String roleName, AsyncCallback<DmResult<String>> result);

    public void saveSitePermission(String name, String selectedUser, boolean isSite, String roleName, AsyncCallback<DmResult<String>> result);

    public void saveLinkedAccount(String selectedUser, String linkedAccountCode, AsyncCallback<DmResult<String>> result);

    public void copyPermission(String fromUserName, String toUserName, AsyncCallback<DmResult<String>> result);

    public void getUserRecord(String userName, String userId, AsyncCallback<DmResult<UserPermission_Mdl>> result);

    public void getMenuRecord(String seletedUserName, String mnuModule, String parent, String mnuLevel, boolean isRoot, AsyncCallback<DmResult<List<GrdMenu_Mdl>>> result);

    public void getTxtHVoucher(DmHelper pageing, String voucher, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucher_Mdl>>> result);

    public void getTxtHUser(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHUsers_Mdl>>> result);

    public void getTxtHAdministrator(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSite(DmHelper pageing, String siteList, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdSite_Mdl>>> result);

    public void getTxtHCostCenter(DmHelper pageing, String ccList, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdCostCenter_Mdl>>> result);

    public void getTxtHLinkedAccount(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHLinkedAccount_Mdl>>> result);
}
