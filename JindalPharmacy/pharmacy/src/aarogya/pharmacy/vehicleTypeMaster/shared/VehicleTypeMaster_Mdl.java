package aarogya.pharmacy.vehicleTypeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import java.io.Serializable;

public class VehicleTypeMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String gstVehicleTypeCode;
    private String gstVehicleTypeName;
    private ModifyReason_Mdl modifyReasonMdl;

    public VehicleTypeMaster_Mdl() {

    }

    public String getGstVehicleTypeCode() {
        return gstVehicleTypeCode;
    }

    public void setGstVehicleTypeCode(String gstVehicleTypeCode) {
        this.gstVehicleTypeCode = gstVehicleTypeCode;
    }

    public String getGstVehicleTypeName() {
        return gstVehicleTypeName;
    }

    public void setGstVehicleTypeName(String gstVehicleTypeName) {
        this.gstVehicleTypeName = gstVehicleTypeName;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

}
