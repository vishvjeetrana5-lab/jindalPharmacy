package aarogya.master.serviceCategoryMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ServiceCategoryMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<ServiceCategoryMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ServiceCategoryMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHITS(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);

    public DmResultPageing<List<KeyFieldExtended>> getTxtisCultantRequired(DmHelper pageing, boolean blnToEndOfFile);

}
