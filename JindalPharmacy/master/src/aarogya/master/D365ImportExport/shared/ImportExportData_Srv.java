package aarogya.master.D365ImportExport.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;

public interface ImportExportData_Srv extends RemoteService {

    public DmResult<String> getIpdRevenue(String fromDates);

    public DmResult<String> exportOpdPosting(String fromDate);

    public DmResult<String> exportCancelIPDBills(String fromDate);

    public DmResult<String> exportUnBilledService(String fromDate);

    public DmResult<String> exportStockTransfer(String fromDate);

    public DmResult<String> exportStockComsumption(String fromDate);

    public DmResult<String> exportPharmacyBillStock(String fromDate);

    public DmResult<String> getItemDetails();

    public DmResult<String> getTpaSettlementBills(String fromDate);

    public DmResult<String> exportPaymentDetails(String postingFromDate);

    public DmResult<String> syncDepartmentMaster();

    public DmResult<String> updateIndentStatus();

}
