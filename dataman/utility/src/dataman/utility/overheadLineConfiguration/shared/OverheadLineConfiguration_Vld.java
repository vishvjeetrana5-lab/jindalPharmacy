package dataman.utility.overheadLineConfiguration.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class OverheadLineConfiguration_Vld {

    public static void chkSave(OverheadLineConfiguration_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getOverheadName().trim(), "Overhead Name", 150);
        DmShared.isValidText(mrTemp.getTableAffectedText(), "Table Affected", 200);
        DmShared.isValidText(mrTemp.getApplicableFrom(), "Applicable From", 300);
    }

    public static boolean checkGrid(List<GrdOverheadLine_Mdl> grdOverhead) {
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

                        }
                    }

                    if (!DmShared.xNull(grdOverhead.get(i).getAtRateSaveIn()).equalsIgnoreCase("")) {
                        if (DmShared.xNull(grdOverhead.get(i).getAtRateSaveInCode()).equalsIgnoreCase("")) {
                            blnRtn = false;
                            throw new DmException(0, DmShared.xNull(grdOverhead.get(i).getAtRateSaveIn()) + " <At Rate Save In> Is Unmapped. Please Map It.");

                        }
                    }

                    if (!DmShared.xNull(grdOverhead.get(i).getOverheadSaveIn()).equalsIgnoreCase("")) {
                        if (DmShared.xNull(grdOverhead.get(i).getOverheadSaveInCode()).equalsIgnoreCase("")) {
                            blnRtn = false;
                            throw new DmException(0, DmShared.xNull(grdOverhead.get(i).getOverheadSaveIn()) + " <Head Save In> Is Unmapped. Please Map It.");
                        }
                    }

                    if (!DmShared.xNull(grdOverhead.get(i).getAcSave()).equalsIgnoreCase("")) {
                        if (DmShared.xNull(grdOverhead.get(i).getAcSaveCode()).equalsIgnoreCase("")) {
                            blnRtn = false;
                            throw new DmException(0, DmShared.xNull(grdOverhead.get(i).getAcSave()) + " <A/c Save In> Is Unmapped. Please Map It.");
                        }
                    }

                    checkAtRateSaveIn(i, grdOverhead);
                    checkAmtSaveIn(i, grdOverhead);
                    checkOverheadSaveIn(i, grdOverhead);
                    checkAcSave(i, grdOverhead);

                    for (int j = i + 1; j < grdOverhead.size(); j++) {
                        if (!DmShared.xNull(grdOverhead.get(i).getOverhead()).equalsIgnoreCase("")) {
                            if (DmShared.xNull(grdOverhead.get(i).getOverhead()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getOverhead()))) {
                                blnRtn = false;
                                throw new DmException(0, "Duplicate Overhead.");
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

    protected static void checkAtRateSaveIn(int rowIndex, List<GrdOverheadLine_Mdl> grdOverhead) {
        boolean blnRtn = true;
        if (!DmShared.xNull(grdOverhead.get(rowIndex).getAtRateSaveIn()).equalsIgnoreCase("")) {
            for (int j = 0; j < grdOverhead.size(); j++) {
                if (j != rowIndex) {
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAtRateSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAtRateSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAtRateSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAmtSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAtRateSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getOverheadSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAtRateSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAcSave()))) {
                        blnRtn = false;
                    }
                } else {
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAtRateSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAmtSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAtRateSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getOverheadSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAtRateSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAcSave()))) {
                        blnRtn = false;
                    }
                }
            }
        }

        if (!blnRtn) {
            throw new DmException(0, "Save In Fields Must Not Be Repeatable");
        }
    }

    protected static void checkAmtSaveIn(int rowIndex, List<GrdOverheadLine_Mdl> grdOverhead) {
        boolean blnRtn = true;
        if (!DmShared.xNull(grdOverhead.get(rowIndex).getAmtSaveIn()).equalsIgnoreCase("")) {
            for (int j = 0; j < grdOverhead.size(); j++) {
                if (j != rowIndex) {
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAmtSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAmtSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAmtSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAtRateSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAmtSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getOverheadSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAmtSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAcSave()))) {
                        blnRtn = false;
                    }
                } else {
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAmtSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAtRateSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAmtSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getOverheadSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAmtSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAcSave()))) {
                        blnRtn = false;
                    }
                }
            }
        }

        if (!blnRtn) {
            throw new DmException(0, "Save In Fields Must Not Be Repeatable");
        }
    }

    protected static void checkOverheadSaveIn(int rowIndex, List<GrdOverheadLine_Mdl> grdOverhead) {
        boolean blnRtn = true;

        if (!DmShared.xNull(grdOverhead.get(rowIndex).getOverheadSaveIn()).equalsIgnoreCase("")) {
            for (int j = 0; j < grdOverhead.size(); j++) {
                if (j != rowIndex) {
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getOverheadSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getOverheadSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getOverheadSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAtRateSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getOverheadSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAmtSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getOverheadSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAcSave()))) {
                        blnRtn = false;
                    }
                } else {
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getOverheadSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAtRateSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getOverheadSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAmtSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getOverheadSaveIn()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAcSave()))) {
                        blnRtn = false;
                    }
                }
            }
        }

        if (!blnRtn) {
            throw new DmException(0, "Save In Fields Must Not Be Repeatable");
        }
    }

    protected static void checkAcSave(int rowIndex, List<GrdOverheadLine_Mdl> grdOverhead) {
        boolean blnRtn = true;

        if (!DmShared.xNull(grdOverhead.get(rowIndex).getAcSave()).equalsIgnoreCase("")) {
            for (int j = 0; j < grdOverhead.size(); j++) {
                if (j != rowIndex) {
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAcSave()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAcSave()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAcSave()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAtRateSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAcSave()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAmtSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAcSave()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getOverheadSaveIn()))) {
                        blnRtn = false;
                    }
                } else {
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAcSave()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAtRateSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAcSave()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getAmtSaveIn()))) {
                        blnRtn = false;
                    }
                    if (DmShared.xNull(grdOverhead.get(rowIndex).getAcSave()).equalsIgnoreCase(DmShared.xNull(grdOverhead.get(j).getOverheadSaveIn()))) {
                        blnRtn = false;
                    }
                }
            }
        }

        if (!blnRtn) {
            throw new DmException(0, "Save In Fields Must Not Be Repeatable");
        }
    }

}
