package dataman.dmToolkit.singleLineMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;

public class SingleLineMaster_Vld {

    public static void chkSave(KeyFieldExtended keyField, SingleLineMaster_Mdl singleLineMaster) {
        DmShared.isValidText(keyField.getSharedKeyFieldName(), singleLineMaster.getFieldTitle(), 100);
    }
}
