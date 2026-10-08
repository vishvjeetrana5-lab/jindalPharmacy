package dataman.dmToolkitR.printerMapping.server;

import dataman.dmBase.dmSession.server.DmRSSPublic;
import dataman.dmBase.jvmSerialization.shared.JVMLocalSetting_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkitR.dmStatic.server.DmServer;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmToolkitR.printerMapping.shared.PrinterMapping_Srv;
import dataman.dmToolkitR.printerMapping.shared.PrinterMappingLocalSetting_Mdl;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;

public class PrinterMapping_SrvImpl extends DmRSSPublic implements PrinterMapping_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPrinter(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Printer-1");
            listNode.setSharedKeyFieldName("Printer-1");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<String> getJsonOfLocalSettingReportMapModel(DmLocalSettingPrintMap_Mdl reportMapModel) {
        DmResult<String> result = new DmResult<>();
        try {
            result.setData(DmServer.convertToJson(reportMapModel, new TypeToken<DmLocalSettingPrintMap_Mdl>() {
            }));
            result.setStatus(true);
        } catch (Exception ex) {
            result.setErrorMessage(ex.getMessage());
            result.setStatus(false);
        }
        return result;
    }

    @Override
    public DmResult<PrinterMappingLocalSetting_Mdl> getPrinterMappingLocalSettingFromJSon(String jsonOfJVMLocalSettingModel) {
        DmResult<PrinterMappingLocalSetting_Mdl> result = new DmResult<>();
        try {
            JVMLocalSetting_Mdl settingsModel = DmServer.convertFromJson(jsonOfJVMLocalSettingModel, new TypeToken<JVMLocalSetting_Mdl>() {
            });

            PrinterMappingLocalSetting_Mdl printerMappingModel = new PrinterMappingLocalSetting_Mdl();
            printerMappingModel.getPrinterMappings().addAll(settingsModel.getPrinterSettings());
            printerMappingModel.getReportMappings().addAll(settingsModel.getReportMapSettings());

            result.setData(printerMappingModel);
            result.setStatus(true);
        } catch (Exception ex) {
            result.setErrorMessage(ex.getMessage());
            result.setStatus(false);
        }
        return result;
    }

}
