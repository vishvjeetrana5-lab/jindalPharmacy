package dataman.accounts.fundTransferEntry.shared;

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

public class FundTransferEntry_Vld {

    public static void chkSave(FundTransferEntry_Mdl mrTemp) {
        Date dtCheque = DmShared.convertDate(mrTemp.getDtCheque());
        Date dtClearing = DmShared.convertDate(mrTemp.getDtClearing());
        DmShared.isValidText(mrTemp.getDtEntry(), "Entry Date", 100);
        DmShared.isValidText(mrTemp.getCurrentSiteTag(), "Current Site", 200);
        DmShared.isValidText(mrTemp.getTrnToSiteTag(), "Transfer site", 300);
        DmShared.isValidText(mrTemp.getDrAccountTag(), "Debit A/c", 400);
        DmShared.isValidText(mrTemp.getCrAccountTag(), "Credit A/c", 500);
        DmShared.isValidAmount(mrTemp.getAmount(), "Transfer Amount", 600);
        if (!(DmShared.dNull(mrTemp.getDtCheque())).equalsIgnoreCase("") && !DmShared.dNull(mrTemp.getDtClearing()).equals("")) {
            if (dtCheque.after(dtClearing)) {
                throw new DmException(600, "Clearing date can't be less than cheque data.");
            }
        }
    }

}
