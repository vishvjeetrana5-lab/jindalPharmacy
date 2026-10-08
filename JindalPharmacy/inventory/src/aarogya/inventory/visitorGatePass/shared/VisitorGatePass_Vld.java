package aarogya.inventory.visitorGatePass.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class VisitorGatePass_Vld {

    public static void chkSave(VisitorGatePass_Mdl mrTemp, boolean isOutEntry) {

        DmShared.isValidText(mrTemp.getInDate(), "Date", 100);
        DmShared.isValidText(mrTemp.getInTime(), "Time", 200);
        if (!isOutEntry) {
            DmShared.isValidText(mrTemp.getVisitorName(), "Visitor Name", 300);
            DmShared.isValidText(mrTemp.getAddress(), "Address", 400);
            DmShared.isValidText(mrTemp.getCity_Code(), "City", 500);

            if (!DmShared.xNull(mrTemp.getMobile()).trim().equalsIgnoreCase("")) {
                DmShared.isValidMobile(mrTemp.getMobile(), 600);
            }
            if (!DmShared.xNull(mrTemp.getVehicleType()).trim().equalsIgnoreCase("") || !DmShared.xNull(mrTemp.getVehicleNo()).trim().equalsIgnoreCase("")) {
                DmShared.isValidText(mrTemp.getVehicleType(), "Vehicle Type", 700);
                DmShared.isValidText(mrTemp.getVehicleNo(), "Vehicle NO.", 750);
            }
            DmShared.isValidText(mrTemp.getPersonToMeet(), "Person To Meet", 800);
            DmShared.isValidText(mrTemp.getPurpose(), "Purpose", 900);
        } else {
            DmShared.isValidText(mrTemp.getGateOutDate(), "Out Date", 1000);
            DmShared.isValidText(mrTemp.getGateOutTime(), "Out Time", 1100);

            if (DmShared.convertDate(DmShared.dNull(mrTemp.getGateOutDate())).getTime() < DmShared.convertDate(DmShared.dNull(mrTemp.getInDate())).getTime()) {
                throw new DmException(800, "Out date can not be less than in date.");
            }

            if (DmShared.convertDate(DmShared.dNull(mrTemp.getGateOutDate())).getTime() == DmShared.convertDate(DmShared.dNull(mrTemp.getInDate())).getTime()) {
                if (DmShared.vNull(mrTemp.getGateOutTime()).doubleValue() < DmShared.vNull(mrTemp.getInTime()).doubleValue()) {
                    throw new DmException(900, "Out time can not be less than in time.");
                }
            }
        }
    }
}
