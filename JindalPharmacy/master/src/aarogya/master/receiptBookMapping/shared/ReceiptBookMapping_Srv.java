package aarogya.master.receiptBookMapping.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ReceiptBookMapping_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<ReceiptBookMapping_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ReceiptBookMapping_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntryPoint(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHReceiptVoucher(DmHelper pageing, boolean blnToEndOfFile, String entryPoint);

    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHRefundVoucher(DmHelper pageing, boolean blnToEndOfFile, String entryPoint);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsDefault(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);

}
