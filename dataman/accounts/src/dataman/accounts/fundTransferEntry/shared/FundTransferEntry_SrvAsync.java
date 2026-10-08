package dataman.accounts.fundTransferEntry.shared;

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

public interface FundTransferEntry_SrvAsync {

    public void getNavigator(UserPermission userPermission, String vPrefix, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<FundTransferEntry_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserPermission userPermission, FundTransferEntry_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, String vPrefix, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHFromCostCenter(DmHelper pageing, boolean blnToEndOfFile, String strSiteCode, AsyncCallback<DmResultPageing<List<TxtHFromCostCenter_Mdl>>> result);

    public void getTxtHFromCostCenter(DmHelper pageing, boolean blnToEndOfFile, String strSiteCode, boolean blnInsert, AsyncCallback<DmResultPageing<List<TxtHFromCostCenter_Mdl>>> result);

    public void getTxtHAccount(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHAccounts_Mdl>>> result);

    public void getTxtHTransferSite(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTransferSite_Mdl>>> result);

    public void setApproval(String apporvedBy, String docId, AsyncCallback<DmResult<String>> result);

    public void getVoucherType(AsyncCallback<DmResult<String>> result);
    //<editor-fold defaultstate="collapsed" desc="Get Current Site">

    public void getCurrentSite(AsyncCallback<DmResult<KeyFieldExtended>> result);

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="On Edit\Delete Check">
    public void getEditCheck(String docId, AsyncCallback<DmResult<String>> result);
    //</editor-fold>
}
