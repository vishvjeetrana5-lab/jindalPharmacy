package dataman.accounts.locationMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class LocationMaster_Vld {

    public static void chkSave(LocationMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Location Name", 100);
        DmShared.isValidText(mrTemp.getModCode().trim(), " Mapping Module ", 200);
      
    }

}
