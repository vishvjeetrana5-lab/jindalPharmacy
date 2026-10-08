package dataman.accounts.gstVoucherEntry.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.List;

public class GstVoucherEntry_Vld {

    public static void chkSave(GstVoucherEntry_Mdl mrTemp) {
        List<GridAcDetailsMdl> listAcDetailsData;
//        List<GridHSNDetailsMdl> listHsnDetailsData;
        LibraryVoucherCategory lvc = new LibraryVoucherCategory();
        boolean flag = false, flagGstDetail = false;

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
        if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(lvc.CREDIT_NOTE_GST)
                || DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(lvc.DEBIT_NOTE_GST)
                || DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(lvc.CREDIT_INVOICE_GST)) {
            if (DmShared.xNull(mrTemp.getReferenceNo()).trim().equals("")) {
                throw new DmException(300, "Reference No. is mandatory.");
            }
            if (DmShared.dNull(mrTemp.getReferenceDate()).trim().equals("")) {
                throw new DmException(400, "Reference Date is mandatory.");
            }
            if (!DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(lvc.CREDIT_INVOICE_GST)) {
                if (DmShared.xNull(mrTemp.getGstReasonCode()).trim().equals("")) {
                    throw new DmException(410, "Reason is mandatory.");
                }
            }

        }
        if (DmShared.xNull(mrTemp.getGstTypeCode()).trim().equals("")) {
            throw new DmException(800, "GST Type is mandatory.");
        }

        listAcDetailsData = mrTemp.getGrdAcDetails();
//        listHsnDetailsData = mrTemp.getGrdAcDetails();

        for (int i = 0; i < listAcDetailsData.size(); i++) {
            if (DmShared.iNull(listAcDetailsData.get(i).getAccCode()).longValue() != 0) {
                if (!flag) {
                    flag = true;
                    break;
                }
            }
        }

        for (int i = 0; i < listAcDetailsData.size(); i++) {
            if (DmShared.iNull(listAcDetailsData.get(i).getHsnCode()).longValue() != 0) {
                if (!flagGstDetail) {
                    if (DmShared.xNull(listAcDetailsData.get(i).getRoundOffGST()).equalsIgnoreCase("")) {
                        throw new DmException(210, "GST Rounding is mandatory.");
                    }
                    flagGstDetail = true;
                    break;
                }
            }
        }

        if (!flag) {
            throw new DmException(700, "Please Fill A/c Details");
        }
        if (!flagGstDetail) {
            throw new DmException(600, "Please fill GST Detail.");
        }

        if (mrTemp.getDefaultValueMdl() != null) {
            if (DmShared.xNull(mrTemp.getDefaultValueMdl().getSiteStateCode()).trim().equalsIgnoreCase("")) {
                throw new DmException(600, "Please fill Site's State in Site Master");
            }
            if (DmShared.dNull(mrTemp.getDefaultValueMdl().getSiteGstinDate()).trim().equalsIgnoreCase("")) {
                throw new DmException(600, "Please fill Site's GSTN No. & GSTIN Date in Site Master");
            }
        } else {
            throw new DmException(600, "Default values not available!");
        }

    }
}
