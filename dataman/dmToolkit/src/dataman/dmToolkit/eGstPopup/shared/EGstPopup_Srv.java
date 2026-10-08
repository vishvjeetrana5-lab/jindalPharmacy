package dataman.dmToolkit.eGstPopup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gst.shared.EGstParam_Mdl;
import dataman.dmToolkit.gst.shared.EGstToken_Mdl;
import java.util.List;

public interface EGstPopup_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTransportMode(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCancelReason(int actionCode, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHTranporter_Mdl>> getTxtHTransporter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getHVehicleType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getHEwaySubSupplyType(String supplyType, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getHEwayDocumentType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getHEwayTransactionType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getHEinvSupplyType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getHEinvDocumentType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<EGstToken_Mdl> getAuthToken(int actionCode);

    public DmResult<KeyFieldExtended> print(String ewayNo, int actionCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDocumentType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDocumentNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHIrpInfo_Mdl>> getTxtHIrp(int irpTypeCode, DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<EGstParam_Mdl> getEgstParamSessionDetail();
    
    public DmResult<String> setEgstParamSessionDetail(EGstParam_Mdl mrTemp);
    
    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(String gstin,String stateNumericCode,String subCode,String siteCode,DmHelper helper, boolean blnToEndOfFile);

}
