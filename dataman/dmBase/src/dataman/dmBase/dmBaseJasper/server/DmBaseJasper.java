package dataman.dmBase.dmBaseJasper.server;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ibm.icu.util.ULocale;
import com.sun.javafx.fxml.builder.URLBuilder;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmEnum;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRDefaultScriptlet;

public class DmBaseJasper extends JRDefaultScriptlet {

//<editor-fold defaultstate="collapsed" desc="Convert Number To Word">
//    private static String numberToWords(int number) {
//        //if this number is 75, then this function should return seventy five
//        int intQuotient = (int) (number / 10);
//        StringBuilder word = new StringBuilder();
//
//        if (intQuotient > 0) {
//
//            if (intQuotient == 1 && (number % 10) > 0) {
//                word.append(wordInTens(number % 10));
//                return word.toString();
//            }
//            word.append(wordInTenMultiples(intQuotient));
//        }
//        int remainder = number % 10;
//        if (remainder > 0) {
//            if (word.length() > 0) {
//                word.append(" ");
//            }
//            word.append(numberInWords(remainder));
//        }
//
//        return word.toString();
//
//    }
//    private static String wordInTenMultiples(int number) {
//        String[] words = {"Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty",
//            "Seventy", "Eighty", "Ninety"};
//        return words[number - 1];
//    }
//    private static String wordInTens(int number) {
//        String words[] = {"Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
//            "Seventeen", "Eighteen", "Ninteen"};
//        return words[number - 1];
//
//    }
//    private static String numberInWords(int number) {
//        String[] words = {"One", "Two", "Three", "Four", "Five", "Six", "Seven",
//            "Eight", "Nine"};
//        return words[number - 1];
//    }
//    public static String getAmountToWords(BigDecimal parameter) {
//        boolean negativeAmount = false;
//
//        if (parameter.signum() == -1) {
//            negativeAmount = true;
//            parameter = parameter.abs();
//        }
//
//        StringBuilder word = new StringBuilder();
//
//        String numberString = parameter.setScale(2,
//                RoundingMode.HALF_UP).toPlainString();
//
//        double number = Double.parseDouble(numberString);
//
//        int quotient = (int) (number / 10000000);
//        if (quotient > 0) {
//            word.append(DmBaseJasper.getAmountToWords(new BigDecimal(quotient))
//                    + "Crore ");
//        }
//
//        number = number % 10000000;
//
//        quotient = (int) (number / 100000);
//        if (quotient > 0) {
//            word.append(numberToWords(quotient) + " Lakh ");
//        }
//        number = number % 100000;
//
//        quotient = (int) (number / 1000);
//        if (quotient > 0) {
//            word.append(numberToWords(quotient) + " Thousand ");
//        }
//
//        number = number % 1000;
//
//        quotient = (int) (number / 100);
//        if (quotient > 0) {
//            word.append(numberToWords(quotient) + " Hundred ");
//        }
//
//        number = number % 100;
//        if (number != 0) {
//            word.append(numberToWords((int) number) + "  ");
//        }
//
//        int decimal = 0;
//        String val;
//        if (number % 1 != 0) {
//            String decimalInWords = Double.toString(number);
//            int index = decimalInWords.indexOf(".");
//            decimalInWords = decimalInWords.substring(index + 1);
//            if (decimalInWords.length() > 2) {
//                val = decimalInWords.substring(0, 2);
//                decimal = Integer.parseInt(val);
//                if (Integer.parseInt(decimalInWords.substring(2, 3)) > 5) {
//                    decimal++;
//                }
//            } else {
//                decimal = Integer.parseInt(decimalInWords);
//            }
//            if (decimalInWords.length() == 1) {
//                decimal *= 10;
//            }
//            word = new StringBuilder(word.toString().replace("Rupees", "").trim());
//            word.append((" Rupees"));
//            if (word.toString().trim().length() > 0) {
//                word.append((" & "));
//            }
//
//            word.append(numberToWords(decimal));
//            if (decimal > 1) {
//                word.append(" Paise");
//            } else {
//                word.append(" Paisa");
//            }
//        } else {
//            if (word.toString().trim().length() == 0) {
//                word.append("Zero");
//            }
//            word = new StringBuilder(word.toString().replace("Rupees", "").trim());
//            word.append((" Rupees"));
//        }
//
//        if (word.toString().trim().length() == 0) {
//            return "Zero";
//        }
//
//        String result = word.toString().trim();
//        if (negativeAmount) {
//            result = "Minus " + result;
//        }
//
//        result = result.replace("Only", "");
//        return result + " Only";
//
//    }
//</editor-fold>
    public static String getAmountToWords(BigDecimal bigDecimal) {
        return getAmountToWords(bigDecimal, "");
    }

    public static String getAmountToWords(BigDecimal bigDecimal, String localeDmCode) {
        return DmAmtInWord.getAmountToWords(bigDecimal, localeDmCode);
    }

    public static String formatAmount(BigDecimal n) {
        return formatAmount(n, 2);
    }

    public static String formatAmount(BigDecimal decimal, int allowedDecimalPlaces) {
        return formatAmount(decimal, allowedDecimalPlaces, true);
    }

    public static String formatAmount(BigDecimal decimal, int allowedDecimalPlaces, boolean showDecimalUptoAllowedDecimalPlaces) {
        return formatAmount(decimal, allowedDecimalPlaces, showDecimalUptoAllowedDecimalPlaces, false);
    }

    public static String formatAmount(BigDecimal decimal, int allowedDecimalPlaces, boolean showDecimalUptoAllowedDecimalPlaces, boolean showCurrencySymbol) {
        return formatAmount(decimal, allowedDecimalPlaces, showDecimalUptoAllowedDecimalPlaces, showCurrencySymbol, "");
    }

    public static String formatAmount(BigDecimal decimal, int allowedDecimalPlaces, boolean showDecimalUptoAllowedDecimalPlaces, boolean showCurrencySymbol, String localeDmCode) {
        ULocale locale;
        if (DmBaseShared.xNull(localeDmCode).trim().equalsIgnoreCase("")) {
            locale = DmBaseServer.baseULocale;
        } else {
            locale = new ULocale("en", DmEnum.DmLocaleCountry.getLocaleViaDmCode(localeDmCode).getISOCode3());
        }
        return new DmNumberFormat().formatNumber(decimal, allowedDecimalPlaces, showDecimalUptoAllowedDecimalPlaces, showCurrencySymbol, locale);
    }

//    public static String formatAmount(BigDecimal n, int allowDecimalPlaces) {
//        DecimalFormat formatter;
//        String decimalPattern = "";
//
//        if (DmBaseShared.vNull(n) == 0) {
//            return "-";
//        }
//
//        if (allowDecimalPlaces < 0) {
//            allowDecimalPlaces = 0;
//        }
//
//        for (int i = 1; i <= allowDecimalPlaces; i++) {
//            if (decimalPattern.equalsIgnoreCase("")) {
//                decimalPattern = ".";
//            }
//            decimalPattern += "0";
//        }
//        formatter = new DecimalFormat("#,###" + decimalPattern);
//        //we never reach double digit grouping so return
//        if (n.doubleValue() < 100000) {
//            return formatter.format(n.setScale(allowDecimalPlaces, RoundingMode.HALF_UP).doubleValue());
//        }
//        StringBuffer returnValue = new StringBuffer();
//        //Spliting integer part and decimal part
//        String value = n.setScale(allowDecimalPlaces, RoundingMode.HALF_UP).toString();
//        String intpart = value.substring(0, value.indexOf("."));
//        String decimalpart = value.substring(value.indexOf("."), value.length());
//        //switch to double digit grouping
//        formatter.applyPattern("#,##");
//        returnValue.append(formatter.format(new BigDecimal(intpart).longValue() / 1000)).append(",");
//        //appending last 3 digits and decimal part
//        returnValue.append(intpart.substring(intpart.length() - (allowDecimalPlaces + 1), intpart.length())).append(decimalpart);
//        //returning complete string
//        return returnValue.toString();
//    }
    public static DmWaterMark getWatermark(String watermarkText, double rotate, int leftMargin, int waterMarkWidth, int waterMarkHeight, int fontSize) {
        DmWaterMark waterMark = null;

        waterMark = new DmWaterMark(DmBaseShared.xNull(watermarkText).trim(), rotate, leftMargin, waterMarkWidth, waterMarkHeight, fontSize);

        return waterMark;
    }
  public static   String qrPath ="";
    public static String getQrCode(String qrText,  String path, String charset,   int qrWidth, int qrHeight) throws WriterException, IOException, NotFoundException{
        QrCode qrCode = null;
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
//generates QR code with Low level(L) error correction capability  
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        qrCode = new QrCode(DmBaseShared.xNull(qrText).trim(),  path, charset,  qrWidth, qrHeight);
//        qRCodes(qrText, path, charset, hashMap, qrHeight, qrWidth);

        return qrPath;
    }
   
    
}
