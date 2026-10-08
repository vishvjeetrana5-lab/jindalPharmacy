package aarogya.master.cardTypeMaster.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CardTypeMaster_Vld {

    public static void chkSave(CardTypeMaster_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Card Name", 100);
        DmShared.isValidText(mrTemp.getShortName(), "Short Name", 200);
        DmShared.isValidText(mrTemp.getLinkedAccountCode(), "Linked Account", 300);
        DmShared.isValidText(mrTemp.getCardTypeCode(), "Card Type", 400);
        DmShared.isValidText(mrTemp.getNatureCode(), "Nature", 500);
        DmShared.isValidText(mrTemp.getActiveCode(), "Active", 600);
        DmShared.isValidText(mrTemp.getPhysicalIdReqCode(), "Check Limit", 700);
        if (DmShared.iNull(mrTemp.getCardTypeCode()).intValue() == CardType.FOREIGN_CURRENCY.getEnumValue()) {
            if (DmShared.xNull(mrTemp.getCurrencyName()).equalsIgnoreCase("")) {
                throw new DmException(800, "Currency  is mandatory.");
            }
        }

        DmShared.isValidText(mrTemp.getIsPOSIntegrationCode(), "POS Integration", 900);

        if (DmShared.iNull(mrTemp.getIsPOSIntegrationCode()).intValue() > 0) {
            DmShared.isValidText(mrTemp.getIsPOSIntegrationTypeCode(), "POS Integration Type", 910);
            DmShared.isValidText(mrTemp.getIntegrationParams(), "Integration Params", 920);
        }
    }

}
