package dataman.dmToolkit.overHeadLine.shared;

import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.ArrayList;
import java.util.List;

public class OverHeadLine_Shared {

    public enum GetWhat {

        AMOUNT, ATRATE
    }

    public static String getOHByManualCode(String ohManualCode, GetWhat getWhat, List<OverHeadLine_Mdl> overHeadList) {
        String rtnValue = "";

        if (overHeadList != null) {
            for (int i = 0; i < overHeadList.size(); i++) {
                if (DmShared.xNull(overHeadList.get(i).getOhManualCode()).trim().equalsIgnoreCase(DmShared.xNull(ohManualCode).trim())) {
                    switch (getWhat) {
                        case ATRATE:
                            rtnValue = overHeadList.get(i).getAtRate();
                            break;
                        case AMOUNT:
                            rtnValue = overHeadList.get(i).getAmount();
                            break;
                    }
                }
            }
        }

        return rtnValue;
    }

    public static OverHeadLine_Mdl getOHByManualCode(String ohManualCode, List<OverHeadLine_Mdl> overHeadList) {

        if (overHeadList != null) {
            for (OverHeadLine_Mdl overHead : overHeadList) {
                if (DmShared.xNull(overHead.getOhManualCode()).trim().equalsIgnoreCase(DmShared.xNull(ohManualCode).trim())) {
                    return overHead;
                }
            }
        }

        return null;
    }

    public static OverHeadLine_Mdl getOHByUniqueId(String uniqueId, List<OverHeadLine_Mdl> overHeadList) {

        if (overHeadList != null) {
            for (OverHeadLine_Mdl overHead : overHeadList) {
                if (DmShared.xNull(overHead.getUniqueId()).trim().equalsIgnoreCase(DmShared.xNull(uniqueId).trim())) {
                    return overHead;
                }
            }
        }

        return null;
    }

    public static List<OverHeadLine_Mdl> getOHByUniqueIds(String uniqueId, List<OverHeadLine_Mdl> overHeadList) {
        List<OverHeadLine_Mdl> headLine_Mdls = new ArrayList<>();
        if (overHeadList != null) {
            for (OverHeadLine_Mdl overHead : overHeadList) {
                if (DmShared.xNull(overHead.getUniqueId()).trim().equalsIgnoreCase("[" + DmShared.xNull(uniqueId).trim() + "]")
                        || DmShared.xNull(overHead.getUniqueId()).trim().equalsIgnoreCase(DmShared.xNull(uniqueId).trim())) {
                    headLine_Mdls.add(overHead);
                }
            }
        }

        return headLine_Mdls;
    }

    public static <T extends OHLExtended> void chkGrid(List<T> rowList) {
        T t;
        int index = -1;

        try {
            if (rowList != null) {
                for (int i = 0; i < rowList.size(); i++) {
                    t = rowList.get(i);
                    index = i;
                    chkGridSingleOH(t.getOHLList());
                }

            }
        } catch (Exception e) {
            throw new DmException(index, "Line " + String.valueOf(index + 1) + ": " + e.getMessage());
        }
    }

    public static void chkGridSingleOH(List<OverHeadLine_Mdl> overHeadList) {
        if (overHeadList == null || overHeadList.size() == 0) {
            throw new DmException(0, "Please define overhead.");
        } else {
            for (int h = 0; h < overHeadList.size(); h++) {
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
            }
        }
    }

    public static void calculateSingleOH(List<OverHeadLine_Mdl> overHeadList, OverHeadLine_Mdl overHead, String grossAmount) {
        String orignalFormula = "", tempFormula = "", convertedFormula = "", ohManualCode = "";
        OverHeadLine_Shared.GetWhat getWhat = OverHeadLine_Shared.GetWhat.AMOUNT;
        boolean flag = false;
        double tempValue;

        orignalFormula = overHead.getFormula().trim();
        convertedFormula = "";
        for (int j = 0; (j < orignalFormula.length()); j++) {
            tempFormula = orignalFormula.substring(j, j + 1);
            if (flag) {
                if (tempFormula.equalsIgnoreCase("]") || tempFormula.equalsIgnoreCase("}")) {
                    flag = false;
                    if (ohManualCode.equalsIgnoreCase("G") && getWhat == OverHeadLine_Shared.GetWhat.AMOUNT) {
                        convertedFormula = convertedFormula + "(" + DmClient.vNull(grossAmount, "0.0000000") + ")";
                    } else {
                        convertedFormula = convertedFormula + "(" + DmClient.vNull(OverHeadLine_Shared.getOHByManualCode(ohManualCode, getWhat, overHeadList), "0.0000000") + ")";
                    }

                } else {
                    ohManualCode = ohManualCode + tempFormula;
                }
            } else {
                if (tempFormula.equalsIgnoreCase("[") || tempFormula.equalsIgnoreCase("{")) {
                    ohManualCode = "";
                    flag = true;
                    if (tempFormula.equalsIgnoreCase("{")) {
                        getWhat = OverHeadLine_Shared.GetWhat.ATRATE;
                    } else {
                        getWhat = OverHeadLine_Shared.GetWhat.AMOUNT;
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

        if (DmShared.iNull(overHead.getShowFormulaValue()).longValue() == 1
                && DmShared.iNull(overHead.getAmountEditable()).longValue() == 0) {

            tempValue = DmShared.vNull(DmClient.vNull(tempValue, "0.0000000"));
        } else if (DmShared.iNull(overHead.getShowFormulaValue()).longValue() == 1
                && DmShared.vNull(overHead.getAtRate()).doubleValue() != 0) {

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
    }
}
