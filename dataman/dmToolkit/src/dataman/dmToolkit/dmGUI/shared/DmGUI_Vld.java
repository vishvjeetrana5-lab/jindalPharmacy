package dataman.dmToolkit.dmGUI.shared;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class DmGUI_Vld {

    public static void chkSave(List<DmGUIRow_Mdl> guiRowList) {
        if (guiRowList != null) {
            for (DmGUIRow_Mdl guiRow : guiRowList) {
                for (DmGUIControl_Mdl guiControl : guiRow.getGUIControlList()) {
                    if (guiControl.isMandatory()) {
                        if (DmShared.xNull(guiControl.getDefaultText()).trim().equalsIgnoreCase("")) {
                            throw new DmException(0, "GUI", DmShared.xNull(guiControl.getSaveInFieldName()), "Please fill " + DmShared.xNull(guiControl.getFieldCaption()));
                        }
                    }
                }
            }
        }
    }
}
