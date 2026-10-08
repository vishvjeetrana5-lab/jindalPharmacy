package aarogya.master.guiMapping.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class GuiMapping_Vld {

    public static void chkSave(GuiMapping_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getEntrypoint().trim(), "Entry point", 100);
        DmShared.isValidText(mrTemp.getGuiTempalteCode().trim(), "Gui Template", 200);

    }
}
