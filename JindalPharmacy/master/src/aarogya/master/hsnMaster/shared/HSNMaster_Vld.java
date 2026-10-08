package aarogya.master.hsnMaster.shared;

import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class HSNMaster_Vld {

    public static void chkSave(HSNMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getHSN(), "HSN", 100);
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 200);

        DmShared.isValidText(mrTemp.getNatureTag(), "Goods/Service Nature", 300);
        DmShared.isValidText(mrTemp.getGstNatureTag(), "GST Nature", 400);
        DmShared.isValidText(mrTemp.getTaxBasedOnTag(), "Tax Based On", 500);

    }

}
