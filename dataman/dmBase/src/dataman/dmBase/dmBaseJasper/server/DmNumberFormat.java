package dataman.dmBase.dmBaseJasper.server;

import com.ibm.icu.text.DecimalFormat;
import java.math.BigDecimal;
import java.text.Format;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;

public class DmNumberFormat {

    public String formatNumber(BigDecimal bigDecimal, int allowedDecimalPlaces, boolean showDecimalUptoAllowedDecimalPlaces, boolean showCurrenySymbol, ULocale locale) {
        String returnValue = "-", currencySymbol = "";
        Format format;
        DecimalFormat decimalFormat;

        try {
            if (bigDecimal != null && bigDecimal.doubleValue() != 0) {
                if (allowedDecimalPlaces <= 0) {
                    allowedDecimalPlaces = 0;
                }
                if (locale == null) {
                    locale = new ULocale("en", DmBaseServer.baseDmLocaleCountry.getISOCode3());
                }
                format = NumberFormat.getCurrencyInstance(locale);
                decimalFormat = (DecimalFormat) format;
                if (showDecimalUptoAllowedDecimalPlaces) {
                    decimalFormat.setMinimumFractionDigits(allowedDecimalPlaces);
                } else {
                    decimalFormat.setMinimumFractionDigits(0);
                }
                decimalFormat.setMaximumFractionDigits(allowedDecimalPlaces);

                if (showCurrenySymbol) {
                    currencySymbol = DmBaseShared.xNull(decimalFormat.getCurrency().getSymbol()).trim();
                    decimalFormat.setPositivePrefix(currencySymbol);
                    decimalFormat.setNegativePrefix(currencySymbol + "-");
                } else {
                    decimalFormat.setPositivePrefix("");
                    decimalFormat.setNegativePrefix("-");
                }

                decimalFormat.setPositiveSuffix("");
                decimalFormat.setNegativeSuffix("");

                returnValue = decimalFormat.format(bigDecimal);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return returnValue;
    }

//    public String formatNumber(BigDecimal decimal, int allowedDecimalPlaces, boolean showDecimalUptoAllowedDecimalPlaces) {
//        StringBuilder returnValue = new StringBuilder();
//        try {
//
//            if ((decimal != null) && (decimal.doubleValue() != 0d)) {
//
//                if (allowedDecimalPlaces <= 0) {
//                    allowedDecimalPlaces = 0;
//                }
//
//                String tempFormat = "#";
//
//                if (allowedDecimalPlaces > 0) {
//                    String strictString = ((showDecimalUptoAllowedDecimalPlaces) ? "0" : "#");
//                    tempFormat += ".";
//                    for (int i = 0; i < allowedDecimalPlaces; i++) {
//                        tempFormat += strictString;
//                    }
//                }
//
//                BigDecimal tempDecimal = decimal.setScale(allowedDecimalPlaces, RoundingMode.HALF_UP);
//                DecimalFormat decimalFormat = new DecimalFormat(tempFormat);
//                decimalFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
//                decimalFormat.setGroupingUsed(false);
//
//                boolean hasNegation = (tempDecimal.signum() == -1);
//                String formattedValue = decimalFormat.format(tempDecimal);
//
//                String integralPart = "";
//                String fractionalPart = "";
//
//                int indexOfPeriod = formattedValue.indexOf(".");
//                if (indexOfPeriod >= 0) {
//                    integralPart = formattedValue.substring(0, indexOfPeriod);
//                    fractionalPart = formattedValue.substring(indexOfPeriod + 1);
//                } else {
//                    integralPart = formattedValue;
//                }
//
//                String formattedIntegralPart = formatIntegralPart(integralPart, hasNegation);
//
//                returnValue.append(formattedIntegralPart);
//                if (indexOfPeriod >= 0) {
//                    returnValue.append(".").append(fractionalPart);
//                }
//            } else {
//                returnValue.append("-");
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return returnValue.toString();
//    }
//
//    private String formatIntegralPart(String integralPart, boolean hasNegation) {
//        StringBuilder returnValue = new StringBuilder();
//        try {
//
//            if (hasNegation) {
//                returnValue.append("-");
//                integralPart = integralPart.substring(1);
//            }
//
//            if (integralPart.length() > 0) {
//                Long intPart = Long.parseLong(integralPart);
//                String formattablePart = String.valueOf((Math.abs(intPart) / 1000l));
//                String constantPart = "";
//                if (integralPart.length() > 3) {
//                    constantPart = integralPart.substring(formattablePart.length(), integralPart.length());
//                } else {
//                    constantPart = integralPart.substring(0);
//                }
//
//                if ((formattablePart.length() > 0) && (Long.parseLong(formattablePart) > 0)) {
//                    DecimalFormat decimalFormat = new DecimalFormat("#,##");
//                    String tempFormat = decimalFormat.format(Long.parseLong(formattablePart));
//                    returnValue.append(tempFormat).append(",");
//                }
//
//                returnValue.append(constantPart);
//            }
//
//        } finally {
//        }
//        return returnValue.toString();
//    }
}
