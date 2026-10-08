package dataman.dmBase.dmBaseJasper.server;

import com.ibm.icu.util.ULocale;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmEnum;
import java.math.BigDecimal;

public class DmAmtInWord {

    public static String getAmountToWords(BigDecimal bigDecimal) {
        return getAmountToWords(bigDecimal, "");
    }

    public static String getAmountToWords(BigDecimal bigDecimal, String localeDmCode) {
//        ULocale locale = new ULocale(DmBaseServer.baseLocaleLanguage, localeCountry);
        DmEnum.DmLocaleCountry dmLocaleCountry;

        if (DmBaseShared.xNull(localeDmCode).trim().equalsIgnoreCase("")) {
            dmLocaleCountry = DmBaseServer.baseDmLocaleCountry;
        } else {
            dmLocaleCountry = DmEnum.DmLocaleCountry.getLocaleViaDmCode(localeDmCode);
        }

        return getAmountToWords(bigDecimal, dmLocaleCountry);
    }

    public static String getAmountToWords(BigDecimal bigDecimal, DmEnum.DmLocaleCountry localeCountry) {
        String amtInWord = "";
//        String currencyName = "", subCurrencyName = "";
//        boolean isLakhFormat = false;
//        DmEnum.DmLocaleCountry dmLocaleCountry;

//        if (locale == null) {
//            locale = new ULocale(DmBaseServer.baseLocaleLanguage, DmBaseServer.baseLocaleCountry);
//        }
//        switch (locale.getCountry().toLowerCase()) {
//            case "in":
//                currencyName = "Rupees";
//                subCurrencyName = "Paisa";
//                isLakhFormat = true;
//                break;
//            case "kh":
//                currencyName = "Riel";
//                subCurrencyName = "Paisa";
//                break;
//            default:
//                currencyName = "Dollar";
//                subCurrencyName = "Cent";
//                break;
//        }
        if (localeCountry == null) {
            localeCountry = DmBaseServer.baseDmLocaleCountry;
        }

//        localeCountry = DmEnum.DmLocaleCountry.getLocaleCountry(locale.getCountry());
//        if (isLakhFormat) {
//            amtInWord = DmAmtInWordLakh.getAmountToWords(bigDecimal, currencyName, subCurrencyName);
//        } else {
//            amtInWord = DmAmtInWordMillion.getAmountToWords(bigDecimal, currencyName, subCurrencyName);
//        }
        if (DmBaseServer.majorCurrency != null && !DmBaseServer.majorCurrency.equalsIgnoreCase("")) {
            if (localeCountry.isLakhFormat()) {
                amtInWord = DmAmtInWordLakh.getAmountToWords(bigDecimal, DmBaseServer.majorCurrency, DmBaseServer.minorCurrency);

            } else {
                amtInWord = DmAmtInWordMillion.getAmountToWords(bigDecimal, DmBaseServer.majorCurrency, DmBaseServer.minorCurrency);

            }
        } else {
            if (localeCountry.isLakhFormat()) {
                amtInWord = DmAmtInWordLakh.getAmountToWords(bigDecimal, localeCountry.getMajorCurrency(), localeCountry.getMinorCurrency());

            } else {
                amtInWord = DmAmtInWordMillion.getAmountToWords(bigDecimal, localeCountry.getMajorCurrency(), localeCountry.getMinorCurrency());

            }
        }

        return amtInWord;
    }
}
