package dataman.accounts.currencyMaster.server;

import dataman.dmBase.self.shared.*;
import dataman.accounts.currencyMaster.shared.*;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmEnum.DmLocaleCountry;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import dataman.dmToolkit.dmImportData.server.ImportCurrency;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CurrencyMaster_SrvImpl extends DmRSSPrivate implements CurrencyMaster_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("Select Code From Currency Order By Name;");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
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

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select Name From Currency ");
            helper.addSort("Name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResult<CurrencyMaster_Mdl> getMoveRec(String strKey) {
        DmResult<CurrencyMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        CurrencyMaster_Mdl mrTemp = new CurrencyMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "Select Code, Name,countryLocale ,u_Name, preparedBy From Currency "
                    + "Where Code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                mrTemp.setCountryLocale(DmShared.xNull(rsTemp.getString("countryLocale")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
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
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "Delete From Currency Where Code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();

            spBegin.commit();
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
    public DmResult<String> saveRecord(CurrencyMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            CurrencyMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setCountryLocale(DmShared.removeSpace(mrTemp.getCountryLocale()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "Currency", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            //****************************** <Duplicacy Check> *************************************
            strSQL = "SELECT code FROM Currency WHERE name = :name AND code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "Curreny already exist.");
            }
            rsTemp.close();
            psTemp.close();

            //****************************** <End> *************************************************
            if (blnAdd) {
                strSQL = "Insert Into Currency (Code, Name, countryLocale, u_EntDt,preparedBy) "
                        + " Values (:code,:name,:countryLocale,:u_EntDt,:preparedBy) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("countryLocale", DmShared.xNull(mrTemp.getCountryLocale()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            } else {
                strSQL = "Update Currency Set "
                        + "Name = :name, "
                        + "countryLocale = :countryLocale, "
                        + "U_Name = :u_Name, "
                        + "U_EntDt = :u_EntDt "
                        + "Where Code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("countryLocale", DmShared.xNull(mrTemp.getCountryLocale()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
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
            sqlWithParam.setSQL("Select Code, Name,countryLocale From Currency ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setLocale(DmShared.xNull(rsTemp.getString("countryLocale")));
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
    public DmResultPageing<List<TxtHCountryLocale_Mdl>> getTxtHCountryLocale(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCountryLocale_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCountryLocale_Mdl> lstTemp = new ArrayList<>();
        TxtHCountryLocale_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;

        try {

            for (DmLocaleCountry LocaleCountry : DmLocaleCountry.values()) {
                listNode = new TxtHCountryLocale_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(LocaleCountry.getDmCode()));
                listNode.setSharedKeyFieldName(DmShared.xNull(LocaleCountry.getDmCode()));
                listNode.setCountryName(DmShared.xNull(LocaleCountry.getCountryName()));
                listNode.setCurrencyName(DmShared.xNull(LocaleCountry.getCurrencyName()));
                lstTemp.add(listNode);
            }
            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            cnTemp.close();

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        String csvHeader = DmImportData_Server.getCSVFormats(new ImportCurrency().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "currency";
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
            sqlQuery = "select cur.name,cur.countryLocale "
                    + " from currency as cur "
                    + "	order by cur.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("name")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("countryLocale")) + "\"";
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
