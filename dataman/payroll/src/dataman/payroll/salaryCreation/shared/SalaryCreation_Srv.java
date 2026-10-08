package dataman.payroll.salaryCreation.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface SalaryCreation_Srv extends RemoteService {

    public DmResult<KeyFieldExtended> getDefaultValues();

    public List<GrdSalaryCreation_Mdl> getFillRecords(SalaryCreation_Mdl param);

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSalaryMethod(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<SalaryCreation_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey);

    public DmResult<String> saveRecord(SalaryCreation_Mdl mrTemp, String voucherPrefix);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

}
