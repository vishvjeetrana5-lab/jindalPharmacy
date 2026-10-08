package aarogya.selfToolkit.uhidPopup.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class UhidPopup_Vld {

    public static void chkSave(UhidPopup_Mdl mrTemp) {

        DmShared.isValidEMail(mrTemp.getEmail(), 100);
        DmShared.isValidMobile(mrTemp.getMobileNo(), 200);

        /*====================================< Check whether Aadhar No. is valid or not >====================================*/
        if (DmShared.xNull(mrTemp.getIdType()).trim().equalsIgnoreCase("Aadhaar")) {
            if (!DmShared.xNull(mrTemp.getIdNo()).trim().equalsIgnoreCase("")) {
                mrTemp.setIdNo(DmShared.xNull(mrTemp.getIdNo()).replaceAll(" ", ""));
                if (DmShared.xNull(mrTemp.getIdNo()).trim().length() != 12) {
                    throw new DmException(300, "Aadhar No. should be 12 digit.");
                }
                try {
                    long l = Long.parseLong(DmShared.xNull(mrTemp.getIdNo()));
                } catch (NumberFormatException nfe) {
                    throw new DmException(300, "Aadhar No. should be numeric.");
                }
            }
        }
    }
}
