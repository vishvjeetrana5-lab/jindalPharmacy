package dataman.webJobs.staffInfo.shared;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class StaffInfo_Vld {

//    public static void chkSave(ConsultantMaster_Mdl mrTemp) {
//        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Consultant Name", 200);
//        DmShared.isValidText(mrTemp.getShortName(), "Short Name", 250);
//        DmShared.isValidText(mrTemp.getDepartmentTag(), "Department Name", 300);
//        DmShared.isValidText(mrTemp.getSpecilizationTag(), "Specialization", 400);
//        DmShared.isValidText(mrTemp.getDisplayName(), "Display Name", 500);
//        DmShared.isValidAmount(DmShared.iNull(mrTemp.getConsultantNatureTag()).intValue(), "Nature", 550);
//        if (!DmShared.xNull(mrTemp.geteMail()).equalsIgnoreCase("")) {
//            DmShared.isValidEMail(DmShared.xNull(mrTemp.geteMail()), 600);
//        }
//        if (!DmShared.xNull(mrTemp.getDoa()).trim().equalsIgnoreCase("") && !DmShared.xNull(mrTemp.getDob()).trim().equalsIgnoreCase("")) {
//            if (DmShared.convertDate(DmShared.dNull(mrTemp.getDoa())).getTime() <= DmShared.convertDate(DmShared.dNull(mrTemp.getDob())).getTime()) {
//                throw new DmException(700, "Anniversary date should not be less than or equal to Date of Birth");
//            }
//        }
//
//
//        DmShared.isValidText(mrTemp.getActiveTag(), "Active", 800);
//        if (mrTemp.getGrdTime().size() <= 1 && DmShared.xNull(mrTemp.getGrdTime().get(0).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
//            throw new DmException(1000, "Time Wise Detail is mandatory");
//        }
//
//    }

}
