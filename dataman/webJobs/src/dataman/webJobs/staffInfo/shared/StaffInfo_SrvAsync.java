package dataman.webJobs.staffInfo.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

public interface StaffInfo_SrvAsync {

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
