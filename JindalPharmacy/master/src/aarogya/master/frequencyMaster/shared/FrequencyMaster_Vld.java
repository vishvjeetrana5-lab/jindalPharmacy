package aarogya.master.frequencyMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class FrequencyMaster_Vld {

    public static void chkSave(FrequencyMaster_Mdl mrTemp, boolean isClientSide) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Frequency", 100);
        DmShared.isValidText(mrTemp.getPresentation(), "Presentation", 200);

        try {
            String dosePresentation[] = null;
            if (DmShared.xNull(mrTemp.getPresentation()).contains("+")) {
                dosePresentation = mrTemp.getPresentation().split("\\+");
            }
            if (dosePresentation != null) {
                for (String str : dosePresentation) {
                    if (!(DmShared.iNull(str).intValue() == 0 || DmShared.iNull(str).intValue() == 1)) {
                        throw new DmException(200, "Please input presentation in valid format.");
                    }
                }
            } else {
                if (!(DmShared.iNull(mrTemp.getPresentation()).intValue() == 0 || DmShared.iNull(mrTemp.getPresentation()).intValue() == 1)) {
                    throw new DmException(200, "Please input presentation in valid format.");
                }
            }
            if (isClientSide) {
                DmShared.compute(mrTemp.getPresentation());
            }
        } catch (Exception e) {
            throw new DmException(200, DmShared.xNull(e.getMessage()));
        }
    }

}
