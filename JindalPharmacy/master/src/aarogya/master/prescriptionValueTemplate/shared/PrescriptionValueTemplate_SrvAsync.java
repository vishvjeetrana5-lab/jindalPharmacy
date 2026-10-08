package aarogya.master.prescriptionValueTemplate.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PrescriptionValueTemplate_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<PrescriptionValueTemplate_Mdl>> result);

    public void getSuggestionName(String guiTemplateCode, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void deleteRecord(String guiCode, String keyCode, AsyncCallback<DmResult<String>> result);

    public void saveRecord(PrescriptionValueTemplate_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHGUITemplate(String guiTemplate, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHCopyGUI(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCopyGUI_Mdl>>> result);

    public void getTemplateName(String guiCode, String consultantCode, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getInitializeGUI(String guiCode, AsyncCallback<DmResult<DmGUI_Mdl>> result);

    public void getCopyPrescriptionCustomData(String docId, List<String> listCustomWidgetToCopy, AsyncCallback<DmResult<PrescriptionValueTemplate_Mdl>> result);

    public void getRecordViaAction(String consultantCode, String guiConfigCode, AsyncCallback<DmResult<PrescriptionValueTemplate_Mdl>> result);

}
