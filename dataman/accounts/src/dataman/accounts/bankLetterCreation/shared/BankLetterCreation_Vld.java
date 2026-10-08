package dataman.accounts.bankLetterCreation.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class BankLetterCreation_Vld {

    public static void chkSave(BankLetterCreation_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getDate().trim(), "Date", 100);

        boolean isGrdEmpty = true;
        for (GrdBankLetterCreation_Mdl detailMdl : mrTemp.getGrdData()) {
            if (!DmShared.xNull(detailMdl.getSharedKeyFieldCode()).trim().equals("")) {
                isGrdEmpty = false;
                break;
            }
        }
        if (isGrdEmpty) {
            throw new DmException(600, "Please fill Grid details.");
        }

    }
}
