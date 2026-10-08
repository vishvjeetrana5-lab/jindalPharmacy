package aarogya.selfToolkit.serviceGrid.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import java.util.List;

public interface ServiceGrid_Srv extends RemoteService {

    public DmResult<DefaultValue_Mdl> getDefaultValues();
    
    public DmResult<List<GSTS_Mdl>> getITS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualITS);

    public DmResultPageing<List<TxtHServiceDetail_Mdl>> getTxtHItemName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHValuationType(DmHelper pageing, boolean blnToEndOfFile);

}
