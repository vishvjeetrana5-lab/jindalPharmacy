package dataman.utility.importProcedure.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImportProcedure_Mdl extends KeyField implements Serializable,IsSerializable{

    private List<GrdProcedure_Mdl> grdReport_Mdl = new ArrayList<GrdProcedure_Mdl>();

    public ImportProcedure_Mdl() {
    }

    public List<GrdProcedure_Mdl> getGrdReport_Mdl() {
        return grdReport_Mdl;
    }

}
