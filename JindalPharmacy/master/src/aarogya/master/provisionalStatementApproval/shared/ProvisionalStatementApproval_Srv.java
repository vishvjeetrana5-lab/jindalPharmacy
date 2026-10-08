package aarogya.master.provisionalStatementApproval.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ProvisionalStatementApproval_Srv extends RemoteService {

    public DmResultPageing<List<GrdProvisionalStatementApproval_Mdl>> getFillRecords(SharingFilter_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHFinancialYear(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMonth(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> getChecked(int isClosed, String docId);

    public DmResult<SharingFilter_Mdl> getDefault();

}
