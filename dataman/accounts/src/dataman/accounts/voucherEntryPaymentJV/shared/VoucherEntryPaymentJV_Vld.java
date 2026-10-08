package dataman.accounts.voucherEntryPaymentJV.shared;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.List;

public class VoucherEntryPaymentJV_Vld {

    public static void chkSave(VoucherEntryPaymentJV_Mdl mrTemp) {
        List<GrdVoucherEntryPaymentJV_Mdl> listData;
        List<TdsCategoryGrid_Mdl> listDataTDS;
        LibraryVoucherCategory lvc = new LibraryVoucherCategory();
        double amtDr = 0, amtCr = 0;
        boolean flag = false;
        boolean vldPmtCr = false, vldRctDr = false;
        String acNature = "";
        int chequeCount = 0;

        if (DmShared.xNull(mrTemp.getVoucherTypeCode()).trim().equals("")) {
            throw new DmException(100, "Voucher Type is mandatory.");
        }
        if (DmShared.dNull(mrTemp.getVoucherDate()).trim().equals("")) {
            throw new DmException(200, "Voucher Date is mandatory.");
        }
        if (!DmShared.dNull(mrTemp.getReferenceDate()).trim().equals("")) {
            if ((DmShared.convertDate(mrTemp.getVoucherDate()).getTime() < (DmShared.convertDate(mrTemp.getReferenceDate()).getTime()))) {
                throw new DmException(200, "Voucher Date should be greater than or equal to Ref. Date.");
            }
        }
//        if (DmShared.xNull(mrTemp.getIsAccountNameEnable()).equalsIgnoreCase("Y")) {
//            if (DmShared.xNull(mrTemp.getAccountNameCode()).trim().equals("")) {
//                throw new DmException(500, "Account Name is mandatory.");
//            }
//        }

        listData = mrTemp.getgrdData();

        for (int i = 0; i < listData.size(); i++) {
            if (DmShared.iNull(listData.get(i).getAccCode()).longValue() != 0) {
                flag = true;

                amtDr += DmShared.vNull(listData.get(i).getDebit());
                amtCr += DmShared.vNull(listData.get(i).getCredit());

                listDataTDS = listData.get(i).getTds_Mdl().getTdsGridList();
                if (listDataTDS != null) {
                    for (int j = 0; j < listDataTDS.size(); j++) {
                        if (!DmShared.xNull(listDataTDS.get(j).getDescriptionCode()).trim().equalsIgnoreCase("")) {
                            if (DmShared.xNull(listDataTDS.get(j).getPostingAccountCode()).trim().equalsIgnoreCase("")) {
                                throw new DmException(600, "Please Define Posting A/c For TDS Category: " + listData.get(i).getTds_Mdl().getTdsCategoryName() + "");
                            }
                        }
                    }
                }
            }

            if (DmShared.xNull(mrTemp.getVoucherShortName()).trim().equalsIgnoreCase("PMT")) {
                acNature = "At least one Bank nature account must be credit";
                if ((DmShared.xNull(listData.get(i).getAccNature()).trim().equalsIgnoreCase("BK"))
                        && (DmShared.vNull(listData.get(i).getCredit()) > 0)) {
                    vldPmtCr = true;
                    mrTemp.setAccountNameCode(DmShared.xNull(listData.get(i).getAccCode()));
                }
            }

            if (DmShared.xNull(mrTemp.getVoucherShortName()).trim().equalsIgnoreCase("CPV")) {
                acNature = "At least one Cash nature account must be credit";
                if ((DmShared.xNull(listData.get(i).getAccNature()).trim().equalsIgnoreCase("CS"))
                        && (DmShared.vNull(listData.get(i).getCredit()) > 0)) {
                    vldPmtCr = true;
                    mrTemp.setAccountNameCode(DmShared.xNull(listData.get(i).getAccCode()));
                }
            }

            if (DmShared.xNull(mrTemp.getVoucherShortName()).trim().equalsIgnoreCase("RCT")) {
                acNature = "At least one Bank nature account must be Debit";
                if ((DmShared.xNull(listData.get(i).getAccNature()).trim().equalsIgnoreCase("BK"))
                        && (DmShared.vNull(listData.get(i).getDebit()) > 0)) {
                    vldPmtCr = true;
                    mrTemp.setAccountNameCode(DmShared.xNull(listData.get(i).getAccCode()));
                }
            }

            if (DmShared.xNull(mrTemp.getVoucherShortName()).trim().equalsIgnoreCase("CRV")) {
                acNature = "At least one Cash nature account must be Debit";
                if ((DmShared.xNull(listData.get(i).getAccNature()).trim().equalsIgnoreCase("CS"))
                        && (DmShared.vNull(listData.get(i).getDebit()) > 0)) {
                    vldPmtCr = true;
                    mrTemp.setAccountNameCode(DmShared.xNull(listData.get(i).getAccCode()));
                }
            }
            
            if (!DmShared.xNull(listData.get(i).getChequeDetail_Mdl().getChequeNo()).equalsIgnoreCase("")){
                chequeCount+=1;
            };
        }

        if ((DmShared.xNull(mrTemp.getVoucherShortName()).trim().equalsIgnoreCase("PMT"))
                || (DmShared.xNull(mrTemp.getVoucherShortName()).trim().equalsIgnoreCase("CPV"))) {
            if (vldPmtCr == false) {
                throw new DmException(700, acNature);
            }
        } else if ((DmShared.xNull(mrTemp.getVoucherShortName()).trim().equalsIgnoreCase("RCT"))
                || (DmShared.xNull(mrTemp.getVoucherShortName()).trim().equalsIgnoreCase("CRV"))) {
            if (vldPmtCr == false) {
                throw new DmException(700, acNature);
            }
        }

        if (Math.round(amtCr) != Math.round(amtDr)) {
            throw new DmException(600, "Debit & Credit amount is not equal.");
        }

        if(chequeCount>1){
            throw new DmException(800, "Only one cheque can be entered.");
        }
        
        if (!flag) {
            throw new DmException(600, "Please Fill Entry");
        }
    }
}
