package aarogya.inventory.purchaseBill.shared;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;

public class PurchaseBill_Vld {

    private static ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    public static void chkSave(PurchaseBill_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getSupplierTag(), "Supplier", 300);
        DmShared.isValidText(mrTemp.getSupplierBillNo(), "Bill No.", 400);
        DmShared.isValidText(mrTemp.getSupplierBillDate(), "Bill Date", 500);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Voucher Date", 550);
        DmShared.isValidText(mrTemp.getCalculateOnTag(), "Calc. On", 600);
        DmShared.isValidText(mrTemp.getPaymentDueDate(), "Payment Due Date", 650);
        //RCM
        DmShared.isValidText(mrTemp.getRcmApplicable(), "RCM", 800);
        if (mrTemp.getDefaultValueMdl().isIncludeTaxInCost()) {
            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                throw new DmException(800, "RCM cannot be yes.");
            }
        }

        if ((DmShared.convertDate(mrTemp.getPaymentDueDate()).getTime() < (DmShared.convertDate(mrTemp.getSupplierBillDate()).getTime()))) {
            throw new DmException(540, "Payment Due Date should be greater than or equal to Bill Date.");
        }
        
        if ((DmShared.convertDate(mrTemp.getVoucherDate()).getTime() < (DmShared.convertDate(mrTemp.getSupplierBillDate()).getTime()))) {
            throw new DmException(550, "Voucher Date should be greater than or equal to Bill Date.");
        }

        if (mrTemp.getDefaultValueMdl().isInventoryAccountingActive()) {
            if (DmShared.iNull(mrTemp.getBalancingAc()).longValue() == 0) {
                throw new DmException(100, "Please define balancing a/c for " + mrTemp.getVoucherTypeName());
            }
        } else {
            if (DmShared.iNull(mrTemp.getRoundOffAcCode()).longValue() == 0) {
                throw new DmException(0, "Round-off a/c cannot be blank.");
            }
            if (DmShared.iNull(mrTemp.getFinalDiscountAcCode()).longValue() == 0) {
                throw new DmException(0, "Final discount a/c cannot be blank.");
            }
        }

        boolean flag = false;
        String grnItemChangeCondition = "";
        List<GrdItemDetail_Mdl> listItemDetail = mrTemp.getListItemDetail();
        for (GrdItemDetail_Mdl detail_Mdl : listItemDetail) {
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                if (!detail_Mdl.isServiceRecord()) {
                    flag = true;
                    /*==============================<< Create condition string for GRN item change check >>===============================*/
                    if (!grnItemChangeCondition.trim().equalsIgnoreCase("")) {
                        grnItemChangeCondition += " OR ";
                    } else {
                        grnItemChangeCondition += " WHERE ";
                    }
                    grnItemChangeCondition += " (pcd.docId = '" + DmShared.xNull(detail_Mdl.getPcDocId()) + "'";
                    grnItemChangeCondition += " AND pcd.v_SNo = '" + DmShared.xNull(detail_Mdl.getPcV_SNo()) + "'";
                    grnItemChangeCondition += " AND pcd.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";

                    if (DmShared.vNull(detail_Mdl.getAcceptQty()).doubleValue() == 0) {
                        throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Accept Quantity in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(detail_Mdl.getSkuQty()).doubleValue() == 0) {
                        throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter SKU Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }

                    if (mrTemp.getDefaultValueMdl().isInventoryAccountingActive()) {
                        if (DmShared.iNull(detail_Mdl.getIsubCode()).longValue() == 0) {
                            throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Inventory a/c is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                        }

                    } else {
                        if (DmShared.iNull(detail_Mdl.getIsubCode()).longValue() == 0) {
                            throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Purchase a/c is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                        }
                    }

                    if (!mrTemp.getDefaultValueMdl().isIncludeTaxInCost()) {
                        if (DmShared.vNull(detail_Mdl.getSgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getSgstAcCode()).longValue() == 0) {
                                throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "SGST a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getSgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "SGST-RCM a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                                }
                            }
                        }
                        if (DmShared.vNull(detail_Mdl.getUgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getUgstAcCode()).longValue() == 0) {
                                throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "UGST a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getUgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "UGST-RCM a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                                }
                            }
                        }
                        if (DmShared.vNull(detail_Mdl.getCgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getCgstAcCode()).longValue() == 0) {
                                throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "CGST a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getCgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "CGST-RCM a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                                }
                            }
                        }

                        if (DmShared.vNull(detail_Mdl.getIgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getIgstAcCode()).longValue() == 0) {
                                throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "IGST a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getIgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "IGST-RCM a/c  is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                                }
                            }
                        }
                    }

                }
            }
        }
        mrTemp.setGrnItemChangeCondition(grnItemChangeCondition);

        if (!flag) {
            throw new DmException(700, "Items are mandatory.");
        }

        if (mrTemp.getDefaultValueMdl() != null) {

            if (DmShared.xNull(mrTemp.getDefaultValueMdl().getSiteStateCode()).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Please fill Site's State in Site Master");
            }

            if (DmShared.xNull(mrTemp.getSupplierStateCode()).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Please fill Supplier's State in Supplier Master");
            }

            if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(pvc.PURCHASE_BILL_UNREGISTERED)
                    && !DmShared.xNull(mrTemp.getPartyTypeForEntry()).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                throw new DmException(0, "Only unregistered suppliers are allowed for " + DmShared.xNull(mrTemp.getVoucherTypeName()));
            }

            if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(pvc.PURCHASE_BILL)
                    && DmShared.xNull(mrTemp.getPartyTypeForEntry()).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                throw new DmException(0, "Unregistered suppliers are not allowed for " + DmShared.xNull(mrTemp.getVoucherTypeName()));
            }

            if (!DmShared.dNull(mrTemp.getDefaultValueMdl().getSiteGstinDate()).trim().equals("")
                    && !DmShared.dNull(mrTemp.getSupplierBillDate()).trim().equals("")
                    && DmShared.convertDate(mrTemp.getDefaultValueMdl().getSiteGstinDate()).getTime() <= DmShared.convertDate(mrTemp.getSupplierBillDate()).getTime()
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
                            throw new DmException(900, "ServiceRow", String.valueOf(serviceRowIndex), "Service a/c is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                        }
                    }

                    if (!mrTemp.getDefaultValueMdl().isIncludeTaxInCost()) {
                        if (DmShared.vNull(detail_Mdl.getSgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getSgstAcCode()).longValue() == 0) {
                                throw new DmException(900, "ServiceRow", String.valueOf(serviceRowIndex), "SGST a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getSgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(900, "ServiceRow", String.valueOf(serviceRowIndex), "SGST-RCM a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                                }
                            }
                        }
                        if (DmShared.vNull(detail_Mdl.getUgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getUgstAcCode()).longValue() == 0) {
                                throw new DmException(900, "ServiceRow", String.valueOf(serviceRowIndex), "UGST a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getUgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(900, "ServiceRow", String.valueOf(serviceRowIndex), "UGST-RCM a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                                }
                            }
                        }
                        if (DmShared.vNull(detail_Mdl.getCgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getCgstAcCode()).longValue() == 0) {
                                throw new DmException(900, "ServiceRow", String.valueOf(serviceRowIndex), "CGST a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getCgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(900, "ServiceRow", String.valueOf(serviceRowIndex), "CGST-RCM a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                                }
                            }

                        }
                        if (DmShared.vNull(detail_Mdl.getIgst()).doubleValue() != 0) {
                            if (DmShared.iNull(detail_Mdl.getIgstAcCode()).longValue() == 0) {
                                throw new DmException(900, "ServiceRow", String.valueOf(serviceRowIndex), "IGST a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
                            }
                            if (DmShared.iNull(mrTemp.getRcmApplicable()).intValue() == 1) {
                                if (DmShared.iNull(detail_Mdl.getIgstRcmAcCode()).longValue() == 0) {
                                    throw new DmException(900, "ServiceRow", String.valueOf(serviceRowIndex), "IGST-RCM a/c  is required for line no. " + String.valueOf(serviceRowIndex + 1) + ".");
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
