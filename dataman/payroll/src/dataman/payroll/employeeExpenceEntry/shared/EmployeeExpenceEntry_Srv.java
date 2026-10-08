package dataman.payroll.employeeExpenceEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface EmployeeExpenceEntry_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<EmployeeExpenceEntry_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UserPermission userPermission, EmployeeExpenceEntry_Mdl mrTemp, String voucherPrefix);

    public DmResult<List<GridEmployeeExpenceEntry_Mdl>> getRecords();

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Employee_Mdl>> getTxtHEmployee(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<SalaryMethod_Mdl>> getTxtHSalaryMethod(DmHelper pageing, boolean blnToEndOfFile);

}
