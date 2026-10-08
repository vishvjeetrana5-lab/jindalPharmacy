package aarogya.inventory.gateIn.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class GateIn_Vld {

    public static void chkSave(GateIn_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getPassTypeCode(), "Type", 100);
        if ((DmShared.iNull(mrTemp.getPassTypeCode()).toString().equalsIgnoreCase(DmShared.iNull(STEnum.GateType.NON_RETURNABLE.getId()).toString()))
                || (DmShared.iNull(mrTemp.getPassTypeCode()).toString().equalsIgnoreCase(DmShared.iNull(STEnum.GateType.PREVIOUS_GATE_IN_RETURNABLE.getId()).toString()))) {
            DmShared.isValidText(mrTemp.getPurposeText(), "Purpose", 200);
            DmShared.isValidText(mrTemp.getMode(), "Mode", 300);
            DmShared.isValidText(mrTemp.getDepartmentCode(), "Department", 400);
            DmShared.isValidText(mrTemp.getDate(), "Date", 600);
            DmShared.isValidText(mrTemp.getTime(), "Time", 700);

            if (!DmShared.xNull(mrTemp.getEstdDate()).equalsIgnoreCase("")) {
                if (DmShared.convertDate(mrTemp.getEstdDate()).getTime() < (DmShared.convertDate(mrTemp.getDate())).getTime()) {
                    throw new DmException(600, "Estd. Date should be greater than current date.");
                }
            }
        }
    }

}
