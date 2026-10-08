package aarogya.pharmacy.enquiryApproval.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface EnquiryApproval_Srv extends RemoteService {

    public DmResult<String> openRecord(boolean isOpen, String docId, String subCode);

    public DmResultPageing<List<GrdEnquiryApproval_Mdl>> getFillRecords(EnquiryApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVendor(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<EnquiryApproval_Mdl> fillVendorGrid(String code);

    public DmResultPageing<List<TxtHEnquiryNo_Mdl>> getTxtHEnquiryNo(DmHelper pageing, boolean blnToEndOfFile);

}
