package dataman.payroll.employeeMaster.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface EmployeeMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getAccountNameSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<EmployeeMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(EmployeeMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountNamePrefix(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHAccountGroup_Mdl>> getTxtHAccountGroup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDesignation(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCountry(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHZone(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTDSCategory(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSex(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMaritalStatus(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHHRDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHBloodGroup(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHReligion(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPFLimit(DmHelper helper, boolean blnToEndOfFile);
    

    public DmResult<DefaultValue_Mdl> getDefaultValue();

    public String getTempKeyCode();
    
    public DmResult<List<GrdLeave_Mdl>> getLeaveDetails(String empCode);
    
    public DmResult<List<GrdEarningDed_Mdl>> getEarningDedDetails(String empCode);
    
    public DmResult<List<GrdFamily_Mdl>> getFamilyDetails(String empCode);
    
    public DmResult<List<GrdSoftId_Mdl>> getSoftIdDetails(String empCode);
    
    public DmResult<GrdLeave_Mdl> updateRecord(GrdLeave_Mdl mrTemp, String docId);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<GrdEarningDed_Mdl> updateEarning(GrdEarningDed_Mdl mrTemp, String docId);
    
    public DmResult<GrdFamily_Mdl> updateFamily(GrdFamily_Mdl mrTemp, String docId);
    
    public DmResult<GrdSoftId_Mdl> updateSoftId(GrdSoftId_Mdl mrTemp, String docId);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHoffDay(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<String> getDuration(String fromDate, String toDate);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAmtPer(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHShift(DmHelper pageing, boolean blnToEndOfFile);
}
