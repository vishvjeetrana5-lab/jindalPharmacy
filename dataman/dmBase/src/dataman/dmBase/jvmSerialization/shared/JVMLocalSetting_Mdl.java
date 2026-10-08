package dataman.dmBase.jvmSerialization.shared;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import dataman.dmBase.self.shared.DmLocalSettingPrinter_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JVMLocalSetting_Mdl implements Serializable, IsSerializable {

    @Expose(serialize = true, deserialize = true)
    @SerializedName("printerSettings")
    private List<DmLocalSettingPrinter_Mdl> printerSettings;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("reportMapSettings")
    private List<DmLocalSettingPrintMap_Mdl> reportMapSettings;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("extendedLocalSettings")
    private Map<String, String> extendedLocalSettings;

    public JVMLocalSetting_Mdl() {
        extendedLocalSettings = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        printerSettings = new ArrayList<>();
        reportMapSettings = new ArrayList<>();
    }

    public List<DmLocalSettingPrinter_Mdl> getPrinterSettings() {
        return printerSettings;
    }

    public List<DmLocalSettingPrintMap_Mdl> getReportMapSettings() {
        return reportMapSettings;
    }

    public void setPrinterSettings(List<DmLocalSettingPrinter_Mdl> printerSettings) {
        this.printerSettings = printerSettings;
    }

    public void setReportMapSettings(List<DmLocalSettingPrintMap_Mdl> reportMapSettings) {
        this.reportMapSettings = reportMapSettings;
    }

    public boolean hasExtendedLocalSetting(String extendedSettingKey) {
        return extendedLocalSettings.containsKey(DmBaseShared.xNull(extendedSettingKey).trim());
    }

    public void setExtendedLocalSetting(String extendedSettingKey, String extendedSettingValue) {
        String localSettingKey = DmBaseShared.xNull(extendedSettingKey).trim();
        if (extendedLocalSettings.containsKey(localSettingKey)) {
            extendedLocalSettings.remove(localSettingKey);
        }
        extendedLocalSettings.put(localSettingKey, DmBaseShared.xNull(extendedSettingValue).trim());
    }

    public String getExtendedLocalSetting(String extendedSettingKey) {
        String localSettingKey = DmBaseShared.xNull(extendedSettingKey).trim();
        String localSettingValue = "";
        if (extendedLocalSettings.containsKey(localSettingKey)) {
            localSettingValue = DmBaseShared.xNull(extendedLocalSettings.get(localSettingKey)).trim();
        }
        return localSettingValue;
    }

}
