package aarogya.pharmacy.medicineTransferApproval.shared;

import aarogya.pharmacy.medicineTransferApproval.shared.*;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface MedicineTransferApproval_Srv extends RemoteService {

    public DmResult<String> save(GrdMedicineDetail_Mdl approval_Mdl);

    public DmResult<String> openRecord(boolean isOpen, String docId, String v_SNo);

    public DmResult<MedicineTransferApproval_Mdl> fillItemGrid(String code);

    public DmResultPageing<List<GrdMedicineTransferApproval_Mdl>> getFillRecords(MedicineTransferApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMedicineName_Mdl>> getTxtHMedicine(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTransferTo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHTransferNo_Mdl>> getTxtHTransferNo(DmHelper pageing, boolean blnToEndOfFile);

}
