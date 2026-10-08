package aarogya.pharmacy.reports.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.LinkedHashMap;
import java.util.List;

public interface RptPhysicalStockBeforePosting_SrvAsync {

    public void getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>> result);

    public void getTxtHMedicineGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMedicineCat(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDate(String reportType, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHDate_Mdl>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHReportType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHManufacturer(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHManufacturer_Mdl>>> result);

    public void getTxtHMedCompany(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedCompany_Mdl>>> result);

    public void execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, AsyncCallback<DmResult<String>> result);

}
