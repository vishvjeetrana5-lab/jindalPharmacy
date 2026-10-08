package aarogya.selfToolkit.cardPaymentPopup.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.PaymentSource;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class CardPaymentPopup_Vld {

    public static double calculate(List<GrdCardPayment_Mdl> cardPaymentList) {
        double rtnValue = 0.00;

        if (cardPaymentList != null) {
            for (int i = 0; i < cardPaymentList.size(); i++) {
                if (DmShared.iNull(cardPaymentList.get(i).getCardTypeCode()).longValue() != 0) {
                    rtnValue = rtnValue + DmShared.vNull(cardPaymentList.get(i).getAmount());
                }
            }
        }

        return rtnValue;
    }

    public static void chkSave(String netAmount, CardPaymentPopup_Mdl cardPaymentPopup_Mdl, boolean isCTRAllowed, boolean isCTOAllowed, boolean isCTPAllowed) {
        double total = 0;
        int countPositive = 0, countNegative = 0;

        if (cardPaymentPopup_Mdl != null) {
            if (cardPaymentPopup_Mdl.getPaymentSource() == null) {
                throw new DmException(0, "No Payment Source Choosen.");
            }

            for (int i = 0; i < cardPaymentPopup_Mdl.getGrdCardPaymentList().size(); i++) {
                GrdCardPayment_Mdl grdCardPayment_Mdl = cardPaymentPopup_Mdl.getGrdCardPaymentList().get(i);

                if ((DmShared.iNull(grdCardPayment_Mdl.getCardTypeCode()).longValue() != 0)
                        || DmShared.vNull(grdCardPayment_Mdl.getAmount()).doubleValue() != 0) {

                    total += DmShared.vNull(grdCardPayment_Mdl.getAmount()).doubleValue();

                    if (DmShared.iNull(grdCardPayment_Mdl.getCardTypeCode()).longValue() == 0) {
                        throw new DmException(0, "Please fill Card Type.");
                    }

                    if (DmShared.vNull(grdCardPayment_Mdl.getAmount()).doubleValue() == 0) {
                        throw new DmException(0, "Please fill Amount.");
                    }

                    if (DmShared.iNull(grdCardPayment_Mdl.getAcNameCode()).longValue() == 0) {
                        throw new DmException(0, "Please define Card Account.");
                    }

                    CardType cardType = CardType.getValueOf(DmShared.iNull(grdCardPayment_Mdl.getCardTypeNatureCode()).intValue());
                    if (cardType == CardType.CREDIT_CARD) {
//                        DmShared.isValidText(DmShared.xNull(grdCardPayment_Mdl.getCardNo()), "When we select payment mode " + grdCardPayment_Mdl.getCardTypeName() + " then Card No.", 0);
//                        DmShared.isValidText(DmShared.xNull(grdCardPayment_Mdl.getCardHolderName()), "When we select payment mode " + grdCardPayment_Mdl.getCardTypeName() + " then Card Holder Name", 0);
                    } else if (cardType == CardType.WALLET) {
                        if (DmShared.xNull(grdCardPayment_Mdl.getCardNo()).trim().equalsIgnoreCase("")) {
                            throw new DmException(0, "Invalid wallet no.");
                        } else if (DmShared.iNull(grdCardPayment_Mdl.getCardAuthorizationDocId()).longValue() == 0) {
                            throw new DmException(0, "Invalid authorization.");
                        }
                    } else if (cardType == CardType.FOREIGN_CURRENCY) {
                        if (DmShared.vNull(grdCardPayment_Mdl.getForeignCurrencyAmount()).doubleValue() == 0) {
                            throw new DmException(0, "Please fill Currency.");
                        }
                    }
                    if (DmShared.vNull(grdCardPayment_Mdl.getAmount()).doubleValue() < 0) {
                        ++countNegative;
                    }
                    if (DmShared.vNull(grdCardPayment_Mdl.getAmount()).doubleValue() > 0) {
                        ++countPositive;
                    }
                }
            }
        }
        if (countNegative > 0 && countPositive > 0) {
            throw new DmException(0, "All Amount should be negative or positive.");
        }

        if (cardPaymentPopup_Mdl.getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {
            if (!isCTOAllowed) {
                throw new DmException(0, "Credit To Organization Payment Source not allowed for this entry point.");
            }

            if (DmShared.vNull(cardPaymentPopup_Mdl.getCreditAmount()).doubleValue() == 0 && !DmShared.xNull(cardPaymentPopup_Mdl.getStatus()).equalsIgnoreCase("Revisit")) {
                throw new DmException(0, "Credit Amount is mandatory.");
            }

            if (DmShared.vNull(total).doubleValue() != 0) {
                throw new DmException(0, "In case of Credit To Organization, Receipt amount should be zero.");
            }
        } else if (cardPaymentPopup_Mdl.getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {
            if (!isCTRAllowed) {
                throw new DmException(0, "Credit To Registration Payment Source not allowed for this entry point.");
            }
            if (DmShared.vNull(cardPaymentPopup_Mdl.getCreditAmount()).doubleValue() == 0) {
                throw new DmException(0, "Credit Amount is mandatory.");
            }
            if (DmShared.vNull(total).doubleValue() != 0) {
                throw new DmException(0, "In case of Credit To Registration, Receipt amount should be zero.");
            }
        } else if (cardPaymentPopup_Mdl.getPaymentSource() == PaymentSource.CREDIT_TO_PATIENT
                || cardPaymentPopup_Mdl.getPaymentSource() == PaymentSource.PARTIAL_PAYMENT) {
            if (!isCTPAllowed) {
                throw new DmException(0, "Credit To Patient Payment Source not allowed for this entry point.");
            }

            if (DmShared.vNull(cardPaymentPopup_Mdl.getCreditAmount()).doubleValue() == 0) {
                throw new DmException(0, "Credit Amount is mandatory.");
            }
        }

        if (Math.abs(total) > Math.abs(DmShared.vNull(netAmount).doubleValue())) {
            throw new DmException(0, "Receipt amount cannot be greater than amount.");
        }

        if (total + DmShared.vNull(cardPaymentPopup_Mdl.getCreditAmount()).doubleValue() != DmShared.vNull(netAmount).doubleValue()) {
            throw new DmException(0, "Please adjust full value.");
        }
    }

    public static void chkPosSeeting(RazorPayParams_Mdl posSetting) {
        DmShared.isValidText(posSetting.getAppKey(), "AppKey", 100);
        DmShared.isValidText(posSetting.getCancelURL(), "CancelURL", 200);
        DmShared.isValidText(posSetting.getDeviceId(), "DeviceId", 300);
        DmShared.isValidText(posSetting.getResponseURL(), "ResponseURL", 400);
        DmShared.isValidText(posSetting.getUserName(), "UserName", 500);
    }
}
