package dataman.accounts.fundInTransitEntry.shared;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.Date;

public class FundInTransitEntry_Vld {

    public static void chkSave(FundInTransitEntry_Mdl mrTemp) {
        Date dtCheque = DmShared.convertDate(mrTemp.getDtChq());
        Date dtClearing = DmShared.convertDate(mrTemp.getDtClearing());
        DmShared.isValidText(mrTemp.getDtEntry(), "Entry Date", 100);
        DmShared.isValidText(mrTemp.getCurrentSiteTag(), "Current Site", 200);
        DmShared.isValidText(mrTemp.getTransferNoTag(), "Transfer No.", 300);
        DmShared.isValidText(mrTemp.getDrAccountTag(), "Debit A/c", 400);
        DmShared.isValidText(mrTemp.getCrAccountTag(), "Credit A/c", 500);
        DmShared.isValidAmount(mrTemp.getAmount(), "Amount", 600);
        if (!(DmShared.dNull(mrTemp.getDtChq())).equalsIgnoreCase("") && !DmShared.dNull(mrTemp.getDtClearing()).equals("")) {
            if (dtCheque.after(dtClearing)) {
                throw new DmException(700, "Clearing date can't be less than cheque data.");
            }
        }
    }

}
