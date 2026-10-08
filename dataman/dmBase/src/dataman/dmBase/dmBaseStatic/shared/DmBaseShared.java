package dataman.dmBase.dmBaseStatic.shared;

import dataman.dmBase.self.shared.DmBaseURL;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmDateTimeFormat;
import java.math.BigInteger;
import java.util.Date;

public class DmBaseShared {

    public static Integer[] mobileDigit = new Integer[]{0};

    public static DmBaseURL getHostPageBaseURL(String prefix, String baseUrlString) {
        DmBaseURL baseURL = new DmBaseURL();
        prefix = xNull(prefix).trim().toLowerCase();
        if (prefix.equals("")) {
            prefix = "http";
        }

        if (baseUrlString.trim().startsWith("http://")) {
            baseURL.setProtocol(prefix);
            baseURL.setUrl(baseUrlString.replace("http://", ""));
        } else if (baseUrlString.trim().startsWith("https://")) {
            baseURL.setProtocol(prefix + "s");
            baseURL.setUrl(baseUrlString.replace("https://", ""));
        }

        return baseURL;
    }

    public static String classPathToPtah(String classPath) {
        return classPath.replace(".", "/");
    }

    public static String xNull(String value) {
        if (value == null) {
            value = "";
        }

        return (String) value;
    }

    public static BigInteger iNull(Object value) {
        if (value == null) {
            value = 0;
        }
        if (String.valueOf(value).equalsIgnoreCase("")) {
            value = 0;
        }
        if (value.toString().contains(".")) {
            try {
                value = value.toString().split("\\.")[0];
            } catch (Exception e) {
                value = 0;
            }
            if (String.valueOf(value).equalsIgnoreCase("")) {
                value = 0;
            }
        }
        return new BigInteger(value.toString());
    }

    public static Double vNull(Object value) {
        if (value == null) {
            value = 0;
        }
        if (String.valueOf(value).equalsIgnoreCase("")) {
            value = 0;
        }
        return Double.valueOf(value.toString());
    }

    public static String dNull(String value) {
        String strRtn = "";
        Date date;

        try {
            if (value != null) {
                if (!value.equalsIgnoreCase("")) {
                    date = convertDate(value);
                    if (date != null) {
                        strRtn = dNull(date);
                    }
                }
            }
        } catch (Exception e) {
        }

        return strRtn;
    }

    public static String dNull(Date value) {
        String strRtn = "";

        try {
            if (value != null) {
                strRtn = DmDateTimeFormat.getFormat("dd-MMM-yyyy").format(value);
            }
        } catch (Exception e) {
        }

        return strRtn;
    }

    public static Date convertDate(String value) {
        Date date = null;

        try {
            if (value != null) {
                if (!value.equalsIgnoreCase("")) {
                    value = value.replace("/", "-");
                    value = value.replace(".", "-");
                    date = DmDateTimeFormat.getFormat("dd-MM-yyyy").parse(value);
                }
            }
        } catch (Exception e) {
        }

        return date;
    }

    public static String foriegnKey(String value) {
        if (value != null) {
            if (value.trim().equalsIgnoreCase("") || value.trim().equalsIgnoreCase("0")) {
                value = null;
            }
        }
        return value;
    }

    public static String removeSpace(String strValue) {
        String strRtn = "";
        boolean blnFlag = false;
        int i;

        strValue = strValue.trim();

        for (i = 0; i < strValue.length(); i++) {
            if (strValue.substring(i, i + 1).equals(" ")) {
                blnFlag = true;
            } else {
                if (blnFlag) {
                    strRtn += " ";
                }
                strRtn += strValue.substring(i, i + 1);
                blnFlag = false;
            }
        }

        return strRtn;
    }

    public static String properCase(String inputString) {
        String result = "";

        inputString = removeSpace(inputString);

        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char previousChar = inputString.charAt(i - 1);
            if (previousChar == ' ') {
                char currentCharToUpperCase = Character.toUpperCase(currentChar);
                result = result + currentCharToUpperCase;
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
        }

        return result;
    }

    public static String sentenceCase(String inputString) {
        String result = "";

        inputString = removeSpace(inputString);

        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        boolean terminalCharacterEncountered = false;
        char[] terminalCharacters = {'.', '?', '!'};
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            if (terminalCharacterEncountered) {
                if (currentChar == ' ') {
                    result = result + currentChar;
                } else {
                    char currentCharToUpperCase = Character.toUpperCase(currentChar);
                    result = result + currentCharToUpperCase;
                    terminalCharacterEncountered = false;
                }
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
            for (int j = 0; j < terminalCharacters.length; j++) {
                if (currentChar == terminalCharacters[j]) {
                    terminalCharacterEncountered = true;
                    break;
                }
            }
        }
        return result;
    }

    public static String space(int noOfSpaces) {
        String rtnSpace = "";

        for (int i = 0; i < noOfSpaces; i++) {
            rtnSpace += " ";
        }

        return rtnSpace;
    }

    public static void isValidText(String fieldValue, String fieldName, int errorNo) {
        if (DmBaseShared.xNull(fieldValue).trim().equalsIgnoreCase("")) {
            throw new DmException(errorNo, fieldName.trim() + " is mandatory.");
        }
    }

    public static void isValidAmount(Object fieldValue, String fieldName, int errorNo) {
        if (DmBaseShared.vNull(fieldValue).doubleValue() <= 0) {
            throw new DmException(errorNo, fieldName.trim() + " is mandatory.");
        }
    }

    public static void isValidEMail(String eMail, int errorNo) {
        String eMailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        if (!DmBaseShared.xNull(eMail).trim().equalsIgnoreCase("")) {
            if (!eMail.matches(eMailRegex)) {
                throw new DmException(errorNo, "Invalid eMail Id.");
            }
        }

    }

    public static void isValidMobile(String fieldValue, int errorNo) {
        boolean isMobileDigitChecked = true;

        if (!xNull(fieldValue).trim().equalsIgnoreCase("")) {
            String[] numbers = fieldValue.split("\\,");
            for (String number : numbers) {

                if (mobileDigit != null) {
                    if (mobileDigit.length > 0) {
                        isMobileDigitChecked = false;
                        for (Integer md : mobileDigit) {
                            if (xNull(number).trim().length() == md.intValue() || md.intValue() == 0) {
                                isMobileDigitChecked = true;
                            }
                        }
//                        if (xNull(number).trim().length() != 10) {
//                            throw new DmException(errorNo, "Invalid Mobile No.");
//                        }
                    }
                }

                if (!isMobileDigitChecked) {
                    throw new DmException(errorNo, "Invalid Mobile No.");
                }

                try {
                    long l = Long.parseLong(DmBaseShared.xNull(number));
                } catch (NumberFormatException nfe) {
                    throw new DmException(errorNo, "Mobile No. should be numeric.");
                }
            }
        }
    }

    public static boolean isNumeric(String value) {
        try {
            double d = Double.parseDouble(value);
        } catch (Exception nfe) {
            return false;
        }
        return true;
    }

    public static void isValidGSTIN(String stateCode, String panNo, String gstinNo, int errorNo) {
        if (DmBaseShared.xNull(gstinNo).trim().length() != 15) {
            throw new DmException(errorNo, "Invalid GSTIN.");
        }

        if (stateCode.length() < 2) {
            stateCode = "0" + stateCode;
        }
        if ((!DmBaseShared.xNull(gstinNo).substring(0, 2).equalsIgnoreCase(stateCode))
                || (!DmBaseShared.xNull(gstinNo.toUpperCase()).substring(2, 12).equalsIgnoreCase(panNo.toUpperCase()))
                || (!DmBaseShared.xNull(gstinNo).substring(12, 13).matches("[1-9]{1}"))
                || (!DmBaseShared.xNull(gstinNo.toUpperCase()).substring(13, 14).equalsIgnoreCase("Z"))
                || (!DmBaseShared.xNull(gstinNo).substring(14, 15).matches("[A-Z0-9]{1}"))) {
            throw new DmException(errorNo, "Invalid GSTIN.");
        }

    }

    public static void isValidPanNo(String panNo, int errorNo) {
        String panformat = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
        boolean bool = false;
        if (panNo.matches(panformat)) {
            bool = true;
        }
        if (!bool) {
            throw new DmException(errorNo, "Invalid PAN No.");
        }

    }

}
