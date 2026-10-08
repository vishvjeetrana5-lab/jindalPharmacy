package dataman.payroll.loanEntry.shared;

import dataman.payroll.loanEntry.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface LoanEntry_Srv extends RemoteService {
   
    public DmResult<List<KeyFieldExtended>> getNavigator();
 
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<LoanEntry_Mdl> getMoveRec(String strKey);
    
    public DmResult<String> deleteRecord(String strKey);

    public DmResult<String> saveRecord(LoanEntry_Mdl mrTemp,String voucherPrefix);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);
    
    public String[] getServerDateTime();
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntryType(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStop(DmHelper helper, boolean blnToEndOfFile);
    
}
