package dataman.utility.dataBackup.server;

import dataman.dmBase.dmPrepareStatement.server.*;
import dataman.dmBase.dmSession.server.*;
import dataman.utility.dataBackup.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DataBackUp;
import dataman.dmToolkit.dmStatic.server.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataBackup_SrvImpl extends DmRSSPrivate implements DataBackup_Srv {

    @Override
    public DmResult<List<GrdDataBackup_Mdl>> getDefaultValue() {
        DmResult<List<GrdDataBackup_Mdl>> dmrTemp = new DmResult<List<GrdDataBackup_Mdl>>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<GrdDataBackup_Mdl> lstTemp = new ArrayList<>();
        GrdDataBackup_Mdl node;
        dmrTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlQuery = "select elementType, elementName, elementLocation, backupLocation "
                    + "	from backupInfo "
                    + "	order by elementType, elementName";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdDataBackup_Mdl();
                node.setChecked(false);
                node.setBackupLocation(DmShared.xNull(rsTemp.getString("backupLocation")));
                node.setElementLocation(DmShared.xNull(rsTemp.getString("elementLocation")));
                node.setElementName(DmShared.xNull(rsTemp.getString("elementName")));
                node.setElementType(DataBackUp.getKeyOf(DmShared.iNull(rsTemp.getString("elementType")).intValue()).getValue());
                node.setElementTypeCode(DmShared.iNull(rsTemp.getString("elementType")).toString());
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResult<String> deleteRecord(String elementName, String elementType) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "delete from backupInfo where elementType = :type and elementName = :name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("type", elementType);
            psTemp.setString("name", elementName);
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
    public DmResult<String> saveRecord(GrdDataBackup_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

            sqlQuery = "select elementType, elementName, elementLocation, backupLocation "
                    + " from backupInfo "
                    + " where elementType = :type and elementName = :name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("type", mrTemp.getElementTypeCode());
            psTemp.setString("name", mrTemp.getElementName());
            rsTemp = psTemp.executeQuery();
            blnAdd = !rsTemp.next();
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "insert into backupInfo (elementType, elementName, elementLocation, backupLocation) "
                        + " values (:type,:name,:elementLocation,:backupLocation)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("type", mrTemp.getElementTypeCode());
                psTemp.setString("name", mrTemp.getElementName());
                psTemp.setString("elementLocation", mrTemp.getElementLocation());
                psTemp.setString("backupLocation", mrTemp.getBackupLocation());
            } else {
                sqlQuery = "update backupInfo set "
                        + "   elementLocation = :elementLocation "
                        + " , backupLocation = :backupLocation "
                        + " where elementType = :type and elementName = :name";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("type", mrTemp.getElementTypeCode());
                psTemp.setString("name", mrTemp.getElementName());
                psTemp.setString("elementLocation", mrTemp.getElementLocation());
                psTemp.setString("backupLocation", mrTemp.getBackupLocation());
            }
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
    public DmResult<List<GrdDataBackup_Mdl>> backupElement(List<GrdDataBackup_Mdl> mrTemp) {
        DmResult<List<GrdDataBackup_Mdl>> dmrTemp = new DmResult<List<GrdDataBackup_Mdl>>();
        dmrTemp.setData(mrTemp);
        try {

            new DBCompany_Server().backup(mrTemp);

            new DBTransaction_Server().backup(mrTemp);

            new DBMongo_Server().backup(mrTemp);

            new FSDir_Server().backup(mrTemp);

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getElementType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DataBackUp db : DataBackUp.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(db.getId()).toString());
                listNode.setSharedKeyFieldName(db.getValue());
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
    public DmResultPageing<List<KeyFieldExtended>> getDatabaseName(DmHelper helper, boolean blnToEndOfFile, String elementType) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        DataBackUp db;
        try {
            rstTemp.setData(lstTemp);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            db = DataBackUp.getKeyOf(DmShared.iNull(elementType).intValue());
            switch (db) {
                case DB_COMPANY:
                    sqlQuery = "select db.name dbName, mf.physical_name physicalPath "
                            + "	from sys.master_Files mf "
                            + "	left join sys.databases db on db.database_id = mf.database_id "
                            + "	where db.name = '" + DmServer.dbCompany + "' and mf.type_desc = 'ROWS' ";
                    break;
                case DB_TRANSACTION:
                    sqlQuery = "select max(db.name) dbName, max(mf.physical_name) physicalPath "
                            + "	from company c "
                            + "	left join sys.databases db on db.name = c.centralData_Path "
                            + "	left join sys.master_files mf on mf.database_id = db.database_id "
                            + " where mf.type_desc = 'ROWS' "
                            + "	group by db.database_id ";
                    break;
                case DB_MONGO:
                    sqlQuery = "select c.centralFileData_Path dbName, '' as physicalPath "
                            + "	from company c "
                            + "	where ISNULL(c.centralFileData_Path, '') <> '' "
                            + "	group by c.centralFileData_Path ";
                    break;
            }
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("dbName")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("dbName")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("physicalPath")));
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
}
