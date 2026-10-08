package dataman.payroll.employeeShiftDetail.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.util.List;

public interface EmployeeShiftDetail_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<EmployeeShiftDetail_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey);

    public String[] getServerDateTime();
    
    public DmResult<String> saveRecord(EmployeeShiftDetail_Mdl mrTemp, String voucherPrefix);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<Employee_Mdl>> getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<TxtHShift_Mdl>> getTxtHShift(DmHelper helper, boolean blnToEndOfFile);

}
