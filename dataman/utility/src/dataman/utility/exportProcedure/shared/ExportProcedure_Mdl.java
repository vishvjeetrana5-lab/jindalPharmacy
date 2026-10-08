package dataman.utility.exportProcedure.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExportProcedure_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<GrdProcedure_Mdl> lstProcedure = new ArrayList<GrdProcedure_Mdl>();
    private String sessionId;

    public ExportProcedure_Mdl() {
    }

    public List<GrdProcedure_Mdl> getLstProcedure() {
        return lstProcedure;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
