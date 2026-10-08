package aarogya.localSetting.shared;

import aarogya.selfToolkit.stStatic.shared.STLocalSetting_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;
import java.util.Map;

public interface LocalSetting_Srv extends RemoteService {

//    public DmResult<Integer> getPACWebPort();
    public DmResultPageing<List<KeyFieldExtended>> convertPrintServicesModel(DmHelper helper, String printerJsonString);

    public DmResult<String> getJsonOfSettings(STLocalSetting_Mdl settingModel);

    public DmResult<STLocalSetting_Mdl> getSettingsFromJson(String jsonString);

    public DmResultPageing<List<TxtHMenuText_Mdl>> getTxtHMenuText(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMachineSpecification_Mdl>> getTxtHMachineSpecification(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<LabMachineConfiguration_Mdl> parseMachineDetails(String jsonString);

    public DmResult<String> getJsonOfLabMachineConfigurations(List<LabMachineConfiguration_Mdl> labMachineConfigurations);

    public DmResult<List<LabMachineConfiguration_Mdl>> getLabMachineModelsFromJsonOfSavedConfigs(String jsonString);

    public DmResult<String> saveRecordToSQL(LabMachineConfiguration_Mdl mrTemp);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMachineName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<LabMachineConfiguration_Mdl> getData(String strKey);

    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> deleteRecordFromSQL(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getSubTest(DmHelper helper, boolean blnToEndOfFile);
//    public DmResult<LocalSetting_Mdl> getSettingsFromJson(String jsonString);
}
