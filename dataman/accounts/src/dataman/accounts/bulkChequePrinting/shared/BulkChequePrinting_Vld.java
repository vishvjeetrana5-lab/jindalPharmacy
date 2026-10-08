/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.accounts.bulkChequePrinting.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

/**
 *
 * @author Dataman
 */
public class BulkChequePrinting_Vld {

    static boolean isGridDetailsExists = false;

    public static void chkSave(BulkChequePrinting_Mdl mrTemp) {
        for (GrdBulkChequePrinting_Mdl mdl : mrTemp.getGrdChequeDetail_Mdl()) {
            if (DmShared.iNull(mdl.getSharedKeyFieldCode()).longValue() > 0) {
                isGridDetailsExists = true;

                DmShared.isValidText(DmShared.xNull(mdl.getPrintName()), "Print Name", 100);
                DmShared.isValidText(DmShared.xNull(mdl.getChqNo()), "Cheque Number", 100);
                DmShared.isValidText(DmShared.xNull(mdl.getChqDate()), "Cheque Date", 100);
                DmShared.isValidText(DmShared.xNull(mdl.getSharedKeyFieldName()), "Voucher No.", 100);
                DmShared.isValidText(DmShared.xNull(mdl.getV_TypeShortName()), "Type", 100);
            }
        }

        if (!isGridDetailsExists) {
            throw new DmException(100, "Grid Details are mandatory");
        }

    }

}
