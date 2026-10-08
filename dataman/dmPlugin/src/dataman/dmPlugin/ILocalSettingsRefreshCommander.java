/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmPlugin;

import dataman.dmBase.jvmSerialization.shared.JVMLocalSetting_Mdl;
import java.util.Map;

/**
 *
 * @author abhishek
 */
public interface ILocalSettingsRefreshCommander {

    public void executeAfterLocalSettingReloaded(JVMLocalSetting_Mdl reloadedLocalSettings, Map<String, String> additionalParams);

    public void executeAfterServerStarted();

    public void executeAfterServerStopped();
}
