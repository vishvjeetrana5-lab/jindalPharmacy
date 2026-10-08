package dataman.dmToolkitR.printerMapping.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;

import java.util.List;

public interface PrinterMapping_SrvAsync {

    public void getTxtHPrinter(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getJsonOfLocalSettingReportMapModel(DmLocalSettingPrintMap_Mdl reportMapModel, AsyncCallback<DmResult<String>> jsonString);
    
     public void getPrinterMappingLocalSettingFromJSon(String jsonOfJVMLocalSettingModel,AsyncCallback<DmResult<PrinterMappingLocalSetting_Mdl>> callback);

}
