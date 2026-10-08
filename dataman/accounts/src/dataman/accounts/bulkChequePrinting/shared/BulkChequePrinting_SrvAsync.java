package dataman.accounts.bulkChequePrinting.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public interface BulkChequePrinting_SrvAsync {

    public void getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>> result);

    public void executePrint(BulkChequePrinting_Mdl mrTemp, List<Print_Mdl> listPrint, boolean isMerge, AsyncCallback<DmResult<String>> result);

    public void updateVoucher(GrdBulkChequePrinting_Mdl mrTemp, List<ChangeChqNo_Mdl> mdl, String chkNoLength, boolean isMerged, AsyncCallback<DmResult<String>> result);

    public void getTxtHBankName(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHBankName_Mdl>>> result);

    public void getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile, String contraSub, String fromDate, String toDate, String voucherNoDocid, AsyncCallback<DmResultPageing<List<TxtHAccountName_Mdl>>> result);

    public void showRecords(BulkChequePrinting_Mdl mrTemp, AsyncCallback<DmResult<List<GrdBulkChequePrinting_Mdl>>> result);

    public void getTxtHVoucherNo(DmHelper pageing, boolean blnToEndOfFile, String contraSub, String fromDate, String toDate, AsyncCallback<DmResultPageing<List<TxtHVoucherNo_Mdl>>> result);

    public void getTxtHRecordType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
}
