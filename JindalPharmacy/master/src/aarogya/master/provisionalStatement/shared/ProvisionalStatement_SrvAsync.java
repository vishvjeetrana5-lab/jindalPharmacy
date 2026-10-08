package aarogya.master.provisionalStatement.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ProvisionalStatement_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<ProvisionalStatement_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHSite(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMonth(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void saveRecord(ProvisionalStatement_Mdl mrTemp, String[] dateRange,String siteCode,String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getServiceDetail(String[] dateRange, String siteCode, String serviceCategoryCSV, AsyncCallback<DmResult<List<GrdProvisionalStatement_Mdl>>> result);

    public void getDefault(AsyncCallback<DmResult<DefaultData_Mdl>> result);
}
