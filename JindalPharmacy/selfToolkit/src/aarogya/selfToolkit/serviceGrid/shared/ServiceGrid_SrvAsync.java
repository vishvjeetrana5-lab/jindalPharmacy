package aarogya.selfToolkit.serviceGrid.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import java.util.List;

public interface ServiceGrid_SrvAsync {

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getITS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualITS, AsyncCallback<DmResult<List<GSTS_Mdl>>> result);

    public void getTxtHItemName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHServiceDetail_Mdl>>> asyncCallback);
       
    public void getTxtHGstType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);    

    public void getTxtHValuationType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);
}
