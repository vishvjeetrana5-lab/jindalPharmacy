package dataman.payroll.leavingMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface LeavingMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();
    
    public DmResultPageing<List<Employee_Mdl>> getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile);
  
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHResignationMode(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSalaryStatus(DmHelper helper, boolean blnToEndOfFile);
    
    public String[] getServerDateTime();

    public DmResult<LeavingMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(LeavingMaster_Mdl mrTemp, String voucherPrefix);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

}
