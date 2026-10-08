package aarogya.pharmacy.medicineTemplateMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public interface MedicineTemplateMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<MedicineTemplateMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(MedicineTemplateMaster_Mdl mrTemp);

    public DmResultPageing<List<String>> getTemplateNameSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItem(DmHelper pageing, boolean blnToEndOfFile);

}
