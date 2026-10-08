package aarogya.nabh.roomHelp.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.MappingNature;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface RoomHelp_SrvAsync {

    public void deleteRecord(String strKey, ModifyReason_Mdl modifyReasonMdl, AsyncCallback<DmResult<String>> result);

    public void getDefaultValues(String templateCode, AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveGUIRecord(Rd_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getTemplateName(DmHelper helper, boolean blnToEndOfFile, MappingNature mappingNature, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getPatientDetail(String regDocId, String IPDNo, AsyncCallback<DmResult<RoomHelp_Mdl>> result);

    public void getPatientHistory(DmHelper helper, boolean blnToEndOfFile, PatientHistorySearch_Mdl svm, AsyncCallback<DmResultPageing<List<GrdRoomHelp_Mdl>>> result);

    public void getGUIData(String regdocId, String docId, String templateCode, MappingNature mappingNature, AsyncCallback<DmResult<Rd_Mdl>> result);

}
