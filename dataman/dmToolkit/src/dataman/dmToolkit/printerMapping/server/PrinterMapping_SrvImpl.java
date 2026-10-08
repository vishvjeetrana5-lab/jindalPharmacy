package dataman.dmToolkit.printerMapping.server;

import com.google.gson.reflect.TypeToken;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.jvmSerialization.shared.JVMLocalSetting_Mdl;
import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.printerMapping.shared.PrinterMappingLocalSetting_Mdl;
import dataman.dmToolkit.printerMapping.shared.PrinterMapping_Srv;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.ArrayList;
import java.util.List;

public class PrinterMapping_SrvImpl extends DmRSSPrivate implements PrinterMapping_Srv {

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

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Printer-2");
            listNode.setSharedKeyFieldName("Printer-2");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Printer-3");
            listNode.setSharedKeyFieldName("Printer-3");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Printer-4");
            listNode.setSharedKeyFieldName("Printer-4");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Printer-5");
            listNode.setSharedKeyFieldName("Printer-5");
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
