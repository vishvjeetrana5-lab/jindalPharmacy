package dataman.payroll.departmentMaster.server;

import dataman.payroll.departmentMaster.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentMaster_SrvImpl extends DmRSSPrivate implements DepartmentMaster_Srv {

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
                    + "     Select dm.subCode as code From departmentmast as dm     "
                    + "         left join subGroup as sdm on (sdm.subCode=dm.subCode)  "
                    + "         left join subgroupNature sgn on (sgn.code = sdm.filterNature)"
                    + "         where sgn.manualCode='PY'   "
                    + "         Order By sdm.name; ");

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
    public DmResultPageing<List<String>> getSuggestionDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" Select sdm.name as name From departmentmast as dm "
                    + " left join subGroup as sdm on (sdm.subCode=dm.subCode)  ");
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
            sqlWithParam.setSQL("Select sdm.manualcode as shortName From departmentmast as dm "
                    + " left join subGroup as sdm on (sdm.subCode=dm.subCode) ");
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
    public DmResult<DepartmentMaster_Mdl> getMoveRec(String strKey) {
        DmResult<DepartmentMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DepartmentMaster_Mdl mrTemp = new DepartmentMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  dm.subcode, sdm.name, "
//                    + "sdm.costCenter as costCenterCode, ccm.name as costCenterName, "
                    + "         sdm.manualCode, sdm.displayName, "
                    + "         sdm.u_Name, sdm.preparedBy "
                    + "         from departmentmast dm "
                    + "         left join subGroup sdm on sdm.subCode = dm.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sdm.nature "
//                    + "         left join costCenterMast ccm on ccm.code = sdm.costCenter "
                    + "         where dm.subCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
//                mrTemp.setCostCenterCode(DmShared.xNull(rsTemp.getString("costCenterCode")));
//                mrTemp.setCostCenter(DmShared.xNull(rsTemp.getString("costCenterName")));
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

            sqlQuery = "Delete From subgroupSiteList Where subCode= :code ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("code", strKey);
            psTemp1.executeUpdate();
            psTemp1.close();
            
            sqlQuery = "Delete From departmentmast Where subcode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From subgroup Where subCode= :code ";
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
    public DmResult<String> saveRecord(DepartmentMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, filterNatureCode = "", acDepartmentCode = "", filterNature = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        mrTemp.setShortName(mrTemp.getShortName().replace(" ", "").trim());

        try {
            DepartmentMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setDisplayName(DmShared.removeSpace(mrTemp.getDisplayName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "subGroup", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select  count(sdm.name) as cnt "
                    + "         from subgroup sdm "
                    + "         where lower(sdm.name) = :name and sdm.subCode <> :code ";
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

            sqlQuery = "select  count(sdm.manualCode) as cnt "
                    + "         from subgroup sdm "
                    + "         where lower(sdm.manualCode) = :shortName and sdm.subCode <> :code ";
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

            acDepartmentCode = DmServer.getCodeViaManualCode("1", "1", "enviroCompany", "acGroupDepartmentCode", cnTemp);

            filterNatureCode = DmServer.getCodeViaManualCode("PY", "manualCode", "subgroupNature", "Code", cnTemp);
            filterNature = DmServer.getCodeViaManualCode("PY", "manualCode", "subgroupNature", "Name", cnTemp);
            if (blnAdd) {
                sqlQuery = "insert into subGroup(subcode, name, displayname, nature, groupcode, filterNature, manualCode, site_Code, preparedBy, u_EntDt"
//                        + ", costCenter"
                        + ") "
                        + " values (:subcode, :name, :displayname, :nature, :groupcode, :filterNature, :manualCode, :sitecode, :uName, :uEntDt"
//                        + ", :costCenter"
                        + ") ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subcode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("displayname", mrTemp.getDisplayName());
                psTemp.setString("nature", filterNatureCode);
                psTemp.setString("groupcode", acDepartmentCode);
                psTemp.setString("manualCode", mrTemp.getShortName());
                psTemp.setString("filterNature", filterNatureCode);
                psTemp.setString("sitecode", DmServer.getSsnSiteCode(this));
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
//              psTemp.setString("costCenter", mrTemp.getCostCenterCode());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "insert into departmentMast(subCode, nature) "
                        + " values (:subcode, :nature) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subcode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("nature", filterNature);
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "insert into subGroupSiteList(subCode, site_Code) "
                        + " values (:subcode, :siteCode );";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subcode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("siteCode", DmShared.foriegnKey(DmServer.getSsnSiteCode(this)));
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                sqlQuery = "update subGroup set "
                        + " name            = :name, "
                        + " displayname     = :displayname, "
                        + " nature          = :nature, "
                        + " manualCode      = :shortName, "
                        + " u_Name          = :uName, "
                        + " u_EntDt         = :uEntDt "
//                        + " costCenter      = :costCenter "
                        + " Where subcode   = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("displayname", mrTemp.getDisplayName());
                psTemp.setString("nature", filterNatureCode);
                psTemp.setString("shortName", mrTemp.getShortName());
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
//                psTemp.setString("costCenter", mrTemp.getCostCenterCode());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "update departmentMast set "
                        + " nature = :nature "
                        + " Where subcode = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("nature", filterNature);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.executeUpdate();
                psTemp.close();

            }
            /////////////////////////////////////////////////////////////////////////////////////
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "    SELECT CCM.Code,CCm.Name "
                    + "         FROM CostCenterPath CCP  "
                    + "         LEFT JOIN CostCenterMast CCM ON CCM.Code = CCP.UnderCostCenter ";

            sqlWithParam.setSQL(sqlQuery);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
                    "       SELECT dm.subCode AS CODE, sdm.name AS NAME, sgn.name as nature, "
//                            + "ccm.name as costCenter, "
                    + "         sdm.manualCode as shortName "
                    + "         FROM departmentmast AS dm "
                    + "         left join subGroup as sdm on (sdm.subCode=dm.subCode) "
                    + "         left join subGroupNature as sgn on (sdm.nature=sgn.code) "
//                    + "         left join costCenterMast ccm on ccm.code = sdm.costCenter  "
                    + "         where sgn.manualCode='PY'   ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("CODE")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
//                listNode.setCostCenter(DmShared.xNull(rsTemp.getString("costCenter")));
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
//        String csvHeader = ImportData_Server.getCSVFormats(new DepartmentMaster().getImportDataModel().getHmCSV()).get("header");
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

}
