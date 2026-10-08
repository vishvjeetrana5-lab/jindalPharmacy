package aarogya.mis.graphicalDashboard.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public interface GraphicalDashBoard_Srv extends RemoteService {

    public DmResult<MainChart_Mdl> getChartData(Filter_Mdl filterMdl);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHInvestigation(DmHelper helper, boolean blnToEndOfFile);

    public String[] getServerDateTime(String fromDate);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPaymentMode(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

}
