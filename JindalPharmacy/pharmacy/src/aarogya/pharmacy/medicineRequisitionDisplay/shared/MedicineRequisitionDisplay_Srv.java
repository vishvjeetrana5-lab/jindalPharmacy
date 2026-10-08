package aarogya.pharmacy.medicineRequisitionDisplay.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface MedicineRequisitionDisplay_Srv extends RemoteService {

    public DmResult<List<MedicineRequisitionDisplay_Mdl>> getFillRecords(String departmentCode, SearchValue_Mdl searchValueMdl);

    public DmResult<MedicineRequisitionDisplay_Mdl> getRequisitionDetail(String department);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(List<MedicineRequisitionDisplay_Mdl> mrTemp);

    public DmResult<KeyFieldExtended> getDefaultValues();

}
