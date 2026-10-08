package aarogya.pharmacy.medicineRequisitionDisplay.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface MedicineRequisitionDisplay_SrvAsync {

    public void getFillRecords(String departmentCode, SearchValue_Mdl searchValueMdl, AsyncCallback<DmResult<List<MedicineRequisitionDisplay_Mdl>>> result);

    public void getRequisitionDetail(String department, AsyncCallback<DmResult<MedicineRequisitionDisplay_Mdl>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void saveRecord(List<MedicineRequisitionDisplay_Mdl> mrTemp, AsyncCallback<DmResult<String>> result);

    public void getDefaultValues(AsyncCallback<DmResult<KeyFieldExtended>> result);
}
