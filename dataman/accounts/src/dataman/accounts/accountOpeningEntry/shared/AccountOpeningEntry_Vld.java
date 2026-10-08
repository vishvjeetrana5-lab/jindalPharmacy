package dataman.accounts.accountOpeningEntry.shared;

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

public class AccountOpeningEntry_Vld {

    public static void chkSave(AccountOpeningEntry_Mdl mrTemp) {
        Date vDate = DmShared.convertDate(mrTemp.getDate());
        Date chqDate = DmShared.convertDate(mrTemp.getChqDate());
        Date clearDate = DmShared.convertDate(mrTemp.getChqClearDate());
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getDate(), "Date", 300);
        DmShared.isValidText(mrTemp.getAccountNameCode(), "A/c Name", 400);
        if (DmShared.vNull(mrTemp.getCredit()).equals(0.0) && DmShared.vNull(mrTemp.getDebit()).equals(0.0)) {
            throw new DmException(500, "Please Fill Debit / Credit Amount.");
        }
        if (!DmShared.vNull(mrTemp.getCredit()).equals(0.0) && !DmShared.vNull(mrTemp.getDebit()).equals(0.0)) {
            throw new DmException(600, "You Can Only Fill One Debit / Credit Amount.");
        }
        if (null != chqDate) {
            if (null != vDate) {
                if (!DmShared.xNull(mrTemp.getChqDate()).equals("")) {
                    if (vDate.after(chqDate)) {
                        throw new DmException(700, "Chq Date Should be Greater Then Or Equal Voucher Date.");
                    }
                }
            }
        }

        if (null != chqDate) {
            if (null != clearDate) {
                if (DmShared.xNull(mrTemp.getChqDate()).equals("") && DmShared.xNull(mrTemp.getChqClearDate()).equals("")) {
                    if (chqDate.after(clearDate)) {
                        throw new DmException(800, "Clear Date Should be Greater Then Or Equal Chq Date.");
                    } else if (!clearDate.equals(chqDate)) {
                        throw new DmException(800, "Clear Date Should be Greater Then Or Equal Chq Date.");
                    }
                }
            }
        }

        if (null != vDate) {
            if (null != clearDate) {
                if (DmShared.xNull(mrTemp.getChqClearDate()).equals("")) {
                    if (clearDate.after(vDate)) {
                        throw new DmException(900, "Clear Date Should be Greater Then Or Equal Voucher Date.");
                    } else if (!clearDate.equals(vDate)) {
                        throw new DmException(900, "Clear Date Should be Greater Then Or Equal Voucher Date.");
                    }
                }
            }
        }

    }

}
