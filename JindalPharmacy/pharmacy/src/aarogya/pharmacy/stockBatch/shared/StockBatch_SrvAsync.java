package aarogya.pharmacy.stockBatch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface StockBatch_SrvAsync {

    public void getParticulars(KeyFieldExtended keyField, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<StockBatch_Mdl>> result);

    public void getDefault(String itemCode, Setting_Mdl setting_Mdl, AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getTxtHMedicineCateory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMedicineGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMedicineCompany(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMedicineName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHBatchNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<BatchNo_Mdl>>> result);

    public void getTxtHStatus(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHValueOn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, boolean isActiveCall, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
