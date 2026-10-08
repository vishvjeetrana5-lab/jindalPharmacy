package dataman.accounts.reports.shared;


import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.acGroupDetail.shared.RptAcGroupDetail_Mdl;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptAccountGroupDetail_SrvAsync {

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty,RptAcGroupDetail_Mdl accountGroupPrint_Mdl, AsyncCallback<DmResult<String>> result);
    
    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getSite(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getLedgerAcount(DmHelper helper, boolean blnToEndOfFile, String siteList, AsyncCallback<DmResultPageing<List<RptAccountGroupDetail_Mdl>>> result);

    

}
