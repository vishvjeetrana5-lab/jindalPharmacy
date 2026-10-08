package aarogya.inventory.purchaseReturn.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class TxtHGateEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private String gateEntryDate;
    private String transporterCode;
    private String transportername;
    private String vehicleNo;

    public TxtHGateEntry_Mdl() {

    }

    public String getGateEntryDate() {
        return gateEntryDate;
    }

    public void setGateEntryDate(String gateEntryDate) {
        this.gateEntryDate = gateEntryDate;
    }

    public String getTransporterCode() {
        return transporterCode;
    }

    public void setTransporterCode(String transporterCode) {
        this.transporterCode = transporterCode;
    }

    public String getTransportername() {
        return transportername;
    }

    public void setTransportername(String transportername) {
        this.transportername = transportername;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

}
