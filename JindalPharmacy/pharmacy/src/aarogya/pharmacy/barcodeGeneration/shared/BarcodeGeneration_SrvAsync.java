package aarogya.pharmacy.barcodeGeneration.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface BarcodeGeneration_SrvAsync {

    public void saveRecord(BarcodeGeneration_Mdl mrTemp, Setting_Mdl setting_Mdl, AsyncCallback<DmResult<String>> result);

    public void getFillRecords(Setting_Mdl setting_Mdl, AsyncCallback<DmResult<BarcodeGeneration_Mdl>> result);

    public void getMoveRec(String docid, AsyncCallback<DmResult<BarcodeGeneration_Mdl>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHBarCodeHeader(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
