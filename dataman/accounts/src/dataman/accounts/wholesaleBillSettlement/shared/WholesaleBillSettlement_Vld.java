package dataman.accounts.wholesaleBillSettlement.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class WholesaleBillSettlement_Vld {

    public static void chkSave(WholesaleBillSettlement_Mdl mrTemp) {

        Boolean isCheck = false;
        DmShared.isValidText(DmShared.xNull(mrTemp.getVoucherTypeTag()), "Voucher Type", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getSupplierTag()), "Supplier", 200);
//        DmShared.isValidText(DmShared.xNull(mrTemp.getAccountNameCode()), "A/c Name", 300);
//        DmShared.isValidText(DmShared.xNull(mrTemp.getCreditAmt()), "Credit Amt", 400);
        DmShared.isValidText(DmShared.dNull(mrTemp.getV_Date()), "Ent.Date", 500);

        for (GrdPendingFPReqDetail_Mdl grdMdl : mrTemp.getLstPendingFPayReq()) {
            // to check whether any request for payment is there or not.
            if (DmShared.iNull(grdMdl.getChecked()).intValue() == 1) {
                isCheck = true;
                break;
            }

        }
        if (!isCheck) {
            throw new DmException(300, "Select atleast one Bill.");
        }

    }

}
