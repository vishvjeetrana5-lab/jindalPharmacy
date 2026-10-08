package dataman.payroll.salaryCodeDefinition.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.payroll.salaryCodeDefinition.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SalaryCodeDefinition_SrvImpl extends DmRSSPrivate implements SalaryCodeDefinition_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery(" select code from salarycodeDefinitionMast order by name ");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
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
    public DmResultPageing<List<String>> getNameSuggestion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" Select name  From salaryCodeDefinitionMast ");
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
    public DmResult<SalaryCodeDefinition_Mdl> getMoveRec(String strKey) {
        DmResult<SalaryCodeDefinition_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        SalaryCodeDefinition_Mdl mrTemp = new SalaryCodeDefinition_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT  code,  name,  fromDate,  toDate ,  preparedBy,  u_Name "
                    + "     FROM salarycodeDefinitionMast   "
                    + "     where code=:code ";
//                    + "and site_Code=:siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
//            psTemp.setString("sitecode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                mrTemp.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
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
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From salarycodeDefinitionMast Where Code= :code ";
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
    public DmResult<String> saveRecord(SalaryCodeDefinition_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        String salaryMethod = "";
        dmrTemp.setData("");

        try {
            SalaryCodeDefinition_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "salarycodeDefinitionMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select  count(name) as cnt "
                    + "         from salarycodeDefinitionMast  "
                    + "         where lower(name) = :name and Code <> :code and site_code=:sitecode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("sitecode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(100, "Name already exists.");
            }
       
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "INSERT INTO salarycodeDefinitionMast (code, name, fromDate, toDate, u_EntDt, preparedBy"
                        + ", site_Code"
                        + ")  "
                        + "     VALUES             (:code, :name, :fromDate, :toDate, :u_EntDt, :preparedBy"
                        + ", :siteCode"
                        + ") ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setDate("fromDate", DmServer.convertDate(mrTemp.getFromDate()));
                psTemp.setDate("toDate", DmServer.convertDate(mrTemp.getToDate()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("sitecode", DmServer.getSsnSiteCode(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = "update salarycodeDefinitionMast set "
                        + " name         = :name, "
                        + " fromDate     = :fromDate, "
                        + " toDate       = :toDate,    "
                        + " u_Name       = :uName, "
                        + " u_EntDt      = :uEntDt, "
                        + " site_Code     = :sitecode "
                        + " Where code   = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setDate("fromDate", DmServer.convertDate(mrTemp.getFromDate()));
                psTemp.setDate("toDate", DmServer.convertDate(mrTemp.getToDate()));
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("sitecode", DmServer.getSsnSiteCode(this));
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
            sqlWithParam.setSQL("  SELECT  code,  name,  fromDate, toDate "
                    + "             FROM salarycodeDefinitionMast "
//                    + "where site_Code=:siteCode  "
            );
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
//            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setFromDate(DmShared.dNull(rsTemp.getDate("FromDate")));
                listNode.setToDate(DmShared.dNull(rsTemp.getDate("ToDate")));
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
