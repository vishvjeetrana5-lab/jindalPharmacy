package aarogya.master.consultantSharingMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ConsultantSharingMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<ConsultantSharingMaster_Mdl> getMoveRec(String strKey, String applicableFrom, SearchValue_Mdl searchValue_Mdl);

    public DmResult<String> saveRecord(ConsultantSharingMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey, String applicableFrom);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultantName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCopyConsultant_Mdl>> getTxtHCopyConsultant(String consultantCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHEmpanelment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHModule(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceCat(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceGroup(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHService(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoomCategory(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> getApplicableDateList(String consultantCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDtApplicable(DmHelper pageing, boolean blnToEndOfFile, SearchValue_Mdl svm);

    public DmResult<KeyFieldExtended> getMaxApplicableDate(SearchValue_Mdl svm);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSearchType(DmHelper pageing, boolean blnToEndOfFile);

}
