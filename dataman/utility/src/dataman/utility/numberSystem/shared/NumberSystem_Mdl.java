package dataman.utility.numberSystem.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NumberSystem_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<KeyFieldExtended> grdVType = new ArrayList<KeyFieldExtended>();
    private List<GrdNumberFormat_Mdl> grdNumberFormat = new ArrayList<GrdNumberFormat_Mdl>();

    public NumberSystem_Mdl() {
    }

    public List<KeyFieldExtended> getGrdVType() {
        return grdVType;
    }

    public List<GrdNumberFormat_Mdl> getGrdNumberFormat() {
        return grdNumberFormat;
    }
}
