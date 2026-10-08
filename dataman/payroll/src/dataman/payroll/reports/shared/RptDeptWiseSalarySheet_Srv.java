package dataman.payroll.reports.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptDeptWiseSalarySheet_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSalaryMethod(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<Employee_Mdl>> getTxtHEmployee(String departmentCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> execute(String session, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName);

}
