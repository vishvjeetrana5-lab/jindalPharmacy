package aarogya.pharmacy.medicineMakeMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class MedicineMakeMaster_Vld {

    public static void chkSave(MedicineMakeMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Medicine Company", 100);
        DmShared.isValidText(mrTemp.getShortName().trim(), "Short Name", 200);
        DmShared.isValidEMail(mrTemp.getLclEmail(), 300);
        DmShared.isValidEMail(mrTemp.getRegEmail(), 400);
        DmShared.isValidEMail(mrTemp.getNatEmail(), 500);
        if (DmShared.vNull(mrTemp.getPurchaseDisPer()).doubleValue() > 100) {
            throw new DmException(600, "Purchase Discount Percentage Cannot be greater than 100.");
        }
    }
}
