
package aarogya.pharmacy.egstParam.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.gst.shared.EGstParam_Mdl;
import java.util.List;


public interface EgstParam_Srv extends RemoteService
{ 

    public DmResult<String> saveRecord(EGstParam_Mdl mdl);

    public DmResult<EGstParam_Mdl> getMoveRec(String strKey);

    public DmResult<KeyFieldExtended> getDefaultValue();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstIn(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<IrpInfoPopup_Mdl>> getTxtHBaseUrl(int irpTypeCode, DmHelper pageing, boolean blnToEndOfFile);
}
