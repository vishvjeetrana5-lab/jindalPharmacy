package aarogya.master.generateGstCsv.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public interface GenerateGstCsv_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String[]> exportGstDataToCsv(Filter_Mdl filter);

}
