package dataman.payroll.employeeExpenceEntry.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface EmployeeExpenceEntry_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<EmployeeExpenceEntry_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getRecords(AsyncCallback<DmResult<List<GridEmployeeExpenceEntry_Mdl>>> result);

    public void saveRecord(UserPermission userPermission, EmployeeExpenceEntry_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHEmployee(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Employee_Mdl>>> result);

    public void getTxtHSalaryMethod(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<SalaryMethod_Mdl>>> result);

}
