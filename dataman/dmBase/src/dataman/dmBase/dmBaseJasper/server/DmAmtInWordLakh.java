package dataman.dmBase.dmBaseJasper.server;

import java.math.BigDecimal;
import java.math.RoundingMode;

class DmAmtInWordLakh {

    public static String getAmountToWords(BigDecimal bigDecimal, String majorCurrency, String minorCurrency) {
        boolean negativeAmount = false;

        if (bigDecimal.signum() == -1) {
            negativeAmount = true;
            bigDecimal = bigDecimal.abs();
        }

        StringBuilder word = new StringBuilder();

        String numberString = bigDecimal.setScale(2,
                RoundingMode.HALF_UP).toPlainString();

        double number = Double.parseDouble(numberString);

        int quotient = (int) (number / 10000000);
        if (quotient > 0) {
            word.append(DmBaseJasper.getAmountToWords(new BigDecimal(quotient))
                    + "Crore ");
        }

        number = number % 10000000;

        quotient = (int) (number / 100000);
        if (quotient > 0) {
            word.append(numberToWords(quotient) + " Lakh ");
        }
        number = number % 100000;

        quotient = (int) (number / 1000);
        if (quotient > 0) {
            word.append(numberToWords(quotient) + " Thousand ");
        }

        number = number % 1000;

        quotient = (int) (number / 100);
        if (quotient > 0) {
            word.append(numberToWords(quotient) + " Hundred ");
        }

        number = number % 100;
        if (number != 0) {
            word.append(numberToWords((int) number) + "  ");
        }

        int decimal = 0;
        String val;
        if (number % 1 != 0) {
            String decimalInWords = Double.toString(number);
            int index = decimalInWords.indexOf(".");
            decimalInWords = decimalInWords.substring(index + 1);
            if (decimalInWords.length() > 2) {
                val = decimalInWords.substring(0, 2);
                decimal = Integer.parseInt(val);
                if (Integer.parseInt(decimalInWords.substring(2, 3)) > 5) {
                    decimal++;
                }
            } else {
                decimal = Integer.parseInt(decimalInWords);
            }
            if (decimalInWords.length() == 1) {
                decimal *= 10;
            }
            word = new StringBuilder(word.toString().replace(majorCurrency, "").trim());
            word.append(" " + majorCurrency);
            if (word.toString().trim().length() > 0) {
                word.append((" & "));
            }

            word.append(numberToWords(decimal));
            if (decimal > 1) {
                word.append(" " + minorCurrency);
            } else {
                word.append(" " + minorCurrency);
            }
        } else {
            if (word.toString().trim().length() == 0) {
                word.append("Zero");
            }
            word = new StringBuilder(word.toString().replace(majorCurrency, "").trim());
            word.append(" " + majorCurrency);
        }

        if (word.toString().trim().length() == 0) {
            return "Zero";
        }

        String result = word.toString().trim();
        if (negativeAmount) {
            result = "Minus " + result;
        }

        result = result.replace("Only", "");
        return result + " Only";

    }

    private static String numberToWords(int number) {
        //if this number is 75, then this function should return seventy five
        int intQuotient = (int) (number / 10);
        StringBuilder word = new StringBuilder();

        if (intQuotient > 0) {

            if (intQuotient == 1 && (number % 10) > 0) {
                word.append(wordInTens(number % 10));
                return word.toString();
            }
            word.append(wordInTenMultiples(intQuotient));
        }
        int remainder = number % 10;
        if (remainder > 0) {
            if (word.length() > 0) {
                word.append(" ");
            }
            word.append(numberInWords(remainder));
        }

        return word.toString();

    }

    private static String wordInTenMultiples(int number) {
        String[] words = {"Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty",
            "Seventy", "Eighty", "Ninety"};
        return words[number - 1];
    }

    private static String wordInTens(int number) {
        String words[] = {"Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Ninteen"};
        return words[number - 1];

    }

    private static String numberInWords(int number) {
        String[] words = {"One", "Two", "Three", "Four", "Five", "Six", "Seven",
            "Eight", "Nine"};
        return words[number - 1];
    }

}
