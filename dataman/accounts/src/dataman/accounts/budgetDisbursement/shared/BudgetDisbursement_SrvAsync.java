package dataman.accounts.budgetDisbursement.shared;

import dataman.accounts.budgetDisbursement.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface BudgetDisbursement_SrvAsync extends IWalkThrough_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<BudgetDisbursement_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(BudgetDisbursement_Mdl mrTemp, String voucherPrefix, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHDisbursementType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHBudgetHeadAdjustment(String voucherCategory,String budgetDescription,DmHelper pageing, boolean blnToEndOfFile,AsyncCallback<DmResultPageing<List<TxtHBudgetHead_Mdl>>> result) ;
    
    public void getTxtHBudgetName(String voucherCategory,DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);
    
    public void getTxtHPlusMinus (DmHelper pageing, boolean blnToEndOfFile,AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
