package dataman.utility.fileTypeMaster.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.utility.fileTypeMaster.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File; 
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FileTypeMaster_SrvImpl extends DmRSSPrivate implements FileTypeMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstKeyField = new ArrayList<>();
        KeyFieldExtended lstKeyFieldTmp;
        dmrTemp.setData(lstKeyField);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            rsTemp = cnTemp.createStatement().executeQuery(" SELECT ruleCode FROM filetypemast GROUP BY ruleCode ORDER BY ruleCode");

            while (rsTemp.next()) {
                lstKeyFieldTmp = new KeyFieldExtended();
                lstKeyFieldTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("ruleCode")));
                lstKeyField.add(lstKeyFieldTmp);
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
    public DmResult<FileTypeMaster_Mdl> getMoveRec(String strKey) {
        DmResult<FileTypeMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        FileTypeMaster_Mdl mrTemp = new FileTypeMaster_Mdl();

        List<GrdFileTypeMaster_Mdl> lstTemp = new ArrayList<>();

        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "SELECT ruleCode, description ,fileType, maxSize, isDefault, extFilters, u_Name "
                    + " FROM filetypemast WHERE ruleCode= :ruleCode ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("ruleCode", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdFileTypeMaster_Mdl objListTemp = new GrdFileTypeMaster_Mdl();
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("ruleCode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("ruleCode")));
                mrTemp.setDescription(DmShared.xNull(rsTemp.getString("description")));

                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("fileType")));
                objListTemp.setSharedKeyFieldName(DmServer.vNull(rsTemp.getString("maxSize"), "0"));

                objListTemp.setDefaultCode(DmShared.xNull(rsTemp.getString("isDefault")));
                objListTemp.setDefaultName(DmShared.vNull(rsTemp.getString("isDefault")).doubleValue() == 1 ? "Yes" : "No");

                objListTemp.setFilter(DmShared.xNull(rsTemp.getString("extFilters")));

                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));

                lstTemp.add(objListTemp);
            }
            if (lstTemp.isEmpty()) {
                GrdFileTypeMaster_Mdl ObjListTemp = new GrdFileTypeMaster_Mdl();

                ObjListTemp.setSharedKeyFieldCode("");
                ObjListTemp.setSharedKeyFieldName("");
                ObjListTemp.setDefaultCode("");
                ObjListTemp.setDefaultName("");
                ObjListTemp.setFilter("");

                lstTemp.add(ObjListTemp);
                mrTemp.getGrdData().addAll(lstTemp);
            } else {
                mrTemp.getGrdData().addAll(lstTemp);
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
    public DmResult<String> saveRecord(FileTypeMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            FileTypeMaster_Vld.chkSave(mrTemp);

            int grdCount = mrTemp.getGrdData().size();

            if (grdCount <= 1) {
                if (DmShared.xNull(mrTemp.getGrdData().get(0).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("One row is mandatory.");
                }
            }

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(mrTemp.getSharedKeyFieldName());
            }

            if (blnAdd) {
                sqlQuery = "SELECT COUNT(ruleCode) AS cnt FROM filetypemast WHERE LOWER(ruleCode) = :ruleCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("ruleCode", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Rule Code already exists.");
                }
                rsTemp.close();
                psTemp.close();
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /////////////////////////////////////////////////////////////////////////////////////
            sqlQuery = "Delete From filetypemast where ruleCode= :ruleCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("ruleCode", mrTemp.getSharedKeyFieldName());
            psTemp.executeUpdate();
            psTemp.close();
            /////////////////////////////////////////////////////////////////////////////////////

            for (int i = 0; i < grdCount; i++) {
                GrdFileTypeMaster_Mdl gtc = mrTemp.getGrdData().get(i);
                if (!DmShared.xNull(gtc.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    sqlQuery = "Insert Into filetypemast(ruleCode,description ,fileType, maxSize, isDefault, extFilters, u_Name, u_EntDt) Values  "
                            + "(:ruleCode,:description, :fileType, :maxSize, :isDefault, :extFilters, :uName, getdate() ) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("ruleCode", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    psTemp.setString("description" , DmShared.xNull(mrTemp.getDescription()));
                    psTemp.setString("fileType", DmShared.xNull(gtc.getSharedKeyFieldCode()));
                    psTemp.setString("maxSize", DmShared.xNull(gtc.getSharedKeyFieldName()));
                    psTemp.setString("isDefault", DmServer.vNull(gtc.getDefaultCode(), "0"));
                    psTemp.setString("extFilters", DmShared.xNull(gtc.getFilter()));
                    psTemp.setString("uName", DmServer.getSsnUserName(this));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
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
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);
        try {

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "Delete From filetypemast where ruleCode= :ruleCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("ruleCode", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
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

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL(" SELECT ruleCode , description FROM filetypemast GROUP BY ruleCode , description ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("ruleCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("ruleCode")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("description")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDefault(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

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
    public DmResult<Boolean> syncronizeFileDB() {
        DmResult<Boolean> dmrTemp = new DmResult();

        dmrTemp.setData(false);

        try {
            DmFileManagerMongo.synchronizeTransactionalWithFileServer();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
        }

        return dmrTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> exportDataCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = "ruleCode,description,fileType,maxSize,filters,default";
        dmrTemp.setData(keyField);
        try {
            fileName = "ImportFileTypeMaster";
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlQuery = "select  ruleCode, description, fileType, maxsize, extFilters, "
                    + "         case isDefault when 0 then 'No' else 'Yes' end as isDefault "
                    + "         from fileTypeMast "
                    + "         order by ruleCode asc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("ruleCode")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("description")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("fileType")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("maxsize")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("extFilters")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isDefault")) + "\"";
                handleFile.writeLine(lineData);
            }
            psTemp.close();
            rsTemp.close();
            
            handleFile.close();
            dmrTemp.setData(keyField);
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
                if (handleFile != null) {
                    handleFile.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

}
