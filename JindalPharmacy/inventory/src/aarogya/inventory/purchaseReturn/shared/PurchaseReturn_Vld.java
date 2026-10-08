package aarogya.inventory.purchaseReturn.shared;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class PurchaseReturn_Vld {

    public static void chkSave(PurchaseReturn_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode().trim(), "Return Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Return Date", 200);
        DmShared.isValidText(mrTemp.getSupplierCode(), "Supplier", 300);
//        DmShared.isValidText(mrTemp.getDepartmentCode(), "Department", 400);
        DmShared.isValidText(mrTemp.getGstReasonCode(), "Reason", 500);
        DmShared.isValidText(mrTemp.getPartyBillNo(), "Ref. No.", 600);
        DmShared.isValidText(mrTemp.getPartyBillDate(), "Ref. Date", 700);
        DmShared.isValidText(mrTemp.getEwayBillRequiredCode(), "E-Way Bill Req.", 900);
        //RCM
        DmShared.isValidText(mrTemp.getRcmApplicable(), "RCM", 1100);
        if (mrTemp.getDefaultValueMdl().isIncludeTaxInCost()) {
            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                throw new DmException(1100, "RCM cannot be yes.");
            }
        }

        if ((DmShared.convertDate(mrTemp.getVoucherDate()).getTime() < (DmShared.convertDate(mrTemp.getPartyBillDate()).getTime()))) {
            throw new DmException(200, "Return Date should be greater than or equal to Ref. Date.");
        }

        if (!DmShared.dNull(mrTemp.getEwayDate()).trim().equalsIgnoreCase("")) {
            if ((DmShared.convertDate(mrTemp.getVoucherDate()).getTime() < (DmShared.convertDate(mrTemp.getPartyBillDate()).getTime()))) {
                throw new DmException(1000, "e-Way Bill Datte Date should be greater than or equal to Return Date.");
            }
        }
        boolean flag = false;
        String pbItemChangeCondition = "";
        List< GrdItemDetail_Mdl> listItemDetail = mrTemp.getListItemDetail();
        for (GrdItemDetail_Mdl detail_Mdl : listItemDetail) {
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                if (!detail_Mdl.isServiceRecord()) {
                    flag = true;

                    /*==============================<< Create condition string for pBill item change check >>===============================*/
                    if (!pbItemChangeCondition.trim().equalsIgnoreCase("")) {
                        pbItemChangeCondition += " OR ";
                    } else {
                        pbItemChangeCondition += " WHERE ";
                    }
                    pbItemChangeCondition += " (pbd.docId = '" + DmShared.xNull(detail_Mdl.getPbDocId()) + "'";
                    pbItemChangeCondition += " AND pbd.v_SNo = '" + DmShared.xNull(detail_Mdl.getPbV_SNo()) + "'";
                    pbItemChangeCondition += " AND pbd.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";

                    if (DmShared.iNull(detail_Mdl.getDepartmentCode()).longValue() == 0) {
                        throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Department in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(detail_Mdl.getQuantity()).doubleValue() == 0) {
                        throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Quantity in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(detail_Mdl.getRate()).doubleValue() == 0) {
                        throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Rate in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(detail_Mdl.getAmount()).doubleValue() == 0) {
                        throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Amount in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }

                    if (mrTemp.getDefaultValueMdl().isInventoryAccountingActive()) {
                        if (DmShared.iNull(detail_Mdl.getIsubCode()).longValue() == 0) {
                            throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Inventory a/c is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                        }

                    } else {
                        if (DmShared.iNull(detail_Mdl.getIsubCode()).longValue() == 0) {
                            throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Purchase Return a/c is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                        }
                    }

                    if (!mrTemp.getDefaultValueMdl().isIncludeTaxInCost()) {
                        if (DmShared.vNull(detail_Mdl.getSgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getSgstAcCode()).longValue() == 0) {
                                throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "SGST a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getSgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "SGST-RCM a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                                }
                            }

                        }
                        if (DmShared.vNull(detail_Mdl.getUgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getUgstAcCode()).longValue() == 0) {
                                throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "UGST a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getUgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "UGST-RCM a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                                }
                            }
                        }
                        if (DmShared.vNull(detail_Mdl.getCgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getCgstAcCode()).longValue() == 0) {
                                throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "CGST a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getCgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "CGST-RCM a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                                }
                            }
                        }

                        if (DmShared.vNull(detail_Mdl.getIgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getIgstAcCode()).longValue() == 0) {
                                throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "IGST a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getIgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "IGST-RCM a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                                }
                            }
                        }
                    }

                }
            }
        }
        mrTemp.setPbItemChangeCondition(pbItemChangeCondition);
        if (!flag) {
            throw new DmException(800, "Items are mandatory.");
        }

        if (mrTemp.getDefaultValueMdl() != null) {

            if (DmShared.xNull(mrTemp.getDefaultValueMdl().getSiteStateCode()).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Please fill Site's State in Site Master");
            }

            if (DmShared.xNull(mrTemp.getSupplierStateCode()).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Please fill Supplier's State in Supplier Master");
            }

            if (!DmShared.dNull(mrTemp.getDefaultValueMdl().getSiteGstinDate()).trim().equals("")
                    && !DmShared.dNull(mrTemp.getVoucherDate()).trim().equals("")
                    && DmShared.convertDate(mrTemp.getDefaultValueMdl().getSiteGstinDate()).getTime() <= DmShared.convertDate(mrTemp.getVoucherDate()).getTime()
                    && DmShared.xNull(mrTemp.getDefaultValueMdl().getSiteRegistrationType()).equalsIgnoreCase(DmEnum.DmPartyType.Registered.getId())) {

                if (DmShared.xNull(mrTemp.getDefaultValueMdl().getSiteGstinDate()).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Please fill Site's GSTN No. & GSTIN Date in Site Master");
                }

                if (DmShared.xNull(mrTemp.getPartyType()).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Please fill Supplier's Registration Type in Supplier Master");
                }

                if (DmShared.xNull(mrTemp.getPartyTypeForEntry()).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Please fill Supplier's GSTN No. & GSTIN Date in Supplier Master");
                }
            }
        } else {
            throw new DmException(0, "Default values not available!");
        }

        //===============================< Services validation >===============================================================
        int itemRowCount = 0, serviceRowIndex = 0;
        for (GrdItemDetail_Mdl detail_Mdl : mrTemp.getListItemDetail()) {
            if (!detail_Mdl.isServiceRecord()) {
                itemRowCount++;
            } else {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    serviceRowIndex = itemRowCount - DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).intValue();

                    if (!mrTemp.getDefaultValueMdl().isInventoryAccountingActive()) {
                        if (DmShared.iNull(detail_Mdl.getIsubCode()).longValue() == 0) {
                            throw new DmException(1200, "ServiceRow", String.valueOf(serviceRowIndex), "Service a/c is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                        }
                    }

                    if (!mrTemp.getDefaultValueMdl().isIncludeTaxInCost()) {
                        if (DmShared.vNull(detail_Mdl.getSgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getSgstAcCode()).longValue() == 0) {
                                throw new DmException(1200, "ServiceRow", String.valueOf(serviceRowIndex), "SGST a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getSgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(1200, "ServiceRow", String.valueOf(serviceRowIndex), "SGST-RCM a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                                }
                            }
                        }
                        if (DmShared.vNull(detail_Mdl.getUgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getUgstAcCode()).longValue() == 0) {
                                throw new DmException(1200, "ServiceRow", String.valueOf(serviceRowIndex), "UGST a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getUgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(1200, "ServiceRow", String.valueOf(serviceRowIndex), "UGST-RCM a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                                }
                            }
                        }
                        if (DmShared.vNull(detail_Mdl.getCgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getCgstAcCode()).longValue() == 0) {
                                throw new DmException(1200, "ServiceRow", String.valueOf(serviceRowIndex), "CGST a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getCgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(1200, "ServiceRow", String.valueOf(serviceRowIndex), "CGST-RCM a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                                }
                            }

                        }
                        if (DmShared.vNull(detail_Mdl.getIgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getIgstAcCode()).longValue() == 0) {
                                throw new DmException(1200, "ServiceRow", String.valueOf(serviceRowIndex), "IGST a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getIgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(1200, "ServiceRow", String.valueOf(serviceRowIndex), "IGST-RCM a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                                }
                            }
                        }
                    }
                }
            }
        }
        //==================================================================================================
    }

}
