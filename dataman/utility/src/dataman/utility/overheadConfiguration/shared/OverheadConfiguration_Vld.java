package dataman.utility.overheadConfiguration.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class OverheadConfiguration_Vld {

    public static void chkSave(OverheadConfiguration_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getOverheadName().trim(), "Overhead Name", 150);
        DmShared.isValidText(mrTemp.getTableAffectedText(), "Table Affected", 200);
        DmShared.isValidText(mrTemp.getApplicableFrom(), "Applicable From", 300);
    }

    public static boolean checkGrid(List<GrdOverhead_Mdl> grdOverhead) {
        try {

            if (grdOverhead.size() <= 1) {
                if (DmShared.xNull(grdOverhead.get(0).getOverhead()).equalsIgnoreCase("")) {
                    throw new DmException(0, "One row is mandatory.");
                }
            }

            boolean blnRtn = true;
            String sql;
            for (int i = 0; i < grdOverhead.size(); i++) {
                if (!DmShared.xNull(grdOverhead.get(i).getOverhead()).equalsIgnoreCase("")) {
                    if (DmShared.xNull(grdOverhead.get(i).getOverheadCode()).equalsIgnoreCase("")) {
                        blnRtn = false;
                        throw new DmException(0, "<Overhead> Is Unmapped. Please Map It.");
                    }

                    if (!DmShared.xNull(grdOverhead.get(i).getAmtSaveIn()).equalsIgnoreCase("")) {
                        if (DmShared.xNull(grdOverhead.get(i).getAmtSaveInCode()).equalsIgnoreCase("")) {
                            blnRtn = false;
                            throw new DmException(0, DmShared.xNull(grdOverhead.get(i).getAmtSaveIn()) + " <Amount Save In> Is Unmapped. Please Map It.");
                            //break;
                        }
                    }

                    if (!DmShared.xNull(grdOverhead.get(i).getAtRateSaveIn()).equalsIgnoreCase("")) {
                        if (DmShared.xNull(grdOverhead.get(i).getAtRateSaveInCode()).equalsIgnoreCase("")) {
                            blnRtn = false;
                            throw new DmException(0, DmShared.xNull(grdOverhead.get(i).getAtRateSaveIn()) + " <At Rate Save In> Is Unmapped. Please Map It.");
                            //break;
                        }
                    }

                    if (!DmShared.xNull(grdOverhead.get(i).getPostAC()).equalsIgnoreCase("")) {
                        if (DmShared.xNull(grdOverhead.get(i).getPostACCode()).equalsIgnoreCase("")) {
                            blnRtn = false;
                            throw new DmException(0, DmShared.xNull(grdOverhead.get(i).getPostAC()) + " <Account Name> Is Unmapped. Please Map It.");
                        }
                    }

                    if (!DmShared.xNull(grdOverhead.get(i).getPostACCode()).equalsIgnoreCase("")) {
                        if (DmShared.xNull(grdOverhead.get(i).getDrCr()).equalsIgnoreCase("")) {

                            blnRtn = false;
                            throw new DmException(0, "Please Fill Debit/ Credit.");
                        }
                    }

                    if (!checkUniqueId(DmShared.xNull(grdOverhead.get(i).getUniqueID()), grdOverhead)) {
                        blnRtn = false;
                        break;
                        //throw new DmException(0, DmShared.xNull(grdOverhead.get(i).getAmtSaveIn()) + " <Amount Save In> Is Unmapped. Please Map It.");
                    }

                    for (int j = i + 1; j < grdOverhead.size(); j++) {
                        if (!DmShared.xNull(grdOverhead.get(i).getOverhead()).equalsIgnoreCase("")) {
                            if (DmShared.xNull(grdOverhead.get(i).getOverhead()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getOverhead()))) {

                                blnRtn = false;
                                throw new DmException(0, "Duplicate Overhead.");
                            }
                            //=========== Duplicacy Checking For Per Save In Field ================
                            if (!DmShared.xNull(grdOverhead.get(i).getAtRateSaveIn()).equalsIgnoreCase("")) {
                                if (DmShared.xNull(grdOverhead.get(i).getAtRateSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(i).getAmtSaveIn()))) {

                                    blnRtn = false;
                                    throw new DmException(0, "Save In Fields Must Not Be Repeatable");
                                }

                                if (DmShared.xNull(grdOverhead.get(i).getAtRateSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAtRateSaveIn()))) {

                                    blnRtn = false;
                                    throw new DmException(0, "Save In Fields Must Not Be Repeatable");
                                }

                                if (DmShared.xNull(grdOverhead.get(i).getAtRateSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAmtSaveIn()))) {

                                    blnRtn = false;
                                    throw new DmException(0, "Save In Fields Must Not Be Repeatable");
                                }
                            }

                            //  '=========== Duplicacy Checking For Amt Save In Field ================
                            if (!DmShared.xNull(grdOverhead.get(i).getAmtSaveIn()).equalsIgnoreCase("")) {
                                if (DmShared.xNull(grdOverhead.get(i).getAmtSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(i).getAtRateSaveIn()))) {

                                    blnRtn = false;
                                    throw new DmException(0, "Save In Fields Must Not Be Repeatable");
                                }

                                if (DmShared.xNull(grdOverhead.get(i).getAmtSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAtRateSaveIn()))) {

                                    blnRtn = false;
                                    throw new DmException(0, "Save In Fields Must Not Be Repeatable");
                                }

                                if (DmShared.xNull(grdOverhead.get(i).getAmtSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAmtSaveIn()))) {
                                    blnRtn = false;
                                    throw new DmException(0, "Save In Fields Must Not Be Repeatable");
                                }                                
                            }
                            
                            //  '=========== Duplicacy Checking For CalLValOn ================
                            if (DmShared.xNull(grdOverhead.get(i).getCalLValOnCode()).trim().equals("1")) {
                                if (DmShared.xNull(grdOverhead.get(i).getCalLValOnCode()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getCalLValOnCode()))) {
                                    blnRtn = false;
                                    throw new DmException(0, "Landed Value Can Only Be Calculated On One Overhead");
                                }
                            }
                            
                        }
                    }
                    //  '=========== Checking That Contra Expense Should Exist In Expense List =========
                    if (blnRtn) {
                        if (!DmShared.xNull(grdOverhead.get(i).getContraExpText()).equalsIgnoreCase("")) {
                            blnRtn = false;
                            for (int j = 0; j < grdOverhead.size(); j++) {
                                if (DmShared.xNull(grdOverhead.get(i).getContraExpTag()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getOverheadCode()))) {
                                    blnRtn = true;
                                    break;
                                }
                            }
                            if (!blnRtn) {
                                throw new DmException(0, "Contra Expense [ " + DmShared.xNull(grdOverhead.get(i).getContraExpText()) + "] Does Not Exists In Expense List.");
                            }
                        }
                    }
                }
                if (!blnRtn) {
                    break;
                }
            }
            return blnRtn;
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    protected static boolean checkUniqueId(String uniqueId, List<GrdOverhead_Mdl> grdList) {
        String strTemp = "", charSquareOpen = "", charSquareClose = "";
        String chrTemp;
        boolean blnFlag = true;
        int duplicate;

        uniqueId = uniqueId.toUpperCase();
        for (int i = 0; i < uniqueId.length(); i++) {
            chrTemp = uniqueId.substring(i, i + 1);
            char chrTempValue = (char) chrTemp.charAt(0);
            if ((int) chrTempValue >= 65 && (int) chrTempValue <= 90) {
                strTemp = strTemp + chrTemp;
            } else if (chrTemp.equalsIgnoreCase("[")) {
                strTemp = "";
                charSquareOpen = "[";
            } else if (chrTemp.equalsIgnoreCase("]")) {
                charSquareClose = "]";
            } else {
                blnFlag = false;
                throw new DmException(0, "Alphabets & square brakets '[ ]' can only be used as unique id.");
            }
        }
        if (blnFlag) {
            if (!charSquareOpen.equalsIgnoreCase("") && !charSquareClose.equalsIgnoreCase("")) {
                if (!(!charSquareOpen.equalsIgnoreCase("") && !charSquareClose.equalsIgnoreCase("")) && !strTemp.equalsIgnoreCase("")) {
                    blnFlag = false;
                    throw new DmException(0, "Invalid unique id format.");
                }
                if (blnFlag) {
                    String tmp = charSquareOpen + strTemp + charSquareClose;
                    if (tmp.length() != uniqueId.length()) {
                        blnFlag = false;
                        throw new DmException(0, "Invalid unique id format.");
                    }
                }
            }
        }
        if (blnFlag) {
            if (!strTemp.equalsIgnoreCase("")) {
                if (strTemp.length() > 2) {
                    blnFlag = false;
                    throw new DmException(0, "Unique id can not be greater than 2 alphabets.");
                }
            }
        }
        if (blnFlag) {
            duplicate = 0;
            for (int i = 0; i < grdList.size(); i++) {
                if (!strTemp.equalsIgnoreCase("")) {
                    if (strTemp.equalsIgnoreCase(grdList.get(i).getUniqueID())) {
                        duplicate = duplicate + 1;
                    }
                    if (duplicate > 1) {
                        blnFlag = false;
                        throw new DmException(0, "Duplicate Unique Id.");
                    }
                }
            }
        }

        return blnFlag;
    }

}
