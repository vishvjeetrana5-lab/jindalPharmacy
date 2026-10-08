package dataman.accounts.fundInTransitEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
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

public interface FundInTransitEntry_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission, String vPrefix);

    public DmResult<FundInTransitEntry_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UserPermission userPermission, FundInTransitEntry_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, String vPrefix);

    public DmResultPageing<List<TxtHCostCenter_Mdl>> getTxtHCostCenter(DmHelper helper, boolean blnToEndOfFile, String strSiteCode);

    public DmResultPageing<List<TxtHCostCenter_Mdl>> getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile, String strSiteCode, boolean blnInsert);

    public DmResultPageing<List<TxtHAccounts_Mdl>> getTxtHAccount(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> setApproval(String approvedBy, String docId);

    public DmResult<String> getVoucherType();

    public DmResultPageing<List<TxtHTransferNo_Mdl>> getTxtHTransferNo(DmHelper helper, boolean blnToEndOfFile);

    //<editor-fold defaultstate="collapsed" desc="Get Current Site">
    public DmResult<KeyFieldExtended> getCurrentSite();

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="On Edit\Delete Check">
    public DmResult<String> getEditCheck(String docId);
    //</editor-fold>
}
