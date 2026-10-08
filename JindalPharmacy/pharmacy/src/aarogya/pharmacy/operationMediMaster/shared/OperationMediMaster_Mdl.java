package aarogya.pharmacy.operationMediMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OperationMediMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<GrdOperationDetails_Mdl> grdData = new ArrayList<GrdOperationDetails_Mdl>();

    public OperationMediMaster_Mdl() {
    }

    public List<GrdOperationDetails_Mdl> getGrdData() {
        return grdData;
    }

}
