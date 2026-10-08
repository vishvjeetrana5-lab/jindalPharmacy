package aarogya.pharmacy.enquiryApproval.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface EnquiryApproval_SrvAsync {

    public void openRecord(boolean isOpen, String docId, String subCode, AsyncCallback<DmResult<String>> result);

    public void getFillRecords(EnquiryApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdEnquiryApproval_Mdl>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHVendor(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void fillVendorGrid(String code, AsyncCallback<DmResult<EnquiryApproval_Mdl>> result);

    public void getTxtHEnquiryNo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHEnquiryNo_Mdl>>> result);
}
