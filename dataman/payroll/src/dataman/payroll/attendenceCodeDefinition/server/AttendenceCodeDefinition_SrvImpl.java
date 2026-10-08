package dataman.payroll.attendenceCodeDefinition.server;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.payroll.attendenceCodeDefinition.shared.*;
import dataman.payroll.self.shared.PREnum.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AttendenceCodeDefinition_SrvImpl extends DmRSSPrivate implements AttendenceCodeDefinition_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery(""
                    + "     select Code from attendenceCodeDefinitionMast where type_Leave_Attendance="+ TYPE_LEAVE_ATTENDANCE.getViaText("ATTENDANCE").getId()+" order by name");

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
    public DmResultPageing<List<String>> getSuggestionName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" Select name  From attendenceCodeDefinitionMast where type_Leave_Attendance="+ TYPE_LEAVE_ATTENDANCE.getViaText("LEAVE").getId());
            helper.addSort("name", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("name")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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
    public DmResultPageing<List<String>> getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" Select manualCode as shortName  From attendenceCodeDefinitionMast where type_Leave_Attendance="+ TYPE_LEAVE_ATTENDANCE.getViaText("ATTENDANCE").getId());
            helper.addSort("shortName", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("shortName")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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
    public DmResult<AttendenceCodeDefinition_Mdl> getMoveRec(String strKey) {
        DmResult<AttendenceCodeDefinition_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        AttendenceCodeDefinition_Mdl mrTemp = new AttendenceCodeDefinition_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  Code, manualCode, Name, workingHours, days, byDefault, "
                    + "         u_Name, preparedBy,plusMinus "
                    + "         from attendenceCodeDefinitionMast  "
                    + "         where Code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("Code")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                mrTemp.setWorkingHours(DmServer.vNull(rsTemp.getString("workingHours"), "00.00"));
                mrTemp.setDays(DmServer.vNull(rsTemp.getString("days"), "00.00"));
                mrTemp.setByDefaultCode(DmShared.iNull(rsTemp.getString("byDefault")).toString());
                mrTemp.setByDefault(rsTemp.getInt("byDefault") == 1 ? "Yes" : "No");
                mrTemp.setPlusMinusCode(DmShared.iNull(rsTemp.getString("plusMinus")).toString());
                mrTemp.setPlusMinus(rsTemp.getInt("plusMinus") == 1 ? "+" : "-");
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
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
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From attendenceCodeDefinitionMast Where Code= :code ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("code", strKey);
            psTemp1.executeUpdate();
            psTemp1.close();

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
    public DmResult<String> saveRecord(AttendenceCodeDefinition_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        mrTemp.setShortName(mrTemp.getShortName().replace(" ", "").trim());

        try {
            AttendenceCodeDefinition_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "attendenceCodeDefinitionMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select  count(name) as cnt "
                    + "         from attendenceCodeDefinitionMast sdm "
                    + "         where lower(name) = :name and Code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(100, "Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  count(manualCode) as cnt "
                    + "         from subgroup sdm "
                    + "         where lower(manualCode) = :shortName and subCode <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()).toLowerCase().trim());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(200, "Short Name already exists.");
            }
            rsTemp.close();
            psTemp.close();
            
            // Check if there is more byDefault = 'Yes'
            sqlQuery = "select count(sdm.byDefault) as cnt from attendenceCodeDefinitionMast sdm where sdm.byDefault = 1\n"
                    + "and  Code <> :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                if(mrTemp.getByDefaultCode().equalsIgnoreCase("1"))
                {
                  throw new DmException(300, "By Default cannot be Yes.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "insert into attendenceCodeDefinitionMast(code, name, days, byDefault, workingHours, manualCode, site_Code, preparedBy, u_EntDt,type_Leave_Attendance,plusMinus) "
                        + " values (:code, :name, :days, :byDefault, :workingHours, :manualCode, :sitecode, :preparedBy, :u_EntDt, :type_Leave_Attendance,:plusMinus) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("days", mrTemp.getDays());
                psTemp.setString("byDefault", mrTemp.getByDefaultCode());
                psTemp.setString("workingHours", mrTemp.getWorkingHours());
                psTemp.setString("manualCode", mrTemp.getShortName());
                psTemp.setString("sitecode", DmServer.getSsnSiteCode(this));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("type_Leave_Attendance",DmServer.vNull(TYPE_LEAVE_ATTENDANCE.getViaText("ATTENDANCE").getId()));
                psTemp.setString("plusMinus", mrTemp.getPlusMinusCode());
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = "update attendenceCodeDefinitionMast set "
                        + " name            = :name, "
                        + " days            = :days, "
                        + " byDefault       = :byDefault, "
                        + " manualCode      = :shortName, "
                        + " workingHours    = :workingHours, "
                        + " plusMinus       = :plusMinus, "
                        + " u_Name          = :uName, "
                        + " u_EntDt         = :uEntDt"
                        + " Where code   = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("days", mrTemp.getDays());
                psTemp.setString("byDefault", mrTemp.getByDefaultCode());
                psTemp.setString("shortName", mrTemp.getShortName());
                psTemp.setString("workingHours", mrTemp.getWorkingHours());
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("plusMinus", mrTemp.getPlusMinusCode());
                psTemp.executeUpdate();
                psTemp.close();

            }

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDefault(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldName("Yes");
            listNode.setSharedKeyFieldCode("1");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldName("No");
            listNode.setSharedKeyFieldCode("0");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
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
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(
                    "      select code, Name, manualCode,  workingHours, days, byDefault "
                    + "         from attendenceCodeDefinitionMast  where type_Leave_Attendance="+ TYPE_LEAVE_ATTENDANCE.getViaText("ATTENDANCE").getId());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("CODE")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setWorkingHours(DmServer.vNull(rsTemp.getString("workingHours"), "00.00"));
                listNode.setDays(DmServer.vNull(rsTemp.getString("days"), "00.00"));
                listNode.setByDefault(rsTemp.getInt("byDefault") == 1 ? "Yes" : "No");
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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

//    @Override
//    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
//        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
//        Connection cnTemp = null;
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        KeyFieldExtended keyField = new KeyFieldExtended();
//        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
//        DmHandelFile handleFile = null;
//        char separator = File.separatorChar;
//        String csvHeader = ImportData_Server.getCSVFormats(new AttendenceCodeDefinition().getImportDataModel().getHmCSV()).get("header");
//        dmrTemp.setData(keyField);
//        try {
//            fileName = "departmentMaster";
//            if (!DmSession.getSessions().containsKey(sessionId)) {
//                throw new DmException(0, "Invalid request.");
//            }
//
//            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
//                throw new DmException(0, "File Name not given.");
//            }
//            String directoryPath = DmServer.Path.sessions + separator + sessionId;
//            String serverFileName = fileName + ".csv";
//            String fullFilePath = directoryPath + separator + serverFileName;
//            new File(directoryPath).mkdir();
//            handleFile = new DmHandelFile(fullFilePath, true);
//
//            keyField.setSharedKeyFieldCode(directoryPath);
//            keyField.setSharedKeyFieldName(serverFileName);
//
//            handleFile.writeLine(csvHeader);
//
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            sqlQuery = "select  sg.name as departmentName, dm.nature, sg.manualCode as shortName "
//                    + "		from departmentMast dm "
//                    + "		left join subGroup sg on sg.subCode = dm.subCode "
//                    + "		order by sg.name, sg.manualCode ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                lineData = "\"" + DmShared.xNull(rsTemp.getString("departmentName")) + "\"";
//                lineData += commaDelimiter;
//                lineData += "\"" + DmShared.xNull(rsTemp.getString("nature")) + "\"";
//                lineData += commaDelimiter;
//                lineData += "\"" + DmShared.xNull(rsTemp.getString("shortName")) + "\"";
//                handleFile.writeLine(lineData);
//            }
//            psTemp.close();
//            rsTemp.close();
//            handleFile.close();
//            dmrTemp.setData(keyField);
//            cnTemp.close();
//            dmrTemp.setStatus(true);
//        } catch (Exception e) {
//            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//                if (handleFile != null) {
//                    handleFile.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//        return dmrTemp;
//    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPlusMinus(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldName("+");
            listNode.setSharedKeyFieldCode("1");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldName("-");
            listNode.setSharedKeyFieldCode("0");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(pageing, lstTemp));
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }
}
