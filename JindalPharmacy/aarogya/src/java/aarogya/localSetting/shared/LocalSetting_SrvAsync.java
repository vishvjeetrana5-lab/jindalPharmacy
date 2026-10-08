package aarogya.localSetting.shared;

import aarogya.selfToolkit.stStatic.shared.STLocalSetting_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;
import java.util.Map;

public interface LocalSetting_SrvAsync {

//    public void getPACWebPort(AsyncCallback<DmResult<Integer>> callback);
    public void convertPrintServicesModel(DmHelper helper, String printerJsonString, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> callback);

    public void getJsonOfSettings(STLocalSetting_Mdl settingModel, AsyncCallback<DmResult<String>> callback);

    public void getSettingsFromJson(String jsonString, AsyncCallback<DmResult<STLocalSetting_Mdl>> callback);

    public void getTxtHMenuText(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMenuText_Mdl>>> result);

    public void getTxtHMachineSpecification(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMachineSpecification_Mdl>>> result);

    public void parseMachineDetails(String jsonString, AsyncCallback<DmResult<LabMachineConfiguration_Mdl>> callback);

    public void getJsonOfLabMachineConfigurations(List<LabMachineConfiguration_Mdl> labMachineConfigurations, AsyncCallback<DmResult<String>> callback);

    public void getLabMachineModelsFromJsonOfSavedConfigs(String jsonString, AsyncCallback<DmResult<List<LabMachineConfiguration_Mdl>>> callback);

    public void saveRecordToSQL(LabMachineConfiguration_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHMachineName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getData(String strKey, AsyncCallback<DmResult<LabMachineConfiguration_Mdl>> result);

    public void getSuggestion(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void deleteRecordFromSQL(String strKey, AsyncCallback<DmResult<String>> result);

    public void getSubTest(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

//    public void getSettingsFromJson(String jsonString,AsyncCallback<DmResult<LocalSetting_Mdl>> callback);
}
