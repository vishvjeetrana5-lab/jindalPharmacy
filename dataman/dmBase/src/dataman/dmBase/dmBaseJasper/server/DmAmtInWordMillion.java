package dataman.dmBase.dmBaseJasper.server;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class DmAmtInWordMillion {

    private static final HashMap<Integer, String> words = new HashMap<>();

    private static final String digits[] = {"", "Hundred", "Thousand", "Million", "Billion", "Trillion"};

    static {
        words.put(0, "");
        words.put(1, "One");
        words.put(2, "Two");
        words.put(3, "Three");
        words.put(4, "Four");
        words.put(5, "Five");
        words.put(6, "Six");
        words.put(7, "Seven");
        words.put(8, "Eight");
        words.put(9, "Nine");
        words.put(10, "Ten");
        words.put(11, "Eleven");
        words.put(12, "Twelve");
        words.put(13, "Thirteen");
        words.put(14, "Fourteen");
        words.put(15, "Fifteen");
        words.put(16, "Sixteen");
        words.put(17, "Seventeen");
        words.put(18, "Eighteen");
        words.put(19, "Nineteen");
        words.put(20, "Twenty");
        words.put(30, "Thirty");
        words.put(40, "Forty");
        words.put(50, "Fifty");
        words.put(60, "Sixty");
        words.put(70, "Seventy");
        words.put(80, "Eighty");
        words.put(90, "Ninety");
    }

    public static String getAmountToWords(BigDecimal bigDecimal, String majorCurrency, String minorCurrency) {
//        BigDecimal bd = new BigDecimal(num);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        long number = bigDecimal.longValue();
        long no = bigDecimal.longValue();
        int decimal = (int) (bigDecimal.remainder(BigDecimal.ONE).doubleValue() * 100);
        int digits_length = String.valueOf(no).length();
        int i = 0;

        ArrayList<String> groups = new ArrayList<>();
        List<StringBuilder> preCurrString = new ArrayList<>();
        List<StringBuilder> postCurrString = new ArrayList<>();

        if (number > 0) {
            int noOfGroups = (digits_length / 3);
            int extraGroup = (digits_length % 3);

            String numberString = String.valueOf(no);
            int numLength = numberString.length();
            for (int g = noOfGroups - 1; g >= 0; g--) {
                int endIndex = numLength - 3;
                groups.add(numberString.substring(endIndex, endIndex + 3));
                numLength = endIndex;
            }

            if (extraGroup > 0) {
                groups.add(numberString.substring(0, extraGroup));
            }

            for (int g = 0; g < groups.size(); g++) {
                String digitString = "";
                if (g > 0) {
                    digitString = digits[g + 1];
                }
                String groupValue = groups.get(g);
                StringBuilder tempSb = new StringBuilder();
                if (groupValue.length() > 0) {
                    if (groupValue.length() > 2) {
                        tempSb.append(words.get(Integer.parseInt(groupValue.substring(0, 1))));
                        tempSb.append(" ");
                        tempSb.append(digits[1]);
                        tempSb.append(" ");
                        if (Integer.parseInt(groupValue) < 20) {
                            tempSb.append(words.get(Integer.parseInt(groupValue.substring(1, 3))));
                            tempSb.append(" ");
                        } else {
                            tempSb.append(words.get(Integer.parseInt(groupValue.substring(1, 2) + "0")));
                            tempSb.append(" ");
                            tempSb.append(words.get(Integer.parseInt(groupValue.substring(2, 3))));
                            tempSb.append(" ");
                        }
                    } else if (groupValue.length() > 1) {
                        if (Integer.parseInt(groupValue) < 20) {
                            tempSb.append(words.get(Integer.parseInt(groupValue.substring(0, 2))));
                            tempSb.append(" ");
                        } else {
                            tempSb.append(words.get(Integer.parseInt(groupValue.substring(0, 1) + "0")));
                            tempSb.append(" ");
                            tempSb.append(words.get(Integer.parseInt(groupValue.substring(1, 2))));
                            tempSb.append(" ");
                        }
                    } else if (groupValue.length() > 0) {
                        tempSb.append(words.get(Integer.parseInt(groupValue.substring(0, 1))));
                        tempSb.append(" ");
                    }
                }
                tempSb.append(digitString);
                preCurrString.add(tempSb);
            }
        }

        if ((String.valueOf(decimal).length() > 0) && (decimal > 0)) {
            StringBuilder tempSb = new StringBuilder();
            String decimalValue = String.valueOf(decimal);
            if (decimalValue.length() > 1) {
                if (decimal < 20) {
                    tempSb.append(words.get(Integer.parseInt(decimalValue.substring(0, 2))));
                    tempSb.append(" ");
                } else {
                    tempSb.append(words.get(Integer.parseInt(decimalValue.substring(0, 1) + "0")));
                    tempSb.append(" ");
                    tempSb.append(words.get(Integer.parseInt(decimalValue.substring(1, 2))));
                    tempSb.append(" ");
                }
            } else if (decimalValue.length() > 0) {
                tempSb.append(words.get(Integer.parseInt(decimalValue.substring(0, 1))));
                tempSb.append(" ");
            }
            postCurrString.add(tempSb);
        }

        Collections.reverse(preCurrString);

        StringBuilder finalString = new StringBuilder();
        if (preCurrString.size() > 0) {
            finalString.append(majorCurrency);
            finalString.append(" ");
            for (int g = 0; g < preCurrString.size(); g++) {
                finalString.append(preCurrString.get(g).toString());
                finalString.append(" ");
            }
        }

        if (postCurrString.size() > 0) {
            if (finalString.length() > 0) {
                finalString.append("and ");
            }
            finalString.append(postCurrString.get(0));
            finalString.append(minorCurrency);
        }

        return finalString.toString();

    }
}
