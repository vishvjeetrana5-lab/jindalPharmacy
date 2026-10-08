package dataman.utility.menuCodification.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuCodification_Mdl extends KeyField implements Serializable,IsSerializable{

    private List<GrdDescription_Mdl> grdDescrip_Mdl = new ArrayList<GrdDescription_Mdl>();

    public MenuCodification_Mdl() {
    }

    public List<GrdDescription_Mdl> getDescrip_Mdl() {
        return grdDescrip_Mdl;
    }
}
