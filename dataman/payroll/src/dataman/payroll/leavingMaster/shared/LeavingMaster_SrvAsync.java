package dataman.payroll.leavingMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface LeavingMaster_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);
    
    public void getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Employee_Mdl>>> result);
  
    public void getTxtHReason(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
  
    public void getTxtHResignationMode(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
  
    public void getTxtHSalaryStatus(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void getServerDateTime(AsyncCallback<String[]> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<LeavingMaster_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(LeavingMaster_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

}
