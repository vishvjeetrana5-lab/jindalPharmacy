package dataman.webJobs.notificationEntry.server;

import dataman.dmBase.self.shared.*;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.webJobs.notificationEntry.shared.Find_Mdl;
import dataman.webJobs.notificationEntry.shared.NotificationEntry_Mdl;
import dataman.webJobs.notificationEntry.shared.NotificationEntry_Srv;
import dataman.webJobs.notificationEntry.shared.NotificationEntry_Vld;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationEntry_SrvImpl extends DmRSSPrivate implements NotificationEntry_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("Select ne.code from notificationEntry as ne order by ne.notification");
         

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
    public DmResult<NotificationEntry_Mdl> getMoveRec(String strKey) {
        DmResult<NotificationEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        NotificationEntry_Mdl mrTemp = new NotificationEntry_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select ne.code as code,ne.notification as name,ne.entryDate,ne.expiryDate,"
                    + "ne.preparedBy from notificationEntry as ne where ne.code =:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setEntryDate(DmShared.dNull(rsTemp.getDate("entryDate")));
                mrTemp.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));

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

            sqlQuery = "Delete From notificationEntry Where code= :code ";
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
    public DmResult<String> saveRecord(NotificationEntry_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, filterNature = "", acDepartmentCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        // mrTemp.setShortName(mrTemp.getShortName().replace(" ", "").trim());

        try {
            NotificationEntry_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "notificationEntry", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            // acDepartmentCode = DmServer.getCodeViaManualCode("1", "1", "enviroCompany", "acGroupDepartmentCode", cnTemp);
            //  filterNature = DmServer.getCodeViaManualCode("DP", "manualCode", "subgroupNature", "Code", cnTemp);
            if (blnAdd) {
                sqlQuery = "insert into notificationEntry(code, notification, entryDate, expiryDate, siteCode, preparedBy, u_EntDt,u_Name) "
                        + " values (:code, :notification, :entryDate,:expiryDate, :siteCode, :preparedBy, :u_EntDt, :u_Name) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("notification", mrTemp.getSharedKeyFieldName());
                psTemp.setDate("entryDate",DmServer.convertDate(mrTemp.getEntryDate()) );//DmServer.convertDate(mrTemp.getDtVoucher()),mrTemp.getEntryDate()
                psTemp.setDate("expiryDate",DmServer.convertDate(mrTemp.getExpiryDate()) );//DmServer.convertDate(mrTemp.getDtVoucher()),mrTemp.getEntryDate()
               // psTemp.setString("expiryDate", mrTemp.getExpiryDate());
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("sitecode", DmServer.getSsnSiteCode(this));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

  
            } else {
                sqlQuery = "update notificationEntry set "
                        + " notification = :notification, "
                        + " entryDate = :entryDate, "
                        + " expiryDate = :expiryDate, "
                        + " u_Name = :uName, "
                        + " u_EntDt = :uEntDt "
                        + " Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("notification", mrTemp.getSharedKeyFieldName());
                 psTemp.setDate("entryDate",DmServer.convertDate(mrTemp.getEntryDate()) );
                 psTemp.setDate("expiryDate",DmServer.convertDate(mrTemp.getExpiryDate()) );
              //  psTemp.setString("entryDate", mrTemp.getEntryDate());
                psTemp.setString("expiryDate", mrTemp.getExpiryDate());
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
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
            sqlWithParam.setSQL(" select ne.code,ne.notification as name,ne.entryDate,ne.expiryDate from notificationEntry as ne ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setEntryDate(DmShared.xNull(rsTemp.getString("entryDate")));
                listNode.setExpiryDate(DmShared.xNull(rsTemp.getString("expiryDate")));
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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
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
        return dmrTemp;
    }

    @Override
    public String[] getServerDateTime() {
      String[] dateTime, returnValue;

        dateTime = DmServer.getServerDateTime();
        returnValue = new String[3];
        returnValue[0] = dateTime[0];
        returnValue[1] = dateTime[1];
        returnValue[2] = UUID.randomUUID().toString();

        return returnValue;
    }

}
