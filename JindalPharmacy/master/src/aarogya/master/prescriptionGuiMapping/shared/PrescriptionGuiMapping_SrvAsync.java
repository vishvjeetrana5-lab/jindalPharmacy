package aarogya.master.prescriptionGuiMapping.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PrescriptionGuiMapping_SrvAsync {

    public void getTxtHEntry(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHGUITempName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void saveRecord(PrescriptionGuiMapping_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<PrescriptionGuiMapping_Mdl>> result);

//    public void getTxtHEntryPopup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    public void exportJson(String natureCode, String sessionId, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void importJson(String filePath, String fileName, AsyncCallback<DmResult<PrescriptionGuiMapping_Mdl>> result);
}
