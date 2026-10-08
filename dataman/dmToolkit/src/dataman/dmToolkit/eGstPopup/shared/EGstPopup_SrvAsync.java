package dataman.dmToolkit.eGstPopup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gst.shared.EGstParam_Mdl;
import dataman.dmToolkit.gst.shared.EGstToken_Mdl;
import java.util.List;

public interface EGstPopup_SrvAsync {

    public void getTxtHTransportMode(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHCancelReason(int actionCode, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHTransporter(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTranporter_Mdl>>> asyncCallback);

    public void getHVehicleType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getHEwaySubSupplyType(String supplyType, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getHEwayDocumentType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getHEwayTransactionType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getHEinvSupplyType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getHEinvDocumentType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getAuthToken(int actionCode, AsyncCallback<DmResult<EGstToken_Mdl>> asyncCallback);

    public void print(String ewayNo, int actionCode, AsyncCallback<DmResult<KeyFieldExtended>> asyncCallback);

    public void getTxtHDocumentType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDocumentNature(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHIrp(int irpTypeCode, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHIrpInfo_Mdl>>> result);

    public void getEgstParamSessionDetail(AsyncCallback<DmResult<EGstParam_Mdl>> asyncCallback);

    public void setEgstParamSessionDetail(EGstParam_Mdl mrTemp, AsyncCallback<DmResult<String>> asyncCallback);

    public void getTxtHCity(String gstin,String subCode,String stateNumericCode,String siteCode,DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>> asyncCallback);

}
