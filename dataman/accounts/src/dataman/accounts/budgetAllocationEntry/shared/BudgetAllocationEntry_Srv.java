package dataman.accounts.budgetAllocationEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface BudgetAllocationEntry_Srv extends RemoteService, IWalkThrough_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<BudgetAllocationEntry_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(BudgetAllocationEntry_Mdl mrTemp, String voucherPrefix, boolean isApprove);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAllocationType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHBudgetHead_Mdl>> getTxtHBudgetHead(String voucherCategory,String budgetDescription,DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<TxtHBudgetHead_Mdl>> getTxtHBudgetHeadAdjustment(String voucherCategory,String budgetDescription,DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBudgetName(String voucherCategory,DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPlusMinus(DmHelper pageing, boolean blnToEndOfFile);

}
