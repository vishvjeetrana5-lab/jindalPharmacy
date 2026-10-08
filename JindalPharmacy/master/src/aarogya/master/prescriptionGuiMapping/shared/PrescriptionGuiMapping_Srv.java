package aarogya.master.prescriptionGuiMapping.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PrescriptionGuiMapping_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntry(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGUITempName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(PrescriptionGuiMapping_Mdl mrTemp);

    public DmResult<PrescriptionGuiMapping_Mdl> getMoveRec(String strKey);

//    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntryPopup(DmHelper helper, boolean blnToEndOfFile);
    public DmResult<KeyFieldExtended> exportJson(String natureCode, String sessionId);

    public DmResult<PrescriptionGuiMapping_Mdl> importJson(String filePath, String fileName);
}
