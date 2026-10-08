package aarogya.pharmacy.vehicleTypeMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class VehicleTypeMaster_Vld {

    public static void chkSave(VehicleTypeMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Vehicle Name", 100);
        DmShared.isValidText(mrTemp.getGstVehicleTypeCode().trim(), "GST Type", 200);
    }
}
