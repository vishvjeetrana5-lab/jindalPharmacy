package aarogya.selfToolkit.labApproval.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
//import aarogya.selfToolkit.dashboardCardStrip.shared.CardDetail_Mdl;  <Avinash>
import java.util.List;

public interface LabApproval_Srv extends RemoteService, IWalkThrough_Srv {

    public DmResult<String> openRecord(boolean isOpen, String docId, String vSNo);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

//    public DmResult<CardDetail_Mdl> getCardDetail();  <Avinash>

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResultPageing<List<GrdLabApproval_Mdl>> getFillRecords(LabApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHLabNo_Mdl>> getTxtHLabNo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSampleNo_Mdl>> getTxtHSampleNo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHTechnician_Mdl>> getTxtHTechnician(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTestTemplate(DmHelper helper, boolean blnToEndOfFile);

}
