package aarogya.nabh.roomHelp.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.MappingNature;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface RoomHelp_Srv extends RemoteService {

    public DmResult<String> deleteRecord(String strKey, ModifyReason_Mdl modifyReasonMdl);

    public DmResult<DefaultValue_Mdl> getDefaultValues(String templateCode);

    public DmResultPageing<List<KeyFieldExtended>> getTemplateName(DmHelper helper, boolean blnToEndOfFile, MappingNature mappingNature);

    public DmResult<String> saveGUIRecord(Rd_Mdl mrTemp, String voucherPrefix);

    public DmResult<RoomHelp_Mdl> getPatientDetail(String regDocId, String IPDNo);

    public DmResultPageing<List<GrdRoomHelp_Mdl>> getPatientHistory(DmHelper helper, boolean blnToEndOfFile, PatientHistorySearch_Mdl svm);

    public DmResult<Rd_Mdl> getGUIData(String regdocId, String docId, String templateCode, MappingNature mappingNature);

}
