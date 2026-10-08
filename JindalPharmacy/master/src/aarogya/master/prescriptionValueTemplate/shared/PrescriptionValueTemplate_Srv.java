package aarogya.master.prescriptionValueTemplate.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PrescriptionValueTemplate_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<PrescriptionValueTemplate_Mdl> getMoveRec(String docId);

    public DmResultPageing<List<String>> getSuggestionName(String guiTemplateCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(PrescriptionValueTemplate_Mdl mrTemp, String voucherPrefix);

    public DmResult<String> deleteRecord(String guiCode, String keyCode);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGUITemplate(String guiTemplate, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCopyGUI_Mdl>> getTxtHCopyGUI(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> getTemplateName(String guiCode, String consultantCode);

    public DmResult<DmGUI_Mdl> getInitializeGUI(String guiCode);

    public DmResult<PrescriptionValueTemplate_Mdl> getCopyPrescriptionCustomData(String docId, List<String> listCustomWidgetToCopy);

    public DmResult<PrescriptionValueTemplate_Mdl> getRecordViaAction(String consultantCode, String guiConfigCode);
}
