package dataman.accounts.fundInTransitEntry.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
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

public interface FundInTransitEntry_SrvAsync {

    public void getNavigator(UserPermission userPermission, String vPrefix, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<FundInTransitEntry_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserPermission userPermission, FundInTransitEntry_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, String vPrefix, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHCostCenter(DmHelper helper, boolean blnToEndOfFile, String strSiteCode, AsyncCallback<DmResultPageing<List<TxtHCostCenter_Mdl>>> result);

    public void getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile, String strSiteCode, boolean blnInsert, AsyncCallback<DmResultPageing<List<TxtHCostCenter_Mdl>>> result);

    public void getTxtHAccount(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHAccounts_Mdl>>> result);

    public void getTxtHTransferNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTransferNo_Mdl>>> result);

    public void setApproval(String apporvedBy, String docId, AsyncCallback<DmResult<String>> result);

    public void getVoucherType(AsyncCallback<DmResult<String>> result);

    //<editor-fold defaultstate="collapsed" desc="Get Current Site">
    public void getCurrentSite(AsyncCallback<DmResult<KeyFieldExtended>> result);

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="On Edit\Delete Check">
    public void getEditCheck(String docId, AsyncCallback<DmResult<String>> result);
    //</editor-fold>
}
