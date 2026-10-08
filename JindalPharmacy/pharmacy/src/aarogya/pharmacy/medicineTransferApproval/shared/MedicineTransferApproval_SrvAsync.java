package aarogya.pharmacy.medicineTransferApproval.shared;

import aarogya.pharmacy.medicineTransferApproval.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface MedicineTransferApproval_SrvAsync {

    public void save(GrdMedicineDetail_Mdl approval_Mdl, AsyncCallback<DmResult<String>> result);

    public void openRecord(boolean isOpen, String docId, String v_SNo, AsyncCallback<DmResult<String>> result);

    public void getFillRecords(MedicineTransferApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdMedicineTransferApproval_Mdl>>> result);

    public void getTxtHMedicine(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedicineName_Mdl>>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHTransferTo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void fillItemGrid(String code, AsyncCallback<DmResult<MedicineTransferApproval_Mdl>> result);

    public void getTxtHTransferNo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTransferNo_Mdl>>> result);

}
