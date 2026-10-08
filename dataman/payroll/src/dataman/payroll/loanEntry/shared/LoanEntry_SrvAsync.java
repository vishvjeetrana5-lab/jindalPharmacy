package dataman.payroll.loanEntry.shared;

import dataman.payroll.loanEntry.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface LoanEntry_SrvAsync {
   
    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);
 
    public void getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void getMoveRec(String strKey, AsyncCallback<DmResult<LoanEntry_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(LoanEntry_Mdl mrTemp,String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);
    
    public void getServerDateTime(AsyncCallback<String[]> result);
    
    public void getTxtHEntryType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

        public void getTxtHStop(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
}
