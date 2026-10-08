package dataman.dmToolkitR.printerMapping.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;

import java.util.List;

public interface PrinterMapping_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPrinter(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> getJsonOfLocalSettingReportMapModel(DmLocalSettingPrintMap_Mdl reportMapModel);

    public DmResult<PrinterMappingLocalSetting_Mdl> getPrinterMappingLocalSettingFromJSon(String jsonOfJVMLocalSettingModel);
}
