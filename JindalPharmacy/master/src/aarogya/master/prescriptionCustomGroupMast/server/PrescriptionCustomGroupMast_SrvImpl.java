package aarogya.master.prescriptionCustomGroupMast.server;

import dataman.dmBase.self.shared.*;
import aarogya.master.prescriptionCustomGroupMast.shared.*;
import aarogya.selfToolkit.stImportData.server.ImportData_Server;
import aarogya.selfToolkit.stImportData.server.ImportPrescriptionCustomGroupMast;
import aarogya.selfToolkit.stStatic.shared.STEnum.CustomWidgetNature;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionCustomGroupMast_SrvImpl extends DmRSSPrivate implements PrescriptionCustomGroupMast_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            rsTemp = cnTemp.createStatement().executeQuery("Select code from prescriptionDetailGroupMast order by name ");
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select name from prescriptionDetailGroupMast ");
            helper.addSort("name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(data.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<PrescriptionCustomGroupMast_Mdl> getMoveRec(String strKey) {
        DmResult<PrescriptionCustomGroupMast_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PrescriptionCustomGroupMast_Mdl mrTemp = new PrescriptionCustomGroupMast_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  code, name, nature, preparedBy, u_Name, printSNo "
                    + "         from prescriptionDetailGroupMast "
                    + "         where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setNatureCode(DmShared.xNull(rsTemp.getString("nature")));
                mrTemp.setNatureName(CustomWidgetNature.getKeyOf(mrTemp.getNatureCode()).getValue());
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setPrnSrNo(DmServer.vNull(rsTemp.getString("printSNo"), "0"));
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*============================< Delete records from prescriptionDetailGroupMast >==============================*/
            sqlQuery = "Delete from prescriptionDetailGroupMast where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> saveRecord(PrescriptionCustomGroupMast_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            PrescriptionCustomGroupMast_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "prescriptionDetailGroupMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==========================< Check whether name already exist or not >============================*/
            sqlQuery = "Select code from prescriptionDetailGroupMast where code <> :code and name = :name and nature = :nature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
            psTemp.setString("nature", DmShared.xNull(mrTemp.getNatureCode()).trim());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Insert/Update into prescriptionDetailGroupMast >====================================*/
            if (blnAdd) {
                sqlQuery = "Insert into prescriptionDetailGroupMast(code, name, nature, printSNo, preparedBy, u_EntDt) "
                        + " values (:code, :name, :nature, :printSNo, :preparedBy, :u_EntDt) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
                psTemp.setString("nature", DmShared.xNull(mrTemp.getNatureCode()).trim());
                psTemp.setString("printSNo", DmServer.vNull(mrTemp.getPrnSrNo(), "0"));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = "Update prescriptionDetailGroupMast set "
                        + " name = :name, "
                        + " nature = :nature, "
                        + " printSNo = :printSNo,"
                        + " u_Name = :u_Name, "
                        + " u_EntDt = :u_EntDt "
                        + " where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
                psTemp.setString("printSNo", DmServer.vNull(mrTemp.getPrnSrNo(), "0"));
                psTemp.setString("nature", DmShared.xNull(mrTemp.getNatureCode()).trim());
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (CustomWidgetNature value : CustomWidgetNature.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(value.getId());
                listNode.setSharedKeyFieldName(value.getValue());
                lstTemp.add(listNode);
            }

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  code, name, "
                    + "         case when nature = '" + CustomWidgetNature.COMORBID.getId() + "' then '" + CustomWidgetNature.COMORBID.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.COMPLAINTS.getId() + "' then '" + CustomWidgetNature.COMPLAINTS.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.FAMILY_HISTORY.getId() + "' then '" + CustomWidgetNature.FAMILY_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.HABIT.getId() + "' then '" + CustomWidgetNature.HABIT.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.HISTORY_OF_PAST_ILLNESS.getId() + "' then '" + CustomWidgetNature.HISTORY_OF_PAST_ILLNESS.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.HISTORY_OF_PRESENT_ILLNESS.getId() + "' then '" + CustomWidgetNature.HISTORY_OF_PRESENT_ILLNESS.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.INSTRUCTION.getId() + "' then '" + CustomWidgetNature.INSTRUCTION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.PHYSICAL_EXAMINATION.getId() + "' then '" + CustomWidgetNature.PHYSICAL_EXAMINATION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.SYSTEMIC_EXAMINATION.getId() + "' then '" + CustomWidgetNature.SYSTEMIC_EXAMINATION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.MEDICINE_INSTRUCTION.getId() + "' then '" + CustomWidgetNature.MEDICINE_INSTRUCTION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.DEVELOPMENT_HISTORY.getId() + "' then '" + CustomWidgetNature.DEVELOPMENT_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.FEEDING_HISTORY.getId() + "' then '" + CustomWidgetNature.FEEDING_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.GYNAECOLOGICAL_EXAMINATION.getId() + "' then '" + CustomWidgetNature.GYNAECOLOGICAL_EXAMINATION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.IMMUNIZATION_HISTORY.getId() + "' then '" + CustomWidgetNature.IMMUNIZATION_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.MENSTRUAL_EXAMINATION.getId() + "' then '" + CustomWidgetNature.MENSTRUAL_EXAMINATION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.MENSTRUAL_HISTORY.getId() + "' then '" + CustomWidgetNature.MENSTRUAL_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.OBSTETRICS_HISTORY.getId() + "' then '" + CustomWidgetNature.OBSTETRICS_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.OBSTETRIC_EXAMINATION.getId() + "' then '" + CustomWidgetNature.OBSTETRIC_EXAMINATION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.PERINATAL_HISTORY.getId() + "' then '" + CustomWidgetNature.PERINATAL_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.PERSONAL_HISTORY.getId() + "' then '" + CustomWidgetNature.PERSONAL_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.PRECAUTION.getId() + "' then '" + CustomWidgetNature.PRECAUTION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.URGENT_CARE.getId() + "' then '" + CustomWidgetNature.URGENT_CARE.getValue() + "' "
                    + "         else '' end as nature "
                    + "         from prescriptionDetailGroupMast ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("nature")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        KeyFieldExtended keyfiled = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = ImportData_Server.getCSVFormats(new ImportPrescriptionCustomGroupMast().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyfiled);

        try {
            fileName = "PrescriptionCustomGroupMast";
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid Request");
            }
            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, " File Name not given");
            }

            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyfiled.setSharedKeyFieldCode(directoryPath);
            keyfiled.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "Select  name,printSNo, "
                    + "         case when nature = '" + CustomWidgetNature.COMORBID.getId() + "' then '" + CustomWidgetNature.COMORBID.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.COMPLAINTS.getId() + "' then '" + CustomWidgetNature.COMPLAINTS.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.FAMILY_HISTORY.getId() + "' then '" + CustomWidgetNature.FAMILY_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.HABIT.getId() + "' then '" + CustomWidgetNature.HABIT.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.HISTORY_OF_PAST_ILLNESS.getId() + "' then '" + CustomWidgetNature.HISTORY_OF_PAST_ILLNESS.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.HISTORY_OF_PRESENT_ILLNESS.getId() + "' then '" + CustomWidgetNature.HISTORY_OF_PRESENT_ILLNESS.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.INSTRUCTION.getId() + "' then '" + CustomWidgetNature.INSTRUCTION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.PHYSICAL_EXAMINATION.getId() + "' then '" + CustomWidgetNature.PHYSICAL_EXAMINATION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.SYSTEMIC_EXAMINATION.getId() + "' then '" + CustomWidgetNature.SYSTEMIC_EXAMINATION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.MEDICINE_INSTRUCTION.getId() + "' then '" + CustomWidgetNature.MEDICINE_INSTRUCTION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.DEVELOPMENT_HISTORY.getId() + "' then '" + CustomWidgetNature.DEVELOPMENT_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.FEEDING_HISTORY.getId() + "' then '" + CustomWidgetNature.FEEDING_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.GYNAECOLOGICAL_EXAMINATION.getId() + "' then '" + CustomWidgetNature.GYNAECOLOGICAL_EXAMINATION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.IMMUNIZATION_HISTORY.getId() + "' then '" + CustomWidgetNature.IMMUNIZATION_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.MENSTRUAL_EXAMINATION.getId() + "' then '" + CustomWidgetNature.MENSTRUAL_EXAMINATION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.MENSTRUAL_HISTORY.getId() + "' then '" + CustomWidgetNature.MENSTRUAL_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.OBSTETRICS_HISTORY.getId() + "' then '" + CustomWidgetNature.OBSTETRICS_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.OBSTETRIC_EXAMINATION.getId() + "' then '" + CustomWidgetNature.OBSTETRIC_EXAMINATION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.PERINATAL_HISTORY.getId() + "' then '" + CustomWidgetNature.PERINATAL_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.PERSONAL_HISTORY.getId() + "' then '" + CustomWidgetNature.PERSONAL_HISTORY.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.PRECAUTION.getId() + "' then '" + CustomWidgetNature.PRECAUTION.getValue() + "' "
                    + "         when nature = '" + CustomWidgetNature.URGENT_CARE.getId() + "' then '" + CustomWidgetNature.URGENT_CARE.getValue() + "' "
                    + "         else '' end as nature "
                    + "         from prescriptionDetailGroupMast"
                    + "          order by name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("name")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("nature")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("printSNo")) + "\"";
                handleFile.writeLine(lineData);
            }
            psTemp.close();
            rsTemp.close();
            handleFile.close();
            dmrTemp.setData(keyfiled);
            cnTemp.close();
            dmrTemp.setStatus(true);

        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception ex) {

            }
        }

        return dmrTemp;
    }

}
