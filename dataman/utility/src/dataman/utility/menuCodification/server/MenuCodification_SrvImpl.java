package dataman.utility.menuCodification.server;

import dataman.utility.menuCodification.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import dataman.dmToolkit.dmImportData.server.ImportMenuCodification;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuCodification_SrvImpl extends DmRSSPrivate implements MenuCodification_Srv {

    @Override
    public DmResultPageing<List<TxtHMenuName_Mdl>> getTxtHMenuName(String moduleName, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMenuName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMenuName_Mdl> lstTemp = new ArrayList<>();
        TxtHMenuName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            String userName = DmServer.getSsnUserName(this);
            String compCode = DmShared.iNull(DmServer.getSsnCompanyCode(this)).toString();

            if (userName.equalsIgnoreCase("SA") || userName.equalsIgnoreCase("SUPER")) {
                compCode = "-1";
            }

            sqlWithParam.setSQL("select upw.mnuName ,upw.mnuText,upw.mnuModule "
                    + " from user_Permission_Web upw "
                    + " inner join ( "
                    + " select upw.mnuModule, upw.mnuName ,upw.mnuText  "
                    + " from user_Permission_Web upw "
                    + " left join	user_Permission_Web upw1 on upw1.userName = 'SA' and "
                    + " upw.mnuModule = upw1.mnuModule and upw.mnuName = upw1.parent "
                    + " where upw.userName = 'SA' and upw.mnuModule = :mnuModule and upw1.mnuName is Null "
                    + " ) as u on u.mnuModule = upw.mnuModule and u.mnuName = upw.mnuName "
                    + " left join menuCodification as mc ON mc.mnuModule=upw.mnuModule AND mc.mnuName=upw.mnuName "
                    + " where upw.userName = :userName and upw.comp_Code = :compCode and upw.mnuModule = :mnuModule");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            psTemp.setString("mnuModule", moduleName);
            psTemp.setString("userName", userName);
            psTemp.setString("compCode", compCode);
            rsTemp = psTemp.executeQuery();
            int count = 0;
            while (rsTemp.next()) {
                listNode = new TxtHMenuName_Mdl();
                listNode.setSharedKeyFieldCode(++count + "");
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("mnuText")));
                listNode.setMenuName(DmShared.xNull(rsTemp.getString("mnuName")));
                listNode.setModuleName(DmShared.xNull(rsTemp.getString("mnuModule")));

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
    public DmResult<String> saveRecord(GrdDescription_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        try {
            MenuCodification_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);
            mrTemp.setMenuCode(DmShared.removeSpace(mrTemp.getMenuCode()).replaceAll(" ", ""));

            sqlQuery = " select count(mnuShortHand) AS cnt"
                    + "  from  menuCodification as mc"
                    + "  where mnuShortHand=:mnuShortHand "
                    + "  group by mnuShortHand";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuShortHand", mrTemp.getMenuCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).longValue() > 0) {
                    throw new DmException(0, "Menu code already exist.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Delete From menuCodification Where mnuModule=:mnuModule  AND mnuName=:mnuName and mnuText = :mnuText ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", mrTemp.getModuleName());
            psTemp.setString("mnuName", mrTemp.getMenuName());
            psTemp.setString("mnuText", mrTemp.getSharedKeyFieldName());
            psTemp.executeUpdate();
            psTemp.close();
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldName()).equalsIgnoreCase("")) {
                sqlQuery = "Insert Into menuCodification(mnuModule,mnuName,mnuShortHand, mnuText) Values  "
                        + "(:mnuModule,:mnuName,:mnuShortHand, :mnuText) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getModuleName()));
                psTemp.setString("mnuName", DmShared.xNull(mrTemp.getMenuName()));
                psTemp.setString("mnuText", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("mnuShortHand", DmShared.xNull(mrTemp.getMenuCode()).toUpperCase());
                psTemp.executeUpdate();
                psTemp.close();
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
    public DmResult<String> deleteRecord(GrdDescription_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        dmrTemp.setData("");
        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "Delete From menuCodification"
                    + " where mnuModule=:mnuModule AND mnuName=:mnuName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getModuleName()));
            psTemp.setString("mnuName", DmShared.xNull(mrTemp.getMenuName()));
            psTemp.executeUpdate();
            psTemp.close();

            cnTemp.close();
            dmrTemp.setStatus(true);

        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
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
    public DmResult<List<GrdModuleMenu_Mdl>> getModuleList(String moduleType) {
        DmResult<List<GrdModuleMenu_Mdl>> rstTemp = new DmResult<>();
        List<GrdModuleMenu_Mdl> lstTemp = new ArrayList<GrdModuleMenu_Mdl>();
        GrdModuleMenu_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            String userName = DmServer.getSsnUserName(this);

            sqlQuery = "select Max(mnuModule) as module "
                    + " from user_Permission_Web where userName = '" + userName + "' "
                    + " group by mnuModule order by mnuModule ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            int count = 0;
            while (rsTemp.next()) {
                node = new GrdModuleMenu_Mdl();
                node.setSharedKeyFieldCode(++count + "");
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("module")));

                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();
            rstTemp.setStatus(true);

            rstTemp.setData(lstTemp);
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
    public DmResult<List<GrdDescription_Mdl>> getMenuList(String sortingType, String moduleName) {
        DmResult<List<GrdDescription_Mdl>> rstTemp = new DmResult<>();
        List<GrdDescription_Mdl> lstTemp = new ArrayList<>();
        GrdDescription_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, sorting = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            String userName = DmServer.getSsnUserName(this);
            String compCode = DmShared.iNull(DmServer.getSsnCompanyCode(this)).toString();

            if (userName.equalsIgnoreCase("SA") || userName.equalsIgnoreCase("SUPER")) {
                compCode = "-1";
            }

            if (DmShared.xNull(sortingType).equalsIgnoreCase("M")) {
                sorting = " order by upw.mnuText asc ";
            } else if (DmShared.xNull(sortingType).equalsIgnoreCase("C")) {
                sorting = " order by isNull(mc.mnuShortHand,'')   asc ";
            }
            sqlQuery = "select upw.mnuName ,upw.mnuText,mc.mnuShortHand "
                    + " from user_Permission_Web upw "
                    + " inner join ( "
                    + " select upw.mnuModule, upw.mnuName ,upw.mnuText  "
                    + " from user_Permission_Web upw "
                    + " left join	user_Permission_Web upw1 on upw1.userName = 'SA' and "
                    + " upw.mnuModule = upw1.mnuModule and upw.mnuName = upw1.parent "
                    + " where upw.userName = 'SA' and upw.mnuModule = :mnuModule and upw1.mnuName is Null "
                    + " ) as u on u.mnuModule = upw.mnuModule and u.mnuName = upw.mnuName "
                    + " left join menuCodification as mc ON mc.mnuModule=upw.mnuModule AND mc.mnuName=upw.mnuName "
                    + " where upw.userName = :userName and upw.comp_Code = :compCode and upw.mnuModule = :mnuModule "
                    + "  " + sorting;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", moduleName);
            psTemp.setString("userName", userName);
            psTemp.setString("compCode", compCode);
            rsTemp = psTemp.executeQuery();
            int count = 0;
            while (rsTemp.next()) {
                node = new GrdDescription_Mdl();
                node.setSharedKeyFieldCode(++count + "");
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("mnuShortHand")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("mnuText")));
                node.setMenuName(DmShared.xNull(rsTemp.getString("mnuName")));
                node.setMenuCode(DmShared.xNull(rsTemp.getString("mnuShortHand")));
                node.setModuleName(DmShared.xNull((moduleName)));

                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();
            rstTemp.setStatus(true);

            rstTemp.setData(lstTemp);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
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
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = DmImportData_Server.getCSVFormats(new ImportMenuCodification().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "menuCodification";
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
            sqlQuery = "select  mnuModule, mnuName, mnuText, mnuShortHand "
                    + "		from menuCodification "
                    + "		order by mnuModule, mnuName, mnuShortHand ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("mnuModule")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("mnuName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("mnuText")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("mnuShortHand")) + "\"";
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
