package dataman.utility.userPermission.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.utility.userPermission.shared.GrdMenu_Mdl.EventType;
import java.util.List;

public interface UserPermission_Srv extends RemoteService {

    public DmResult<String> saveVoucherPermission(GrdVoucher_Mdl mrTemp, String userName, String roleName);

    public DmResult<String> saveMenuPermission(EventType eventType, GrdMenu_Mdl mrTemp, String userName, String roleName);

    public DmResult<String> saveSitePermission(String name, String selectedUser, boolean isSite, String roleName);

    public DmResult<String> saveLinkedAccount(String selectedUser, String linkedAccountCode);

    public DmResult<String> copyPermission(String fromUserName, String toUserName);

    public DmResult<String> deleteVoucherPermission(String userName, String vTypeSName, String roleName);

    public DmResult<UserPermission_Mdl> getUserRecord(String userName, String userId);

    public DmResult<List<GrdMenu_Mdl>> getMenuRecord(String seletedUserName, String mnuModule, String parent, String mnuLevel, boolean isRoot);

    public DmResultPageing<List<TxtHVoucher_Mdl>> getTxtHVoucher(DmHelper pageing, String voucher, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHUsers_Mdl>> getTxtHUser(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAdministrator(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdSite_Mdl>> getTxtHSite(DmHelper pageing, String siteList, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdCostCenter_Mdl>> getTxtHCostCenter(DmHelper pageing, String ccList, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHLinkedAccount_Mdl>> getTxtHLinkedAccount(DmHelper pageing, boolean blnToEndOfFile);
}
