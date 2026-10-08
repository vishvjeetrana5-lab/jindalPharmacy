package aarogya.mis.graphicalDashboard.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface GraphicalDashBoard_SrvAsync {

    public void getChartData(Filter_Mdl filterMdl, AsyncCallback<DmResult<MainChart_Mdl>> result);

    public void getTxtHInvestigation(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getServerDateTime(String fromDate, AsyncCallback<String[]> result);

    public void getTxtHPaymentMode(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSite(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

}
