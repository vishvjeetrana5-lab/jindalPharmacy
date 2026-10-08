package aarogya.pharmacy.medicineIndentDisplay.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface MedicineIndentDisplay_Srv extends RemoteService {

    public DmResult<List<MedicineIndentDisplay_Mdl>> getFillRecords(SearchValue_Mdl searchValueMdl);
    
    public DmResult<MedicineIndentDisplay_Mdl> getRequisitionDetail(String department);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<KeyFieldExtended> getDefaultValues();
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroup(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile);

}
