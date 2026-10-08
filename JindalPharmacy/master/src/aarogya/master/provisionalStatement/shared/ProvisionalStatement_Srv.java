package aarogya.master.provisionalStatement.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ProvisionalStatement_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<ProvisionalStatement_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(ProvisionalStatement_Mdl mrTemp,String[] dateRange,String siteCode, String voucherPrefix);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMonth(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdProvisionalStatement_Mdl>> getServiceDetail(String[] dateRange, String siteCode, String serviceCategoryCSV);

    public DmResult<DefaultData_Mdl> getDefault();
}
