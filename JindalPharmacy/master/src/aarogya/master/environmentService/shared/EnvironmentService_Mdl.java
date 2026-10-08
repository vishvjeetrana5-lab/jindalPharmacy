package aarogya.master.environmentService.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentService_Mdl extends KeyField implements Serializable, IsSerializable {

    private String destiSiteCode;
    private List<GrdServiceCategory_Mdl> grdGeneral = new ArrayList<GrdServiceCategory_Mdl>();
    private List<GrdServiceCategory_Mdl> grdDiagnostic = new ArrayList<GrdServiceCategory_Mdl>();
    private List<GrdBloodComponent_Mdl> grdBloodComponent = new ArrayList<GrdBloodComponent_Mdl>();

    public EnvironmentService_Mdl() {
    }

    public String getDestiSiteCode() {
        return destiSiteCode;
    }

    public void setDestiSiteCode(String destiSiteCode) {
        this.destiSiteCode = destiSiteCode;
    }

    public List<GrdServiceCategory_Mdl> getGrdGeneral() {
        return grdGeneral;
    }

    public List<GrdServiceCategory_Mdl> getGrdDiagnostic() {
        return grdDiagnostic;
    }

    public List<GrdBloodComponent_Mdl> getGrdBloodComponent() {
        return grdBloodComponent;
    }
}
