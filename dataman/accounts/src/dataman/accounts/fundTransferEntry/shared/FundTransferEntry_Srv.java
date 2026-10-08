package dataman.accounts.fundTransferEntry.shared;

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

public interface FundTransferEntry_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission, String vPrefix);

    public DmResult<FundTransferEntry_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UserPermission userPermission, FundTransferEntry_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, String vPrefix);

    public DmResultPageing<List<TxtHFromCostCenter_Mdl>> getTxtHFromCostCenter(DmHelper pageing, boolean blnToEndOfFile, String strSiteCode);

    public DmResultPageing<List<TxtHFromCostCenter_Mdl>> getTxtHFromCostCenter(DmHelper pageing, boolean blnToEndOfFile, String strSiteCode, boolean blnInsert);

    public DmResultPageing<List<TxtHAccounts_Mdl>> getTxtHAccount(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> setApproval(String approvedBy, String docId);

    public DmResultPageing<List<TxtHTransferSite_Mdl>> getTxtHTransferSite(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> getVoucherType();

    //<editor-fold defaultstate="collapsed" desc="Get Current Site">
    public DmResult<KeyFieldExtended> getCurrentSite();

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="On Edit\Delete Check">
    public DmResult<String> getEditCheck(String docId);
    //</editor-fold>
}
