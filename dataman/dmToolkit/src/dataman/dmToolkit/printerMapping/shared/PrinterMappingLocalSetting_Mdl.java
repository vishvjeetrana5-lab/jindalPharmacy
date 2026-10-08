/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmToolkit.printerMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import dataman.dmBase.self.shared.DmLocalSettingPrinter_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abhishek
 */
public class PrinterMappingLocalSetting_Mdl implements Serializable, IsSerializable {

    private List<DmLocalSettingPrintMap_Mdl> reportMappings;
    private List<DmLocalSettingPrinter_Mdl> printerMappings;

    public PrinterMappingLocalSetting_Mdl() {
        reportMappings = new ArrayList<>();
        printerMappings = new ArrayList<>();
    }

    public List<DmLocalSettingPrintMap_Mdl> getReportMappings() {
        return reportMappings;
    }

    public List<DmLocalSettingPrinter_Mdl> getPrinterMappings() {
        return printerMappings;
    }

}
