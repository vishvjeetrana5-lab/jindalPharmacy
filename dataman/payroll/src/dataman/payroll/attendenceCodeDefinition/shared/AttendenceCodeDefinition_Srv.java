package dataman.payroll.attendenceCodeDefinition.shared;


import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface AttendenceCodeDefinition_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestionName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<String>> getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<AttendenceCodeDefinition_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(AttendenceCodeDefinition_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDefault(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPlusMinus(DmHelper pageing, boolean blnToEndOfFile);

   // public DmResult<KeyFieldExtended> exportCSV(String sessionId);
}
