package dataman.accounts.bankReconciliationEntry.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class BankReconciliationEntry_Vld {

//    public static void chkSave11(BankReconciliationEntry_Mdl mrTemp) {
//        DmShared.isValidText(mrTemp.getBankCode(), "Bank Name", 100);
//        DmShared.isValidText(mrTemp.getToDate(), "To Date", 300);
//
//        for (GrdBankReconciliationEntry_Mdl grdEntry_Mdl : mrTemp.getGrdBankReconciliationEntry_Mdl()) {
//            if (!DmShared.xNull(grdEntry_Mdl.getClearDate()).trim().equalsIgnoreCase("")) {
//
//            }
//        }
////        if (!(DmShared.dNull(mrTemp.getChqDate()).trim().equalsIgnoreCase("")) && (!txtClearDate.getText().trim().equalsIgnoreCase(""))) {
////            if ((DmShared.convertDate(txtClearDate.getText()).getTime() < (DmShared.convertDate(entry_Mdl.getChqDate())).getTime())) {
////                new DmAlertMessageBox("Clear Date should be greater than or equal to Cheque Date.", txtClearDate).show();
////                return false;
////            }
////        }
////        if (!(DmShared.dNull(entry_Mdl.getVDate()).trim().equalsIgnoreCase("")) && (!txtClearDate.getText().trim().equalsIgnoreCase(""))) {
////            if ((DmShared.convertDate(txtClearDate.getText()).getTime() < (DmShared.convertDate(entry_Mdl.getVDate())).getTime())) {
////                new DmAlertMessageBox("Clear Date should be greater than or equal to Voucher Date.", txtClearDate).show();
////                return false;
////            }
////        }
//    }
    public static void chkSave(BankReconciliationEntry_Mdl mrTemp) {
        List<GrdBankReconciliationEntry_Mdl> listBr = mrTemp.getListBr();
//        DmShared.isValidText(setting_Mdl.getBankCode(), "Bank Name", 100);
//        DmShared.isValidText(setting_Mdl.getToDate(), "To Date", 300);

        for (GrdBankReconciliationEntry_Mdl grdEntry_Mdl : mrTemp.getListBr()) {
            if (!DmShared.dNull(grdEntry_Mdl.getClearDate()).trim().equalsIgnoreCase("")) {
                if (!DmShared.dNull(grdEntry_Mdl.getChqDate()).trim().equalsIgnoreCase("")) {
                    if ((DmShared.convertDate(DmShared.dNull(grdEntry_Mdl.getClearDate())).getTime() < (DmShared.convertDate(DmShared.dNull(grdEntry_Mdl.getChqDate()))).getTime())) {
                        throw new DmException(100, "GridRow", DmShared.iNull(listBr.indexOf(grdEntry_Mdl)).toString(), "Clear Date should be greater than or equal to Cheque Date in line no. " + DmShared.iNull(listBr.indexOf(grdEntry_Mdl) + 1).toString() + ".");
                    }
                }
                if (!DmShared.dNull(grdEntry_Mdl.getVDate()).trim().equalsIgnoreCase("")) {
                    if ((DmShared.convertDate(DmShared.dNull(grdEntry_Mdl.getClearDate())).getTime() < (DmShared.convertDate(DmShared.dNull(grdEntry_Mdl.getVDate()))).getTime())) {
                        throw new DmException(200, "GridRow", DmShared.iNull(listBr.indexOf(grdEntry_Mdl)).toString(), "Clear Date should be greater than or equal to Voucher Date in line no. " + DmShared.iNull(listBr.indexOf(grdEntry_Mdl) + 1).toString() + ".");
                    }
                }
            }
        }
    }
}
