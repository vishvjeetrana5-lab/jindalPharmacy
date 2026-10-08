package aarogya.pharmacy.procurementPlannerDisplay.shared;

import aarogya.pharmacy.procurementPlannerDisplay.shared.*;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.LinkedHashMap;
import java.util.List;

public interface ProcurementPlannerDisplay_Srv extends RemoteService {

    public DmResult<List<ProcurementPlannerDisplay_Mdl>> getFillRecords(SearchValue_Mdl searchValueMdl);
    
    public DmResult<ProcurementPlannerDisplay_Mdl> getRequisitionDetail(String department);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<KeyFieldExtended> getDefaultValues(String moduleName);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroup(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<KeyFieldExtended> saveRecord(SearchValue_Mdl filterMdl, List<ProcurementPlannerDisplay_Mdl> gridMdl, String voucherPrefix);
    
    public DmResult<Mdl_Main> getMoveRec(String strKey);
    
    public DmResult<List<KeyFieldExtended>> getNavigator();
    
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHYesNo(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<String> execute(SearchValue_Mdl filterMdl, List<ProcurementPlannerDisplay_Mdl> gridMdl, DmReportProperty_Mdl reportProperty);
}
