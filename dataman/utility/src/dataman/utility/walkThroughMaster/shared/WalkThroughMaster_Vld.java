package dataman.utility.walkThroughMaster.shared;

import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class WalkThroughMaster_Vld {

    public static void chkSave(WalkThroughMaster_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Name", 100);

        if (mrTemp.getGrdAuthority_Mdl().size() == 0) {
            throw new DmException(200, "Authority is Mandatory.");
        } else if (mrTemp.getGrdAuthority_Mdl().size() >= 1) {
            if (DmShared.xNull(mrTemp.getGrdAuthority_Mdl().get(0).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                throw new DmException(200, "Authority is Mandatory.");
            }
        }
//          List<GrdAuthority_Mdl> listauth = mrTemp.getGrdAuthority_Mdl();
//        for (GrdAuthority_Mdl authority_Mdl1 : listauth) {
//            for(int i=0;i<mrTemp.getGrdAuthority_Mdl().size();i++)
//               if (DmShared.xNull(authority_Mdl1.getSrlNo()).equalsIgnoreCase(listauth.get(i).getSrlNo())) {
//                   throw new DmException(300, " S.No. is duplicate.");
//        }
//        }
    }
}
