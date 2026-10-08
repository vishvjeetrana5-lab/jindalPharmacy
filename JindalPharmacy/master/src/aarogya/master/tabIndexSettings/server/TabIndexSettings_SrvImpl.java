package aarogya.master.tabIndexSettings.server;

import aarogya.selfToolkit.stImportData.server.ImportTabIndex;
import aarogya.master.tabIndexSettings.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TabIndexSettings_SrvImpl extends DmRSSPrivate implements TabIndexSettings_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntry(String module, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL(" select mnuName as name,mnuText as text  from user_Permission_Web "
                    + " where mnuModule=:module and userName='SA'");
            sqlWithParam.setString("module", DmShared.xNull(module));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("text")));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModule(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("select mnuModule as module "
                    + " from user_Permission_Web where userName='SA'"
                    + " group by mnuModule");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("module")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("module")));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
    public DmResult<String> deleteRecord(String module, String mnuName) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            sqlQuery = "Delete From tabIndexSettings where mnuModule= :mnuModule and mnuName=:mnuName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", DmShared.xNull(module));
            psTemp.setString("mnuName", DmShared.xNull(mnuName));
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
    public DmResult<String> saveRecord(TabIndexSettings_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;

        String sqlQuery = null;
        ResultSet rsTemp = null;
        dmrTemp.setData(null);

        try {
            TabIndexSettings_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            sqlQuery = " select count(*) as cnt "
                    + " from tabIndexSettings   "
                    + " where mnuModule = :mnuModule and mnuName = :mnuName and caption=:caption "
                    + " and sNo=:sNo  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("mnuName", DmShared.xNull(mrTemp.getMenuName()));
            List<GridTabIndexSettings_Mdl> listTabMdl = mrTemp.getGrdTabIndexMdl();
            for (GridTabIndexSettings_Mdl tabInxMdl : listTabMdl) {
                psTemp.setString("caption", DmShared.xNull(tabInxMdl.getSharedKeyFieldCode()));
                psTemp.setString("sNo", DmShared.xNull(tabInxMdl.getSerialNo()));
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    if (rsTemp.getLong("cnt") > 1) {
                        throw new DmException(0, "Tab index detail is duplicate");
                    }
                }
            }
            /*==============================< Delete & Insert records into tabIndexSettings >================================*/
            sqlQuery = "Delete From tabIndexSettings where mnuModule= :mnuModule and mnuName=:mnuName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("mnuName", mrTemp.getMenuName());
            psTemp.executeUpdate();
            psTemp.close();

            List<GridTabIndexSettings_Mdl> listTabIndex = mrTemp.getGrdTabIndexMdl();
            int listSize = listTabIndex.size();
            for (GridTabIndexSettings_Mdl tab_Mdl : listTabIndex) {
                if (!DmShared.xNull(tab_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    if (DmShared.iNull(tab_Mdl.getSerialNo()).intValue() > DmShared.iNull(listSize).intValue()) {
                        throw new DmException(0, "Serial No. cannot be greator than list size.");
                    } else {
                        sqlQuery = "insert Into tabIndexSettings(mnuModule,mnuName,caption,sNo) Values  "
                                + "(:mnuModule,:mnuName,:caption,:sNo) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("mnuName", DmShared.xNull(mrTemp.getMenuName()));
                        psTemp.setString("caption", DmShared.xNull(tab_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("sNo", DmShared.xNull(tab_Mdl.getSerialNo()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
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
    public DmResult<TabIndexSettings_Mdl> getMoveRec(String module, String entry) {
        DmResult<TabIndexSettings_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        TabIndexSettings_Mdl mrTemp = new TabIndexSettings_Mdl();
        List<GridTabIndexSettings_Mdl> TabIndex = new ArrayList<>();
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "  select tis.mnuModule,tis.mnuName,upw.mnuText "
                    + " from tabIndexSettings as tis"
                    + " left join  " + DmServer.dbCompany + ".dbo.user_Permission_Web as upw on tis.mnuName = upw.mnuName and tis.mnuModule=upw.mnuModule"
                    + " where tis.mnuModule=:mnuModule and tis.mnuName=:mnuName";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", DmShared.xNull(module));
            psTemp.setString("mnuName", DmShared.xNull(entry));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("mnuModule")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("mnuModule")));
                mrTemp.setMenuName(DmShared.xNull(rsTemp.getString("mnuName")));
                mrTemp.setMenuText(DmShared.xNull(rsTemp.getString("mnuText")));
            }
            psTemp.close();
            rsTemp.close();

            ///Bind  Grid
            sqlQuery = "select tis.caption,tis.sNo "
                    + " from tabIndexSettings as tis"
                    + " left join  " + DmServer.dbCompany + ".dbo.user_Permission_Web as upw on tis.mnuName = upw.mnuName and tis.mnuModule=upw.mnuModule"
                    + " where  tis.mnuModule=:mnuModule and tis.mnuName=:mnuName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", DmShared.xNull(module));
            psTemp.setString("mnuName", DmShared.xNull(entry));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GridTabIndexSettings_Mdl grdTabIndex_Mdl = new GridTabIndexSettings_Mdl();
                grdTabIndex_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("caption")));
                grdTabIndex_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("caption")));
                grdTabIndex_Mdl.setSerialNo(DmShared.xNull(rsTemp.getString("sNo")));
                TabIndex.add(grdTabIndex_Mdl);
            }
            mrTemp.getGrdTabIndexMdl().addAll(TabIndex);
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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = DmImportData_Server.getCSVFormats(new ImportTabIndex().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "tabIndex";
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

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  mnuModule, mnuName, caption, sNo "
                    + "		from tabIndexSettings "
                    + "		order by mnuModule, mnuName, sNo, caption";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("mnuModule")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("mnuName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("caption")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("sNo")) + "\"";
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
