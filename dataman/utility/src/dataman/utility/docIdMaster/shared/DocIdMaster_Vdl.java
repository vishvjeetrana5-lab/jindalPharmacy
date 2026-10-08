package dataman.utility.docIdMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class DocIdMaster_Vdl {

    public static void chkSave(DocIdMaster_Mdl mrTemp) {
        if (DmShared.xNull(mrTemp.getSiteName()).trim().equalsIgnoreCase("")) {
            throw new DmException(100, "Site Name is mandatory.");
        }
        if (DmShared.xNull(mrTemp.getVoucherPrefix()).trim().equalsIgnoreCase("")) {
            throw new DmException(200, "Voucher Prefix is mandatory.");
        }
    }
}
