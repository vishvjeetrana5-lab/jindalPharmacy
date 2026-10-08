package aarogya.master.patientScheduleMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PatientScheduleMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<PatientScheduleMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGuiTemplate(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(PatientScheduleMaster_Mdl mrTemp, String voucherPrefix);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMachineBed(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<String>> getSugtnPatSchdlName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<String>> getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile, String string);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSchdlGroup(DmHelper helper, boolean blnToEndOfFile);
}
