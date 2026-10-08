package aarogya.pharmacy.medicineSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface MedicineSearch_Srv extends RemoteService {

    public DmResultPageing<List<MedicineSearch_Mdl>> getMedicineSearchDetail(DmHelper helper, boolean blnToEndOfFile, MedicineSearchFilter_Mdl filter, String v_Type);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineMake(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCategory(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineGroup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDivision(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHComposition(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHHsn_Mdl>> getTxtHHsn(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

}
