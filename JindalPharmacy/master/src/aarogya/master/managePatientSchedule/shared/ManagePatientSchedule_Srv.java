package aarogya.master.managePatientSchedule.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ManagePatientSchedule_Srv extends RemoteService {

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<ManagePatientSchedule_Mdl> getMoveRec(String regDocId, String recId, String schdlDoc, String uhidocId);

    public DmResult<String> changeSchedules(ShuffleDays_Mdl mrTemp);

    public DmResult<GrdSchedules_Mdl> saveSchedule(GrdSchedules_Mdl grdMdl, String tag);

    public DmResult<String> deleteDoneDate(String docIdUHID, String tag, String vSno);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMachineBed(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGuiTemplate(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile, String dNull);

    public DmResultPageing<List<TxtHSchedule_Mdl>> getTxtHSchedule(String uhidDoc, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdSchedules_Mdl>> getScheduleGrid(String schdlDocId);

    public DmResult<KeyFieldExtended> deleteRecord(String regUhid, String scheduleDocId);
}
