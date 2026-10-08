package dataman.dmToolkit.printerMapping.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface PrinterMapping_SrvAsync {

    public void getTxtHPrinter(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getJsonOfLocalSettingReportMapModel(DmLocalSettingPrintMap_Mdl reportMapModel, AsyncCallback<DmResult<String>> jsonString);

    public void getPrinterMappingLocalSettingFromJSon(String jsonOfJVMLocalSettingModel,AsyncCallback<DmResult<PrinterMappingLocalSetting_Mdl>> callback);
}
