package dataman.accounts.voucherEntry.shared;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.List;

public class VoucherEntry_Vld {

    public static void chkSave(VoucherEntry_Mdl mrTemp) {
        List<GrdVoucherEntry_Mdl> listData;
        List<TdsCategoryGrid_Mdl> listDataTDS;
        LibraryVoucherCategory lvc = new LibraryVoucherCategory();
        boolean flag = false;

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
        if (DmShared.xNull(mrTemp.getIsAccountNameEnable()).equalsIgnoreCase("Y")) {
            if (DmShared.xNull(mrTemp.getAccountNameCode()).trim().equals("")) {
                throw new DmException(500, "Account Name is mandatory.");
            }
        }

        listData = mrTemp.getgrdData();

        for (int i = 0; i < listData.size(); i++) {
            if (DmShared.iNull(listData.get(i).getAccCode()).longValue() != 0 && !listData.get(i).isHeaderAcRecord() && !listData.get(i).isTdsRecord()) {
                flag = true;

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
        }

        if (!flag) {
            throw new DmException(600, "Please Fill Entry.");
        }
    }
}
