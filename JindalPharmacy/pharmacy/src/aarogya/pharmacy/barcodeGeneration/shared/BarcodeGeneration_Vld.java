package aarogya.pharmacy.barcodeGeneration.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class BarcodeGeneration_Vld {

    public static void chkSave(BarcodeGeneration_Mdl mrTemp) {
        List<GrdBarcodeGeneration_Mdl> listBr = mrTemp.getListBr();

//        for (GrdBarcodeGeneration_Mdl grdEntry_Mdl : mrTemp.getListBr()) {
//            if (!DmShared.dNull(grdEntry_Mdl.getClearDate()).trim().equalsIgnoreCase("")) {
//                if (!DmShared.dNull(grdEntry_Mdl.getChqDate()).trim().equalsIgnoreCase("")) {
//                    if ((DmShared.convertDate(DmShared.dNull(grdEntry_Mdl.getClearDate())).getTime() < (DmShared.convertDate(DmShared.dNull(grdEntry_Mdl.getChqDate()))).getTime())) {
//                        throw new DmException(100, "GridRow", DmShared.iNull(listBr.indexOf(grdEntry_Mdl)).toString(), "Clear Date should be greater than or equal to Cheque Date in line no. " + DmShared.iNull(listBr.indexOf(grdEntry_Mdl) + 1).toString() + ".");
//                    }
//                }
//                if (!DmShared.dNull(grdEntry_Mdl.getVDate()).trim().equalsIgnoreCase("")) {
//                    if ((DmShared.convertDate(DmShared.dNull(grdEntry_Mdl.getClearDate())).getTime() < (DmShared.convertDate(DmShared.dNull(grdEntry_Mdl.getVDate()))).getTime())) {
//                        throw new DmException(200, "GridRow", DmShared.iNull(listBr.indexOf(grdEntry_Mdl)).toString(), "Clear Date should be greater than or equal to Voucher Date in line no. " + DmShared.iNull(listBr.indexOf(grdEntry_Mdl) + 1).toString() + ".");
//                    }
//                }
//            }
//        }
    }
}
