package aarogya.selfToolkit.stStatic.shared;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class STShared {

    public final static boolean MANANGE_ROUNDOFF_AS_SERVICE = false;
    public final static double INCH_TO_CM = 2.54;

    public static String getOnlyAlphabets(String name) {
        Pattern pattern = Pattern.compile("[^a-zA-Z]");
        Matcher matcher = pattern.matcher(DmShared.xNull(name).trim());
        String alphabet = matcher.replaceAll("");
        return alphabet;
    }

    public static String getValidFileName(String fileName) {
        if (!DmShared.xNull(fileName).equalsIgnoreCase("")) {
            fileName = fileName.replaceAll("\\W", "");
            return fileName;
        }
        return "";
    }

    public static void isValidImpurityFormula_QtyAmt(HashMap<Integer, String> formulaList) throws DmException {
        Set<Integer> formulaKeySet = formulaList.keySet();
        String formula;

        for (Integer keySet : formulaKeySet) {
            int sNo = keySet;
            formula = formulaList.get(keySet);
            isValidImpurityFormula_QtyAmt(DmShared.xNull(formula), sNo);
        }
    }

    private static void isValidImpurityFormula_QtyAmt(String orignalFormula, int sNo) throws DmException {
        String tempFormula, convertedFormula, ohManualCode = "";
        boolean flag = false;

        try {
            orignalFormula = DmShared.xNull(orignalFormula).trim();
            convertedFormula = "";
            for (int j = 0; j < orignalFormula.length(); j++) {
                tempFormula = orignalFormula.substring(j, j + 1);
                if (flag) {
                    if (tempFormula.equalsIgnoreCase("]") || tempFormula.equalsIgnoreCase("}")) {
                        flag = false;

                        if (STEnum.LabOverhead.getViaKey(ohManualCode) == null) {
                            throw new DmException(0, sNo + ":Manual code '" + ohManualCode + "' in formula = " + orignalFormula + " must be calculated before it is being used.");
                        }

                        convertedFormula = convertedFormula + "(1)";
                    } else {
                        ohManualCode = ohManualCode + tempFormula;
                    }
                } else {
                    if (tempFormula.equalsIgnoreCase("[") || tempFormula.equalsIgnoreCase("{")) {
                        ohManualCode = "";
                        flag = true;
                    } else {
                        convertedFormula = convertedFormula + tempFormula;
                    }
                }
            }

            if (convertedFormula.trim().equalsIgnoreCase("")) {
                convertedFormula = "0";
            }

            try {
                DmShared.compute(convertedFormula);
            } catch (Exception e) {
//                throw new DmException(111, "Syntax error in S.No. <b>" + sNo + "</b> in formula = <b>" + orignalFormula + "</b>.");
                throw new DmException(0, "FormulaRow", DmShared.iNull(sNo).toString(), "Syntax error in formula = <b>" + orignalFormula + "</b> in S.No. <b>" + sNo + "</b>.");
            }

        } catch (DmException e) {
            throw e;
        }
    }

    public static void isValidFormula(TreeMap<Integer, String[]> formulaList, boolean ignoreG) throws Exception {
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
        OverHead_Shared.GetWhat getWhat = OverHead_Shared.GetWhat.AMOUNT;

        try {
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

            try {
                DmShared.compute(convertedFormula);
            } catch (Exception e) {
                throw new DmException(111, forManualCode + ": Syntax error in formula = " + orignalFormula + ".");
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static GstLocation_Mdl getGstLocation_IntraState(HashMap gstLocationMap) {
        GstLocation_Mdl glm = new GstLocation_Mdl();
        if (gstLocationMap != null) {
            if (gstLocationMap.containsKey(DmEnum.DmGstLocation_SystemDefined.INTRA_STATE.getManualCode())) {
                glm = (GstLocation_Mdl) gstLocationMap.get(DmEnum.DmGstLocation_SystemDefined.INTRA_STATE.getManualCode());
            }
        }
        return glm;
    }

    public static GstLocation_Mdl getGstLocation_InterState(HashMap gstLocationMap) {
        GstLocation_Mdl glm = new GstLocation_Mdl();
        if (gstLocationMap != null) {
            if (gstLocationMap.containsKey(DmEnum.DmGstLocation_SystemDefined.INTER_STATE.getManualCode())) {
                glm = (GstLocation_Mdl) gstLocationMap.get(DmEnum.DmGstLocation_SystemDefined.INTER_STATE.getManualCode());
            }
        }
        return glm;
    }

    public static GstLocation_Mdl getGstLocation_UnionTerritory(HashMap gstLocationMap) {
        GstLocation_Mdl glm = new GstLocation_Mdl();
        if (gstLocationMap != null) {
            if (gstLocationMap.containsKey(DmEnum.DmGstLocation_SystemDefined.UNION_TERRITORY.getManualCode())) {
                glm = (GstLocation_Mdl) gstLocationMap.get(DmEnum.DmGstLocation_SystemDefined.UNION_TERRITORY.getManualCode());
            }
        }
        return glm;
    }

    public static GstLocation_Mdl getGstLocation_International(HashMap gstLocationMap) {
        GstLocation_Mdl glm = new GstLocation_Mdl();
        if (gstLocationMap != null) {
            if (gstLocationMap.containsKey(DmEnum.DmGstLocation_SystemDefined.UNION_TERRITORY.getManualCode())) {
                glm = (GstLocation_Mdl) gstLocationMap.get(DmEnum.DmGstLocation_SystemDefined.UNION_TERRITORY.getManualCode());
            }
        }
        return glm;
    }
}
