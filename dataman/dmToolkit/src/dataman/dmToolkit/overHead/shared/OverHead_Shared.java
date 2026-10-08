package dataman.dmToolkit.overHead.shared;

import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OverHead_Shared {

    public enum GetWhat {

        AMOUNT, ATRATE
    }

    private static String getOHByManualCode(String ohManualCode, GetWhat getWhat, List<OverHead_Mdl> overHeadList) {
        String rtnValue = "";
        OverHead_Mdl overHead = getOHByManualCode(ohManualCode, overHeadList);

        if (overHead != null) {
            switch (getWhat) {
                case ATRATE:
                    rtnValue = overHead.getAtRate();
                    break;
                case AMOUNT:
                    rtnValue = overHead.getAmount();
                    break;
            }
        }

        return rtnValue;
    }

    public static OverHead_Mdl getOHByManualCode(String ohManualCode, List<OverHead_Mdl> overHeadList) {

        if (overHeadList != null) {
            for (OverHead_Mdl overHead : overHeadList) {
                if (DmShared.xNull(overHead.getOhManualCode()).trim().equalsIgnoreCase(DmShared.xNull(ohManualCode).trim())) {
                    return overHead;
                }
            }
        }

        return null;
    }

    public static OverHead_Mdl getOHByUniqueId(String uniqueId, List<OverHead_Mdl> overHeadList) {

        if (overHeadList != null) {
            for (OverHead_Mdl overHead : overHeadList) {
                if (DmShared.xNull(overHead.getUniqueId()).trim().equalsIgnoreCase(DmShared.xNull(uniqueId).trim())) {
                    return overHead;
                }
            }
        }

        return null;
    }

    public static List<OverHead_Mdl> getOHByUniqueIds(String uniqueId, List<OverHead_Mdl> overHeadList) {
        List<OverHead_Mdl> head_Mdls = new ArrayList<>();
        if (overHeadList != null) {
            for (OverHead_Mdl overHead : overHeadList) {
                if (DmShared.xNull(overHead.getUniqueId()).trim().equalsIgnoreCase("[" + DmShared.xNull(uniqueId).trim() + "]")
                        || DmShared.xNull(overHead.getUniqueId()).trim().equalsIgnoreCase(DmShared.xNull(uniqueId).trim())) {
                    head_Mdls.add(overHead);
                }
            }
        }

        return head_Mdls;
    }

    public static void calculateSingleOH(List<OverHead_Mdl> overHeadList, OverHead_Mdl overHead, String grossAmount) {
        String orignalFormula = "", tempFormula = "", convertedFormula = "", ohManualCode = "";
        OverHead_Shared.GetWhat getWhat = OverHead_Shared.GetWhat.AMOUNT;
        boolean flag = false;
        double tempValue;
        List<OverHeadMA_Mdl> overHeadMAList;
        OverHeadMA_Mdl overHeadMA = null;

        orignalFormula = overHead.getFormula().trim();
        convertedFormula = "";
        for (int j = 0; (j < orignalFormula.length()); j++) {
            tempFormula = orignalFormula.substring(j, j + 1);
            if (flag) {
                if (tempFormula.equalsIgnoreCase("]") || tempFormula.equalsIgnoreCase("}")) {
                    flag = false;
                    if (ohManualCode.equalsIgnoreCase("G") && getWhat == OverHead_Shared.GetWhat.AMOUNT) {
                        convertedFormula = convertedFormula + "(" + DmClient.vNull(grossAmount, "0.0000000") + ")";
                    } else {
                        convertedFormula = convertedFormula + "(" + DmClient.vNull(OverHead_Shared.getOHByManualCode(ohManualCode, getWhat, overHeadList), "0.0000000") + ")";
                    }

                } else {
                    ohManualCode = ohManualCode + tempFormula;
                }
            } else {
                if (tempFormula.equalsIgnoreCase("[") || tempFormula.equalsIgnoreCase("{")) {
                    ohManualCode = "";
                    flag = true;
                    if (tempFormula.equalsIgnoreCase("{")) {
                        getWhat = OverHead_Shared.GetWhat.ATRATE;
                    } else {
                        getWhat = OverHead_Shared.GetWhat.AMOUNT;
                    }
                } else {
                    convertedFormula = convertedFormula + tempFormula;
                }
            }
        }

        if (convertedFormula.trim().equalsIgnoreCase("")) {
            convertedFormula = "0";
        }

        tempValue = DmShared.compute(convertedFormula);

        if (Double.isInfinite(tempValue)
                || Double.isNaN(tempValue)) {
            tempValue = 0d;
        }

        if (DmShared.iNull(overHead.getShowFormulaValue()).longValue() == 1
                && DmShared.iNull(overHead.getAmountEditable()).longValue() == 0) {

            tempValue = DmShared.vNull(DmClient.vNull(tempValue, "0.0000000"));
        } else if (DmShared.iNull(overHead.getShowFormulaValue()).longValue() == 1
                && DmShared.iNull(overHead.getAtRate()).longValue() != 0) {

            tempValue = DmShared.vNull(DmClient.vNull(tempValue, "0.0000000"));
        } else if ((DmShared.iNull(overHead.getShowFormulaValue()).longValue() != 1)
                && DmShared.vNull(overHead.getAtRate()).doubleValue() > 0) {

            tempValue = DmShared.vNull(DmClient.vNull((tempValue * DmShared.vNull(overHead.getAtRate())) / 100.0, "0.0000000"));
        } else if ((DmShared.iNull(overHead.getShowFormulaValue()).longValue() != 1)
                && DmShared.iNull(overHead.getAmountEditable()).longValue() == 0
                && DmShared.iNull(overHead.getAtRateEditable()).longValue() == 1) {

            tempValue = 0;
        } else {
            tempValue = DmShared.vNull(overHead.getAmount());
        }

        if (DmShared.xNull(overHead.getRoundOff()).trim().equalsIgnoreCase("D")) {
            overHead.setAmount(DmClient.vNull(tempValue, "0.0000000"));

        } else {
            if (DmShared.xNull(overHead.getRoundOff()).trim().equalsIgnoreCase("Y")) {
                tempValue = DmShared.vNull(DmClient.vNull(tempValue, "0"));

            } else if (DmShared.xNull(overHead.getRoundOff()).trim().equalsIgnoreCase("H")) {
                tempValue = Math.ceil(tempValue);

            } else if (DmShared.xNull(overHead.getRoundOff()).trim().equalsIgnoreCase("L")) {
                tempValue = Math.floor(tempValue);
            }

            overHead.setAmount(DmClient.vNull(tempValue, "0.00"));
        }

        if (DmShared.vNull(overHead.getAmount()).doubleValue() == 0) {
            overHead.setAmount("");
        }

        if (!DmShared.xNull(overHead.getDrCr()).trim().equalsIgnoreCase("")) {
            overHeadMAList = overHead.getOverHeadMA();
            if (overHeadMAList != null) {
                for (int i = 0; i < overHeadMAList.size(); i++) {
                    if (!DmShared.xNull(overHeadMAList.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")
                            || DmShared.vNull(overHeadMAList.get(i).getAmount()).doubleValue() != 0) {
                        if (overHeadMA != null) {
                            overHeadMA = null;
                            break;
                        }
                        overHeadMA = overHeadMAList.get(i);
                    }
                }
            }
        }

        if (overHeadMA != null) {
            overHeadMA.setAmount(overHead.getAmount());
        }
    }

    public static void chkGrid(List<OverHead_Mdl> overHeadList) {
        List<OverHeadMA_Mdl> overHeadMAList;
        boolean maExist = false;
        double amount = 0, debit = 0, credit = 0;
        BigDecimal d, c;

        if (overHeadList == null || overHeadList.size() == 0) {
            throw new DmException(0, "Please define overhead.");
        } else {
            for (int h = 0; h < overHeadList.size(); h++) {
                overHeadMAList = overHeadList.get(h).getOverHeadMA();

                if (DmShared.xNull(overHeadList.get(h).getDrCr()).trim().equalsIgnoreCase("D")) {
                    debit += DmShared.vNull(overHeadList.get(h).getAmount());
                } else if (DmShared.xNull(overHeadList.get(h).getDrCr()).trim().equalsIgnoreCase("C")) {
                    credit += DmShared.vNull(overHeadList.get(h).getAmount());
                }

                if (DmShared.iNull(overHeadList.get(h).getAtRateMandatory()).longValue() == 1) {
                    if (DmShared.vNull(overHeadList.get(h).getAtRate()).doubleValue() == 0) {
                        throw new DmException(h, "Please fill [" + DmShared.xNull(overHeadList.get(h).getOverHeadAlias()) + "] At Rate.");
                    }
                }

                if (DmShared.iNull(overHeadList.get(h).getAmountMandatory()).longValue() == 1) {
                    if (DmShared.vNull(overHeadList.get(h).getAmount()).doubleValue() == 0) {
                        throw new DmException(h, "Please fill [" + DmShared.xNull(overHeadList.get(h).getOverHeadAlias()) + "] Amount.");
                    }
                }

                if (!DmShared.xNull(overHeadList.get(h).getDrCr()).trim().equalsIgnoreCase("")) {
                    if (DmShared.vNull(overHeadList.get(h).getAmount()).doubleValue() != 0) {
                        maExist = false;
                        amount = 0;
                        if (overHeadMAList != null) {
                            for (int i = 0; i < overHeadMAList.size(); i++) {
                                if (!DmShared.xNull(overHeadMAList.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase("") || DmShared.vNull(overHeadMAList.get(i).getAmount()).doubleValue() != 0) {

                                    maExist = true;
                                    amount += DmShared.vNull(overHeadMAList.get(i).getAmount());
                                    if (DmShared.xNull(overHeadMAList.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                                        throw new DmException(h, "Please fill [" + DmShared.xNull(overHeadList.get(h).getOverHeadAlias()) + "] A/c Name.");
                                    }

                                    for (int j = i + 1; j < overHeadMAList.size(); j++) {
                                        if (DmShared.xNull(overHeadMAList.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(overHeadMAList.get(j).getSharedKeyFieldCode()).trim())) {
                                            throw new DmException(h, "Duplicate [" + DmShared.xNull(overHeadList.get(h).getOverHeadAlias()) + "] A/c Name.");
                                        }
                                    }
                                }
                            }
                        }
                        if (!maExist) {
                            throw new DmException(h, "Please fill [" + DmShared.xNull(overHeadList.get(h).getOverHeadAlias()) + "] A/c Name.");
                        }

                        d = new BigDecimal(amount);
                        c = new BigDecimal(DmShared.vNull(overHeadList.get(h).getAmount()).doubleValue());
                        if (d.setScale(2, RoundingMode.HALF_UP).doubleValue() != c.setScale(2, RoundingMode.HALF_UP).doubleValue()) {
                            throw new DmException(h, "Please bifurcate [" + DmShared.xNull(overHeadList.get(h).getOverHeadAlias()) + "] full Amount.");
                        }
                    }
                }
            }
        }

        d = new BigDecimal(debit);
        c = new BigDecimal(credit);
        if (d.setScale(2, RoundingMode.HALF_UP).doubleValue() != c.setScale(2, RoundingMode.HALF_UP).doubleValue()) {
            throw new DmException(-1, "Debit amount and Credit amount is not equal.");
        }
    }

    public static void isValidFormula(HashMap<Integer, String[]> formulaList, boolean ignoreG) throws Exception {
        Set<Integer> formulaKeySet = formulaList.keySet();
        final Set<String> ohManualCodeList = new HashSet<String>();
        String formula[];

        for (Integer keySet : formulaKeySet) {
            formula = formulaList.get(keySet);
            ohManualCodeList.add("{" + DmShared.xNull(formula[0]).trim().toUpperCase() + "}");
            isValidFormula(ohManualCodeList, DmShared.xNull(formula[0]).trim().toUpperCase(), DmShared.xNull(formula[1]), ignoreG);
            ohManualCodeList.add("[" + DmShared.xNull(formula[0]).trim().toUpperCase() + "]");
        }
    }

    private static void isValidFormula(Set<String> ohManualCodeList, String forManualCode, String orignalFormula, boolean ignoreG) throws Exception {
        String tempFormula, convertedFormula, ohManualCode = "";
        boolean flag = false, check = true;
        OverHead_Shared.GetWhat getWhat = GetWhat.AMOUNT;

        try {
//            orignalFormula = DmShared.xNull(orignalFormula).trim().toUpperCase();
            orignalFormula = DmShared.xNull(orignalFormula).trim();
            convertedFormula = "";
            for (int j = 0; j < orignalFormula.length(); j++) {
                tempFormula = orignalFormula.substring(j, j + 1);
                if (flag) {
                    if (tempFormula.equalsIgnoreCase("]") || tempFormula.equalsIgnoreCase("}")) {
                        flag = false;
                        check = true;
                        if (ohManualCode.equalsIgnoreCase("G")) {
                            if (ignoreG) {
                                check = false;
                            }
                        }

                        if (getWhat == OverHead_Shared.GetWhat.ATRATE) {
                            if (!ohManualCodeList.contains("{" + ohManualCode.toUpperCase() + "}")) {
                                throw new DmException(111, forManualCode + ": Manual code '" + ohManualCode + "' in formula = " + orignalFormula + " must be calculated before it is being used.");
                            }
                        } else {
                            if (check) {
                                if (!ohManualCodeList.contains("[" + ohManualCode.toUpperCase() + "]")) {
                                    throw new DmException(111, forManualCode + ": Manual code '" + ohManualCode + "' in formula = " + orignalFormula + " must be calculated before it is being used.");
                                }
                            }
                        }

                        convertedFormula = convertedFormula + "(1)";

                    } else {
                        ohManualCode = ohManualCode + tempFormula;
                    }
                } else {
                    if (tempFormula.equalsIgnoreCase("[") || tempFormula.equalsIgnoreCase("{")) {
                        ohManualCode = "";
                        flag = true;
                        if (tempFormula.equalsIgnoreCase("{")) {
                            getWhat = GetWhat.ATRATE;
                        } else {
                            getWhat = GetWhat.AMOUNT;
                        }
                    } else {
                        convertedFormula = convertedFormula + tempFormula;
                    }
                }
            }

            if (convertedFormula.trim().equalsIgnoreCase("")) {
                convertedFormula = "0";
            }

            try {
//                DmShared.compute(convertedFormula.replace("ROUND", "Math.round"));
                DmShared.compute(convertedFormula);
            } catch (Exception e) {
                throw new DmException(111, forManualCode + ": Syntax error in formula = " + orignalFormula + ".");
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static double getLandedValue(double landedValue, double totalGrossAmount, double lineGrossAmount) {
        double returnVal = 0;

        if (totalGrossAmount != 0) {
            returnVal = (landedValue / totalGrossAmount) * lineGrossAmount;
        }

        return returnVal;
    }
}
