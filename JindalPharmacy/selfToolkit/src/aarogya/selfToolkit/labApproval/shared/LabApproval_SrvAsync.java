package aarogya.selfToolkit.labApproval.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;
//import aarogya.selfToolkit.dashboardCardStrip.shared.CardDetail_Mdl;  <Avinash>

public interface LabApproval_SrvAsync extends IWalkThrough_SrvAsync {

    public void openRecord(boolean isOpen, String docId, String vSNo, AsyncCallback<DmResult<String>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);

//    public void getCardDetail(AsyncCallback<DmResult<CardDetail_Mdl>> asyncCallback); <Avinash>

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getFillRecords(LabApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdLabApproval_Mdl>>> asyncCallback);

    public void getTxtHSampleNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSampleNo_Mdl>>> asyncCallback);

    public void getTxtHLabNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHLabNo_Mdl>>> asyncCallback);

    public void getTxtHTechnician(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTechnician_Mdl>>> result);

    public void getTxtHItem(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>> asyncCallback);

    public void getTxtHTestTemplate(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

}
