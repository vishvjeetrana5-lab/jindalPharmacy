package aarogya.master.serviceMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class ServiceMaster_Vld {

    public static void chkSave(ServiceMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 100);
        DmShared.isValidText(mrTemp.getShortName(), "Short Name", 200);
        DmShared.isValidText(mrTemp.getIsDiscountAllowTag(), "Discount", 300);
        DmShared.isValidText(mrTemp.getCategoryCode(), "Category", 400);
        DmShared.isValidText(mrTemp.getRoutineTag(), "Routine", 500);
        DmShared.isValidText(mrTemp.getGroupCode(), "Group Name", 600);
        DmShared.isValidText(mrTemp.getNightShiftTag(), "Night Shift", 700);
        if (DmShared.iNull(mrTemp.getNightShiftTag()).intValue() == 1) {
            if (DmShared.vNull(mrTemp.getNightShiftAmount()).doubleValue() == 0) {
                throw new DmException(750, " Amount is mandatory.");
            }
            if (DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase("")) {
                throw new DmException(780, " Type is mandatory.");
            }

        }
        DmShared.isValidText(mrTemp.getRateEditableTag(), "Rate Editable", 800);
        DmShared.isValidText(mrTemp.getActiveTag(), "Active", 900);
        DmShared.isValidText(mrTemp.getServiceNatureCode(), "Service Type", 1000);

        if (DmShared.iNull(mrTemp.getTypeCode()).intValue() == 0 && DmShared.vNull(mrTemp.getNightShiftAmount()).doubleValue() > 100) {
            throw new DmException(750, " Percentage Can't be greater then 100.");
        }

    }
}
