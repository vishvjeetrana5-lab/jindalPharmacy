package aarogya.selfToolkit.stStatic.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import dataman.dmBase.self.shared.DmLocalSettingPrinter_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class STLocalSetting_Mdl implements Serializable, IsSerializable {

    private List<DmLocalSettingPrinter_Mdl> dmLocalSettingPrinter_Mdls;
    private List<DmLocalSettingPrintMap_Mdl> dmLocalSettingPrintMap_Mdls;
    private String extendedLocalSettingJsonString;

    public STLocalSetting_Mdl() {
        dmLocalSettingPrinter_Mdls = new ArrayList<>();
        dmLocalSettingPrintMap_Mdls = new ArrayList<>();
    }

    public List<DmLocalSettingPrinter_Mdl> getDmLocalSettingPrinter_Mdls() {
        return dmLocalSettingPrinter_Mdls;
    }

    public void setDmLocalSettingPrinter_Mdls(List<DmLocalSettingPrinter_Mdl> dmLocalSettingPrinter_Mdls) {
        this.dmLocalSettingPrinter_Mdls = dmLocalSettingPrinter_Mdls;
    }

    public List<DmLocalSettingPrintMap_Mdl> getDmLocalSettingPrintMap_Mdls() {
        return dmLocalSettingPrintMap_Mdls;
    }

    public void setDmLocalSettingPrintMap_Mdls(List<DmLocalSettingPrintMap_Mdl> dmLocalSettingPrintMap_Mdls) {
        this.dmLocalSettingPrintMap_Mdls = dmLocalSettingPrintMap_Mdls;
    }

    public String getExtendedLocalSettingJsonString() {
        return extendedLocalSettingJsonString;
    }

    public void setExtendedLocalSettingJsonString(String extendedLocalSettingJsonString) {
        this.extendedLocalSettingJsonString = extendedLocalSettingJsonString;
    }

}
