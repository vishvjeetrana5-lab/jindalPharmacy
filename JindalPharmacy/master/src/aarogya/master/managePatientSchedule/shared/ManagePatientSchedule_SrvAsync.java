package aarogya.master.managePatientSchedule.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ManagePatientSchedule_SrvAsync {

    public void getTxtHSchedule(String uhidDoc, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSchedule_Mdl>>> result);

    public void getFind(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getMoveRec(String regDocId, String recId, String schdlDoc, String uhidocId, AsyncCallback<DmResult<ManagePatientSchedule_Mdl>> result);

    public void changeSchedules(ShuffleDays_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile, String dNull, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void saveSchedule(GrdSchedules_Mdl grdMdl, String tag, AsyncCallback<DmResult<GrdSchedules_Mdl>> result);

    public void deleteDoneDate(String docIdUHID, String tag, String vSno, AsyncCallback<DmResult<String>> result);

    public void getTxtHMachineBed(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHGuiTemplate(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getScheduleGrid(String schdlDocId, AsyncCallback<DmResult<List<GrdSchedules_Mdl>>> result);

    public void deleteRecord(String regUhid, String scheduleDocId, AsyncCallback<DmResult<KeyFieldExtended>> result);

}
