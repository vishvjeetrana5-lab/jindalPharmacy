package aarogya.pharmacy.stockLedger.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface StockLedger_Srv extends RemoteService {

    public DmResultPageing<StockLedger_Mdl> getParticulars(KeyFieldExtended keyField, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefault(String medicineCode, Setting_Mdl setting_Mdl);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCateory(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineGroup(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCompany(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHValueOn(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<BatchNo_Mdl>> getTxtHBatchNo(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper pageing, boolean blnToEndOfFile, boolean isActiveCall);

}
