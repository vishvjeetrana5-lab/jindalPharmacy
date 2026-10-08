package aarogya.master.organizationServiceMaster.server;

import aarogya.master.organizationServiceMaster.shared.*;
import aarogya.selfToolkit.stImportData.server.ImportOrganization;
import aarogya.selfToolkit.stImportData.server.OrganizationService;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrganizationServiceMaster_SrvImpl extends DmRSSPrivate implements OrganizationServiceMaster_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("Select code From organizationServiceMast ");
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

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select organizationServiceName As Name from organizationServiceMast ");
            helper.addSort("Name", DmSortType.ASC);
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
    public DmResult<OrganizationServiceMaster_Mdl> getMoveRec(String strKey) {
        DmResult<OrganizationServiceMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        OrganizationServiceMaster_Mdl mrTemp = new OrganizationServiceMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  orgsm.code, orgsm.organizationServiceName, orgsm.organizationManualCode, orgsm.organizationServiceGroupName, "
                    + "         orgsm.organizationCode, sg.name as organizationName, orgsm.printSNo, orgsm.u_Name, orgsm.preparedBy "
                    + "         from organizationServiceMast as orgsm "
                    + "         Left Join subGroup as sg on sg.subCode = orgsm.organizationCode "
                    + "         where orgsm.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("organizationServiceName")));
                mrTemp.setOrgServiceCode(DmShared.xNull(rsTemp.getString("organizationManualCode")));
                mrTemp.setServiceGroup(DmShared.xNull(rsTemp.getString("organizationServiceGroupName")));
                mrTemp.setOrganizationCode(DmShared.xNull(rsTemp.getString("organizationCode")));
                mrTemp.setOrganizationName(DmShared.xNull(rsTemp.getString("organizationName")));
                mrTemp.setPrintSno(DmShared.xNull(rsTemp.getString("printSNo")));
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
    public DmResult<String> saveRecord(OrganizationServiceMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            OrganizationServiceMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setOrgServiceCode(DmShared.removeSpace(mrTemp.getOrgServiceCode()));

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "organizationServiceMast", cnTemp));
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*====================================< Check whether record already exist or not >====================================*/
            sqlQuery = "select  code from organizationServiceMast where organizationCode = :organizationCode "
                    + "         and organizationServiceName = :organizationServiceName and code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("organizationCode", DmShared.iNull(mrTemp.getOrganizationCode()).toString());
            psTemp.setString("organizationServiceName", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(300, "Org. Service Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "Insert Into organizationServiceMast(code, organizationServiceName, organizationManualCode, organizationServiceGroupName, "
                        + " organizationCode, printSNo, u_EntDt, preparedBy) "
                        + " values(:code, :organizationServiceName, :organizationManualCode, :organizationServiceGroupName, :organizationCode, "
                        + " :printSNo, :u_EntDt, :preparedBy)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("organizationCode", DmShared.foriegnKey(mrTemp.getOrganizationCode()));
                psTemp.setString("organizationServiceName", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
                psTemp.setString("organizationManualCode", DmShared.xNull(mrTemp.getOrgServiceCode()).trim());
                psTemp.setString("organizationServiceGroupName", DmShared.xNull(mrTemp.getServiceGroup()).trim());
                psTemp.setString("printSNo", DmShared.xNull(mrTemp.getPrintSno()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = "Update organizationServiceMast Set "
                        + " organizationServiceName = :organizationServiceName, "
                        + " organizationManualCode = :organizationManualCode, "
                        + " organizationServiceGroupName = :organizationServiceGroupName, "
                        + " printSNo = :printSNo, "
                        + " u_EntDt = :u_EntDt, "
                        + " u_Name = :u_Name "
                        + " where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("organizationServiceName", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
                psTemp.setString("organizationManualCode", DmShared.xNull(mrTemp.getOrgServiceCode()).trim());
                psTemp.setString("organizationServiceGroupName", DmShared.xNull(mrTemp.getServiceGroup()).trim());
                psTemp.setString("printSNo", DmShared.xNull(mrTemp.getPrintSno()));
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

            sqlQuery = "Delete From organizationServiceMast Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
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

            sqlWithParam.setSQL("Select orgsm.code, sg.name As organization, orgsm.organizationServiceName as serviceName, "
                    + "                 orgSm.organizationManualCode As serviceCode, orgSm.organizationServiceGroupName As serviceGroup "
                    + "                 from organizationServiceMast As orgSm "
                    + "                 Left Join subGroup as sg on sg.subCode = orgSm.organizationCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("organization")));
                listNode.setOrganization(DmShared.xNull(rsTemp.getString("organization")));
                listNode.setServiceCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                listNode.setServiceGroup(DmShared.xNull(rsTemp.getString("serviceGroup")));
                listNode.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOrg(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "Select  om.subCode as CODE,sg.name as NAME "
                    + "         from organizationMast as om "
                    + "         Left Join SubGroup as sg on om.SubCode = sg.SubCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceGroup(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select distinct organizationServiceGroupName As Name, organizationServiceGroupName As code from organizationServiceMast";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = DmImportData_Server.getCSVFormats(new OrganizationService().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "organizationServiceMaster";
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
            sqlQuery = "select  org.name, osm.organizationServiceName, osm.organizationManualCode, osm.organizationServiceGroupName, osm.printSNo"
                    + "		from organizationServiceMast osm "
                    + "		left join subGroup org on org.subCode = osm.organizationCode"
                    + "		order by org.name, osm.organizationServiceName";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("name")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("organizationServiceName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("organizationManualCode")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("organizationServiceGroupName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("printSNo")) + "\"";
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
