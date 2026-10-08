package dataman.accounts.bulkChequePrinting.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public interface BulkChequePrinting_Srv extends RemoteService {

    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> executePrint(BulkChequePrinting_Mdl mrTemp, List<Print_Mdl> listPrint, boolean isMerge);

    public DmResult<String> updateVoucher(GrdBulkChequePrinting_Mdl mrTemp, List<ChangeChqNo_Mdl> mdl, String chkNoLength, boolean isMerged);

    public DmResultPageing<List<TxtHBankName_Mdl>> getTxtHBankName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHAccountName_Mdl>> getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile, String contraSub, String fromDate, String toDate, String voucherNoDocid);

    public DmResultPageing<List<TxtHVoucherNo_Mdl>> getTxtHVoucherNo(DmHelper pageing, boolean blnToEndOfFile, String contraSub, String fromDate, String toDate);

    public DmResult<List<GrdBulkChequePrinting_Mdl>> showRecords(BulkChequePrinting_Mdl mrTemp);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRecordType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper pageing, boolean blnToEndOfFile);
}
