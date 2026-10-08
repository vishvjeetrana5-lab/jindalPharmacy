package dataman.utility.guiConfig.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class GUIConfig_Vld {

    public static void chkSave(GUIConfig_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "GUI Name", 100);
        DmShared.isValidText(mrTemp.getTemplateCode(), "Template Name", 200);
        DmShared.isValidText(mrTemp.getTableName(), "Table Name", 300);
        DmShared.isValidText(mrTemp.getSqlQuery(), "SQL Query", 400);

        boolean isRowExists = false, isColumnExists = false;
        if (mrTemp.getGrdData() != null) {
            for (int i = 0; i < mrTemp.getGrdData().size(); i++) {
                GUIRow_Mdl row = mrTemp.getGrdData().get(i);
                if (DmShared.iNull(row.getSrNo()).intValue() != 0) {
                    isRowExists = true;
                    isColumnExists = false;

                    if (row.getGuiCol() != null) {
                        for (GUIColumn_Mdl column : row.getGuiCol()) {
                            if (DmShared.iNull(column.getSharedKeyFieldCode()).intValue() != 0) {
                                isColumnExists = true;

                                if (DmShared.xNull(column.getFieldDataTypeTag()).trim().equalsIgnoreCase("")) {
                                    throw new DmException(0, "Field Data Type is mandatory for Row No. " + DmShared.xNull(row.getSrNo()) + " and Sr. No. " + DmShared.xNull(column.getSharedKeyFieldCode()) + ".");
                                }
                                if (DmShared.vNull(column.getCaptionWidth()).doubleValue() == 0 && DmShared.vNull(column.getControlWidth()).doubleValue() == 0) {
                                    throw new DmException(0, "Please define Caption or Control width % for Row No. " + DmShared.xNull(row.getSrNo()) + " and Sr. No. " + DmShared.xNull(column.getSharedKeyFieldCode()) + ".");
                                }
                                if ((DmShared.vNull(column.getCaptionWidth()).doubleValue() + DmShared.vNull(column.getControlWidth()).doubleValue()) > 100.0) {
                                    throw new DmException(0, "Sum of Caption and Control width % must be less than or greater than 100 for Row No. " + DmShared.xNull(row.getSrNo()) + " and Sr. No. " + DmShared.xNull(column.getSharedKeyFieldCode()) + ".");
                                }
                                if (DmShared.xNull(column.getMandatoryTag()).trim().equalsIgnoreCase("")) {
                                    throw new DmException(0, "Is Mandatory is mandatory for Row No. " + DmShared.xNull(row.getSrNo()) + " and Sr. No. " + DmShared.xNull(column.getSharedKeyFieldCode()) + ".");
                                }
                                if (DmShared.xNull(column.getIsCopyTag()).trim().equalsIgnoreCase("")) {
                                    throw new DmException(0, "Copy is mandatory for Row No. " + DmShared.xNull(row.getSrNo()) + " and Sr. No. " + DmShared.xNull(column.getSharedKeyFieldCode()) + ".");
                                }

                                for (GUIColumn_Mdl columnOld : row.getGuiCol()) {
                                    if (DmShared.iNull(columnOld.getSharedKeyFieldCode()).intValue() != 0) {
                                        if (column.getSharedKeyFieldHashCode() != columnOld.getSharedKeyFieldHashCode()) {
                                            if (!DmShared.xNull(column.getCustomFieldName()).trim().equalsIgnoreCase("")) {
                                                if (DmShared.xNull(column.getCustomFieldName()).trim().equalsIgnoreCase(DmShared.xNull(columnOld.getCustomFieldName()).trim())) {
                                                    throw new DmException(0, "Custom Field Name already exists for Row No. " + DmShared.xNull(row.getSrNo()) + " and Sr. No. " + DmShared.xNull(column.getSharedKeyFieldCode()) + ".");
                                                }
                                            }
                                            if (!DmShared.xNull(column.getFieldName()).trim().equalsIgnoreCase("")) {
                                                if (DmShared.xNull(column.getFieldName()).trim().equalsIgnoreCase(DmShared.xNull(columnOld.getFieldName()).trim())) {
                                                    throw new DmException(0, "Field Name already exists for Row No. " + DmShared.xNull(row.getSrNo()) + " and Sr. No. " + DmShared.xNull(column.getSharedKeyFieldCode()) + ".");
                                                }
                                            }
                                        }
                                    }
                                }

                                for (int j = 0; j < mrTemp.getGrdData().size(); j++) {
                                    GUIRow_Mdl row_Mdl = mrTemp.getGrdData().get(j);
                                    if (DmShared.iNull(row_Mdl.getSrNo()).intValue() != 0) {
                                        if (i != j) {
                                            if (DmShared.iNull(row.getSrNo()).intValue() == DmShared.iNull(row_Mdl.getSrNo()).intValue()) {
                                                throw new DmException(0, "Row No. could not be conflict for Row No. " + (i + 1) + " and " + (j + 1) + ".");
                                            }

                                            for (GUIColumn_Mdl column_Mdl : row_Mdl.getGuiCol()) {
                                                if (DmShared.iNull(column_Mdl.getSharedKeyFieldCode()).intValue() != 0) {
                                                    if (!DmShared.xNull(column_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("")) {
                                                        if (DmShared.xNull(column.getCustomFieldName()).trim().equalsIgnoreCase(DmShared.xNull(column_Mdl.getCustomFieldName()).trim())) {
                                                            throw new DmException(0, "Custom Field Name already exists for Row No. " + DmShared.xNull(row_Mdl.getSrNo()) + " and Sr. No. " + DmShared.xNull(column_Mdl.getSharedKeyFieldCode()) + ".");
                                                        }
                                                    }
                                                    if (!DmShared.xNull(column.getFieldName()).trim().equalsIgnoreCase("")) {
                                                        if (DmShared.xNull(column.getFieldName()).trim().equalsIgnoreCase(DmShared.xNull(column_Mdl.getFieldName()).trim())) {
                                                            throw new DmException(0, "Field Name already exists for Row No. " + DmShared.xNull(row_Mdl.getSrNo()) + " and Sr. No. " + DmShared.xNull(column_Mdl.getSharedKeyFieldCode()) + ".");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                    if (!isColumnExists) {
                        throw new DmException(0, "GUI Column Detail is mandatory for Row No. " + DmShared.xNull(row.getSrNo()) + ".");
                    }
                }
            }
        }
        if (!isRowExists) {
            throw new DmException(0, "GUI Row Detail is mandatory.");
        }
    }
}
