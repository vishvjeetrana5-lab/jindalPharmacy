package aarogya.master.prescriptionGuiMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionGuiMapping_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<GridDescription_Mdl> grdDescrip_Mdl = new ArrayList<GridDescription_Mdl>();

    public PrescriptionGuiMapping_Mdl() {
    }

    public List<GridDescription_Mdl> getDescrip_Mdl() {
        return grdDescrip_Mdl;
    }

//    public List<GridDescription_Mdl> getGrdDescrip_Mdl() {
//        return grdDescrip_Mdl;
//    }
//
//    public void setGrdDescrip_Mdl(List<GridDescription_Mdl> grdDescrip_Mdl) {
//        this.grdDescrip_Mdl = grdDescrip_Mdl;
//    }
}
